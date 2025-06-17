package fr.iglee42.evolvedmekanism.impl;

import fr.iglee42.evolvedmekanism.recipes.SolidificationRecipe;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMRecipeSerializers;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.PressurizedReactionRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.registries.MekanismRecipeSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

@NothingNullByDefault
public class SolidificationIRecipe extends SolidificationRecipe {

    public SolidificationIRecipe(ResourceLocation id, ItemStackIngredient inputSolid, FluidStackIngredient inputFluid, FluidStackIngredient inputExtraFluid,
                                 FloatingLong energyRequired, int duration, ItemStack outputItem,boolean keepItem) {
        super(id, inputSolid, inputFluid, inputExtraFluid, energyRequired, duration, outputItem,keepItem);
    }

    @Override
    public RecipeType<SolidificationRecipe> getType() {
        return EMRecipeType.SOLIDIFICATION.get();
    }

    @Override
    public RecipeSerializer<SolidificationRecipe> getSerializer() {
        return EMRecipeSerializers.SOLIDIFICATION.get();
    }

    @Override
    public String getGroup() {
        return EMBlocks.SOLIDIFIER.getName();
    }

    @Override
    public ItemStack getToastSymbol() {
        return EMBlocks.SOLIDIFIER.getItemStack();
    }
}