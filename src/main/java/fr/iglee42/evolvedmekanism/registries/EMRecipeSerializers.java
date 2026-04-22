package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.impl.*;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.common.recipe.serializer.ItemStackGasToItemStackRecipeSerializer;
import mekanism.common.registration.impl.RecipeSerializerDeferredRegister;
import mekanism.common.registration.impl.RecipeSerializerRegistryObject;

public class EMRecipeSerializers {

    public static final RecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new RecipeSerializerDeferredRegister(EvolvedMekanism.MODID);

    public static final RecipeSerializerRegistryObject<ItemStackGasToItemStackRecipe> APT = RECIPE_SERIALIZERS.register("apt", () -> new ItemStackGasToItemStackRecipeSerializer<>(APTIRecipe::new));
}
