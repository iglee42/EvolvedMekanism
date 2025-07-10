package fr.iglee42.evolvedmekanism.impl;

import fr.iglee42.evolvedmekanism.recipes.AlloyerRecipe;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMRecipeSerializers;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

@NothingNullByDefault
public class BasicAlloyerRecipe extends AlloyerRecipe {


    public BasicAlloyerRecipe(ItemStackIngredient mainInput, ItemStackIngredient extraInput, ItemStackIngredient secondaryExtraInput, ItemStack output) {
        super(mainInput, extraInput, secondaryExtraInput, output);
    }

    @Override
    public RecipeType<AlloyerRecipe> getType() {
        return EMRecipeType.ALLOYING.get();
    }

    @Override
    public RecipeSerializer<BasicAlloyerRecipe> getSerializer() {
        return EMRecipeSerializers.ALLOYER.get();
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