package fr.iglee42.evolvedmekanism.interfaces;

import fr.iglee42.evolvedmekanism.recipes.FluidCombiningRecipe;
import mekanism.api.recipes.cache.TwoInputCachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;

public class FluidCombiningCachedRecipe extends TwoInputCachedRecipe<FluidStack, FluidStack, FluidStack, FluidCombiningRecipe> {

    public static FluidCombiningCachedRecipe create(FluidCombiningRecipe recipe, BooleanSupplier recheckAllErrors, IInputHandler<@NotNull FluidStack> inputHandler,
                                                     IInputHandler<@NotNull FluidStack> extraInputHandler, IOutputHandler<@NotNull FluidStack> outputHandler) {
        return new FluidCombiningCachedRecipe(recipe, recheckAllErrors, inputHandler, extraInputHandler, outputHandler);
    }

    private FluidCombiningCachedRecipe(FluidCombiningRecipe recipe, BooleanSupplier recheckAllErrors, IInputHandler<@NotNull FluidStack> inputHandler,
                                       IInputHandler<@NotNull FluidStack> extraInputHandler, IOutputHandler<@NotNull FluidStack> outputHandler) {
        super(recipe, recheckAllErrors, inputHandler, extraInputHandler, outputHandler, recipe::getMainInput, recipe::getExtraInput, recipe::getOutput,
                FluidStack::isEmpty, FluidStack::isEmpty, FluidStack::isEmpty);
    }
}
