package fr.iglee42.evolvedmekanism.recipes;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@NothingNullByDefault
public abstract class ChemixerRecipe extends MekanismRecipe implements TriPredicate<@NotNull ItemStack, @NotNull ItemStack, @NotNull GasStack> {

    private final ItemStackIngredient inputMain;
    private final ItemStackIngredient inputExtra;
    private final GasStackIngredient inputGas;
    private final ItemStack outputItem;

    public ChemixerRecipe(ResourceLocation id, ItemStackIngredient inputMain, ItemStackIngredient inputExtra, GasStackIngredient inputGas, ItemStack outputItem) {
        super(id);
        this.inputMain = Objects.requireNonNull(inputMain, "Item input cannot be null.");
        this.inputExtra = Objects.requireNonNull(inputExtra, "Extra input cannot be null.");
        this.inputGas = Objects.requireNonNull(inputGas, "Gas input cannot be null.");
        Objects.requireNonNull(outputItem, "Item output cannot be null.");
        this.outputItem = outputItem.copy();
    }

    public ItemStackIngredient getInputMain() {
        return inputMain;
    }

    public ItemStackIngredient getInputExtra() {
        return inputExtra;
    }

    public GasStackIngredient getInputGas() {
        return inputGas;
    }


    @Override
    public boolean test(ItemStack solid, ItemStack extra, GasStack gas) {
        return this.inputMain.test(solid) && this.inputExtra.test(extra) && this.inputGas.test(gas);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public ItemStack getOutput(@NotNull ItemStack input, @NotNull ItemStack extra, @NotNull GasStack gas) {
        return outputItem.copy();
    }

    @Override
    public boolean isIncomplete() {
        return inputMain.hasNoMatchingInstances() || inputExtra.hasNoMatchingInstances() || inputGas.hasNoMatchingInstances();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        inputMain.write(buffer);
        inputExtra.write(buffer);
        inputGas.write(buffer);
        buffer.writeItem(outputItem);
    }

    public List<ItemStack> getOutputDefinition() {
        return Collections.singletonList(outputItem);
    }

}