package fr.iglee42.evolvedmekanism.recipes.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.iglee42.evolvedmekanism.recipes.MeltingRecipe;
import mekanism.api.JsonConstants;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.Mekanism;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public class ItemStackToFluidRecipeSerializer<RECIPE extends MeltingRecipe> implements RecipeSerializer<RECIPE> {

    private final IFactory<RECIPE> factory;

    public ItemStackToFluidRecipeSerializer(IFactory<RECIPE> factory) {
        this.factory = factory;
    }

    @NotNull
    @Override
    public RECIPE fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        JsonElement input = GsonHelper.isArrayNode(json, JsonConstants.INPUT) ? GsonHelper.getAsJsonArray(json, JsonConstants.INPUT) :
                            GsonHelper.getAsJsonObject(json, JsonConstants.INPUT);
        ItemStackIngredient inputIngredient = IngredientCreatorAccess.item().deserialize(input);
        JsonElement output = GsonHelper.isArrayNode(json, JsonConstants.OUTPUT) ? GsonHelper.getAsJsonArray(json, JsonConstants.OUTPUT) :
                             GsonHelper.getAsJsonObject(json, JsonConstants.OUTPUT);
        FluidStackIngredient outputIngredient = IngredientCreatorAccess.fluid().deserialize(output);
        return this.factory.create(recipeId, inputIngredient, outputIngredient);
    }

    @Override
    public RECIPE fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        try {
            ItemStackIngredient inputIngredient = IngredientCreatorAccess.item().read(buffer);
            FluidStackIngredient output = IngredientCreatorAccess.fluid().read(buffer);
            return this.factory.create(recipeId, inputIngredient, output);
        } catch (Exception e) {
            Mekanism.logger.error("Error reading itemstack to fluidstack recipe from packet.", e);
            throw e;
        }
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull RECIPE recipe) {
        try {
            recipe.write(buffer);
        } catch (Exception e) {
            Mekanism.logger.error("Error writing itemstack to fluidstack recipe to packet.", e);
            throw e;
        }
    }

    @FunctionalInterface
    public interface IFactory<RECIPE extends MeltingRecipe> {

        RECIPE create(ResourceLocation id, ItemStackIngredient input, FluidStackIngredient output);
    }
}
