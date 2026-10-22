package fr.iglee42.evolvedmekanism.recipeviewers.emi;

import java.util.List;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public final class EMEmiIngredients {

    private EMEmiIngredients() {
    }

    public static EmiIngredient items(ItemStackIngredient ingredient) {
        return ofStacks(ingredient.getRepresentations().stream().map(EmiStack::of).toList());
    }

    public static EmiIngredient items(List<ItemStack> stacks) {
        return ofStacks(stacks.stream().map(EmiStack::of).toList());
    }

    public static EmiIngredient fluids(FluidStackIngredient ingredient) {
        return fluids(ingredient.getRepresentations());
    }

    public static EmiIngredient fluids(List<FluidStack> stacks) {
        return ofStacks(stacks.stream().map(stack -> EmiStack.of(stack.getFluid(), stack.getAmount())).toList());
    }

    public static EmiIngredient gases(GasStackIngredient ingredient) {
        return gases(ingredient.getRepresentations());
    }

    public static EmiIngredient gases(List<GasStack> stacks) {
        return ofStacks(stacks.stream().map(GasEmiStack::of).toList());
    }

    public static EmiStack fluidOutput(FluidStack stack) {
        return stack.isEmpty() ? EmiStack.EMPTY : EmiStack.of(stack.getFluid(), stack.getAmount());
    }

    private static EmiIngredient ofStacks(List<EmiStack> stacks) {
        List<EmiStack> present = stacks.stream().filter(stack -> !stack.isEmpty()).toList();
        if (present.isEmpty()) {
            return EmiStack.EMPTY;
        }
        return EmiIngredient.of(present);
    }
}
