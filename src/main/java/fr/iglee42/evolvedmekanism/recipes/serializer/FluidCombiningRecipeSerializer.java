package fr.iglee42.evolvedmekanism.recipes.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.iglee42.evolvedmekanism.recipes.FluidCombiningRecipe;
import mekanism.api.JsonConstants;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.Mekanism;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public class FluidCombiningRecipeSerializer<RECIPE extends FluidCombiningRecipe> implements RecipeSerializer<RECIPE> {

    private final IFactory<RECIPE> factory;

    public FluidCombiningRecipeSerializer(IFactory<RECIPE> factory) {
        this.factory = factory;
    }

    @NotNull
    @Override
    public RECIPE fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        FluidStackIngredient mainInput = fluid(json, JsonConstants.MAIN_INPUT);
        FluidStackIngredient extraInput = fluid(json, JsonConstants.EXTRA_INPUT);
        FluidStackIngredient output = fluid(json, JsonConstants.OUTPUT);
        return this.factory.create(recipeId, mainInput, extraInput, output);
    }

    @Override
    public RECIPE fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        try {
            FluidStackIngredient mainInput = IngredientCreatorAccess.fluid().read(buffer);
            FluidStackIngredient extraInput = IngredientCreatorAccess.fluid().read(buffer);
            FluidStackIngredient output = IngredientCreatorAccess.fluid().read(buffer);
            return this.factory.create(recipeId, mainInput, extraInput, output);
        } catch (Exception e) {
            Mekanism.logger.error("Error reading fluid combining recipe from packet.", e);
            throw e;
        }
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull RECIPE recipe) {
        try {
            recipe.write(buffer);
        } catch (Exception e) {
            Mekanism.logger.error("Error writing fluid combining recipe to packet.", e);
            throw e;
        }
    }

    private static FluidStackIngredient fluid(JsonObject json, String key) {
        JsonElement element = GsonHelper.isArrayNode(json, key) ? GsonHelper.getAsJsonArray(json, key) : GsonHelper.getAsJsonObject(json, key);
        return IngredientCreatorAccess.fluid().deserialize(element);
    }

    @FunctionalInterface
    public interface IFactory<RECIPE extends FluidCombiningRecipe> {

        RECIPE create(ResourceLocation id, FluidStackIngredient mainInput, FluidStackIngredient extraInput, FluidStackIngredient output);
    }
}
