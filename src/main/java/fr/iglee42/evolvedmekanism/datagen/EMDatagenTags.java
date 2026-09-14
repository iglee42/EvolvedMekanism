package fr.iglee42.evolvedmekanism.datagen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;

public final class EMDatagenTags {

    private EMDatagenTags() {
    }

    public static TagKey<Item> item(String namespace, String path) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(namespace, path));
    }

    public static TagKey<Item> cItem(String path) {
        return item("c", path);
    }

    public static TagKey<Item> mekItem(String path) {
        return item("mekanism", path);
    }

    public static TagKey<Block> block(String namespace, String path) {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath(namespace, path));
    }

    public static TagKey<Block> cBlock(String path) {
        return block("c", path);
    }

    public static TagKey<Fluid> cFluid(String path) {
        return FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
    }

    public static TagKey<Chemical> chemical(String namespace, String path) {
        return TagKey.create(MekanismAPI.CHEMICAL_REGISTRY_NAME, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
}
