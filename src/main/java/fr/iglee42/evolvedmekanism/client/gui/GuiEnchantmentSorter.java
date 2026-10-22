package fr.iglee42.evolvedmekanism.client.gui;

import fr.iglee42.evolvedmekanism.tiles.enchantment.TileEntityEnchantmentSorter;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
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
        addRenderableWidget(new GuiProgress(() -> tile.didProcess, ProgressType.BAR, this, 64, 38));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}
