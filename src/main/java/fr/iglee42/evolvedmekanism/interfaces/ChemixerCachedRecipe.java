package fr.iglee42.evolvedmekanism.interfaces;

import java.util.Objects;
import java.util.function.BooleanSupplier;

import fr.iglee42.evolvedmekanism.recipes.ChemixerRecipe;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public class ChemixerCachedRecipe extends CachedRecipe<ChemixerRecipe> {

    private final IOutputHandler<@NotNull ItemStack> outputHandler;
    private final IInputHandler<@NotNull ItemStack> itemInputHandler;
    private final IInputHandler<@NotNull ItemStack> extraInputHandler;
    private final IInputHandler<@NotNull ChemicalStack> gasInputHandler;

    private ItemStack recipeItem = ItemStack.EMPTY;
    private ItemStack recipeExtra = ItemStack.EMPTY;
    private ChemicalStack recipeGas = ChemicalStack.EMPTY;
    @Nullable
    private ItemStack output;


    public ChemixerCachedRecipe(ChemixerRecipe recipe, BooleanSupplier recheckAllErrors, IInputHandler<@NotNull ItemStack> itemInputHandler,
                                IInputHandler<@NotNull ItemStack> extraInputHandler, IInputHandler<@NotNull ChemicalStack> gasInputHandler,
                                IOutputHandler<@NotNull ItemStack> outputHandler) {
        super(recipe, recheckAllErrors);
        this.itemInputHandler = Objects.requireNonNull(itemInputHandler, "Item input handler cannot be null.");
        this.extraInputHandler = Objects.requireNonNull(extraInputHandler, "Extra input handler cannot be null.");
        this.gasInputHandler = Objects.requireNonNull(gasInputHandler, "Gas input handler cannot be null.");
        this.outputHandler = Objects.requireNonNull(outputHandler, "Output handler cannot be null.");
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            recipeItem = itemInputHandler.getRecipeInput(recipe.getInputMain());
            if (recipeItem.isEmpty()) {
                tracker.mismatchedRecipe();
            } else {
                recipeExtra = extraInputHandler.getRecipeInput(recipe.getInputExtra());
                if (recipeExtra.isEmpty()) {
                    tracker.mismatchedRecipe();
                } else {
                    recipeGas = gasInputHandler.getRecipeInput(recipe.getInputGas());
                    if (recipeGas.isEmpty()) {
                        tracker.mismatchedRecipe();
                    } else {
                        itemInputHandler.calculateOperationsCanSupport(tracker, recipeItem);
                        if (tracker.shouldContinueChecking()) {
                            extraInputHandler.calculateOperationsCanSupport(tracker, recipeExtra);
                            if (tracker.shouldContinueChecking()) {
                                gasInputHandler.calculateOperationsCanSupport(tracker, recipeGas);
                                if (tracker.shouldContinueChecking()) {
                                    output = recipe.getOutput(recipeItem, recipeExtra, recipeGas);
                                    outputHandler.calculateOperationsCanSupport(tracker, output);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isInputValid() {
        ItemStack item = itemInputHandler.getInput();
        if (item.isEmpty()) {
            return false;
        }
        ChemicalStack gas = gasInputHandler.getInput();
        if (gas.isEmpty()) {
            return false;
        }
        ItemStack extra = extraInputHandler.getInput();
        return !extra.isEmpty() && recipe.test(item, extra, gas);
    }

    @Override
    protected void finishProcessing(int operations) {
        if (output != null && !recipeItem.isEmpty() && !recipeExtra.isEmpty() && !recipeGas.isEmpty()) {
            itemInputHandler.use(recipeItem, operations);
            extraInputHandler.use(recipeExtra, operations);
            gasInputHandler.use(recipeGas, operations);
            outputHandler.handleOutput(output, operations);
        }
    }
}