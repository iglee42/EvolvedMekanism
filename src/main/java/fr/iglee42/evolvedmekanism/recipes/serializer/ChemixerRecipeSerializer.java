package fr.iglee42.evolvedmekanism.recipes.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import fr.iglee42.evolvedmekanism.recipes.ChemixerRecipe;
import mekanism.api.JsonConstants;
import mekanism.api.SerializerHelper;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.PressurizedReactionRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.Mekanism;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public class ChemixerRecipeSerializer<RECIPE extends ChemixerRecipe> implements RecipeSerializer<RECIPE> {

    private final IFactory<RECIPE> factory;

    public ChemixerRecipeSerializer(IFactory<RECIPE> factory) {
        this.factory = factory;
    }

    @NotNull
    @Override
    public RECIPE fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        JsonElement itemInput = GsonHelper.isArrayNode(json, JsonConstants.MAIN_INPUT) ? GsonHelper.getAsJsonArray(json, JsonConstants.MAIN_INPUT) :
                                GsonHelper.getAsJsonObject(json, JsonConstants.MAIN_INPUT);
        ItemStackIngredient solidIngredient = IngredientCreatorAccess.item().deserialize(itemInput);
        JsonElement extraInput = GsonHelper.isArrayNode(json, JsonConstants.EXTRA_INPUT) ? GsonHelper.getAsJsonArray(json, JsonConstants.EXTRA_INPUT) :
                                 GsonHelper.getAsJsonObject(json, JsonConstants.EXTRA_INPUT);
        ItemStackIngredient extraIngredient = IngredientCreatorAccess.item().deserialize(extraInput);
        JsonElement gasInput = GsonHelper.isArrayNode(json, JsonConstants.GAS_INPUT) ? GsonHelper.getAsJsonArray(json, JsonConstants.GAS_INPUT) :
                               GsonHelper.getAsJsonObject(json, JsonConstants.GAS_INPUT);
        GasStackIngredient gasIngredient = IngredientCreatorAccess.gas().deserialize(gasInput);
        ItemStack output = SerializerHelper.getItemStack(json, JsonConstants.OUTPUT);
        if (output.isEmpty()) {
            throw new JsonSyntaxException("Chemixer recipe output must not be empty.");
        }
        return this.factory.create(recipeId, solidIngredient, extraIngredient, gasIngredient, output);
    }

    @Override
    public RECIPE fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        try {
            ItemStackIngredient inputSolid = IngredientCreatorAccess.item().read(buffer);
            ItemStackIngredient inputExtra = IngredientCreatorAccess.item().read(buffer);
            GasStackIngredient inputGas = IngredientCreatorAccess.gas().read(buffer);
            ItemStack outputItem = buffer.readItem();
            return this.factory.create(recipeId, inputSolid, inputExtra, inputGas, outputItem);
        } catch (Exception e) {
            Mekanism.logger.error("Error reading chemixer recipe from packet.", e);
            throw e;
        }
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull RECIPE recipe) {
        try {
            recipe.write(buffer);
        } catch (Exception e) {
            Mekanism.logger.error("Error writing chemixer recipe to packet.", e);
            throw e;
        }
    }

    @FunctionalInterface
    public interface IFactory<RECIPE extends ChemixerRecipe> {

        RECIPE create(ResourceLocation id, ItemStackIngredient mainInput, ItemStackIngredient extraInput, GasStackIngredient gasInput, ItemStack outputItem);
    }
}