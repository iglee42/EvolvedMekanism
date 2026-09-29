package fr.iglee42.evolvedmekanism.recipes.vanilla_input;

import mekanism.api.annotations.NothingNullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

@NothingNullByDefault
public record BiFluidRecipeInput(FluidStack main, FluidStack extra) implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {
        throw new IllegalArgumentException("No item for index " + index);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return main.isEmpty() || extra.isEmpty();
    }
}
