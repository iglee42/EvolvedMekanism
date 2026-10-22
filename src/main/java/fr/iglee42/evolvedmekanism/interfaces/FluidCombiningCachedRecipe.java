package fr.iglee42.evolvedmekanism.interfaces;

import java.util.Objects;
import java.util.function.BooleanSupplier;

import fr.iglee42.evolvedmekanism.recipes.FluidCombiningRecipe;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public class FluidCombiningCachedRecipe extends CachedRecipe<FluidCombiningRecipe> {

    private final IInputHandler<@NotNull FluidStack> inputHandler;
    private final IInputHandler<@NotNull FluidStack> extraHandler;
    private final IOutputHandler<@NotNull FluidStack> outputHandler;

    private FluidStack recipeMain = FluidStack.EMPTY;
    private FluidStack recipeExtra = FluidStack.EMPTY;
    @Nullable
    private FluidStack output;

    public FluidCombiningCachedRecipe(FluidCombiningRecipe recipe, BooleanSupplier recheckAllErrors, IInputHandler<@NotNull FluidStack> inputHandler,
                                      IInputHandler<@NotNull FluidStack> extraHandler, IOutputHandler<@NotNull FluidStack> outputHandler) {
        super(recipe, recheckAllErrors);
        this.inputHandler = Objects.requireNonNull(inputHandler, "Main input handler cannot be null.");
        this.extraHandler = Objects.requireNonNull(extraHandler, "Secondary input handler cannot be null.");
        this.outputHandler = Objects.requireNonNull(outputHandler, "Output handler cannot be null.");
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            recipeMain = inputHandler.getRecipeInput(recipe.getMainInput());
            if (recipeMain.isEmpty()) {
                tracker.mismatchedRecipe();
            } else {
                recipeExtra = extraHandler.getRecipeInput(recipe.getExtraInput());
                if (recipeExtra.isEmpty()) {
                    tracker.mismatchedRecipe();
                } else {
                    inputHandler.calculateOperationsCanSupport(tracker, recipeMain);
                    if (tracker.shouldContinueChecking()) {
                        extraHandler.calculateOperationsCanSupport(tracker, recipeExtra);
                        if (tracker.shouldContinueChecking()) {
                            output = recipe.getOutput(recipeMain, recipeExtra);
                            outputHandler.calculateOperationsCanSupport(tracker, output);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isInputValid() {
        FluidStack main = inputHandler.getInput();
        FluidStack extra = extraHandler.getInput();
        return !main.isEmpty() && !extra.isEmpty() && recipe.test(main, extra);
    }

    @Override
    protected void finishProcessing(int operations) {
        if (output != null && !output.isEmpty() && !recipeMain.isEmpty() && !recipeExtra.isEmpty()) {
            inputHandler.use(recipeMain, operations);
            extraHandler.use(recipeExtra, operations);
            outputHandler.handleOutput(output, operations);
        }
    }
}
