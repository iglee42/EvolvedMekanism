package fr.iglee42.evolvedmekanism.recipeviewers.emi.widget;

import java.util.Collections;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;
import mekanism.client.gui.element.GuiElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

public class MekanismEmiWidget extends Widget {

    private final GuiElement element;
    private final Bounds bounds;
    private final boolean forwardClicks;

    public MekanismEmiWidget(GuiElement element, boolean forwardClicks) {
        this.element = element;
        this.forwardClicks = forwardClicks;
        this.bounds = new Bounds(element.getX(), element.getY(), element.getWidth(), element.getHeight());
    }

    @Override
    public Bounds getBounds() {
        return bounds;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.translate(element.getGuiLeft(), element.getGuiTop(), 0);
        element.renderShifted(guiGraphics, mouseX, mouseY, 0);
        element.onDrawBackground(guiGraphics, mouseX, mouseY, 0);
        int zOffset = 200;
        pose.pushPose();
        element.onRenderForeground(guiGraphics, mouseX, mouseY, zOffset, zOffset);
        pose.popPose();
        pose.popPose();
    }

    @Override
    public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
        return Collections.emptyList();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        return forwardClicks && element.mouseClicked(mouseX, mouseY, button);
    }
}
