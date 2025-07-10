package fr.iglee42.evolvedmekanism.impl;

import fr.iglee42.evolvedmekanism.recipes.ChemixerRecipe;
import fr.iglee42.evolvedmekanism.recipes.vanilla_input.BiItemChemicalRecipeInput;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMRecipeSerializers;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

@NothingNullByDefault
public class BasicChemixerRecipe extends ChemixerRecipe {

    public BasicChemixerRecipe(ItemStackIngredient mainInput, ItemStackIngredient extraInput, ChemicalStackIngredient gasInput, ItemStack output) {
        super(mainInput, extraInput,gasInput, output);
    }

    @Override
    public RecipeType<ChemixerRecipe> getType() {
        return EMRecipeType.CHEMIXING.get();
    }

    @Override
    public RecipeSerializer<BasicChemixerRecipe> getSerializer() {
        return EMRecipeSerializers.CHEMIXER.get();
    }

    @Override
    public String getGroup() {
        return EMBlocks.ALLOYER.getName();
    }

    @Override
    public ItemStack getToastSymbol() {
        return EMBlocks.ALLOYER.asItem().getDefaultInstance();
    }

}