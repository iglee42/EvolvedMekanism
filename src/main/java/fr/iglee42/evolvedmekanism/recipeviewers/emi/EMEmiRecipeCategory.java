package fr.iglee42.evolvedmekanism.recipeviewers.emi;

import java.util.Collections;
import java.util.List;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class EMEmiRecipeCategory extends EmiRecipeCategory {

    private final Component name;
    private final int xOffset;
    private final int yOffset;
    private final int width;
    private final int height;

    public EMEmiRecipeCategory(ResourceLocation id, EmiRenderable icon, Component name, int xOffset, int yOffset, int width, int height) {
        super(id, icon);
        this.name = name;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.width = width;
        this.height = height;
    }

    @Override
    public Component getName() {
        return name;
    }

    public int xOffset() {
        return xOffset;
    }

    public int yOffset() {
        return yOffset;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }
}
