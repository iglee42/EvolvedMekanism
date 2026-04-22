package fr.iglee42.evolvedmekanism.recipes;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.TriPredicate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Main Input: ItemStack
 * <br>
 * Secondary Input: ItemStack
 * <br>
 * Tertiary Input: ItemStack
 * <br>
 * Output: ItemStack
 *
 * @apiNote Alloyers and Alloying Factories can process this recipe type.
 */
@NothingNullByDefault
public abstract class AlloyerRecipe extends MekanismRecipe implements TriPredicate<@NotNull ItemStack, @NotNull ItemStack, @NotNull ItemStack> {

    private final ItemStackIngredient mainInput;
    private final ItemStackIngredient extraInput;
    private final ItemStackIngredient tertiaryExtraInput;
    private final ItemStack output;

    /**
     * @param id         Recipe name.
     * @param mainInput  Main input.
     * @param extraInput Secondary input.
     * @param secondaryExtraInput Tertiary input.
     * @param output     Output.
     */
    public AlloyerRecipe(ResourceLocation id, ItemStackIngredient mainInput, ItemStackIngredient extraInput, ItemStackIngredient secondaryExtraInput, ItemStack output) {
        super(id);
        this.mainInput = Objects.requireNonNull(mainInput, "Main input cannot be null.");
        this.extraInput = Objects.requireNonNull(extraInput, "Secondary input cannot be null.");
        this.tertiaryExtraInput = Objects.requireNonNull(secondaryExtraInput, "Tertiary input cannot be null.");
        Objects.requireNonNull(output, "Output cannot be null.");
        if (output.isEmpty()) {
            throw new IllegalArgumentException("Output cannot be empty.");
        }
        this.output = output.copy();
    }

    @Override
    public boolean test(ItemStack input, ItemStack extra, ItemStack secondExtra) {
        return mainInput.test(input) && extraInput.test(extra) && tertiaryExtraInput.test(secondExtra);
    }

    /**
     * Gets the main input ingredient.
     */
    public ItemStackIngredient getMainInput() {
        return mainInput;
    }

    /**
     * Gets the secondary input ingredient.
     */
    public ItemStackIngredient getExtraInput() {
        return extraInput;
    }

    /**
     * Gets the tertiary input ingredient.
     */
    public ItemStackIngredient getTertiaryExtraInput() {
        return tertiaryExtraInput;
    }

    /**
     * Gets a new output based on the given inputs.
     *
     * @param input Specific input.
     * @param extra Specific secondary input.
     * @param secondExtra Specific tertiary input.
     *
     * @return New output.
     *
     * @apiNote While Mekanism does not currently make use of the inputs, it is important to support it and pass the proper value in case any addons define input based
     * outputs where things like NBT may be different.
     * @implNote The passed in inputs should <strong>NOT</strong> be modified.
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    public ItemStack getOutput(@NotNull ItemStack input, @NotNull ItemStack extra, @NotNull ItemStack secondExtra) {
        return output.copy();
    }

    @NotNull
    @Override
    public ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return output.copy();
    }

    /**
     * For JEI, gets the output representations to display.
     *
     * @return Representation of the output, <strong>MUST NOT</strong> be modified.
     */
    public List<ItemStack> getOutputDefinition() {
        return Collections.singletonList(output);
    }

    @Override
    public boolean isIncomplete() {
        return mainInput.hasNoMatchingInstances() || extraInput.hasNoMatchingInstances() || tertiaryExtraInput.hasNoMatchingInstances();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        mainInput.write(buffer);
        extraInput.write(buffer);
        tertiaryExtraInput.write(buffer);
        buffer.writeItem(output);
    }
}