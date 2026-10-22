package fr.iglee42.evolvedmekanism.client.gui;

import fr.iglee42.evolvedmekanism.client.gui.tab.GuiDisenchanterTab;
import fr.iglee42.evolvedmekanism.tiles.enchantment.TileEntityLaserDisenchanter;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.bar.GuiChemicalBar;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GuiLaserDisenchanter extends GuiMekanismTile<TileEntityLaserDisenchanter, MekanismTileContainer<TileEntityLaserDisenchanter>> {

    public GuiLaserDisenchanter(MekanismTileContainer<TileEntityLaserDisenchanter> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiChemicalBar<>(this, GuiChemicalBar.getProvider(tile.getChemicalTank(), tile.getGasTanks(null)), 42, 16, 106, 10, true));
        addRenderableWidget(new GuiDisenchanterTab(this, tile));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}
