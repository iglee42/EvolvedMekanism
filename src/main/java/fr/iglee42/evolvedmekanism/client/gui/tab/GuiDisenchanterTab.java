package fr.iglee42.evolvedmekanism.client.gui.tab;

import fr.iglee42.evolvedmekanism.tiles.enchantment.TileEntityLaserDisenchanter;
import mekanism.client.SpecialColors;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.GuiInsetElement;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.Mekanism;
import mekanism.common.MekanismLang;
import mekanism.common.network.to_server.PacketGuiInteract;
import mekanism.common.network.to_server.PacketGuiInteract.GuiInteraction;
import mekanism.common.tile.laser.TileEntityLaserAmplifier.RedstoneOutput;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class GuiDisenchanterTab extends GuiInsetElement<TileEntityLaserDisenchanter> {

    private static final ResourceLocation OFF = MekanismUtils.getResource(ResourceType.GUI, "amplifier_off.png");
    private static final ResourceLocation ENTITY = MekanismUtils.getResource(ResourceType.GUI, "amplifier_entity.png");
    private static final ResourceLocation CONTENTS = MekanismUtils.getResource(ResourceType.GUI, "amplifier_contents.png");

    public GuiDisenchanterTab(IGuiWrapper gui, TileEntityLaserDisenchanter tile) {
        super(OFF, gui, tile, gui.getWidth(), 109, 26, 18, false);
    }

    @Override
    protected ResourceLocation getOverlay() {
        return switch (dataSource.getOutputMode()) {
            case ENTITY_DETECTION -> ENTITY;
            case ENERGY_CONTENTS -> CONTENTS;
            default -> super.getOverlay();
        };
    }

    @Override
    public void renderToolTip(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        displayTooltips(graphics, mouseX, mouseY, MekanismLang.REDSTONE_OUTPUT.translate(dataSource.getOutputMode()));
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        Mekanism.packetHandler().sendToServer(new PacketGuiInteract(button == GLFW.GLFW_MOUSE_BUTTON_LEFT ? GuiInteraction.NEXT_MODE : GuiInteraction.PREVIOUS_MODE, dataSource));
    }

    @Override
    public boolean isValidClickButton(int button) {
        return button == GLFW.GLFW_MOUSE_BUTTON_LEFT || button == GLFW.GLFW_MOUSE_BUTTON_RIGHT;
    }

    @Override
    protected void colorTab(GuiGraphics graphics) {
        MekanismRenderer.color(graphics, SpecialColors.TAB_LASER_AMPLIFIER);
    }
}
