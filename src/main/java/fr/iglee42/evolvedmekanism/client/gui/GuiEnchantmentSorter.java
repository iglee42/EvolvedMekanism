package fr.iglee42.evolvedmekanism.client.gui;

import fr.iglee42.evolvedmekanism.tiles.enchantment.TileEntityEnchantmentSorter;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.tile.machine.TileEntityPrecisionSawmill;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GuiEnchantmentSorter extends GuiConfigurableTile<TileEntityEnchantmentSorter, MekanismTileContainer<TileEntityEnchantmentSorter>> {

    public GuiEnchantmentSorter(MekanismTileContainer<TileEntityEnchantmentSorter> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        //Note: We just draw the wide slot on top of the normal slots so that it looks a bit better
        //addRenderableWidget(new GuiSlot(SlotType.OUTPUT_WIDE, this, 111, 30));
        addRenderableWidget(new GuiProgress(()->tile.didProcess, ProgressType.BAR, this, 64, 38));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        renderInventoryText(guiGraphics);
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}