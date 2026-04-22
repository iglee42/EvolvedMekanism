package fr.iglee42.evolvedmekanism.mixins;

import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.chemical.ItemStackChemicalToItemStackRecipe;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@SuppressWarnings("unused")
@Mixin(MekanismRecipeType.class)
public abstract class MekanismRecipeTypeMixin {

    @Shadow
    protected static <RECIPE extends MekanismRecipe, INPUT_CACHE extends IInputRecipeCache> RecipeTypeRegistryObject<RECIPE, INPUT_CACHE> register(String name, Function<MekanismRecipeType<RECIPE, INPUT_CACHE>, INPUT_CACHE> inputCacheCreator) {
        return null;
    }

    @Inject(method = "<clinit>", at= @At("TAIL"))
    private static void evolvedmekanism$initEmRecipe(CallbackInfo ci){
        EMRecipeType.APT = register("apt", recipeType -> new InputRecipeCache.ItemChemical<>(recipeType, ItemStackChemicalToItemStackRecipe::getItemInput,
                ItemStackChemicalToItemStackRecipe::getChemicalInput));
    }

}
