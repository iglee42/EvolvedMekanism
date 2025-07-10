package fr.iglee42.evolvedmekanism.client.gui;

import fr.iglee42.evolvedmekanism.inventory.personalstorage.TieredPersonalStorageItemContainer;
import fr.iglee42.evolvedmekanism.items.ItemBlockTieredPersonalStorage;
import mekanism.client.gui.GuiMekanism;
import mekanism.client.gui.element.tab.GuiSecurityTab;
import mekanism.common.inventory.container.item.PersonalStorageItemContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GuiTieredPersonalStorageItem extends GuiMekanism<TieredPersonalStorageItemContainer> {

    public GuiTieredPersonalStorageItem(TieredPersonalStorageItemContainer container, Inventory inv, Component title) {
        super(container, inv, title.copy().setStyle(Style.EMPTY));
        imageHeight += 56;
        imageHeight += (((ItemBlockTieredPersonalStorage<?>)container.getStack().getItem()).getTier().rows - 6 ) * 18;
        imageWidth += (((ItemBlockTieredPersonalStorage<?>)container.getStack().getItem()).getTier().columns - 9 ) * 18;
        inventoryLabelY = imageHeight - 94;
        inventoryLabelX = imageWidth / 2 - Minecraft.getInstance().font.width(playerInventoryTitle) / 2;
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiSecurityTab(this, menu.getHand()));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        renderInventoryText(guiGraphics);
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}