package fr.iglee42.evolvedmekanism.interfaces;

import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.DoubleInputRecipeCache;
import mekanism.common.recipe.lookup.cache.TripleInputRecipeCache;
import mekanism.common.recipe.lookup.cache.type.ChemicalInputCache;
import mekanism.common.recipe.lookup.cache.type.FluidInputCache;
import mekanism.common.recipe.lookup.cache.type.IInputCache;
import mekanism.common.recipe.lookup.cache.type.ItemInputCache;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class EMInputRecipeCache {
    public static class TripleItem<RECIPE extends MekanismRecipe & TriPredicate<ItemStack,ItemStack, ItemStack>>
            extends TripleSameInputRecipeCache<ItemStack, ItemStackIngredient, RECIPE, ItemInputCache<RECIPE>> {

        public TripleItem(MekanismRecipeType<RECIPE, ?> recipeType, Function<RECIPE, ItemStackIngredient> inputAExtractor,
                          Function<RECIPE, ItemStackIngredient> inputBExtractor, Function<RECIPE, ItemStackIngredient> inputCExtractor) {
            super(recipeType, inputAExtractor, inputBExtractor,inputCExtractor, ItemInputCache::new);
        }


    }
    /**
     * Helper expansion class for {@link DoubleInputRecipeCache} to simplify the generics when both inputs are of the same type.
     */
    public abstract static class TripleSameInputRecipeCache<INPUT, INGREDIENT extends InputIngredient<INPUT>, RECIPE extends MekanismRecipe & TriPredicate<INPUT, INPUT,INPUT>,
            CACHE extends IInputCache<INPUT, INGREDIENT, RECIPE>> extends TripleInputRecipeCache<INPUT, INGREDIENT, INPUT, INGREDIENT,INPUT,INGREDIENT, RECIPE, CACHE, CACHE,CACHE> {

        protected TripleSameInputRecipeCache(MekanismRecipeType<RECIPE, ?> recipeType, Function<RECIPE, INGREDIENT> inputAExtractor,
                                             Function<RECIPE, INGREDIENT> inputBExtractor, Function<RECIPE,INGREDIENT> inputCExtractor, Supplier<CACHE> cacheSupplier) {
            super(recipeType, inputAExtractor, cacheSupplier.get(), inputBExtractor, cacheSupplier.get(),inputCExtractor,cacheSupplier.get());
        }
    }

    public interface IFindRecipes<INPUT_A, INGREDIENT_A extends InputIngredient<INPUT_A>, INPUT_B, INGREDIENT_B extends InputIngredient<INPUT_B>,
            INPUT_C, INGREDIENT_C extends InputIngredient<INPUT_C>, RECIPE extends MekanismRecipe & TriPredicate<INPUT_A, INPUT_B, INPUT_C>,
            CACHE_A extends IInputCache<INPUT_A, INGREDIENT_A, RECIPE>, CACHE_B extends IInputCache<INPUT_B, INGREDIENT_B, RECIPE>,
            CACHE_C extends IInputCache<INPUT_C, INGREDIENT_C, RECIPE>>{
        @Nullable
        public RECIPE findTypeBasedRecipe(@Nullable Level world, INPUT_A inputA, INPUT_B inputB,INPUT_C inputC, Predicate<RECIPE> matchCriteria);
    }

    public static class ItemItemChemical<CHEMICAL extends Chemical<CHEMICAL>, STACK extends ChemicalStack<CHEMICAL>, RECIPE extends MekanismRecipe &
            TriPredicate<ItemStack, ItemStack, STACK>> extends TripleInputRecipeCache<ItemStack, ItemStackIngredient, ItemStack, ItemStackIngredient, STACK,
            ChemicalStackIngredient<CHEMICAL, STACK>, RECIPE, ItemInputCache<RECIPE>, ItemInputCache<RECIPE>, ChemicalInputCache<CHEMICAL, STACK, RECIPE>> {

        public ItemItemChemical(MekanismRecipeType<RECIPE, ?> recipeType, Function<RECIPE, ItemStackIngredient> inputAExtractor,
                                Function<RECIPE, ItemStackIngredient> inputBExtractor, Function<RECIPE, ChemicalStackIngredient<CHEMICAL, STACK>> inputCExtractor) {
            super(recipeType, inputAExtractor, new ItemInputCache<>(), inputBExtractor, new ItemInputCache<>(), inputCExtractor, new ChemicalInputCache<>());
        }
    }
}
