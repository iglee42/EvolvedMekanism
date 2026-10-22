package fr.iglee42.evolvedmekanism.recipeviewers.emi.widget;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.SlotWidget;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.EMEmiUtils;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.GasEmiStack;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.math.MathUtils;
import mekanism.client.gui.GuiUtils;
import mekanism.client.gui.GuiUtils.TilingDirection;
import mekanism.client.gui.element.GuiElement;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.render.MekanismRenderer.FluidTextureType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class MekanismTankEmiWidget extends SlotWidget {

    private final Bounds tankBounds;
    private final long capacity;
    @Nullable
    private final GuiGauge<?> gauge;

    public MekanismTankEmiWidget(EmiIngredient stack, GuiElement element, long capacity) {
        super(stack, element.getX(), element.getY());
        this.tankBounds = new Bounds(element.getX(), element.getY(), element.getWidth(), element.getHeight());
        this.capacity = Math.max(1, capacity);
        this.gauge = element instanceof GuiGauge<?> g ? g : null;
    }

    @Override
    public Bounds getBounds() {
        return tankBounds;
    }

    @Override
    public void drawStack(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        EmiIngredient ingredient = getStack();
        List<EmiStack> stacks = ingredient.getEmiStacks();
        EmiStack stack = stacks.isEmpty() ? EmiStack.EMPTY : EMEmiUtils.getCurrent(stacks);
        if (!stack.isEmpty() && ingredient.getAmount() > 0) {
            TextureAtlasSprite sprite;
            if (stack instanceof GasEmiStack gasStack) {
                GasStack chemical = new GasStack(gasStack.getGas(), MathUtils.clampToInt(ingredient.getAmount()));
                MekanismRenderer.color(graphics, chemical);
                sprite = MekanismRenderer.getChemicalTexture(chemical.getType());
            } else if (stack.getKey() instanceof Fluid fluid) {
                FluidStack fluidStack = new FluidStack(fluid, MathUtils.clampToInt(ingredient.getAmount()));
                MekanismRenderer.color(graphics, fluidStack);
                sprite = MekanismRenderer.getFluidTexture(fluidStack, FluidTextureType.STILL);
            } else {
                return;
            }
            int x = tankBounds.x() + 1;
            int y = tankBounds.y() + 1;
            int width = tankBounds.width() - 2;
            int height = tankBounds.height() - 2;
            int desiredHeight = MathUtils.clampToInt(height * (double) ingredient.getAmount() / capacity);
            if (desiredHeight < 1) {
                desiredHeight = 1;
            }
            if (desiredHeight > height) {
                desiredHeight = height;
            }
            GuiUtils.drawTiledSprite(graphics, x, y, height, width, desiredHeight, sprite, 16, 16, 0, TilingDirection.UP_RIGHT);
            MekanismRenderer.resetColor(graphics);
        }
        if (this.gauge != null) {
            PoseStack pose = graphics.pose();
            pose.pushPose();
            pose.translate(this.gauge.getGuiLeft(), this.gauge.getGuiTop(), 0);
            this.gauge.drawBarOverlay(graphics);
            pose.popPose();
        }
    }
}
