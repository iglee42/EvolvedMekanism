package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.impl.*;
import fr.iglee42.evolvedmekanism.recipes.AlloyerRecipe;
import fr.iglee42.evolvedmekanism.recipes.ChemixerRecipe;
import fr.iglee42.evolvedmekanism.recipes.serializer.AlloyerRecipeSerializer;
import fr.iglee42.evolvedmekanism.recipes.serializer.ChemixerRecipeSerializer;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.common.recipe.serializer.ItemStackGasToItemStackRecipeSerializer;
import mekanism.common.registration.impl.RecipeSerializerDeferredRegister;
import mekanism.common.registration.impl.RecipeSerializerRegistryObject;

public class EMRecipeSerializers {

    public static final RecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new RecipeSerializerDeferredRegister(EvolvedMekanism.MODID);

    public static final RecipeSerializerRegistryObject<AlloyerRecipe> ALLOYER = RECIPE_SERIALIZERS.register("alloying", () -> new AlloyerRecipeSerializer<>(AlloyerIRecipe::new));
    public static final RecipeSerializerRegistryObject<ChemixerRecipe> CHEMIXER = RECIPE_SERIALIZERS.register("chemixing", () -> new ChemixerRecipeSerializer<>(ChemixerIRecipe::new));
    public static final RecipeSerializerRegistryObject<ItemStackGasToItemStackRecipe> APT = RECIPE_SERIALIZERS.register("apt", () -> new ItemStackGasToItemStackRecipeSerializer<>(APTIRecipe::new));
}