package fr.iglee42.evolvedmekanism.datagen.recipe;

import com.google.gson.JsonObject;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.datagen.EMDatagenTags;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Consumer;

public final class MoltenRecipes {

    private MoltenRecipes() {
    }

    public record MaterialForm(String recipeName, String tagPrefix, String moldPath, int amount) {
        public TagKey<Item> itemTag(String material) {
            return EMDatagenTags.forgeItem(tagPrefix + "/" + material);
        }
    }

    public static final List<MaterialForm> FORMS = List.of(
            new MaterialForm("from_ingot", "ingots", "mold_ingot", 90),
            new MaterialForm("from_nugget", "nuggets", "mold_nugget", 10),
            new MaterialForm("from_block", "storage_blocks", "mold_storage_block", 810),
            new MaterialForm("from_dust", "dusts", "mold_dust", 90),
            new MaterialForm("from_plate", "plates", "mold_plate", 90),
            new MaterialForm("from_gear", "gears", "mold_gear", 360),
            new MaterialForm("from_rod", "rods", "mold_rod", 45),
            new MaterialForm("from_wire", "wires", "mold_wire", 45),
            new MaterialForm("from_coin", "coins", "mold_coin", 30),
            new MaterialForm("from_gem", "gems", "mold_gem", 90)
    );

    public static void addRecipes(Consumer<FinishedRecipe> output) {
        ForgeRegistries.FLUIDS.getKeys().stream()
                .filter(id -> EvolvedMekanism.MODID.equals(id.getNamespace()))
                .map(ResourceLocation::getPath)
                .filter(path -> path.startsWith("molten_"))
                .distinct()
                .forEach(fluidPath -> addMaterial(output, fluidPath.substring("molten_".length())));
    }

    private static void addMaterial(Consumer<FinishedRecipe> output, String material) {
        TagKey<Fluid> moltenTag = EMDatagenTags.forgeFluid("molten_" + material);
        for (MaterialForm form : FORMS) {
            TagKey<Item> itemTag = form.itemTag(material);
            NotCondition present = new NotCondition(new TagEmptyCondition(itemTag.location().toString()));
            int amount = formAmount(form, material);

            JsonObject melting = new JsonObject();
            melting.addProperty("type", "evolvedmekanism:melting");
            melting.add("input", EMJsonRecipes.tagIngredient(itemTag.location().toString()));
            JsonObject fluidOut = new JsonObject();
            fluidOut.addProperty("tag", moltenTag.location().toString());
            fluidOut.addProperty("amount", amount);
            melting.add("output", fluidOut);
            EMJsonRecipes.save(output, EvolvedMekanism.rl("melting/" + material + "/" + form.recipeName()), melting, present);

            String solidName = form.recipeName().replace("from_", "to_");
            JsonObject solidifying = new JsonObject();
            solidifying.addProperty("type", "evolvedmekanism:solidifying");
            solidifying.addProperty("duration", amount * 2 / 3);
            solidifying.add("itemInput", EMJsonRecipes.itemIngredient(itemId(mold(form.moldPath()))));
            JsonObject fluidIn = new JsonObject();
            fluidIn.addProperty("tag", moltenTag.location().toString());
            fluidIn.addProperty("amount", amount);
            solidifying.add("fluidInput", fluidIn);
            JsonObject extra = new JsonObject();
            extra.addProperty("fluid", "mekanism:oxygen");
            extra.addProperty("amount", amount);
            solidifying.add("extraInput", extra);
            JsonObject itemOut = new JsonObject();
            itemOut.addProperty("tag", itemTag.location().toString());
            solidifying.add("output", itemOut);
            EMJsonRecipes.save(output, EvolvedMekanism.rl("solidifying/" + material + "/" + solidName), solidifying, present);
        }
    }

    private static int formAmount(MaterialForm form, String material) {
        if ("from_block".equals(form.recipeName()) && (material.equals("amethyst") || material.equals("glowstone") || material.equals("quartz"))) {
            return 360;
        }
        return form.amount();
    }

    private static ItemLike mold(String path) {
        return switch (path) {
            case "mold_ingot" -> EMItems.MOLD_INGOT;
            case "mold_nugget" -> EMItems.MOLD_NUGGET;
            case "mold_storage_block" -> EMItems.MOLD_BLOCK;
            case "mold_dust" -> EMItems.MOLD_DUST;
            case "mold_plate" -> EMItems.MOLD_PLATE;
            case "mold_gear" -> EMItems.MOLD_GEAR;
            case "mold_rod" -> EMItems.MOLD_ROD;
            case "mold_wire" -> EMItems.MOLD_WIRE;
            case "mold_coin" -> EMItems.MOLD_COIN;
            case "mold_gem" -> EMItems.MOLD_GEM;
            default -> throw new IllegalArgumentException("Unknown mold path: " + path);
        };
    }

    private static String itemId(ItemLike item) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item.asItem());
        if (id == null) {
            throw new IllegalStateException("Unregistered item during datagen: " + item);
        }
        return id.toString();
    }
}
