package fr.iglee42.evolvedmekanism.recipes.vanilla_input;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.vanilla_input.FluidRecipeInput;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Simple implementation of a recipe input of one fluid.
 *
 * @since 10.6.0
 */
@NothingNullByDefault
public record SingleItemBiFluidRecipeInput(ItemStack item, FluidStack fluid, FluidStack fluidExtra) implements FluidRecipeInput {

    public ItemStack getItem(int index) {
        if (index == 0) {
            return item;
        }
        throw new IllegalArgumentException("No item for index " + index);
    }

    @Override
    public FluidStack getFluid(int index) {
        if (index == 0) {
            return fluid;
        } else if (index == 1) {
            return fluidExtra;
        }
        throw new IllegalArgumentException("No fluid for index " + index);
    }

    @Override
    public int size() {
        return 3;
    }

    @Override
    public boolean isEmpty() {
        return fluid.isEmpty() || item.isEmpty() || fluidExtra.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return ItemStack.matches(item,((SingleItemBiFluidRecipeInput) o).item) && FluidStack.matches(fluid, ((SingleItemBiFluidRecipeInput) o).fluid) && FluidStack.matches(fluidExtra, ((SingleItemBiFluidRecipeInput) o).fluidExtra);
    }

    @Override
    public int hashCode() {
        int hash = FluidStack.hashFluidAndComponents(fluid);
        hash = 31 * hash + fluid.getAmount();
        hash = FluidStack.hashFluidAndComponents(fluidExtra);
        hash = 31 * hash + fluidExtra.getAmount();
        hash = 31 * hash + ItemStack.hashItemAndComponents(item);
        hash = 31 + hash + item.getCount();
        return hash;
    }
}