package fr.iglee42.evolvedmekanism.recipes;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@NothingNullByDefault
public abstract class FluidCombiningRecipe extends MekanismRecipe implements BiPredicate<@NotNull FluidStack, @NotNull FluidStack> {

    private final FluidStackIngredient mainInput;
    private final FluidStackIngredient extraInput;
    private final FluidStackIngredient output;

    public FluidCombiningRecipe(ResourceLocation id, FluidStackIngredient mainInput, FluidStackIngredient extraInput, FluidStackIngredient output) {
        super(id);
        this.mainInput = Objects.requireNonNull(mainInput, "Main input cannot be null.");
        this.extraInput = Objects.requireNonNull(extraInput, "Secondary input cannot be null.");
        this.output = Objects.requireNonNull(output, "Output cannot be null.");
    }

    public FluidStackIngredient getMainInput() {
        return mainInput;
    }

    public FluidStackIngredient getExtraInput() {
        return extraInput;
    }

    public FluidStackIngredient getOutputRaw() {
        return output;
    }

    @Override
    public boolean test(FluidStack main, FluidStack extra) {
        return mainInput.test(main) && extraInput.test(extra);
    }

    public List<FluidStack> getMainInputDefinition() {
        return sourceFluids(mainInput.getRepresentations());
    }

    public List<FluidStack> getExtraInputDefinition() {
        return sourceFluids(extraInput.getRepresentations());
    }

    public List<FluidStack> getOutputDefinition() {
        List<FluidStack> fluids = validOutputs();
        return fluids.isEmpty() ? output.getRepresentations() : fluids;
    }

    @Contract(value = "_, _ -> new", pure = true)
    public FluidStack getOutput(@NotNull FluidStack main, @NotNull FluidStack extra) {
        List<FluidStack> fluids = getOutputDefinition();
        return fluids.isEmpty() ? FluidStack.EMPTY : fluids.get(0).copy();
    }

    public static List<FluidStack> sourceFluids(List<FluidStack> fluids) {
        List<FluidStack> sources = fluids.stream()
                .filter(stack -> stack.getFluid().isSource(stack.getFluid().defaultFluidState()))
                .toList();
        return sources.isEmpty() ? fluids : sources;
    }

    private List<FluidStack> validOutputs() {
        return output.getRepresentations().stream()
                .filter(stack -> stack.getFluid().isSource(stack.getFluid().defaultFluidState()))
                .sorted((first, second) -> FluidComparator.INSTANCE.compare(first.getFluid(), second.getFluid()))
                .toList();
    }

    @Override
    public boolean isIncomplete() {
        return mainInput.hasNoMatchingInstances() || extraInput.hasNoMatchingInstances() || output.hasNoMatchingInstances() || validOutputs().isEmpty();
    }

    @Override
    public void logMissingTags() {
        mainInput.logMissingTags();
        extraInput.logMissingTags();
        output.logMissingTags();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        mainInput.write(buffer);
        extraInput.write(buffer);
        output.write(buffer);
    }

    static final class FluidComparator implements Comparator<Fluid> {

        static final FluidComparator INSTANCE = new FluidComparator();

        @Override
        public int compare(Fluid fluid1, Fluid fluid2) {
            boolean source1 = fluid1.isSource(fluid1.defaultFluidState());
            if (source1 != fluid2.isSource(fluid2.defaultFluidState())) {
                return source1 ? -1 : 1;
            }
            return 0;
        }
    }
}
