package fr.iglee42.evolvedmekanism.impl;

import fr.iglee42.evolvedmekanism.registries.EMItems;
import fr.iglee42.evolvedmekanism.registries.EMRecipeSerializers;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ItemStackChemicalToItemStackRecipe;
import mekanism.api.recipes.basic.BasicItemStackChemicalToItemStackRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

@NothingNullByDefault
public class BasicAPTRecipe extends BasicItemStackChemicalToItemStackRecipe {

    public BasicAPTRecipe(ItemStackIngredient itemInput, ChemicalStackIngredient gasInput, ItemStack output, boolean perTickUsage) {
        super( itemInput, gasInput, output,perTickUsage,EMRecipeType.APT.getRecipeType());
    }

    @Override
    public RecipeSerializer<BasicAPTRecipe> getSerializer() {
        return EMRecipeSerializers.APT.get();
    }

    @Override
    public String getGroup() {
        return EMItems.BETTER_GOLD_INGOT.getName();
    }

    @Override
    public ItemStack getToastSymbol() {
        return EMItems.BETTER_GOLD_INGOT.get().getDefaultInstance();
    }
}