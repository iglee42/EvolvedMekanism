package fr.iglee42.evolvedmekanism.impl;

import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMRecipeSerializers;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.api.recipes.ItemStackToGasRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.registries.MekanismRecipeSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;

@NothingNullByDefault
public class MelterIRecipe extends ItemStackToFluidRecipe {

    public MelterIRecipe(ResourceLocation id, ItemStackIngredient input, FluidStack output) {
        super(id, input, output);
    }

    @Override
    public RecipeType<ItemStackToFluidRecipe> getType() {
        return EMRecipeType.MELTING.get();
    }

    @Override
    public RecipeSerializer<ItemStackToFluidRecipe> getSerializer() {
        return EMRecipeSerializers.MELTER.get();
    }

    @Override
    public String getGroup() {
        return EMBlocks.MELTER.getName();
    }

    @Override
    public ItemStack getToastSymbol() {
        return EMBlocks.MELTER.getItemStack();
    }
}