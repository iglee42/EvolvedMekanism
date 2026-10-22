package fr.iglee42.evolvedmekanism.recipeviewers.emi;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import dev.emi.emi.api.render.EmiRender;
import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiStack;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.client.render.MekanismRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GasEmiStack extends EmiStack {

    private final Gas gas;

    public GasEmiStack(Gas gas, long amount) {
        this.gas = gas;
        this.amount = amount;
    }

    public static EmiStack of(GasStack stack) {
        if (stack.isEmpty()) {
            return EmiStack.EMPTY;
        }
        return new GasEmiStack(stack.getType(), stack.getAmount());
    }

    public Gas getGas() {
        return gas;
    }

    @Override
    public EmiStack copy() {
        EmiStack copy = new GasEmiStack(gas, amount);
        copy.setChance(chance);
        copy.setRemainder(getRemainder().copy());
        copy.comparison(comparison);
        return copy;
    }

    @Override
    public boolean isEmpty() {
        return amount == 0 || gas.isEmptyType();
    }

    @Override
    public CompoundTag getNbt() {
        return null;
    }

    @Override
    public Object getKey() {
        return gas;
    }

    @Override
    public ResourceLocation getId() {
        ResourceLocation name = MekanismAPI.gasRegistry().getKey(gas);
        return name != null ? name : new ResourceLocation("mekanism", "empty");
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, float delta, int flags) {
        if ((flags & RENDER_ICON) != 0) {
            TextureAtlasSprite sprite = MekanismRenderer.getChemicalTexture(gas);
            int argb = gas.getColorRepresentation();
            float a = (argb >> 24 & 255) / 255.0F;
            if (a == 0) {
                a = 1;
            }
            graphics.setColor((argb >> 16 & 255) / 255.0F, (argb >> 8 & 255) / 255.0F, (argb & 255) / 255.0F, a);
            graphics.blit(x, y, 0, 16, 16, sprite);
            graphics.setColor(1, 1, 1, 1);
        }
        if ((flags & RENDER_REMAINDER) != 0) {
            EmiRender.renderRemainderIcon(this, graphics, x, y);
        }
    }

    @Override
    public List<Component> getTooltipText() {
        return Lists.newArrayList(getName());
    }

    @Override
    public List<ClientTooltipComponent> getTooltip() {
        List<ClientTooltipComponent> list = getTooltipText().stream().map(EmiTooltipComponents::of).collect(Collectors.toList());
        if (amount > 1) {
            list.add(EmiTooltipComponents.getAmount(this));
        }
        EmiTooltipComponents.appendModName(list, getId().getNamespace());
        list.addAll(super.getTooltip());
        return list;
    }

    @Override
    public Component getName() {
        return gas.getTextComponent();
    }
}
