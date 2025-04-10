package fr.iglee42.evolvedmekanism.recipes.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import fr.iglee42.evolvedmekanism.recipes.AlloyerRecipe;
import fr.iglee42.evolvedmekanism.utils.EMJsonConstants;
import mekanism.api.JsonConstants;
import mekanism.api.SerializerHelper;
import mekanism.api.recipes.CombinerRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.Mekanism;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public class AlloyerRecipeSerializer<RECIPE extends AlloyerRecipe> implements RecipeSerializer<RECIPE> {

    private final IFactory<RECIPE> factory;

    public AlloyerRecipeSerializer(IFactory<RECIPE> factory) {
        this.factory = factory;
    }

    @NotNull
    @Override
    public RECIPE fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        JsonElement mainInput = GsonHelper.isArrayNode(json, JsonConstants.MAIN_INPUT) ? GsonHelper.getAsJsonArray(json, JsonConstants.MAIN_INPUT) :
                                GsonHelper.getAsJsonObject(json, JsonConstants.MAIN_INPUT);
        ItemStackIngredient mainIngredient = IngredientCreatorAccess.item().deserialize(mainInput);
        JsonElement extraInput = GsonHelper.isArrayNode(json, JsonConstants.EXTRA_INPUT) ? GsonHelper.getAsJsonArray(json, JsonConstants.EXTRA_INPUT) :
                                 GsonHelper.getAsJsonObject(json, JsonConstants.EXTRA_INPUT);
        ItemStackIngredient extraIngredient = IngredientCreatorAccess.item().deserialize(extraInput);
        JsonElement secondExtraInput = GsonHelper.isArrayNode(json, EMJsonConstants.SECOND_EXTRA_INPUT) ? GsonHelper.getAsJsonArray(json, EMJsonConstants.SECOND_EXTRA_INPUT) :
                GsonHelper.getAsJsonObject(json, EMJsonConstants.SECOND_EXTRA_INPUT);
        ItemStackIngredient secondExtraIngredient = IngredientCreatorAccess.item().deserialize(secondExtraInput);
        ItemStack output = SerializerHelper.getItemStack(json, JsonConstants.OUTPUT);
        if (output.isEmpty()) {
            throw new JsonSyntaxException("Alloyer recipe output must not be empty.");
        }
        return this.factory.create(recipeId, mainIngredient, extraIngredient,secondExtraIngredient, output);
    }

    @Override
    public RECIPE fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        try {
            ItemStackIngredient mainInput = IngredientCreatorAccess.item().read(buffer);
            ItemStackIngredient extraInput = IngredientCreatorAccess.item().read(buffer);
            ItemStackIngredient secondExtraInput = IngredientCreatorAccess.item().read(buffer);
            ItemStack output = buffer.readItem();
            return this.factory.create(recipeId, mainInput, extraInput,secondExtraInput, output);
        } catch (Exception e) {
            Mekanism.logger.error("Error reading alloyer recipe from packet.", e);
            throw e;
        }
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull RECIPE recipe) {
        try {
            recipe.write(buffer);
        } catch (Exception e) {
            Mekanism.logger.error("Error writing alloyer recipe to packet.", e);
            throw e;
        }
    }

    @FunctionalInterface
    public interface IFactory<RECIPE extends AlloyerRecipe> {

        RECIPE create(ResourceLocation id, ItemStackIngredient mainInput, ItemStackIngredient extraInput, ItemStackIngredient secondExtraInput, ItemStack output);
    }
}