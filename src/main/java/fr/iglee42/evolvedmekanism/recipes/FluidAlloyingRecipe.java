package fr.iglee42.evolvedmekanism.recipes;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import fr.iglee42.evolvedmekanism.recipes.vanilla_input.TriFluidRecipeInput;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.util.TriPredicate;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@NothingNullByDefault
public abstract class FluidAlloyingRecipe extends MekanismRecipe<TriFluidRecipeInput> implements TriPredicate<@NotNull FluidStack, @NotNull FluidStack, @NotNull FluidStack> {

    private final FluidStackIngredient mainInput;
    private final FluidStackIngredient extraInput;
    private final FluidStackIngredient tertiaryInput;
    private final FluidStackIngredient output;

    public FluidAlloyingRecipe(FluidStackIngredient mainInput, FluidStackIngredient extraInput, FluidStackIngredient tertiaryInput, FluidStackIngredient output) {
        this.mainInput = Objects.requireNonNull(mainInput, "Main input cannot be null.");
        this.extraInput = Objects.requireNonNull(extraInput, "Secondary input cannot be null.");
        this.tertiaryInput = Objects.requireNonNull(tertiaryInput, "Tertiary input cannot be null.");
        this.output = Objects.requireNonNull(output, "Output cannot be null.");
    }

    @Override
    public boolean test(FluidStack main, FluidStack extra, FluidStack tertiary) {
        return mainInput.test(main) && extraInput.test(extra) && tertiaryInput.test(tertiary);
    }

    public FluidStackIngredient getMainInput() {
        return mainInput;
    }

    public FluidStackIngredient getExtraInput() {
        return extraInput;
    }

    public FluidStackIngredient getTertiaryExtraInput() {
        return tertiaryInput;
    }

    public FluidStackIngredient getOutputRaw() {
        return output;
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public FluidStack getOutput(@NotNull FluidStack main, @NotNull FluidStack extra, @NotNull FluidStack tertiary) {
        return outputFluid().copy();
    }

    public List<FluidStack> getOutputDefinition() {
        return List.of(outputFluid());
    }

    private FluidStack outputFluid() {
        return output.getRepresentations().stream()
                .filter(stack -> stack.getFluid().isSource(stack.getFluid().defaultFluidState()))
                .min(Comparator.comparing(FluidStack::getFluid, FluidOrder.INSTANCE))
                .orElseGet(() -> output.getRepresentations().isEmpty() ? FluidStack.EMPTY : output.getRepresentations().getFirst());
    }

    @Override
    public boolean isIncomplete() {
        return mainInput.hasNoMatchingInstances() || extraInput.hasNoMatchingInstances() || tertiaryInput.hasNoMatchingInstances() || output.hasNoMatchingInstances();
    }

    @Override
    public boolean matches(TriFluidRecipeInput input, Level level) {
        return !isIncomplete() && test(input.main(), input.extra(), input.secondExtra());
    }

    @Override
    public @NotNull ItemStack getResultItem(net.minecraft.core.HolderLookup.@NotNull Provider provider) {
        return ItemStack.EMPTY;
    }

    static final class FluidOrder implements Comparator<Fluid> {
        static final FluidOrder INSTANCE = new FluidOrder();

        @Override
        public int compare(Fluid fluid1, Fluid fluid2) {
            boolean source1 = fluid1.isSource(fluid1.defaultFluidState());
            boolean source2 = fluid2.isSource(fluid2.defaultFluidState());
            if (source1 != source2) {
                return source1 ? -1 : 1;
            }
            return 0;
        }
    }
}
