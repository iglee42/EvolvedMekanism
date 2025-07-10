package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.interfaces.EMInputRecipeCache;
import fr.iglee42.evolvedmekanism.recipes.AlloyerRecipe;
import fr.iglee42.evolvedmekanism.recipes.ChemixerRecipe;
import fr.iglee42.evolvedmekanism.recipes.SolidificationRecipe;
import fr.iglee42.evolvedmekanism.recipes.vanilla_input.BiItemChemicalRecipeInput;
import fr.iglee42.evolvedmekanism.recipes.vanilla_input.SingleItemBiFluidRecipeInput;
import fr.iglee42.evolvedmekanism.recipes.vanilla_input.TriItemRecipeInput;
import mekanism.api.recipes.ItemStackChemicalToItemStackRecipe;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.api.recipes.vanilla_input.SingleItemChemicalRecipeInput;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;
import net.minecraft.world.item.crafting.SingleRecipeInput;

public class EMRecipeType{


    public static RecipeTypeRegistryObject<TriItemRecipeInput,AlloyerRecipe, EMInputRecipeCache.TripleItem<AlloyerRecipe>> ALLOYING;
    public static RecipeTypeRegistryObject<BiItemChemicalRecipeInput,ChemixerRecipe, EMInputRecipeCache.ItemItemChemical<ChemixerRecipe>> CHEMIXING;
    public static RecipeTypeRegistryObject<SingleItemChemicalRecipeInput,ItemStackChemicalToItemStackRecipe, InputRecipeCache.ItemChemical<ItemStackChemicalToItemStackRecipe>> APT;
    public static RecipeTypeRegistryObject<SingleRecipeInput,ItemStackToFluidRecipe, InputRecipeCache.SingleItem<ItemStackToFluidRecipe>> MELTING;
    public static RecipeTypeRegistryObject<SingleItemBiFluidRecipeInput,SolidificationRecipe, EMInputRecipeCache.ItemFluidFluid<SolidificationRecipe>> SOLIDIFICATION;
}