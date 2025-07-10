package fr.iglee42.evolvedmekanism.impl;

import fr.iglee42.evolvedmekanism.recipes.SolidificationRecipe;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMRecipeSerializers;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

@NothingNullByDefault
public class BasicSolidificationRecipe extends SolidificationRecipe {

    public BasicSolidificationRecipe(ItemStackIngredient inputSolid, FluidStackIngredient inputFluid, FluidStackIngredient inputFluidExtra, long energyRequired, int duration, ItemStackIngredient outputItem, boolean keepItem) {
        super(inputSolid, inputFluid, inputFluidExtra, energyRequired, duration, outputItem, keepItem);
    }

    @Override
    public RecipeType<SolidificationRecipe> getType() {
        return EMRecipeType.SOLIDIFICATION.get();
    }

    @Override
    public RecipeSerializer<BasicSolidificationRecipe> getSerializer() {
        return EMRecipeSerializers.SOLIDIFICATION.get();
    }

    @Override
    public String getGroup() {
        return EMBlocks.SOLIDIFIER.getName();
    }

    @Override
    public ItemStack getToastSymbol() {
        return EMBlocks.SOLIDIFIER.asItem().getDefaultInstance();
    }
}