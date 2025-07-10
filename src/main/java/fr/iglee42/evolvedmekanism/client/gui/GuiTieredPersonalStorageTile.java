package fr.iglee42.evolvedmekanism.client.gui;

import fr.iglee42.evolvedmekanism.containers.TieredPersonalStorageContainer;
import fr.iglee42.evolvedmekanism.tiles.TileEntityTieredPersonalStorage;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.TileEntityPersonalStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GuiTieredPersonalStorageTile extends GuiMekanismTile<TileEntityTieredPersonalStorage, TieredPersonalStorageContainer> {

    public GuiTieredPersonalStorageTile(TieredPersonalStorageContainer container, Inventory inv, Component title) {
        super(container, inv, title);
        imageHeight += 56;
        imageHeight += (container.getTileEntity().getTier().rows - 6 ) * 18;
        imageWidth += (container.getTileEntity().getTier().columns - 9 ) * 18;
        inventoryLabelY = imageHeight - 94;
        inventoryLabelX = imageWidth / 2 - Minecraft.getInstance().font.width(playerInventoryTitle) / 2;
        dynamicSlots = true;
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        renderInventoryText(guiGraphics);
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}