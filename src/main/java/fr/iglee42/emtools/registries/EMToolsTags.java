package fr.iglee42.emtools.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.api.chemical.ChemicalTags;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.common.Mekanism;
import mekanism.common.tags.TagUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

public class EMToolsTags {

    /**
     * Call to force make sure this is all initialized
     */
    public static void init() {
        Items.init();
        Blocks.init();
        Fluids.init();
        Gases.init();
        InfuseTypes.init();
        Slurries.init();
        TileEntityTypes.init();
    }

    private EMToolsTags() {
    }

    public static class Items {

        private static void init() {
        }

        private Items() {
        }


        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }
        private static TagKey<Item> mekTag(String name) {
            return ItemTags.create(new ResourceLocation("mekanism", name));
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

        public static final TagKey<Block> NEEDS_BETTER_GOLD_TOOL = tag("needs_better_gold_tool");
        public static final TagKey<Block> NEEDS_PLASLITHERITE_TOOL = tag("needs_plaslitherite_tool");

        private static TagKey<Block> forgeTag(String name) {
            return BlockTags.create(new ResourceLocation("forge", name));
        }

        private static TagKey<Block> mekTag(String name) {
            return BlockTags.create(new ResourceLocation("mekanism", name));
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
            return FluidTags.create(new ResourceLocation("forge", name));
        }
    }

    public static class Gases {

        private static void init() {
        }

        private Gases() {
        }


        private static TagKey<Gas> tag(String name) {
            return ChemicalTags.GAS.tag(EvolvedMekanism.rl(name));
        }
    }

    public static class InfuseTypes {

        private static void init() {
        }

        private InfuseTypes() {
        }

        //public static final TagKey<InfuseType> CARBON = tag("carbon");


        private static TagKey<InfuseType> tag(String name) {
            return ChemicalTags.INFUSE_TYPE.tag(EvolvedMekanism.rl(name));
        }
    }

    public static class Slurries {

        private static void init() {
        }

        private Slurries() {
        }

        private static TagKey<Slurry> tag(String name) {
            return ChemicalTags.SLURRY.tag(Mekanism.rl(name));
        }
    }


    public static class TileEntityTypes {

        private static void init() {
        }

        private TileEntityTypes() {
        }


        private static TagKey<BlockEntityType<?>> tag(String name) {
            return TagUtils.createKey(ForgeRegistries.BLOCK_ENTITY_TYPES, EvolvedMekanism.rl(name));
        }

        private static TagKey<BlockEntityType<?>> forgeTag(String name) {
            return TagUtils.createKey(ForgeRegistries.BLOCK_ENTITY_TYPES, new ResourceLocation("forge", name));
        }
    }
}