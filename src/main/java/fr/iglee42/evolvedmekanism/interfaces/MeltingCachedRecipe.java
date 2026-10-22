package fr.iglee42.evolvedmekanism.interfaces;

import fr.iglee42.evolvedmekanism.recipes.MeltingRecipe;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BooleanSupplier;

@NothingNullByDefault
public class MeltingCachedRecipe extends CachedRecipe<MeltingRecipe> {

    private final IInputHandler<@NotNull ItemStack> inputHandler;
    private final IOutputHandler<@NotNull FluidStack> outputHandler;

    private ItemStack recipeItem = ItemStack.EMPTY;
    @Nullable
    private FluidStack output;

    public MeltingCachedRecipe(MeltingRecipe recipe, BooleanSupplier recheckAllErrors, IInputHandler<@NotNull ItemStack> inputHandler,
                               IOutputHandler<@NotNull FluidStack> outputHandler) {
        super(recipe, recheckAllErrors);
        this.inputHandler = Objects.requireNonNull(inputHandler, "Input handler cannot be null.");
        this.outputHandler = Objects.requireNonNull(outputHandler, "Output handler cannot be null.");
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            recipeItem = inputHandler.getRecipeInput(recipe.getInput());
            if (recipeItem.isEmpty()) {
                tracker.mismatchedRecipe();
            } else {
                inputHandler.calculateOperationsCanSupport(tracker, recipeItem);
                if (tracker.shouldContinueChecking()) {
                    output = recipe.getOutput(recipeItem);
                    if (output.isEmpty()) {
                        tracker.mismatchedRecipe();
                    } else {
                        outputHandler.calculateOperationsCanSupport(tracker, output);
                    }
                }
            }
        }
    }

    @Override
    public boolean isInputValid() {
        ItemStack input = inputHandler.getInput();
        return !input.isEmpty() && recipe.test(input);
    }

    @Override
    protected void finishProcessing(int operations) {
        if (output != null && !recipeItem.isEmpty() && !output.isEmpty()) {
            inputHandler.use(recipeItem, operations);
            outputHandler.handleOutput(output, operations);
        }
    }
}
