package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.crafting.PersonalBarrelUpgrading;
import fr.iglee42.evolvedmekanism.crafting.PersonalChestUpgrading;
import fr.iglee42.evolvedmekanism.impl.AlloyerIRecipe;
import fr.iglee42.evolvedmekanism.impl.ChemixerIRecipe;
import fr.iglee42.evolvedmekanism.recipes.AlloyerRecipe;
import fr.iglee42.evolvedmekanism.recipes.ChemixerRecipe;
import fr.iglee42.evolvedmekanism.recipes.serializer.AlloyerRecipeSerializer;
import fr.iglee42.evolvedmekanism.recipes.serializer.ChemixerRecipeSerializer;
import mekanism.common.registration.impl.RecipeSerializerDeferredRegister;
import mekanism.common.registration.impl.RecipeSerializerRegistryObject;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class EMRecipeSerializers {

    public static final RecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new RecipeSerializerDeferredRegister(EvolvedMekanism.MODID);

    public static final RecipeSerializerRegistryObject<PersonalChestUpgrading> PERSONAL_CHEST_UPGRADING = RECIPE_SERIALIZERS.register("personal_chest_upgrade",()->new SimpleCraftingRecipeSerializer<>(PersonalChestUpgrading::new));
    public static final RecipeSerializerRegistryObject<PersonalBarrelUpgrading> PERSONAL_BARREL_UPGRADING = RECIPE_SERIALIZERS.register("personal_barrel_upgrade",()->new SimpleCraftingRecipeSerializer<>(PersonalBarrelUpgrading::new));
    public static final RecipeSerializerRegistryObject<AlloyerRecipe> ALLOYER = RECIPE_SERIALIZERS.register("alloying", () -> new AlloyerRecipeSerializer<>(AlloyerIRecipe::new));
    public static final RecipeSerializerRegistryObject<ChemixerRecipe> CHEMIXER = RECIPE_SERIALIZERS.register("chemixing", () -> new ChemixerRecipeSerializer<>(ChemixerIRecipe::new));


}
