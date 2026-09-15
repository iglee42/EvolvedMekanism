package fr.iglee42.evolvedmekanism.datagen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public final class EMDatagenTags {

    private EMDatagenTags() {
    }

    public static TagKey<Item> item(String namespace, String path) {
        return ItemTags.create(new ResourceLocation(namespace, path));
    }

    public static TagKey<Item> forgeItem(String path) {
        return item("forge", path);
    }

    public static TagKey<Item> mekItem(String path) {
        return item("mekanism", path);
    }

    public static TagKey<Block> block(String namespace, String path) {
        return BlockTags.create(new ResourceLocation(namespace, path));
    }

    public static TagKey<Block> forgeBlock(String path) {
        return block("forge", path);
    }

    public static TagKey<Fluid> forgeFluid(String path) {
        return FluidTags.create(new ResourceLocation("forge", path));
    }
}
