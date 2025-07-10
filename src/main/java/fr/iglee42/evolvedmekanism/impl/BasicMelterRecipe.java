package fr.iglee42.evolvedmekanism.impl;

import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMRecipeSerializers;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.FluidStack;

@NothingNullByDefault
public class BasicMelterRecipe extends BasicItemStackToFluidRecipe {


    public BasicMelterRecipe(ItemStackIngredient input, FluidStack output) {
        super(input, output);
    }

    @Override
    public RecipeType<ItemStackToFluidRecipe> getType() {
        return EMRecipeType.MELTING.get();
    }

    @Override
    public RecipeSerializer<BasicMelterRecipe> getSerializer() {
        return EMRecipeSerializers.MELTER.get();
    }

    @Override
    public String getGroup() {
        return EMBlocks.MELTER.getName();
    }

    @Override
    public ItemStack getToastSymbol() {
        return EMBlocks.MELTER.asItem().getDefaultInstance();
    }
}