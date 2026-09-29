package fr.iglee42.evolvedmekanism.impl;

import fr.iglee42.evolvedmekanism.recipes.FluidAlloyingRecipe;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMRecipeSerializers;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

@NothingNullByDefault
public class BasicFluidAlloyingRecipe extends FluidAlloyingRecipe {

    public BasicFluidAlloyingRecipe(FluidStackIngredient mainInput, FluidStackIngredient extraInput, FluidStackIngredient tertiaryInput, FluidStackIngredient output) {
        super(mainInput, extraInput, tertiaryInput, output);
    }

    @Override
    public RecipeType<FluidAlloyingRecipe> getType() {
        return EMRecipeType.FLUID_ALLOYING.get();
    }

    @Override
    public RecipeSerializer<BasicFluidAlloyingRecipe> getSerializer() {
        return EMRecipeSerializers.FLUID_ALLOYING.get();
    }

    @Override
    public String getGroup() {
        return EMBlocks.FLUID_ALLOYER.getName();
    }

    @Override
    public ItemStack getToastSymbol() {
        return EMBlocks.FLUID_ALLOYER.asItem().getDefaultInstance();
    }
}
