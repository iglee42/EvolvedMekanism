package fr.iglee42.evolvedmekanism.datagen.recipe;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.common.recipe.upgrade.MekanismShapedRecipe;
import mekanism.tools.common.recipe.PaxelRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public final class EMCrafting {

    private EMCrafting() {
    }

    public static RecipeOutput withConditions(RecipeOutput output, ICondition... conditions) {
        return conditions.length == 0 ? output : output.withConditions(conditions);
    }

    public static RecipeOutput mekData(RecipeOutput output) {
        return wrap(output, recipe -> recipe instanceof ShapedRecipe shaped ? new MekanismShapedRecipe(shaped) : recipe);
    }

    public static RecipeOutput paxel(RecipeOutput output) {
        return wrap(output, recipe -> recipe instanceof ShapedRecipe shaped ? new PaxelRecipe(shaped) : recipe);
    }

    private static RecipeOutput wrap(RecipeOutput output, java.util.function.Function<Recipe<?>, Recipe<?>> wrapper) {
        return new RecipeOutput() {
            @Override
            public void accept(ResourceLocation id, Recipe<?> recipe, @Nullable AdvancementHolder advancement, ICondition... conditions) {
                output.accept(id, wrapper.apply(recipe), advancement, conditions);
            }

            @Override
            public Advancement.Builder advancement() {
                return output.advancement();
            }
        };
    }

    public static void shaped(RecipeOutput output, ResourceLocation id, ItemLike result, int count, String[] pattern,
                              Map<Character, Object> keys, Criterion<?> criterion, ICondition... conditions) {
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

    public static void shaped(RecipeOutput output, String path, ItemLike result, int count, String[] pattern,
                              Map<Character, Object> keys, Criterion<?> criterion, ICondition... conditions) {
        shaped(output, EvolvedMekanism.rl(path), result, count, pattern, keys, criterion, conditions);
    }

    public static void shapeless(RecipeOutput output, ResourceLocation id, ItemLike result, int count, Object ingredient,
                                 Criterion<?> criterion, ICondition... conditions) {
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

    public static void smelting(RecipeOutput output, ResourceLocation id, Ingredient input, ItemLike result, float xp, int time,
                                Criterion<?> criterion, ICondition... conditions) {
        SimpleCookingRecipeBuilder.smelting(input, RecipeCategory.MISC, result, xp, time)
                .unlockedBy("has_item", criterion)
                .save(withConditions(output, conditions), id);
    }

    public static void blasting(RecipeOutput output, ResourceLocation id, Ingredient input, ItemLike result, float xp, int time,
                                Criterion<?> criterion, ICondition... conditions) {
        SimpleCookingRecipeBuilder.blasting(input, RecipeCategory.MISC, result, xp, time)
                .unlockedBy("has_item", criterion)
                .save(withConditions(output, conditions), id);
    }

    @SuppressWarnings("unchecked")
    private static void define(ShapedRecipeBuilder builder, char key, Object value) {
        switch (value) {
            case TagKey<?> tag -> builder.define(key, (TagKey<Item>) tag);
            case ItemLike item -> builder.define(key, item);
            case Ingredient ingredient -> builder.define(key, ingredient);
            default -> throw new IllegalArgumentException("Unsupported recipe key: " + value);
        }
    }

    @SuppressWarnings("unchecked")
    private static void addIngredient(ShapelessRecipeBuilder builder, Object value) {
        switch (value) {
            case TagKey<?> tag -> builder.requires((TagKey<Item>) tag);
            case ItemLike item -> builder.requires(item);
            case Ingredient ingredient -> builder.requires(ingredient);
            default -> throw new IllegalArgumentException("Unsupported recipe ingredient: " + value);
        }
    }

    public static Item item(String id) {
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
        if (item == net.minecraft.world.item.Items.AIR) {
            throw new IllegalStateException("Unknown item during datagen: " + id);
        }
        return item;
    }

    public static ItemStack stack(ItemLike item) {
        return new ItemStack(item);
    }
}
