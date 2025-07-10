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

        public static final TagKey<Item> STORAGE_BLOCKS_BETTER_GOLD = forgeTag("storage_blocks/better_gold");
        public static final TagKey<Item> STORAGE_BLOCKS_PLASLITHERITE = forgeTag("storage_blocks/plaslitherite");

        public static final TagKey<Item> INGOTS_BETTER_GOLD = forgeTag("ingots/better_gold");
        public static final TagKey<Item> INGOTS_PLASLITHERITE = forgeTag("ingots/plaslitherite");

        public static final TagKey<Item> NUGGETS_BETTER_GOLD = forgeTag("nuggets/better_gold");
        public static final TagKey<Item> NUGGETS_PLASLITHERITE = forgeTag("nuggets/plaslitherite");


        public static final TagKey<Item> DUSTS_BETTER_GOLD = forgeTag("dusts/better_gold");
        public static final TagKey<Item> DUSTS_PLASLITHERITE = forgeTag("dusts/plaslitherite");

        public static final TagKey<Item> ALLOYS = tag("alloys");
        public static final TagKey<Item> ALLOYS_CREATIVE = tag("alloys/creative");
        public static final TagKey<Item> ALLOYS_EXOVERSAL = tag("alloys/exoversal");
        public static final TagKey<Item> ALLOYS_HYPERCHARGED = tag("alloys/hypercharged");
        public static final TagKey<Item> ALLOYS_SINGULAR = tag("alloys/singular");
        public static final TagKey<Item> ALLOYS_SUBATOMIC = tag("alloys/subatomic");

        public static final TagKey<Item> ALLOYS_CREATIVE_FORGE = forgeTag("alloys/creative");
        public static final TagKey<Item> ALLOYS_DENSE = forgeTag("alloys/dense");
        public static final TagKey<Item> ALLOYS_MULTIVERSAL = forgeTag("alloys/multiversal");
        public static final TagKey<Item> ALLOYS_OVERCLOCKED = forgeTag("alloys/overclocked");
        public static final TagKey<Item> ALLOYS_QUANTUM = forgeTag("alloys/quantum");
        
        public static final TagKey<Item> ENRICHED = tag("enriched");
        public static final TagKey<Item> ENRICHED_BETTER_GOLD = tag("enriched/better_gold");
        public static final TagKey<Item> ENRICHED_PLASLITHERITE = tag("enriched/plaslitherite");
        public static final TagKey<Item> ENRICHED_URANIUM = tag("enriched/uranium");

        public static final TagKey<Item> CIRCUITS_CREATIVE_FORGE = forgeTag("circuits/creative");
        public static final TagKey<Item> CIRCUITS_DENSE = forgeTag("circuits/dense");
        public static final TagKey<Item> CIRCUITS_MULTIVERSAL = forgeTag("circuits/multiversal");
        public static final TagKey<Item> CIRCUITS_OVERCLOCKED = forgeTag("circuits/overclocked");
        public static final TagKey<Item> CIRCUITS_QUANTUM = forgeTag("circuits/quantum");

        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", name));
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

        public static final TagKey<Block> STORAGE_BLOCKS_BETTER_GOLD = forgeTag("storage_blocks/better_gold");
        public static final TagKey<Block> STORAGE_BLOCKS_PLASLITHERITE = forgeTag("storage_blocks/plaslitherite");

        public static final TagKey<Block> STORAGE_BLOCKS_ALLOYS = forgeTag("storage_blocks/alloys");
        public static final TagKey<Block> STORAGE_BLOCKS_ALLOYS_INFUSED = forgeTag("storage_blocks/alloys/infused");
        public static final TagKey<Block> STORAGE_BLOCKS_ALLOYS_REINFORCED = forgeTag("storage_blocks/alloys/reinforced");
        public static final TagKey<Block> STORAGE_BLOCKS_ALLOYS_ATOMIC = forgeTag("storage_blocks/alloys/atomic");
        public static final TagKey<Block> STORAGE_BLOCKS_ALLOYS_HYPERCHARGED = forgeTag("storage_blocks/alloys/hypercharged");
        public static final TagKey<Block> STORAGE_BLOCKS_ALLOYS_SUBATOMIC = forgeTag("storage_blocks/alloys/subatomic");
        public static final TagKey<Block> STORAGE_BLOCKS_ALLOYS_SINGULAR = forgeTag("storage_blocks/alloys/singular");
        public static final TagKey<Block> STORAGE_BLOCKS_ALLOYS_EXOVERSAL = forgeTag("storage_blocks/alloys/exoversal");
        public static final TagKey<Block> STORAGE_BLOCKS_ALLOYS_CREATIVE = forgeTag("storage_blocks/alloys/creative");



        private static TagKey<Block> forgeTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("forge", name));
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

        private static TagKey<Fluid> forgeTag(String name) {
            return FluidTags.create(ResourceLocation.fromNamespaceAndPath("forge", name));
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