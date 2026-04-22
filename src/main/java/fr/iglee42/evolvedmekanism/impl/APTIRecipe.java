package fr.iglee42.evolvedmekanism.impl;

import fr.iglee42.evolvedmekanism.registries.EMItems;
import fr.iglee42.evolvedmekanism.registries.EMRecipeSerializers;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

@NothingNullByDefault
public class APTIRecipe extends ItemStackGasToItemStackRecipe {

    public APTIRecipe(ResourceLocation id, ItemStackIngredient itemInput, GasStackIngredient gasInput, ItemStack output) {
        super(id, itemInput, gasInput, output);
    }

    @Override
    public RecipeType<ItemStackGasToItemStackRecipe> getType() {
        return EMRecipeType.APT.get();
    }

    @Override
    public RecipeSerializer<ItemStackGasToItemStackRecipe> getSerializer() {
        return EMRecipeSerializers.APT.get();
    }

    @Override
    public String getGroup() {
        return EMItems.BETTER_GOLD_INGOT.getName();
    }

    @Override
    public ItemStack getToastSymbol() {
        return EMItems.BETTER_GOLD_INGOT.getItemStack();
    }
}