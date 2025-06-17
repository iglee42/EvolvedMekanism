package fr.iglee42.evolvedmekanism.recipes.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import fr.iglee42.evolvedmekanism.recipes.SolidificationRecipe;
import fr.iglee42.evolvedmekanism.utils.EMJsonConstants;
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

public class SolidificationRecipeSerializer<RECIPE extends SolidificationRecipe> implements RecipeSerializer<RECIPE> {

    private final IFactory<RECIPE> factory;

    public SolidificationRecipeSerializer(IFactory<RECIPE> factory) {
        this.factory = factory;
    }

    @NotNull
    @Override
    public RECIPE fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        JsonElement itemInput = GsonHelper.isArrayNode(json, JsonConstants.ITEM_INPUT) ? GsonHelper.getAsJsonArray(json, JsonConstants.ITEM_INPUT) :
                                GsonHelper.getAsJsonObject(json, JsonConstants.ITEM_INPUT);
        ItemStackIngredient solidIngredient = IngredientCreatorAccess.item().deserialize(itemInput);
        JsonElement fluidInput = GsonHelper.isArrayNode(json, JsonConstants.FLUID_INPUT) ? GsonHelper.getAsJsonArray(json, JsonConstants.FLUID_INPUT) :
                                 GsonHelper.getAsJsonObject(json, JsonConstants.FLUID_INPUT);
        FluidStackIngredient fluidIngredient = IngredientCreatorAccess.fluid().deserialize(fluidInput);
        JsonElement extraFluidInput = GsonHelper.isArrayNode(json, JsonConstants.EXTRA_INPUT) ? GsonHelper.getAsJsonArray(json, JsonConstants.EXTRA_INPUT) :
                               GsonHelper.getAsJsonObject(json, JsonConstants.EXTRA_INPUT);
        FluidStackIngredient extraFluidIngredient = IngredientCreatorAccess.fluid().deserialize(extraFluidInput);
        FloatingLong energyRequired = FloatingLong.ZERO;
        if (json.has(JsonConstants.ENERGY_REQUIRED)) {
            energyRequired = SerializerHelper.getFloatingLong(json, JsonConstants.ENERGY_REQUIRED);
        }

        JsonElement ticks = json.get(JsonConstants.DURATION);
        if (!GsonHelper.isNumberValue(ticks)) {
            throw new JsonSyntaxException("Expected duration to be a number greater than zero.");
        }
        int duration = ticks.getAsJsonPrimitive().getAsInt();
        if (duration <= 0) {
            throw new JsonSyntaxException("Expected duration to be a number greater than zero.");
        }
        ItemStack itemOutput = SerializerHelper.getItemStack(json, JsonConstants.OUTPUT);
        if (itemOutput.isEmpty()) {
            throw new JsonSyntaxException("Solidification chamber item output must not be empty.");
        }
        boolean keepItem = !json.has(EMJsonConstants.KEEP_ITEM) || json.get(EMJsonConstants.KEEP_ITEM).getAsBoolean();
        return this.factory.create(recipeId, solidIngredient, fluidIngredient, extraFluidIngredient, energyRequired, duration, itemOutput,keepItem);
    }

    @Override
    public RECIPE fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        try {
            ItemStackIngredient inputSolid = IngredientCreatorAccess.item().read(buffer);
            FluidStackIngredient inputFluid = IngredientCreatorAccess.fluid().read(buffer);
            FluidStackIngredient inputExtraFluid = IngredientCreatorAccess.fluid().read(buffer);
            FloatingLong energyRequired = FloatingLong.readFromBuffer(buffer);
            int duration = buffer.readVarInt();
            ItemStack outputItem = buffer.readItem();
            boolean keepItem = buffer.readBoolean();
            return this.factory.create(recipeId, inputSolid, inputFluid, inputExtraFluid, energyRequired, duration, outputItem,keepItem);
        } catch (Exception e) {
            Mekanism.logger.error("Error reading solidification recipe from packet.", e);
            throw e;
        }
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull RECIPE recipe) {
        try {
            recipe.write(buffer);
        } catch (Exception e) {
            Mekanism.logger.error("Error writing solidification recipe to packet.", e);
            throw e;
        }
    }

    @FunctionalInterface
    public interface IFactory<RECIPE extends SolidificationRecipe> {

        RECIPE create(ResourceLocation id, ItemStackIngredient itemInput, FluidStackIngredient fluidInput, FluidStackIngredient extraFluidInput, FloatingLong energyRequired, int duration,
              ItemStack outputItem,boolean keepItem);
    }
}