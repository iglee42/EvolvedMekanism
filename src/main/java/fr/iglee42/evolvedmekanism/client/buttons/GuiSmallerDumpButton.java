package fr.iglee42.evolvedmekanism.client.buttons;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.GuiTexturedElement;
import mekanism.common.Mekanism;
import mekanism.common.network.to_server.PacketGuiInteract;
import mekanism.common.network.to_server.PacketGuiInteract.GuiInteraction;
import mekanism.common.tile.interfaces.IHasDumpButton;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class GuiSmallerDumpButton<TILE extends BlockEntity & IHasDumpButton> extends GuiTexturedElement {

    protected final TILE tile;

    public GuiSmallerDumpButton(IGuiWrapper gui, TILE tile, int x, int y) {
        super(EvolvedMekanism.getResource(ResourceType.GUI, "dump.png"), gui, x, y, 21, 8);
        this.tile = tile;
        this.clickSound = SoundEvents.UI_BUTTON_CLICK;
    }

    @Override
    public void drawBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.drawBackground(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.blit(getResource(), relativeX, relativeY, 0, 0, width, height, width, height);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        Mekanism.packetHandler().sendToServer(new PacketGuiInteract(GuiInteraction.DUMP_BUTTON, tile));
    }
}