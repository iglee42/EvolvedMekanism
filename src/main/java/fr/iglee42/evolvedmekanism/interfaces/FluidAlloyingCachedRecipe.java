package fr.iglee42.evolvedmekanism.interfaces;

import java.util.Objects;
import java.util.function.BooleanSupplier;

import fr.iglee42.evolvedmekanism.recipes.FluidAlloyingRecipe;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public class FluidAlloyingCachedRecipe extends CachedRecipe<FluidAlloyingRecipe> {

    private final IInputHandler<@NotNull FluidStack> inputHandler;
    private final IInputHandler<@NotNull FluidStack> extraHandler;
    private final IInputHandler<@NotNull FluidStack> tertiaryHandler;
    private final IOutputHandler<@NotNull FluidStack> outputHandler;

    private FluidStack recipeMain = FluidStack.EMPTY;
    private FluidStack recipeExtra = FluidStack.EMPTY;
    private FluidStack recipeTertiary = FluidStack.EMPTY;
    @Nullable
    private FluidStack output;

    public FluidAlloyingCachedRecipe(FluidAlloyingRecipe recipe, BooleanSupplier recheckAllErrors, IInputHandler<@NotNull FluidStack> inputHandler,
                                     IInputHandler<@NotNull FluidStack> extraHandler, IInputHandler<@NotNull FluidStack> tertiaryHandler,
                                     IOutputHandler<@NotNull FluidStack> outputHandler) {
        super(recipe, recheckAllErrors);
        this.inputHandler = Objects.requireNonNull(inputHandler, "Main input handler cannot be null.");
        this.extraHandler = Objects.requireNonNull(extraHandler, "Secondary input handler cannot be null.");
        this.tertiaryHandler = Objects.requireNonNull(tertiaryHandler, "Tertiary input handler cannot be null.");
        this.outputHandler = Objects.requireNonNull(outputHandler, "Output handler cannot be null.");
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (!tracker.shouldContinueChecking()) {
            return;
        }
        recipeMain = inputHandler.getRecipeInput(recipe.getMainInput());
        if (recipeMain.isEmpty()) {
            tracker.mismatchedRecipe();
            return;
        }
        recipeExtra = extraHandler.getRecipeInput(recipe.getExtraInput());
        if (recipeExtra.isEmpty()) {
            tracker.mismatchedRecipe();
            return;
        }
        recipeTertiary = tertiaryHandler.getRecipeInput(recipe.getTertiaryExtraInput());
        if (recipeTertiary.isEmpty()) {
            tracker.mismatchedRecipe();
            return;
        }
        inputHandler.calculateOperationsCanSupport(tracker, recipeMain);
        if (!tracker.shouldContinueChecking()) {
            return;
        }
        extraHandler.calculateOperationsCanSupport(tracker, recipeExtra);
        if (!tracker.shouldContinueChecking()) {
            return;
        }
        tertiaryHandler.calculateOperationsCanSupport(tracker, recipeTertiary);
        if (tracker.shouldContinueChecking()) {
            output = recipe.getOutput(recipeMain, recipeExtra, recipeTertiary);
            outputHandler.calculateOperationsCanSupport(tracker, output);
        }
    }

    @Override
    public boolean isInputValid() {
        FluidStack main = inputHandler.getInput();
        FluidStack extra = extraHandler.getInput();
        FluidStack tertiary = tertiaryHandler.getInput();
        return !main.isEmpty() && !extra.isEmpty() && !tertiary.isEmpty() && recipe.test(main, extra, tertiary);
    }

    @Override
    protected void finishProcessing(int operations) {
        if (output != null && !output.isEmpty() && !recipeMain.isEmpty() && !recipeExtra.isEmpty() && !recipeTertiary.isEmpty()) {
            inputHandler.use(recipeMain, operations);
            extraHandler.use(recipeExtra, operations);
            tertiaryHandler.use(recipeTertiary, operations);
            outputHandler.handleOutput(output, operations);
        }
    }
}
