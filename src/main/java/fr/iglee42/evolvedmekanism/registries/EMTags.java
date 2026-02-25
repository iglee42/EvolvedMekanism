package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class EMTags {

    /**
     * Call to force make sure this is all initialized
     */
    public static void init() {
        Items.init();
        Blocks.init();
        Fluids.init();
        Gases.init();
    }

    private EMTags() {
    }

    public static class Items {

        private static void init() {
        }

        private Items() {
        }

        public static final TagKey<Item> STORAGE_BLOCKS_BETTER_GOLD = commonTag("storage_blocks/better_gold");
        public static final TagKey<Item> STORAGE_BLOCKS_PLASLITHERITE = commonTag("storage_blocks/plaslitherite");
        public static final TagKey<Item> STORAGE_BLOCKS_REFINED_REDSTONE = commonTag("storage_blocks/refined_redstone");
        public static final TagKey<Item> STORAGE_BLOCKS_NOCTIS_ROZULI = commonTag("storage_blocks/noctis_rozuli");

        public static final TagKey<Item> INGOTS_BETTER_GOLD = commonTag("ingots/better_gold");
        public static final TagKey<Item> INGOTS_PLASLITHERITE = commonTag("ingots/plaslitherite");
        public static final TagKey<Item> INGOTS_REFINED_REDSTONE = commonTag("ingots/refined_redstone");
        public static final TagKey<Item> GEMS_NOCTIS_ROZULI = commonTag("gems/noctis_rozuli");

        public static final TagKey<Item> NUGGETS_BETTER_GOLD = commonTag("nuggets/better_gold");
        public static final TagKey<Item> NUGGETS_PLASLITHERITE = commonTag("nuggets/plaslitherite");
        public static final TagKey<Item> NUGGETS_REFINED_REDSTONE = commonTag("nuggets/refined_redstone");


        public static final TagKey<Item> DUSTS_BETTER_GOLD = commonTag("dusts/better_gold");
        public static final TagKey<Item> DUSTS_PLASLITHERITE = commonTag("dusts/plaslitherite");
        public static final TagKey<Item> DUSTS_NOCTIS_ROZULI = commonTag("dusts/noctis_rozuli");

        public static final TagKey<Item> ALLOYS = tag("alloys");
        public static final TagKey<Item> ALLOYS_CREATIVE = tag("alloys/creative");
        public static final TagKey<Item> ALLOYS_EXOVERSAL = tag("alloys/exoversal");
        public static final TagKey<Item> ALLOYS_HYPERCHARGED = tag("alloys/hypercharged");
        public static final TagKey<Item> ALLOYS_SINGULAR = tag("alloys/singular");
        public static final TagKey<Item> ALLOYS_SUBATOMIC = tag("alloys/subatomic");

        public static final TagKey<Item> ALLOYS_CREATIVE_FORGE = commonTag("alloys/creative");
        public static final TagKey<Item> ALLOYS_DENSE = commonTag("alloys/dense");
        public static final TagKey<Item> ALLOYS_MULTIVERSAL = commonTag("alloys/multiversal");
        public static final TagKey<Item> ALLOYS_OVERCLOCKED = commonTag("alloys/overclocked");
        public static final TagKey<Item> ALLOYS_QUANTUM = commonTag("alloys/quantum");
        
        public static final TagKey<Item> ENRICHED = tag("enriched");
        public static final TagKey<Item> ENRICHED_BETTER_GOLD = tag("enriched/better_gold");
        public static final TagKey<Item> ENRICHED_PLASLITHERITE = tag("enriched/plaslitherite");
        public static final TagKey<Item> ENRICHED_URANIUM = tag("enriched/uranium");

        public static final TagKey<Item> CIRCUITS_CREATIVE_FORGE = commonTag("circuits/creative");
        public static final TagKey<Item> CIRCUITS_DENSE = commonTag("circuits/dense");
        public static final TagKey<Item> CIRCUITS_MULTIVERSAL = commonTag("circuits/multiversal");
        public static final TagKey<Item> CIRCUITS_OVERCLOCKED = commonTag("circuits/overclocked");
        public static final TagKey<Item> CIRCUITS_QUANTUM = commonTag("circuits/quantum");

        private static TagKey<Item> commonTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }
        private static TagKey<Item> mekTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("mekanism", name));
        }

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(EvolvedMekanism.rl(name));
        }
    }

    public static class Blocks {

        private static void init() {
        }

        private Blocks() {
        }

        public static final TagKey<Block> STORAGE_BLOCKS_BETTER_GOLD = commonTag("storage_blocks/better_gold");
        public static final TagKey<Block> STORAGE_BLOCKS_PLASLITHERITE = commonTag("storage_blocks/plaslitherite");
        public static final TagKey<Block> STORAGE_BLOCKS_REFINED_REDSTONE = commonTag("storage_blocks/refined_redstone");

        public static final TagKey<Block> STORAGE_BLOCKS_ALLOYS = commonTag("storage_blocks/alloys");
        public static final TagKey<Block> STORAGE_BLOCKS_ALLOYS_INFUSED = commonTag("storage_blocks/alloys/infused");
        public static final TagKey<Block> STORAGE_BLOCKS_ALLOYS_REINFORCED = commonTag("storage_blocks/alloys/reinforced");
        public static final TagKey<Block> STORAGE_BLOCKS_ALLOYS_ATOMIC = commonTag("storage_blocks/alloys/atomic");
        public static final TagKey<Block> STORAGE_BLOCKS_ALLOYS_HYPERCHARGED = commonTag("storage_blocks/alloys/hypercharged");
        public static final TagKey<Block> STORAGE_BLOCKS_ALLOYS_SUBATOMIC = commonTag("storage_blocks/alloys/subatomic");
        public static final TagKey<Block> STORAGE_BLOCKS_ALLOYS_SINGULAR = commonTag("storage_blocks/alloys/singular");
        public static final TagKey<Block> STORAGE_BLOCKS_ALLOYS_EXOVERSAL = commonTag("storage_blocks/alloys/exoversal");
        public static final TagKey<Block> STORAGE_BLOCKS_ALLOYS_CREATIVE = commonTag("storage_blocks/alloys/creative");



        private static TagKey<Block> commonTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }

        private static TagKey<Block> mekTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("mekanism", name));
        }

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(EvolvedMekanism.rl(name));
        }
    }


    public static class Fluids {

        private static void init() {
        }

        private Fluids() {
        }

        private static TagKey<Fluid> commonTag(String name) {
            return FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }

    public static class Gases {

        private static void init() {
        }

        private Gases() {
        }
        public static final TagKey<Chemical> BETTER_GOLD = tag("better_gold");
        public static final TagKey<Chemical> PLASLITHERITE = tag("plaslitherite");
        public static final TagKey<Chemical> URANIUM = tag("uranium");



        private static TagKey<Chemical> tag(String name) {
            return TagKey.create(MekanismAPI.CHEMICAL_REGISTRY_NAME,EvolvedMekanism.rl(name));
        }
    }
}