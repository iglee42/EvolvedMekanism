package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.interfaces.EMInputRecipeCache;
import fr.iglee42.evolvedmekanism.recipes.AlloyerRecipe;
import fr.iglee42.evolvedmekanism.recipes.ChemixerRecipe;
import fr.iglee42.evolvedmekanism.recipes.SolidificationRecipe;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.api.recipes.PressurizedReactionRecipe;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;

public class EMRecipeType{


    public static RecipeTypeRegistryObject<AlloyerRecipe, EMInputRecipeCache.TripleItem<AlloyerRecipe>> ALLOYING;
    public static RecipeTypeRegistryObject<ChemixerRecipe, EMInputRecipeCache.ItemItemChemical<Gas, GasStack, ChemixerRecipe>> CHEMIXING;
    public static RecipeTypeRegistryObject<ItemStackGasToItemStackRecipe, InputRecipeCache.ItemChemical<Gas, GasStack, ItemStackGasToItemStackRecipe>> APT;
    public static RecipeTypeRegistryObject<ItemStackToFluidRecipe, InputRecipeCache.SingleItem<ItemStackToFluidRecipe>> MELTING;
    public static RecipeTypeRegistryObject<SolidificationRecipe, EMInputRecipeCache.ItemFluidFluid<SolidificationRecipe>> SOLIDIFICATION;
}