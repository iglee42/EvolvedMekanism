package fr.iglee42.evolvedmekanism.impl;

import fr.iglee42.evolvedmekanism.recipes.MeltingRecipe;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMRecipeSerializers;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

@NothingNullByDefault
public class MelterIRecipe extends MeltingRecipe {

    public MelterIRecipe(ResourceLocation id, ItemStackIngredient input, FluidStackIngredient output) {
        super(id, input, output);
    }

    @Override
    public RecipeType<MeltingRecipe> getType() {
        return EMRecipeType.MELTING.get();
    }

    @Override
    public RecipeSerializer<MeltingRecipe> getSerializer() {
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
