package fr.iglee42.evolvedmekanism.recipes.vanilla_input;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.ChemicalStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

@NothingNullByDefault
public record TriItemRecipeInput(ItemStack main, ItemStack extra,ItemStack secondExtra) implements RecipeInput {

    @Override
    public ItemStack getItem(int i) {
        if (i == 0) return main;
        else if (i == 1) return extra;
        else if (i == 2) return secondExtra;
        throw new IllegalArgumentException("No item for index " + i);
    }

    @Override
    public int size() {
        return 3;
    }

    @Override
    public boolean isEmpty() {
        return main.isEmpty() || extra.isEmpty() || secondExtra.isEmpty();
    }
}