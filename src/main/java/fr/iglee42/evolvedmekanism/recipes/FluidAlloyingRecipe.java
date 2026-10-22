package fr.iglee42.evolvedmekanism.recipes;

import java.util.List;
import java.util.Objects;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@NothingNullByDefault
public abstract class FluidAlloyingRecipe extends MekanismRecipe implements TriPredicate<@NotNull FluidStack, @NotNull FluidStack, @NotNull FluidStack> {

    private final FluidStackIngredient mainInput;
    private final FluidStackIngredient extraInput;
    private final FluidStackIngredient tertiaryInput;
    private final FluidStackIngredient output;

    public FluidAlloyingRecipe(ResourceLocation id, FluidStackIngredient mainInput, FluidStackIngredient extraInput, FluidStackIngredient tertiaryInput, FluidStackIngredient output) {
        super(id);
        this.mainInput = Objects.requireNonNull(mainInput, "Main input cannot be null.");
        this.extraInput = Objects.requireNonNull(extraInput, "Secondary input cannot be null.");
        this.tertiaryInput = Objects.requireNonNull(tertiaryInput, "Tertiary input cannot be null.");
        this.output = Objects.requireNonNull(output, "Output cannot be null.");
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

    @Override
    public boolean test(FluidStack main, FluidStack extra, FluidStack tertiary) {
        return mainInput.test(main) && extraInput.test(extra) && tertiaryInput.test(tertiary);
    }

    public List<FluidStack> getMainInputDefinition() {
        return FluidCombiningRecipe.sourceFluids(mainInput.getRepresentations());
    }

    public List<FluidStack> getExtraInputDefinition() {
        return FluidCombiningRecipe.sourceFluids(extraInput.getRepresentations());
    }

    public List<FluidStack> getTertiaryInputDefinition() {
        return FluidCombiningRecipe.sourceFluids(tertiaryInput.getRepresentations());
    }

    public List<FluidStack> getOutputDefinition() {
        List<FluidStack> sources = sourceOutputs();
        return sources.isEmpty() ? output.getRepresentations() : sources;
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public FluidStack getOutput(@NotNull FluidStack main, @NotNull FluidStack extra, @NotNull FluidStack tertiary) {
        List<FluidStack> fluids = getOutputDefinition();
        return fluids.isEmpty() ? FluidStack.EMPTY : fluids.get(0).copy();
    }

    @Override
    public boolean isIncomplete() {
        return mainInput.hasNoMatchingInstances() || extraInput.hasNoMatchingInstances() || tertiaryInput.hasNoMatchingInstances()
                || output.hasNoMatchingInstances() || sourceOutputs().isEmpty();
    }

    private List<FluidStack> sourceOutputs() {
        return output.getRepresentations().stream()
                .filter(stack -> stack.getFluid().isSource(stack.getFluid().defaultFluidState()))
                .toList();
    }

    @Override
    public void logMissingTags() {
        mainInput.logMissingTags();
        extraInput.logMissingTags();
        tertiaryInput.logMissingTags();
        output.logMissingTags();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        mainInput.write(buffer);
        extraInput.write(buffer);
        tertiaryInput.write(buffer);
        output.write(buffer);
    }
}
