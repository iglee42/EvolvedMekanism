package fr.iglee42.evolvedmekanism.interfaces;

import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.ITripleRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.DoubleInputRecipeCache;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.recipe.lookup.cache.TripleInputRecipeCache;
import mekanism.common.recipe.lookup.cache.type.ChemicalInputCache;
import mekanism.common.recipe.lookup.cache.type.FluidInputCache;
import mekanism.common.recipe.lookup.cache.type.IInputCache;
import mekanism.common.recipe.lookup.cache.type.ItemInputCache;
import mekanism.common.util.ChemicalUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.TriPredicate;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class EMInputRecipeCache {
    public static class TripleItem<RECIPE extends MekanismRecipe<?> & TriPredicate<ItemStack,ItemStack, ItemStack>>
            extends TripleSameInputRecipeCache<ItemStack, ItemStackIngredient, RECIPE, ItemInputCache<RECIPE>> {

        public <INPUT_CACHE extends IInputRecipeCache> TripleItem(MekanismRecipeType<?,RECIPE, ?> recipeType, Function<RECIPE, ItemStackIngredient> inputAExtractor,
                                                                  Function<RECIPE, ItemStackIngredient> inputBExtractor, Function<RECIPE, ItemStackIngredient> inputCExtractor) {
            super(recipeType, inputAExtractor, inputBExtractor,inputCExtractor, ItemInputCache::new);
        }

    }
    /**
     * Helper expansion class for {@link DoubleInputRecipeCache} to simplify the generics when both inputs are of the same type.
     */
    public abstract static class TripleSameInputRecipeCache<INPUT, INGREDIENT extends InputIngredient<INPUT>, RECIPE extends MekanismRecipe<?> & TriPredicate<INPUT, INPUT,INPUT>,
            CACHE extends IInputCache<INPUT, INGREDIENT, RECIPE>> extends TripleInputRecipeCache<INPUT, INGREDIENT, INPUT, INGREDIENT,INPUT,INGREDIENT, RECIPE, CACHE, CACHE,CACHE> {

        protected TripleSameInputRecipeCache(MekanismRecipeType<?,RECIPE, ?> recipeType, Function<RECIPE, INGREDIENT> inputAExtractor,
                                             Function<RECIPE, INGREDIENT> inputBExtractor, Function<RECIPE,INGREDIENT> inputCExtractor, Supplier<CACHE> cacheSupplier) {
            super(recipeType, inputAExtractor, cacheSupplier.get(), inputBExtractor, cacheSupplier.get(),inputCExtractor,cacheSupplier.get());
        }
    }

    public interface IFindRecipes<INPUT_A, INGREDIENT_A extends InputIngredient<INPUT_A>, INPUT_B, INGREDIENT_B extends InputIngredient<INPUT_B>,
            INPUT_C, INGREDIENT_C extends InputIngredient<INPUT_C>, RECIPE extends MekanismRecipe<?> & TriPredicate<INPUT_A, INPUT_B, INPUT_C>,
            CACHE_A extends IInputCache<INPUT_A, INGREDIENT_A, RECIPE>, CACHE_B extends IInputCache<INPUT_B, INGREDIENT_B, RECIPE>,
            CACHE_C extends IInputCache<INPUT_C, INGREDIENT_C, RECIPE>>{
        @Nullable
        public RECIPE findTypeBasedRecipe(@Nullable Level world, INPUT_A inputA, INPUT_B inputB,INPUT_C inputC, Predicate<RECIPE> matchCriteria);
    }

    public static class ItemItemChemical<RECIPE extends MekanismRecipe<?> &
            TriPredicate<ItemStack, ItemStack, ChemicalStack>> extends TripleInputRecipeCache<ItemStack, ItemStackIngredient, ItemStack, ItemStackIngredient, ChemicalStack,
            ChemicalStackIngredient, RECIPE, ItemInputCache<RECIPE>, ItemInputCache<RECIPE>, ChemicalInputCache<RECIPE>> {

        public ItemItemChemical(MekanismRecipeType<?,RECIPE, ?> recipeType, Function<RECIPE, ItemStackIngredient> inputAExtractor,
                                Function<RECIPE, ItemStackIngredient> inputBExtractor, Function<RECIPE, ChemicalStackIngredient> inputCExtractor) {
            super(recipeType, inputAExtractor, new ItemInputCache<>(), inputBExtractor, new ItemInputCache<>(), inputCExtractor, new ChemicalInputCache<>());
        }
    }

    public static class ItemFluidFluid<RECIPE extends MekanismRecipe<?> &
            TriPredicate<ItemStack, FluidStack, FluidStack>> extends TripleInputRecipeCache<ItemStack, ItemStackIngredient, FluidStack, FluidStackIngredient, FluidStack,
            FluidStackIngredient, RECIPE, ItemInputCache<RECIPE>, FluidInputCache<RECIPE>, FluidInputCache<RECIPE>> {

        public ItemFluidFluid(MekanismRecipeType<?,RECIPE, ?> recipeType, Function<RECIPE, ItemStackIngredient> inputAExtractor,
                                 Function<RECIPE, FluidStackIngredient> inputBExtractor, Function<RECIPE, FluidStackIngredient> inputCExtractor) {
            super(recipeType, inputAExtractor, new ItemInputCache<>(), inputBExtractor, new FluidInputCache<>(), inputCExtractor, new FluidInputCache<>());
        }
    }

    /**
     * Helper interface to make the generics that we have to pass to {@link ITripleRecipeLookupHandler} not as messy.
     */
    public interface ItemFluidFluidRecipeLookupHandler<RECIPE extends MekanismRecipe<?> &
            TriPredicate<ItemStack, FluidStack, FluidStack>> extends ObjectObjectObjectRecipeLookupHandler<ItemStack, FluidStack, FluidStack, RECIPE,
            ItemFluidFluid<RECIPE>> {
    }

    interface ObjectObjectObjectRecipeLookupHandler<INPUT_A, INPUT_B, INPUT_C,
            RECIPE extends MekanismRecipe<?> & TriPredicate<INPUT_A, INPUT_B, INPUT_C>, INPUT_CACHE extends TripleInputRecipeCache<INPUT_A, ?, INPUT_B, ?, INPUT_C, ?, RECIPE, ?, ?, ?>>
            extends ITripleRecipeLookupHandler<INPUT_A, INPUT_B, INPUT_C, RECIPE, INPUT_CACHE> {
    }
}
