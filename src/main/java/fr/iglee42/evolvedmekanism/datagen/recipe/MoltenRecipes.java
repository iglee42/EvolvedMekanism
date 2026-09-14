package fr.iglee42.evolvedmekanism.datagen.recipe;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.datagen.EMDatagenTags;
import fr.iglee42.evolvedmekanism.datagen.recipe.builders.EMRecipeBuilders;
import fr.iglee42.evolvedmekanism.registries.EMFluids;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.registries.MekanismFluids;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

public final class MoltenRecipes {

    private MoltenRecipes() {
    }

    public record MaterialForm(String recipeName, String tagPrefix, String moldPath, int amount) {
        public TagKey<Item> itemTag(String material) {
            return EMDatagenTags.cItem(tagPrefix + "/" + material);
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

    public static void addRecipes(RecipeOutput output) {
        EMFluids.FLUIDS.getFluidEntries().stream()
                .map(DeferredHolder::getId)
                .map(ResourceLocation::getPath)
                .filter(path -> path.startsWith("molten_"))
                .distinct()
                .forEach(fluidPath -> addMaterial(output, fluidPath.substring("molten_".length())));
    }

    private static void addMaterial(RecipeOutput output, String material) {
        TagKey<Fluid> moltenTag = EMDatagenTags.cFluid("molten_" + material);
        for (MaterialForm form : FORMS) {
            TagKey<Item> itemTag = form.itemTag(material);
            NotCondition present = new NotCondition(new TagEmptyCondition(itemTag.location()));
            int amount = formAmount(form, material);

            EMRecipeBuilders.melting(
                    IngredientCreatorAccess.item().from(itemTag),
                    IngredientCreatorAccess.fluid().from(moltenTag, amount)
            ).addCondition(present).build(output, EvolvedMekanism.rl("melting/" + material + "/" + form.recipeName()));

            String solidName = form.recipeName().replace("from_", "to_");
            EMRecipeBuilders.solidifying(
                    IngredientCreatorAccess.item().from(mold(form.moldPath())),
                    IngredientCreatorAccess.fluid().from(moltenTag, amount),
                    IngredientCreatorAccess.fluid().fromHolder(MekanismFluids.OXYGEN, amount),
                    amount * 2 / 3,
                    IngredientCreatorAccess.item().from(itemTag)
            ).addCondition(present).build(output, EvolvedMekanism.rl("solidifying/" + material + "/" + solidName));
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
}
