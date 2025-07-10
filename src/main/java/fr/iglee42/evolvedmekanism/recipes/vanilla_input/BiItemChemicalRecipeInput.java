package fr.iglee42.evolvedmekanism.recipes.vanilla_input;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.vanilla_input.ItemChemicalRecipeInput;
import net.minecraft.world.item.ItemStack;

/**
 * Simple implementation of a recipe input of one item and one chemical.
 *
 * @since 10.6.0
 */
@NothingNullByDefault
public record BiItemChemicalRecipeInput(ItemStack item, ItemStack extra, ChemicalStack chemical) implements ItemChemicalRecipeInput {

    @Override
    public ItemStack getItem(int index) {
        if (index == 0) {
            return item;
        } else if (index == 1) {
            return extra;
        }
        throw new IllegalArgumentException("No item for index " + index);
    }

    @Override
    public ChemicalStack getChemical(int index) {
        if (index != 0) {
            throw new IllegalArgumentException("No chemical for index " + index);
        }
        return chemical;
    }

    @Override
    public int size() {
        return 3;
    }

    @Override
    public boolean isEmpty() {
        return item.isEmpty() || extra.isEmpty() || chemical.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BiItemChemicalRecipeInput other = (BiItemChemicalRecipeInput) o;
        return chemical.equals(other.chemical) && ItemStack.matches(item, other.item) &&  ItemStack.matches(extra, other.extra);
    }

    @Override
    public int hashCode() {
        int hash = chemical.hashCode();
        hash = 31 * hash + ItemStack.hashItemAndComponents(item);
        hash = 31 + hash + item.getCount();
        hash = 31 * hash + ItemStack.hashItemAndComponents(extra);
        hash = 31 + hash + extra.getCount();
        return hash;
    }
}