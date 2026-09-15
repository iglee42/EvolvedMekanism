package fr.iglee42.evolvedmekanism.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class EMCrafting {

    private EMCrafting() {
    }

    public static Consumer<FinishedRecipe> withConditions(Consumer<FinishedRecipe> output, ICondition... conditions) {
        if (conditions.length == 0) {
            return output;
        }
        return recipe -> output.accept(new ConditionedFinishedRecipe(recipe, conditions));
    }

    public static Consumer<FinishedRecipe> mekData(Consumer<FinishedRecipe> output) {
        return wrapType(output, "mekanism:mek_data");
    }

    public static Consumer<FinishedRecipe> paxel(Consumer<FinishedRecipe> output) {
        return wrapType(output, "mekanismtools:paxel");
    }

    private static Consumer<FinishedRecipe> wrapType(Consumer<FinishedRecipe> output, String type) {
        return recipe -> output.accept(new TypedFinishedRecipe(recipe, type));
    }

    public static void shaped(Consumer<FinishedRecipe> output, ResourceLocation id, ItemLike result, int count, String[] pattern,
                              Map<Character, Object> keys, InventoryChangeTrigger.TriggerInstance criterion, ICondition... conditions) {
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, count);
        for (String line : pattern) {
            builder.pattern(line);
        }
        for (Map.Entry<Character, Object> entry : keys.entrySet()) {
            define(builder, entry.getKey(), entry.getValue());
        }
        builder.unlockedBy("has_item", criterion);
        builder.save(withConditions(output, conditions), id);
    }

    public static void shaped(Consumer<FinishedRecipe> output, String path, ItemLike result, int count, String[] pattern,
                              Map<Character, Object> keys, InventoryChangeTrigger.TriggerInstance criterion, ICondition... conditions) {
        shaped(output, EvolvedMekanism.rl(path), result, count, pattern, keys, criterion, conditions);
    }

    public static void shapeless(Consumer<FinishedRecipe> output, ResourceLocation id, ItemLike result, int count, Object ingredient,
                                 InventoryChangeTrigger.TriggerInstance criterion, ICondition... conditions) {
        ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, count);
        addIngredient(builder, ingredient);
        builder.unlockedBy("has_item", criterion);
        builder.save(withConditions(output, conditions), id);
    }

    public static Map<Character, Object> keys(Object... values) {
        if (values.length % 2 != 0) {
            throw new IllegalArgumentException("Key values must come in pairs");
        }
        Map<Character, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < values.length; i += 2) {
            map.put((Character) values[i], values[i + 1]);
        }
        return map;
    }

    public static void smelting(Consumer<FinishedRecipe> output, ResourceLocation id, Ingredient input, ItemLike result, float xp, int time,
                                InventoryChangeTrigger.TriggerInstance criterion, ICondition... conditions) {
        SimpleCookingRecipeBuilder.smelting(input, RecipeCategory.MISC, result, xp, time)
                .unlockedBy("has_item", criterion)
                .save(withConditions(output, conditions), id);
    }

    public static void blasting(Consumer<FinishedRecipe> output, ResourceLocation id, Ingredient input, ItemLike result, float xp, int time,
                                InventoryChangeTrigger.TriggerInstance criterion, ICondition... conditions) {
        SimpleCookingRecipeBuilder.blasting(input, RecipeCategory.MISC, result, xp, time)
                .unlockedBy("has_item", criterion)
                .save(withConditions(output, conditions), id);
    }

    @SuppressWarnings("unchecked")
    private static void define(ShapedRecipeBuilder builder, char key, Object value) {
        if (value instanceof TagKey<?> tag) {
            builder.define(key, (TagKey<Item>) tag);
        } else if (value instanceof ItemLike item) {
            builder.define(key, item);
        } else if (value instanceof Ingredient ingredient) {
            builder.define(key, ingredient);
        } else {
            throw new IllegalArgumentException("Unsupported recipe key: " + value);
        }
    }

    @SuppressWarnings("unchecked")
    private static void addIngredient(ShapelessRecipeBuilder builder, Object value) {
        if (value instanceof TagKey<?> tag) {
            builder.requires((TagKey<Item>) tag);
        } else if (value instanceof ItemLike item) {
            builder.requires(item);
        } else if (value instanceof Ingredient ingredient) {
            builder.requires(ingredient);
        } else {
            throw new IllegalArgumentException("Unsupported recipe ingredient: " + value);
        }
    }

    public static Item item(String id) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
        if (item == null || item == net.minecraft.world.item.Items.AIR) {
            throw new IllegalStateException("Unknown item during datagen: " + id);
        }
        return item;
    }

    public static ItemStack stack(ItemLike item) {
        return new ItemStack(item);
    }

    private record TypedFinishedRecipe(FinishedRecipe inner, String type) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            inner.serializeRecipeData(json);
        }

        @Override
        public JsonObject serializeRecipe() {
            JsonObject json = inner.serializeRecipe();
            json.addProperty("type", type);
            return json;
        }

        @Override
        public ResourceLocation getId() {
            return inner.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            RecipeSerializer<?> serializer = ForgeRegistries.RECIPE_SERIALIZERS.getValue(new ResourceLocation(type));
            return serializer != null ? serializer : inner.getType();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return inner.serializeAdvancement();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return inner.getAdvancementId();
        }
    }

    private record ConditionedFinishedRecipe(FinishedRecipe inner, ICondition[] conditions) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            inner.serializeRecipeData(json);
        }

        @Override
        public JsonObject serializeRecipe() {
            JsonObject json = inner.serializeRecipe();
            JsonArray array = json.has("conditions") ? json.getAsJsonArray("conditions") : new JsonArray();
            for (ICondition condition : conditions) {
                array.add(CraftingHelper.serialize(condition));
            }
            json.add("conditions", array);
            return json;
        }

        @Override
        public ResourceLocation getId() {
            return inner.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return inner.getType();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return inner.serializeAdvancement();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return inner.getAdvancementId();
        }
    }
}
