package fr.iglee42.evolvedmekanism.recipeviewers.emi.categories;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiStack;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import mekanism.client.recipe_viewer.emi.MekanismEmiRecipeCategory;
import mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;

public class APTEMIRecipeCategory extends MekanismEmiRecipeCategory {

    public static APTEMIRecipeCategory create(IRecipeViewerRecipeType<?> recipeType) {
        ItemStack stack = recipeType.iconStack();
        ResourceLocation icon = recipeType.icon();
        if (stack.isEmpty()) {
            if (icon == null) {
                throw new IllegalStateException("Expected recipe type to have either an icon stack or an icon location");
            }
            return new APTEMIRecipeCategory(recipeType, renderIcon(icon));
        }
        if (icon == null) {
            return new APTEMIRecipeCategory(recipeType, EmiStack.of(stack));
        }
        return new APTEMIRecipeCategory(recipeType, EmiStack.of(stack), renderIcon(icon));
    }

    private static EmiRenderable renderIcon(ResourceLocation icon) {
        return (graphics, x, y, delta) -> graphics.blit(icon, x - 1, y - 1, 0, 0, 18, 18, 18, 18);
    }

    private final IRecipeViewerRecipeType<?> recipeType;

    private APTEMIRecipeCategory(IRecipeViewerRecipeType<?> recipeType, EmiRenderable icon) {
        super(recipeType, icon,icon);
        this.recipeType = recipeType;
    }

    public APTEMIRecipeCategory(IRecipeViewerRecipeType<?> recipeType, EmiRenderable icon, EmiRenderable simplified) {
        super(recipeType, icon, simplified);
        this.recipeType = recipeType;
    }

    @Override
    public Component getName() {
        return EvolvedMekanismLang.APT.translate();
    }

    public int xOffset() {
        return recipeType.xOffset();
    }

    public int yOffset() {
        return recipeType.yOffset();
    }

    public int width() {
        return recipeType.width();
    }

    public int height() {
        return recipeType.height();
    }
}