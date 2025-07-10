package fr.iglee42.evolvedmekanism.mixins;

import fr.iglee42.evolvedmekanism.interfaces.EMInputRecipeCache;
import fr.iglee42.evolvedmekanism.recipes.AlloyerRecipe;
import fr.iglee42.evolvedmekanism.recipes.ChemixerRecipe;
import fr.iglee42.evolvedmekanism.recipes.SolidificationRecipe;
import fr.iglee42.evolvedmekanism.recipes.vanilla_input.TriItemRecipeInput;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.recipes.ItemStackChemicalToItemStackRecipe;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.Mekanism;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeInput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(MekanismRecipeType.class)
public abstract class MekanismRecipeTypeMixin {


    @Shadow
    protected static <VANILLA_INPUT extends RecipeInput, RECIPE extends MekanismRecipe<VANILLA_INPUT>, INPUT_CACHE extends IInputRecipeCache> RecipeTypeRegistryObject<VANILLA_INPUT, RECIPE, INPUT_CACHE> register(ResourceLocation name, Function<MekanismRecipeType<VANILLA_INPUT, RECIPE, INPUT_CACHE>, INPUT_CACHE> inputCacheCreator) {
        return null;
    }


    @Inject(method = "<clinit>", at= @At("TAIL"))
    private static void evolvedmekanism$initEmRecipe(CallbackInfo ci){
        EMRecipeType.ALLOYING = register(Mekanism.rl("alloying"), recipeType -> new EMInputRecipeCache.TripleItem<>(recipeType, AlloyerRecipe::getMainInput, AlloyerRecipe::getExtraInput, AlloyerRecipe::getTertiaryExtraInput));
        EMRecipeType.CHEMIXING = register(Mekanism.rl("chemixing"), recipeType -> new EMInputRecipeCache.ItemItemChemical<>(recipeType, ChemixerRecipe::getInputMain, ChemixerRecipe::getInputExtra, ChemixerRecipe::getInputGas));
        EMRecipeType.APT = register(Mekanism.rl("apt"), recipeType -> new InputRecipeCache.ItemChemical<>(recipeType, ItemStackChemicalToItemStackRecipe::getItemInput,
                ItemStackChemicalToItemStackRecipe::getChemicalInput));
        EMRecipeType.MELTING = register(Mekanism.rl("melting"), recipeType -> new InputRecipeCache.SingleItem<>(recipeType, ItemStackToFluidRecipe::getInput));
        EMRecipeType.SOLIDIFICATION =register(Mekanism.rl("solidification"), recipeType -> new EMInputRecipeCache.ItemFluidFluid<>(recipeType, SolidificationRecipe::getInputSolid,
                SolidificationRecipe::getInputFluid, SolidificationRecipe::getFluidInputExtra));
    }



}
