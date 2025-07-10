package fr.iglee42.emtools.registries;

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

public class EMToolsTags {

    /**
     * Call to force make sure this is all initialized
     */
    public static void init() {
        Items.init();
        Blocks.init();
        Fluids.init();
        Gases.init();
    }

    private EMToolsTags() {
    }

    public static class Items {

        private static void init() {
        }

        private Items() {
        }

        public static final TagKey<Item> ARMORS_HELMETS_BETTER_GOLD = commonTag("armors/helmets/better_gold");
        public static final TagKey<Item> ARMORS_HELMETS_PLASLITHERITE = commonTag("armors/helmets/plaslitherite");

        public static final TagKey<Item> ARMORS_CHESTPLATES_BETTER_GOLD = commonTag("armors/chestplates/better_gold");
        public static final TagKey<Item> ARMORS_CHESTPLATES_PLASLITHERITE = commonTag("armors/chestplates/plaslitherite");

        public static final TagKey<Item> ARMORS_LEGGINGS_BETTER_GOLD = commonTag("armors/leggings/better_gold");
        public static final TagKey<Item> ARMORS_LEGGINGS_PLASLITHERITE = commonTag("armors/leggings/plaslitherite");

        public static final TagKey<Item> ARMORS_BOOTS_BETTER_GOLD = commonTag("armors/boots/better_gold");
        public static final TagKey<Item> ARMORS_BOOTS_PLASLITHERITE = commonTag("armors/boots/plaslitherite");

        public static final TagKey<Item> TOOLS_AXES_BETTER_GOLD = commonTag("tools/axes/better_gold");
        public static final TagKey<Item> TOOLS_AXES_PLASLITHERITE = commonTag("tools/axes/plaslitherite");

        public static final TagKey<Item> TOOLS_PICKAXES_BETTER_GOLD = commonTag("tools/pickaxes/better_gold");
        public static final TagKey<Item> TOOLS_PICKAXES_PLASLITHERITE = commonTag("tools/pickaxes/plaslitherite");

        public static final TagKey<Item> TOOLS_HOES_BETTER_GOLD = commonTag("tools/hoes/better_gold");
        public static final TagKey<Item> TOOLS_HOES_PLASLITHERITE = commonTag("tools/hoes/plaslitherite");

        public static final TagKey<Item> TOOLS_SHOVELS_BETTER_GOLD = commonTag("tools/shovels/better_gold");
        public static final TagKey<Item> TOOLS_SHOVELS_PLASLITHERITE = commonTag("tools/shovels/plaslitherite");

        public static final TagKey<Item> TOOLS_SWORDS_BETTER_GOLD = commonTag("tools/swords/better_gold");
        public static final TagKey<Item> TOOLS_SWORDS_PLASLITHERITE = commonTag("tools/swords/plaslitherite");

        public static final TagKey<Item> TOOLS_PAXELS_BETTER_GOLD = commonTag("tools/paxels/better_gold");
        public static final TagKey<Item> TOOLS_PAXELS_PLASLITHERITE = commonTag("tools/paxels/plaslitherite");

        public static final TagKey<Item> TOOLS_SHIELDS_BETTER_GOLD = commonTag("tools/shields/better_gold");
        public static final TagKey<Item> TOOLS_SHIELDS_PLASLITHERITE = commonTag("tools/shields/plaslitherite");


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

        public static final TagKey<Block> INCORRECT_FOR_NEEDS_BETTER_GOLD_TOOL = tag("incorrect_for_better_gold_tool");
        public static final TagKey<Block> INCORRECT_FOR_PLASLITHERITE_TOOL = tag("incorrect_for_plaslitherite_tool");

        private static TagKey<Block> forgeTag(String name) {
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

        private static TagKey<Fluid> forgeTag(String name) {
            return FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }

    public static class Gases {

        private static void init() {
        }

        private Gases() {
        }


        private static TagKey<Chemical> tag(String name) {
            return TagKey.create(MekanismAPI.CHEMICAL_REGISTRY_NAME,EvolvedMekanism.rl(name));
        }
    }

}