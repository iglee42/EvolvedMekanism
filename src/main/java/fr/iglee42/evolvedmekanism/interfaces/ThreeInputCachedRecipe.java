package fr.iglee42.evolvedmekanism.interfaces;

import fr.iglee42.evolvedmekanism.recipes.AlloyerRecipe;
import fr.iglee42.evolvedmekanism.recipes.EMCachedRecipeHelper;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.TriPredicate;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Base class to help implement handling of recipes with two inputs.
 */
@NothingNullByDefault
public class ThreeInputCachedRecipe<INPUT_A, INPUT_B, INPUT_C, OUTPUT, RECIPE extends MekanismRecipe<?> & TriPredicate<INPUT_A, INPUT_B, INPUT_C>> extends CachedRecipe<RECIPE> {

    private final IInputHandler<INPUT_A> inputHandler;
    private final IInputHandler<INPUT_B> secondaryInputHandler;
    private final IInputHandler<INPUT_C> tertiaryInputHandler;
    private final IOutputHandler<OUTPUT> outputHandler;
    private final Predicate<INPUT_A> inputEmptyCheck;
    private final Predicate<INPUT_B> secondaryInputEmptyCheck;
    private final Predicate<INPUT_C> tertiaryInputEmptyCheck;
    private final Supplier<? extends InputIngredient<INPUT_A>> inputSupplier;
    private final Supplier<? extends InputIngredient<INPUT_B>> secondaryInputSupplier;
    private final Supplier<? extends InputIngredient<INPUT_C>> tertiaryInputSupplier;
    private final TriFunction<INPUT_A, INPUT_B, INPUT_C, OUTPUT> outputGetter;
    private final Predicate<OUTPUT> outputEmptyCheck;

    //Note: Our inputs and outputs shouldn't be null in places they are actually used, but we mark them as nullable, so we don't have to initialize them
    @Nullable
    private INPUT_A input;
    @Nullable
    private INPUT_B secondaryInput;
    @Nullable
    private INPUT_C tertiaryInput;
    @Nullable
    private OUTPUT output;

    /**
     * @param recipe                   Recipe.
     * @param recheckAllErrors         Returns {@code true} if processing should be continued even if an error is hit in order to gather all the errors. It is recommended
     *                                 to not do this every tick or if there is no one viewing recipes.
     * @param inputHandler             Main input handler.
     * @param secondaryInputHandler    Secondary input handler.
     * @param outputHandler            Output handler.
     * @param inputSupplier            Supplier of the recipe's input ingredient.
     * @param secondaryInputSupplier   Supplier of the recipe's secondary input ingredient.
     * @param outputGetter             Gets the recipe's output when given the corresponding inputs.
     * @param inputEmptyCheck          Checks if the primary input is empty.
     * @param secondaryInputEmptyCheck Checks if the secondary input is empty.
     * @param outputEmptyCheck         Checks if the output is empty (indicating something went horribly wrong).
     */
    protected ThreeInputCachedRecipe(RECIPE recipe, BooleanSupplier recheckAllErrors, IInputHandler<INPUT_A> inputHandler, IInputHandler<INPUT_B> secondaryInputHandler, IInputHandler<INPUT_C> tertiaryInputHandler,
                                     IOutputHandler<OUTPUT> outputHandler, Supplier<InputIngredient<INPUT_A>> inputSupplier, Supplier<InputIngredient<INPUT_B>> secondaryInputSupplier, Supplier<InputIngredient<INPUT_C>> tertiaryInputSupplier,
                                     TriFunction<INPUT_A, INPUT_B, INPUT_C, OUTPUT> outputGetter, Predicate<INPUT_A> inputEmptyCheck, Predicate<INPUT_B> secondaryInputEmptyCheck, Predicate<INPUT_C> tertiaryInputEmptyCheck,
                                     Predicate<OUTPUT> outputEmptyCheck) {
        super(recipe, recheckAllErrors);
        this.inputHandler = Objects.requireNonNull(inputHandler, "Input handler cannot be null.");
        this.secondaryInputHandler = Objects.requireNonNull(secondaryInputHandler, "Secondary input handler cannot be null.");
        this.tertiaryInputHandler = Objects.requireNonNull(tertiaryInputHandler, "Tertiary input handler cannot be null.");
        this.outputHandler = Objects.requireNonNull(outputHandler, "Output handler cannot be null.");
        this.inputSupplier = Objects.requireNonNull(inputSupplier, "Input ingredient supplier cannot be null.");
        this.secondaryInputSupplier = Objects.requireNonNull(secondaryInputSupplier, "Secondary input ingredient supplier cannot be null.");
        this.tertiaryInputSupplier = Objects.requireNonNull(tertiaryInputSupplier, "Tertiary input ingredient supplier cannot be null.");
        this.outputGetter = Objects.requireNonNull(outputGetter, "Output getter cannot be null.");
        this.inputEmptyCheck = Objects.requireNonNull(inputEmptyCheck, "Input empty check cannot be null.");
        this.secondaryInputEmptyCheck = Objects.requireNonNull(secondaryInputEmptyCheck, "Secondary input empty check cannot be null.");
        this.tertiaryInputEmptyCheck = Objects.requireNonNull(tertiaryInputEmptyCheck, "Tertiary input empty check cannot be null.");
        this.outputEmptyCheck = Objects.requireNonNull(outputEmptyCheck, "Output empty check cannot be null.");
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        EMCachedRecipeHelper.threeInputCalculateOperationsThisTick(tracker, inputHandler, inputSupplier, secondaryInputHandler, secondaryInputSupplier, tertiaryInputHandler, tertiaryInputSupplier, (input, secondary,tertiary) -> {
            this.input = input;
            this.secondaryInput = secondary;
            this.tertiaryInput = tertiary;
        }, outputHandler, outputGetter, output -> this.output = output, inputEmptyCheck, secondaryInputEmptyCheck, tertiaryInputEmptyCheck);
    }

    @Override
    public boolean isInputValid() {
        INPUT_A input = inputHandler.getInput();
        if (inputEmptyCheck.test(input)) {
            return false;
        }
        INPUT_B secondaryInput = secondaryInputHandler.getInput();
        if (secondaryInputEmptyCheck.test(secondaryInput)) {
            return false;
        }
        INPUT_C tertiaryInput = tertiaryInputHandler.getInput();
        return !tertiaryInputEmptyCheck.test(tertiaryInput) && recipe.test(input, secondaryInput,tertiaryInput);
    }

    @Override
    protected void finishProcessing(int operations) {
        //Validate something didn't go horribly wrong
        if (input != null && secondaryInput != null && tertiaryInput != null && output != null && !inputEmptyCheck.test(input) && !secondaryInputEmptyCheck.test(secondaryInput) && !tertiaryInputEmptyCheck.test(tertiaryInput) &&
            !outputEmptyCheck.test(output)) {
            inputHandler.use(input, operations);
            secondaryInputHandler.use(secondaryInput, operations);
            tertiaryInputHandler.use(tertiaryInput, operations);
            outputHandler.handleOutput(output, operations);
        }
    }

  /*
    public static <CHEMICAL extends Chemical<CHEMICAL>, STACK extends ChemicalStack<CHEMICAL>, INGREDIENT extends ChemicalStackIngredient<CHEMICAL, STACK>,
          RECIPE extends FluidChemicalToChemicalRecipe<CHEMICAL, STACK, INGREDIENT>>
    ThreeInputCachedRecipe<@NotNull FluidStack, @NotNull STACK, @NotNull STACK, RECIPE> fluidChemicalToChemical(RECIPE recipe, BooleanSupplier recheckAllErrors,
                                                                                                                IInputHandler<@NotNull FluidStack> fluidInputHandler, IInputHandler<@NotNull STACK> chemicalInputHandler, IOutputHandler<@NotNull STACK> outputHandler) {
        return new ThreeInputCachedRecipe<>(recipe, recheckAllErrors, fluidInputHandler, chemicalInputHandler, outputHandler, recipe::getFluidInput,
              recipe::getChemicalInput, recipe::getOutput, FluidStack::isEmpty, ChemicalStack::isEmpty, ChemicalStack::isEmpty);
    }


    public static <CHEMICAL extends Chemical<CHEMICAL>, STACK extends ChemicalStack<CHEMICAL>, INGREDIENT extends ChemicalStackIngredient<CHEMICAL, STACK>,
          RECIPE extends ItemStackChemicalToItemStackRecipe<CHEMICAL, STACK, INGREDIENT>>
    ThreeInputCachedRecipe<@NotNull ItemStack, @NotNull STACK, @NotNull ItemStack, RECIPE> itemChemicalToItem(RECIPE recipe, BooleanSupplier recheckAllErrors,
                                                                                                              IInputHandler<@NotNull ItemStack> itemInputHandler, IInputHandler<@NotNull STACK> chemicalInputHandler, IOutputHandler<@NotNull ItemStack> outputHandler) {
        return new ThreeInputCachedRecipe<>(recipe, recheckAllErrors, itemInputHandler, chemicalInputHandler, outputHandler, recipe::getItemInput, recipe::getChemicalInput,
              recipe::getOutput, ItemStack::isEmpty, ChemicalStack::isEmpty, ItemStack::isEmpty);
    }*/


    public static ThreeInputCachedRecipe<@NotNull ItemStack, @NotNull ItemStack, @NotNull ItemStack, @NotNull ItemStack, AlloyerRecipe> alloyer(AlloyerRecipe recipe,
                                                                                                                             BooleanSupplier recheckAllErrors, IInputHandler<@NotNull ItemStack> inputHandler, IInputHandler<@NotNull ItemStack> extraInputHandler,IInputHandler<@NotNull ItemStack> secondExtraInputHandler,
                                                                                                                             IOutputHandler<@NotNull ItemStack> outputHandler) {
        return new ThreeInputCachedRecipe<>(recipe, recheckAllErrors, inputHandler, extraInputHandler, secondExtraInputHandler, outputHandler, recipe::getMainInput, recipe::getExtraInput, recipe::getTertiaryExtraInput,
              recipe::getOutput, ItemStack::isEmpty, ItemStack::isEmpty, ItemStack::isEmpty, ItemStack::isEmpty);
    }
}