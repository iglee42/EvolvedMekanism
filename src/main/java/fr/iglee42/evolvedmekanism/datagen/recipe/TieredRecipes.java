package fr.iglee42.evolvedmekanism.datagen.recipe;

import fr.iglee42.evolvedmekanism.datagen.EMDatagenTags;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
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
            new TierData("overclocked", EMDatagenTags.item("evolvedmekanism", "alloys/hypercharged"), EMDatagenTags.forgeItem("circuits/overclocked"), EMDatagenTags.forgeItem("ingots/uranium"), "ultimate", true),
            new TierData("quantum", EMDatagenTags.item("evolvedmekanism", "alloys/subatomic"), EMDatagenTags.forgeItem("circuits/quantum"), EMDatagenTags.forgeItem("ingots/tin"), "overclocked", false),
            new TierData("dense", EMDatagenTags.item("evolvedmekanism", "alloys/singular"), EMDatagenTags.forgeItem("circuits/dense"), EMDatagenTags.forgeItem("ingots/bronze"), "quantum", false),
            new TierData("multiversal", EMDatagenTags.item("evolvedmekanism", "alloys/exoversal"), EMDatagenTags.forgeItem("circuits/multiversal"), EMDatagenTags.forgeItem("ingots/netherite"), "dense", false),
            new TierData("creative", EMDatagenTags.item("evolvedmekanism", "alloys/creative"), EMDatagenTags.forgeItem("circuits/creative"), Items.NETHER_STAR, "multiversal", false)
    );

    public static final List<TierData> ALLOYING_VANILLA_TIERS = List.of(
            new TierData("basic", EMDatagenTags.mekItem("alloys/basic"), EMDatagenTags.forgeItem("circuits/basic"), EMDatagenTags.forgeItem("ingots/iron"), null, false),
            new TierData("advanced", EMDatagenTags.mekItem("alloys/infused"), EMDatagenTags.forgeItem("circuits/advanced"), EMDatagenTags.forgeItem("ingots/osmium"), "basic", false),
            new TierData("elite", EMDatagenTags.mekItem("alloys/reinforced"), EMDatagenTags.forgeItem("circuits/elite"), EMDatagenTags.forgeItem("ingots/gold"), "advanced", false),
            new TierData("ultimate", EMDatagenTags.mekItem("alloys/atomic"), EMDatagenTags.forgeItem("circuits/ultimate"), EMDatagenTags.forgeItem("gems/diamond"), "elite", false)
    );

    public static ItemLike previousFactory(TierData tier, String type) {
        if (tier.previousPrefix() == null) {
            return EMBlocks.ALLOYER;
        }
        String name = tier.previousPrefix() + "_" + type + "_factory";
        if ("alloying".equals(type) && isVanillaAlloyingTier(tier.previousPrefix())) {
            return EMCrafting.item("mekanism:" + name);
        }
        if (tier.previousMekanism()) {
            return EMCrafting.item("mekanism:" + name);
        }
        return EMCrafting.item("evolvedmekanism:" + name);
    }

    public static ItemLike factoryResult(String tier, String type) {
        if ("alloying".equals(type) && isVanillaAlloyingTier(tier)) {
            return EMCrafting.item("mekanism:" + tier + "_alloying_factory");
        }
        return EMCrafting.item("evolvedmekanism:" + tier + "_" + type + "_factory");
    }

    public static boolean isVanillaAlloyingTier(String tier) {
        return "basic".equals(tier) || "advanced".equals(tier) || "elite".equals(tier) || "ultimate".equals(tier);
    }

    public static List<String> factoryTypes() {
        return List.of("smelting", "enriching", "crushing", "compressing", "combining", "purifying", "injecting", "infusing", "sawing", "alloying");
    }
}
