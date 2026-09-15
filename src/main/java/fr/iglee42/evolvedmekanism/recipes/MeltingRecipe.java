package fr.iglee42.evolvedmekanism.recipes;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Input: ItemStack
 * <br>
 * Fluid Output: FluidStack (supports fluid tags)
 *
 * @apiNote Thermalizer can process this recipe type.
 */
@NothingNullByDefault
public abstract class MeltingRecipe extends MekanismRecipe implements Predicate<@NotNull ItemStack> {

    private final ItemStackIngredient input;
    private final FluidStackIngredient output;

    public MeltingRecipe(ResourceLocation id, ItemStackIngredient input, FluidStackIngredient output) {
        super(id);
        this.input = Objects.requireNonNull(input, "Item input cannot be null.");
        this.output = Objects.requireNonNull(output, "Fluid output cannot be null.");
    }

    public ItemStackIngredient getInput() {
        return input;
    }

    public FluidStackIngredient getOutputRaw() {
        return output;
    }

    @Override
    public boolean test(ItemStack input) {
        return this.input.test(input);
    }

    public List<FluidStack> getOutputDefinition() {
        return getValidOutputFluids();
    }

    @Contract(value = "_ -> new", pure = true)
    public FluidStack getOutput(ItemStack input) {
        List<FluidStack> fluids = getValidOutputFluids();
        return fluids.isEmpty() ? FluidStack.EMPTY : fluids.get(0).copy();
    }

    private List<FluidStack> getValidOutputFluids() {
        return output.getRepresentations().stream()
                .filter(stack -> stack.getFluid().isSource(stack.getFluid().defaultFluidState()))
                .sorted((f1, f2) -> FluidComparator.INSTANCE.compare(f1.getFluid(), f2.getFluid()))
                .toList();
    }

    @Override
    public boolean isIncomplete() {
        return input.hasNoMatchingInstances() || output.hasNoMatchingInstances() || getValidOutputFluids().isEmpty();
    }

    @Override
    public void logMissingTags() {
        input.logMissingTags();
        output.logMissingTags();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        input.write(buffer);
        output.write(buffer);
    }

    static final class FluidComparator implements Comparator<Fluid> {

        static final FluidComparator INSTANCE = new FluidComparator();

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
