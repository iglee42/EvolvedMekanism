package fr.iglee42.evolvedmekanism.client.gui;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.client.gui.scroll.GuiEnchantmentsScrollList;
import fr.iglee42.evolvedmekanism.network.PacketApplyEnchantments;
import fr.iglee42.evolvedmekanism.tiles.enchantment.TileEntityEnchantingStation;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.bar.GuiChemicalBar;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.button.MekanismButton;
import mekanism.client.gui.element.button.MekanismImageButton;
import mekanism.client.gui.element.button.TranslationButton;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.MekanismLang;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class GuiEnchantingStation extends GuiMekanismTile<TileEntityEnchantingStation, MekanismTileContainer<TileEntityEnchantingStation>> {

    private GuiEnchantmentsScrollList enchantmentList;
    private MekanismButton setButton;
    private MekanismButton plusButton;
    private MekanismButton minusButton;

    public GuiEnchantingStation(MekanismTileContainer<TileEntityEnchantingStation> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
        imageHeight += 64;
        inventoryLabelY = imageHeight - 92;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 156, 40))
              .warning(WarningType.NOT_ENOUGH_ENERGY, () -> {
                  MachineEnergyContainer<TileEntityEnchantingStation> energyContainer = tile.getEnergyContainer();
                  return tile.isWorking() && energyContainer.getEnergyPerTick().greaterThan(energyContainer.getEnergy());
              });
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::usedEnergy));
        addRenderableWidget(new GuiProgress(tile::getScaledProgress, ProgressType.LARGE_RIGHT, this, 65, 123));
        enchantmentList = addRenderableWidget(new GuiEnchantmentsScrollList(this, 34, 20, 108, 74, () -> tile.containerSlot.getStack().copy()));
        setButton = addRenderableWidget(new TranslationButton(this, 28, 96, 86, 17, MekanismLang.BUTTON_SET, () -> {
            if (enchantmentList.hasChanges()) {
                EvolvedMekanism.packetHandler().sendToServer(new PacketApplyEnchantments(tile.getBlockPos(), new HashMap<>(enchantmentList.getPendingLevels())));
            }
        }));
        plusButton = addRenderableWidget(new MekanismImageButton(this, 114, 96, 17, 17, 14, 14, MekanismUtils.getResource(MekanismUtils.ResourceType.GUI_BUTTON, "up.png"), () -> enchantmentList.stepSelected(1, hasShiftDown())));
        minusButton = addRenderableWidget(new MekanismImageButton(this, 131, 96, 17, 17, 14, 14, MekanismUtils.getResource(MekanismUtils.ResourceType.GUI_BUTTON, "down.png"), () -> enchantmentList.stepSelected(-1, hasShiftDown())));
        addRenderableWidget(new GuiChemicalBar<>(this, GuiChemicalBar.getProvider(tile.getChemicalTank(), tile.getGasTanks(null)), 35, 117, 16, 16, false));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        long chemicalCost = tile.calculateChemicalCost(enchantmentList.getPendingLevels());
        setButton.active = enchantmentList.hasChanges() && tile.operatingTicks == 0 && chemicalCost <= tile.getChemicalTank().getStored();
        if (setButton.active) {
            setButton.setMessage(EvolvedMekanismLang.BUTTON_APPLY_ENCHANTMENTS.translate());
        } else if (enchantmentList.hasChanges() && chemicalCost > tile.getChemicalTank().getStored()) {
            setButton.setMessage(EvolvedMekanismLang.BUTTON_NOT_ENOUGH_GAS.translate(chemicalCost));
        } else {
            setButton.setMessage(EvolvedMekanismLang.BUTTON_NO_CHANGES.translate());
        }
        plusButton.active = enchantmentList.canChangeSelected(1);
        minusButton.active = enchantmentList.canChangeSelected(-1);
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}
