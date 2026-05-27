package fr.iglee42.evolvedmekanism.recipes;

import fr.iglee42.evolvedmekanism.recipes.vanilla_input.SingleItemBiFluidRecipeInput;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Input: ItemStack
 * <br>
 * Item Output: ItemStack
 * @apiNote Thermalizer can process this recipe type.
 */
@NothingNullByDefault
public abstract class MeltingRecipe extends MekanismRecipe<SingleRecipeInput> implements Predicate<@NotNull ItemStack> {

    private final ItemStackIngredient input;
    private final FluidStackIngredient output;

    /**
     * @param input     Item input.
     * @param output     Item output.
     *
     * @apiNote At least one output must not be empty.
     */
    public MeltingRecipe(ItemStackIngredient input,
                         FluidStackIngredient output) {
        this.input = Objects.requireNonNull(input, "Item input cannot be null.");
        this.output = Objects.requireNonNull(output, "Fluid output cannot be null.");
    }

    /**
     * Gets the item input ingredient.
     */
    public ItemStackIngredient getInput() {
        return input;
    }

    @Override
    public boolean test(ItemStack input) {
        return this.input.test(input);
    }

    /**
     * For JEI, gets the output representations to display.
     *
     * @return Representation of the output, <strong>MUST NOT</strong> be modified.
     */
    public List<FluidStack> getOutputDefinition() {
        return getValidOutputFluids();
    }

    /**
     * Gets a new output based on the given inputs.
     *
     * @param input Specific item input.
     * @return New output.
     * @apiNote While Mekanism does not currently make use of the inputs, it is important to support it and pass the proper value in case any addons define input based
     * outputs where things like NBT may be different.
     * @implNote The passed in inputs should <strong>NOT</strong> be modified.
     */
    @Contract(value = "_ -> new", pure = true)
    public FluidStack getOutput(ItemStack input) {
        return getValidOutputFluids().getFirst().copy();
    }

    public FluidStackIngredient getOutputRaw(){
        return output;
    }

    private List<FluidStack> getValidOutputFluids(){
        return output.getRepresentations().stream().filter(stack->stack.getFluid().isSource(stack.getFluid().defaultFluidState())).sorted((f1,f2)->FluidComparator.INSTANCE.compare(f1.getFluid(),f2.getFluid())).toList();
    }

    @Override
    public boolean isIncomplete() {
        return input.hasNoMatchingInstances() || output.hasNoMatchingInstances();
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return !isIncomplete() && test(input.item());
    }

    static final class FluidComparator implements Comparator<Fluid> {

        static final FluidComparator INSTANCE = new FluidComparator();
        FluidComparator() {
        }

        @Override
        public int compare(Fluid fluid1, Fluid fluid2) {
            boolean isSource1 = fluid1.isSource(fluid1.defaultFluidState());
            if (isSource1 != fluid2.isSource(fluid2.defaultFluidState())) {
                return isSource1 ? -1 : 1;
            }
            return 0;
        }
    }

}