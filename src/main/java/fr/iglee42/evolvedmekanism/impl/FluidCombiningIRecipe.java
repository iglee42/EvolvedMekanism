package fr.iglee42.evolvedmekanism.impl;

import fr.iglee42.evolvedmekanism.recipes.FluidCombiningRecipe;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMRecipeSerializers;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

@NothingNullByDefault
public class FluidCombiningIRecipe extends FluidCombiningRecipe {

    public FluidCombiningIRecipe(ResourceLocation id, FluidStackIngredient mainInput, FluidStackIngredient extraInput, FluidStackIngredient output) {
        super(id, mainInput, extraInput, output);
    }

    @Override
    public RecipeType<FluidCombiningRecipe> getType() {
        return EMRecipeType.FLUID_COMBINING.get();
    }

    @Override
    public RecipeSerializer<FluidCombiningRecipe> getSerializer() {
        return EMRecipeSerializers.FLUID_COMBINING.get();
    }

    @Override
    public String getGroup() {
        return EMBlocks.FLUID_COMBINER.getName();
    }

    @Override
    public ItemStack getToastSymbol() {
        return EMBlocks.FLUID_COMBINER.getItemStack();
    }
}
