package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.crafting.PersonalBarrelUpgrading;
import fr.iglee42.evolvedmekanism.crafting.PersonalChestUpgrading;
import fr.iglee42.evolvedmekanism.impl.*;
import fr.iglee42.evolvedmekanism.recipes.AlloyerRecipe;
import fr.iglee42.evolvedmekanism.recipes.ChemixerRecipe;
import fr.iglee42.evolvedmekanism.recipes.SolidificationRecipe;
import fr.iglee42.evolvedmekanism.recipes.serializer.AlloyerRecipeSerializer;
import fr.iglee42.evolvedmekanism.recipes.serializer.ChemixerRecipeSerializer;
import fr.iglee42.evolvedmekanism.recipes.serializer.ItemStackToFluidRecipeSerializer;
import fr.iglee42.evolvedmekanism.recipes.serializer.SolidificationRecipeSerializer;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.api.recipes.PressurizedReactionRecipe;
import mekanism.common.recipe.impl.InjectingIRecipe;
import mekanism.common.recipe.impl.PressurizedReactionIRecipe;
import mekanism.common.recipe.serializer.ItemStackGasToItemStackRecipeSerializer;
import mekanism.common.recipe.serializer.PressurizedReactionRecipeSerializer;
import mekanism.common.registration.impl.RecipeSerializerDeferredRegister;
import mekanism.common.registration.impl.RecipeSerializerRegistryObject;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class EMRecipeSerializers {

    public static final RecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new RecipeSerializerDeferredRegister(EvolvedMekanism.MODID);

    public static final RecipeSerializerRegistryObject<PersonalChestUpgrading> PERSONAL_CHEST_UPGRADING = RECIPE_SERIALIZERS.register("personal_chest_upgrade",()->new SimpleCraftingRecipeSerializer<>(PersonalChestUpgrading::new));
    public static final RecipeSerializerRegistryObject<PersonalBarrelUpgrading> PERSONAL_BARREL_UPGRADING = RECIPE_SERIALIZERS.register("personal_barrel_upgrade",()->new SimpleCraftingRecipeSerializer<>(PersonalBarrelUpgrading::new));
    public static final RecipeSerializerRegistryObject<AlloyerRecipe> ALLOYER = RECIPE_SERIALIZERS.register("alloying", () -> new AlloyerRecipeSerializer<>(AlloyerIRecipe::new));
    public static final RecipeSerializerRegistryObject<ChemixerRecipe> CHEMIXER = RECIPE_SERIALIZERS.register("chemixing", () -> new ChemixerRecipeSerializer<>(ChemixerIRecipe::new));
    public static final RecipeSerializerRegistryObject<ItemStackGasToItemStackRecipe> APT = RECIPE_SERIALIZERS.register("apt", () -> new ItemStackGasToItemStackRecipeSerializer<>(APTIRecipe::new));
    public static final RecipeSerializerRegistryObject<ItemStackToFluidRecipe> MELTER = RECIPE_SERIALIZERS.register("melting", () -> new ItemStackToFluidRecipeSerializer<>(MelterIRecipe::new));
    public static final RecipeSerializerRegistryObject<SolidificationRecipe> SOLIDIFICATION = RECIPE_SERIALIZERS.register("solidifying", () -> new SolidificationRecipeSerializer<>(SolidificationIRecipe::new));


}
