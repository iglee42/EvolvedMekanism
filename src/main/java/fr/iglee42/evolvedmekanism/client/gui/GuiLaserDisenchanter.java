package fr.iglee42.evolvedmekanism.client.gui;

import fr.iglee42.evolvedmekanism.tiles.enchantment.TileEntityLaserDisenchanter;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.bar.GuiChemicalBar;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.slot.InventoryContainerSlot;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.component.config.DataType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GuiLaserDisenchanter extends GuiMekanismTile<TileEntityLaserDisenchanter, MekanismTileContainer<TileEntityLaserDisenchanter>> {

    public GuiLaserDisenchanter(MekanismTileContainer<TileEntityLaserDisenchanter> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiChemicalBar(this, GuiChemicalBar.getProvider(tile.getChemicalTank(), tile.getChemicalTanks(null)), 42, 16, 106, 10, true));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        renderInventoryText(guiGraphics);
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected @Nullable DataType findDataType(InventoryContainerSlot slot) {
        return slot.getSlotType() == ContainerSlotType.OUTPUT ? DataType.OUTPUT : slot.getSlotType() == ContainerSlotType.INPUT ? DataType.INPUT : super.findDataType(slot);
    }
}