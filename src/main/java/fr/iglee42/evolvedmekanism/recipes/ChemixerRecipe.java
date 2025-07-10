package fr.iglee42.evolvedmekanism.recipes;

import fr.iglee42.evolvedmekanism.recipes.vanilla_input.BiItemChemicalRecipeInput;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.TriPredicate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@NothingNullByDefault
public abstract class ChemixerRecipe extends MekanismRecipe<BiItemChemicalRecipeInput> implements TriPredicate<@NotNull ItemStack, @NotNull ItemStack, @NotNull ChemicalStack> {

    private final ItemStackIngredient inputMain;
    private final ItemStackIngredient inputExtra;
    private final ChemicalStackIngredient inputGas;
    private final ItemStack outputItem;

    public ChemixerRecipe(ItemStackIngredient inputMain, ItemStackIngredient inputExtra, ChemicalStackIngredient inputGas, ItemStack outputItem) {
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

    public ChemicalStackIngredient getInputGas() {
        return inputGas;
    }


    @Override
    public boolean test(ItemStack solid, ItemStack extra, ChemicalStack gas) {
        return this.inputMain.test(solid) && this.inputExtra.test(extra) && this.inputGas.test(gas);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public ItemStack getOutput(@NotNull ItemStack input, @NotNull ItemStack extra, @NotNull ChemicalStack gas) {
        return outputItem.copy();
    }

    public ItemStack getOutputRaw(){
        return outputItem.copy();
    }

    @Override
    public boolean isIncomplete() {
        return inputMain.hasNoMatchingInstances() || inputExtra.hasNoMatchingInstances() || inputGas.hasNoMatchingInstances();
    }

    public List<ItemStack> getOutputDefinition() {
        return Collections.singletonList(outputItem);
    }

    @Override
    public boolean matches(BiItemChemicalRecipeInput input, Level level) {
        //Don't match incomplete recipes or ones that don't match
        return !isIncomplete() && test(input.item(),input.extra(),input.chemical());
    }

}