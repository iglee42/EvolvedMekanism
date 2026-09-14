package fr.iglee42.evolvedmekanism.datagen.recipe;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.datagen.EMDatagenTags;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMFactoryType;
import fr.iglee42.evolvedmekanism.tiers.EMFactoryTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.content.blocktype.FactoryType;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public final class TieredRecipes {

    private TieredRecipes() {
    }

    public record TierData(String name, TagKey<Item> alloy, TagKey<Item> circuit, Object extra, String previousPrefix, boolean previousMekanism) {
    }

    public static final List<TierData> EM_TIERS = List.of(
            new TierData("overclocked", EMDatagenTags.item("evolvedmekanism", "alloys/hypercharged"), EMDatagenTags.cItem("circuits/overclocked"), EMDatagenTags.cItem("ingots/uranium"), "ultimate", true),
            new TierData("quantum", EMDatagenTags.item("evolvedmekanism", "alloys/subatomic"), EMDatagenTags.cItem("circuits/quantum"), EMDatagenTags.cItem("ingots/tin"), "overclocked", false),
            new TierData("dense", EMDatagenTags.item("evolvedmekanism", "alloys/singular"), EMDatagenTags.cItem("circuits/dense"), EMDatagenTags.cItem("ingots/bronze"), "quantum", false),
            new TierData("multiversal", EMDatagenTags.item("evolvedmekanism", "alloys/exoversal"), EMDatagenTags.cItem("circuits/multiversal"), EMDatagenTags.cItem("ingots/netherite"), "dense", false),
            new TierData("creative", EMDatagenTags.item("evolvedmekanism", "alloys/creative"), EMDatagenTags.cItem("circuits/creative"), Items.NETHER_STAR, "multiversal", false)
    );

    public static final List<TierData> ALLOYING_VANILLA_TIERS = List.of(
            new TierData("basic", EMDatagenTags.mekItem("alloys/basic"), EMDatagenTags.cItem("circuits/basic"), EMDatagenTags.cItem("ingots/iron"), null, false),
            new TierData("advanced", EMDatagenTags.mekItem("alloys/infused"), EMDatagenTags.cItem("circuits/advanced"), EMDatagenTags.cItem("ingots/osmium"), "basic", false),
            new TierData("elite", EMDatagenTags.mekItem("alloys/reinforced"), EMDatagenTags.cItem("circuits/elite"), EMDatagenTags.cItem("ingots/gold"), "advanced", false),
            new TierData("ultimate", EMDatagenTags.mekItem("alloys/atomic"), EMDatagenTags.cItem("circuits/ultimate"), EMDatagenTags.cItem("gems/diamond"), "elite", false)
    );

    public static ItemLike previousFactory(TierData tier, String type) {
        if (tier.previousPrefix() == null) {
            return EMBlocks.ALLOYER;
        }
        String name = tier.previousPrefix() + "_" + type + "_factory";
        if (tier.previousMekanism() && !"alloying".equals(type)) {
            return EMCrafting.item("mekanism:" + name);
        }
        return EMCrafting.item("evolvedmekanism:" + name);
    }

    public static ItemLike factoryResult(String tier, String type) {
        return EMCrafting.item("evolvedmekanism:" + tier + "_" + type + "_factory");
    }

    public static List<String> factoryTypes() {
        return List.of("smelting", "enriching", "crushing", "compressing", "combining", "purifying", "injecting", "infusing", "sawing", "alloying");
    }
}
