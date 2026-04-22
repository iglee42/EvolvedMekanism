package fr.iglee42.evolvedmekanism.registries;

import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;

public class EMRecipeType{

    public static RecipeTypeRegistryObject<ItemStackGasToItemStackRecipe, InputRecipeCache.ItemChemical<Gas, GasStack, ItemStackGasToItemStackRecipe>> APT;
}