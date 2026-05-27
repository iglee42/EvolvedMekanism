package fr.iglee42.evolvedmekanism.interfaces;

import fr.iglee42.evolvedmekanism.recipes.MeltingRecipe;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.functions.ConstantPredicates;
import mekanism.api.recipes.ElectrolysisRecipe;
import mekanism.api.recipes.ItemStackToFluidOptionalItemRecipe;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipeHelper;
import mekanism.api.recipes.cache.OneInputCachedRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.*;

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

    /**
     * Base implementation for handling ItemStack to Fluid Recipes.
     *
     * @param recipe           Recipe.
     * @param recheckAllErrors Returns {@code true} if processing should be continued even if an error is hit in order to gather all the errors. It is recommended to not
     *                         do this every tick or if there is no one viewing recipes.
     * @param inputHandler     Input handler.
     * @param outputHandler    Output handler.
     */
    public static OneInputCachedRecipe<@NotNull ItemStack, @NotNull FluidStack, MeltingRecipe> itemToFluid(MeltingRecipe recipe,
                                                                                                                     BooleanSupplier recheckAllErrors, IInputHandler<@NotNull ItemStack> inputHandler, IOutputHandler<@NotNull FluidStack> outputHandler) {
        return new OneInputCachedRecipe<>(recipe, recheckAllErrors, inputHandler, outputHandler, recipe::getInput, recipe::getOutput, ConstantPredicates.ITEM_EMPTY,
                ConstantPredicates.FLUID_EMPTY);
    }

    @NothingNullByDefault
    public static class OneInputCachedRecipe<INPUT, OUTPUT, RECIPE extends MekanismRecipe<?> & Predicate<INPUT>> extends CachedRecipe<RECIPE> {
        private final IInputHandler<INPUT> inputHandler;
        private final IOutputHandler<OUTPUT> outputHandler;
        private final Predicate<INPUT> inputEmptyCheck;
        private final Supplier<? extends InputIngredient<INPUT>> inputSupplier;
        private final Function<INPUT, OUTPUT> outputGetter;
        private final Predicate<OUTPUT> outputEmptyCheck;
        private final Consumer<INPUT> inputSetter;
        private final Consumer<OUTPUT> outputSetter;

        //Note: Our input and output shouldn't be null in places they are actually used, but we mark them as nullable, so we don't have to initialize them
        @Nullable
        private INPUT input;
        @Nullable
        private OUTPUT output;

        /**
         * @param recipe           Recipe.
         * @param recheckAllErrors Returns {@code true} if processing should be continued even if an error is hit in order to gather all the errors. It is recommended to not
         *                         do this every tick or if there is no one viewing recipes.
         * @param inputHandler     Input handler.
         * @param outputHandler    Output handler.
         * @param inputSupplier    Supplier of the recipe's input ingredient.
         * @param outputGetter     Gets the recipe's output when given the corresponding input.
         * @param inputEmptyCheck  Checks if the input is empty.
         * @param outputEmptyCheck Checks if the output is empty (indicating something went horribly wrong).
         */
        protected OneInputCachedRecipe(RECIPE recipe, BooleanSupplier recheckAllErrors, IInputHandler<INPUT> inputHandler, IOutputHandler<OUTPUT> outputHandler,
                                       Supplier<? extends InputIngredient<INPUT>> inputSupplier, Function<INPUT, OUTPUT> outputGetter, Predicate<INPUT> inputEmptyCheck,
                                       Predicate<OUTPUT> outputEmptyCheck) {
            super(recipe, recheckAllErrors);
            this.inputHandler = Objects.requireNonNull(inputHandler, "Input handler cannot be null.");
            this.outputHandler = Objects.requireNonNull(outputHandler, "Output handler cannot be null.");
            this.inputSupplier = Objects.requireNonNull(inputSupplier, "Input ingredient supplier cannot be null.");
            this.outputGetter = Objects.requireNonNull(outputGetter, "Output getter cannot be null.");
            this.inputEmptyCheck = Objects.requireNonNull(inputEmptyCheck, "Input empty check cannot be null.");
            this.outputEmptyCheck = Objects.requireNonNull(outputEmptyCheck, "Output empty check cannot be null.");
            this.inputSetter = input -> this.input = input;
            this.outputSetter = output -> this.output = output;
        }


        @Override
        protected void calculateOperationsThisTick(OperationTracker tracker) {
            super.calculateOperationsThisTick(tracker);
            CachedRecipeHelper.oneInputCalculateOperationsThisTick(tracker, inputHandler, inputSupplier, inputSetter, outputHandler, outputGetter, outputSetter, inputEmptyCheck);
        }

        @Override
        public boolean isInputValid() {
            INPUT input = inputHandler.getInput();
            return !inputEmptyCheck.test(input) && recipe.test(input);
        }

        @Override
        protected void finishProcessing(int operations) {
            //Validate something didn't go horribly wrong
            if (input != null && output != null && !inputEmptyCheck.test(input) && !outputEmptyCheck.test(output)) {
                inputHandler.use(input, operations);
                outputHandler.handleOutput(output, operations);
            }
        }
    }
}
