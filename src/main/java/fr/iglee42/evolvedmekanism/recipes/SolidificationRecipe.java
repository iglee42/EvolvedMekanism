package fr.iglee42.evolvedmekanism.recipes;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Input: ItemStack
 * <br>
 * Input: FluidStack
 * <br>
 * Input: FluidStack
 * <br>
 * Item Output: ItemStack
 * @apiNote Pressurized Reaction Chambers can process this recipe type.
 */
@NothingNullByDefault
public abstract class SolidificationRecipe extends MekanismRecipe implements TriPredicate<@NotNull ItemStack, @NotNull FluidStack, @NotNull FluidStack> {

    private final ItemStackIngredient inputSolid;
    private final FluidStackIngredient inputFluid;
    private final FluidStackIngredient fluidInputExtra;
    private final FloatingLong energyRequired;
    private final int duration;
    private final ItemStack outputItem;
    private final boolean keepItem;

    /**
     * @param id             Recipe name.
     * @param inputSolid     Item input.
     * @param inputFluid     Fluid input.
     * @param inputFluidExtra Extra fluid input.
     * @param energyRequired Amount of "extra" energy this recipe requires, compared to the base energy requirements of the machine performing the recipe.
     * @param duration       Base duration in ticks that this recipe takes to complete. Must be greater than zero.
     * @param outputItem     Item output.
     * @param keepItem       Should the input item not be consumed
     *
     * @apiNote At least one output must not be empty.
     */
    public SolidificationRecipe(ResourceLocation id, ItemStackIngredient inputSolid, FluidStackIngredient inputFluid, FluidStackIngredient inputFluidExtra,
                                FloatingLong energyRequired, int duration, ItemStack outputItem,boolean keepItem) {
        super(id);
        this.inputSolid = Objects.requireNonNull(inputSolid, "Item input cannot be null.");
        this.inputFluid = Objects.requireNonNull(inputFluid, "Fluid input cannot be null.");
        this.fluidInputExtra = Objects.requireNonNull(inputFluidExtra, "Gas input cannot be null.");
        this.energyRequired = Objects.requireNonNull(energyRequired, "Required energy cannot be null.").copyAsConst();
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be positive.");
        }
        this.duration = duration;
        Objects.requireNonNull(outputItem, "Item output cannot be null.");
        if (outputItem.isEmpty()) {
            throw new IllegalArgumentException("Output Item can't be empty");
        }
        this.outputItem = outputItem.copy();
        this.keepItem = keepItem;
    }

    /**
     * Gets the item input ingredient.
     */
    public ItemStackIngredient getInputSolid() {
        return inputSolid;
    }

    /**
     * Gets the fluid input ingredient.
     */
    public FluidStackIngredient getInputFluid() {
        return inputFluid;
    }

    /**
     * Gets the fluid input ingredient.
     */
    public FluidStackIngredient getFluidInputExtra() {
        return fluidInputExtra;
    }

    /**
     * Gets the amount of "extra" energy this recipe requires, compared to the base energy requirements of the machine performing the recipe.
     */
    public FloatingLong getEnergyRequired() {
        return energyRequired;
    }

    /**
     * Gets the base duration in ticks that this recipe takes to complete.
     */
    public int getDuration() {
        return duration;
    }

    public boolean shouldKeepItem() {
        return keepItem;
    }

    @Override
    public boolean test(ItemStack solid, FluidStack liquid, FluidStack extra) {
        return this.inputSolid.test(solid) && this.inputFluid.test(liquid) && this.fluidInputExtra.test(extra);
    }

    /**
     * For JEI, gets the output representations to display.
     *
     * @return Representation of the output, <strong>MUST NOT</strong> be modified.
     */
    public List<ItemStack> getOutputDefinition() {
        return Collections.singletonList(outputItem);
    }

    /**
     * Gets a new output based on the given inputs.
     *
     * @param solid  Specific item input.
     * @param liquid Specific fluid input.
     * @param extra    Specific extra fluid input.
     *
     * @return New output.
     *
     * @apiNote While Mekanism does not currently make use of the inputs, it is important to support it and pass the proper value in case any addons define input based
     * outputs where things like NBT may be different.
     * @implNote The passed in inputs should <strong>NOT</strong> be modified.
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    public ItemStack getOutput(ItemStack solid, FluidStack liquid, FluidStack extra) {
        return outputItem.copy();
    }

    @Override
    public boolean isIncomplete() {
        return inputSolid.hasNoMatchingInstances() || inputFluid.hasNoMatchingInstances() || fluidInputExtra.hasNoMatchingInstances();
    }

    @Override
    public void logMissingTags() {
        inputSolid.logMissingTags();
        inputFluid.logMissingTags();
        fluidInputExtra.logMissingTags();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        inputSolid.write(buffer);
        inputFluid.write(buffer);
        fluidInputExtra.write(buffer);
        energyRequired.writeToBuffer(buffer);
        buffer.writeVarInt(duration);
        buffer.writeItem(outputItem);
        buffer.writeBoolean(keepItem);
    }

}