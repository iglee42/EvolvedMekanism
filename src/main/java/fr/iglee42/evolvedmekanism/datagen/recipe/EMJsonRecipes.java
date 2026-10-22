package fr.iglee42.evolvedmekanism.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class EMJsonRecipes {

    private EMJsonRecipes() {
    }

    public static void save(Consumer<FinishedRecipe> output, ResourceLocation id, JsonObject json, ICondition... conditions) {
        JsonObject copy = json.deepCopy();
        if (conditions.length > 0) {
            JsonArray array = copy.has("conditions") ? copy.getAsJsonArray("conditions") : new JsonArray();
            for (ICondition condition : conditions) {
                array.add(CraftingHelper.serialize(condition));
            }
            copy.add("conditions", array);
        }
        output.accept(new JsonFinishedRecipe(id, copy));
    }

    public static JsonObject itemIngredient(String item) {
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("item", item);
        JsonObject wrapper = new JsonObject();
        wrapper.add("ingredient", ingredient);
        return wrapper;
    }

    public static JsonObject tagIngredient(String tag) {
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("tag", tag);
        JsonObject wrapper = new JsonObject();
        wrapper.add("ingredient", ingredient);
        return wrapper;
    }

    public static JsonObject countedTagIngredient(String tag, int amount) {
        JsonObject json = tagIngredient(tag);
        if (amount != 1) {
            json.addProperty("amount", amount);
        }
        return json;
    }

    public static JsonObject countedItemIngredient(String item, int amount) {
        JsonObject json = itemIngredient(item);
        if (amount != 1) {
            json.addProperty("amount", amount);
        }
        return json;
    }

    public static JsonObject itemResult(String item, int count) {
        JsonObject json = new JsonObject();
        json.addProperty("item", item);
        if (count != 1) {
            json.addProperty("count", count);
        }
        return json;
    }

    private record JsonFinishedRecipe(ResourceLocation id, JsonObject json) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject recipe) {
            json.entrySet().forEach(entry -> {
                if (!"type".equals(entry.getKey())) {
                    recipe.add(entry.getKey(), entry.getValue());
                }
            });
        }

        @Override
        public JsonObject serializeRecipe() {
            return json.deepCopy();
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            ResourceLocation type = new ResourceLocation(json.get("type").getAsString());
            RecipeSerializer<?> serializer = ForgeRegistries.RECIPE_SERIALIZERS.getValue(type);
            return serializer != null ? serializer : RecipeSerializer.SHAPELESS_RECIPE;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
