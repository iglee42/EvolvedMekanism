package fr.iglee42.evolvedmekanism.datagen;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.utils.ModsCompats;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class EMItemModelProvider extends ItemModelProvider {

    private static final String[] TRIM_NAMES = {
            "quartz", "iron", "netherite", "redstone", "copper", "gold", "emerald", "diamond", "lapis", "amethyst"
    };
    private static final float[] TRIM_VALUES = {0.1F, 0.2F, 0.3F, 0.4F, 0.5F, 0.6F, 0.7F, 0.8F, 0.9F, 1.0F};
    private static final String[] TOOL_MATERIALS = {"better_gold", "plaslitherite", "refined_redstone", "noctis_rozuli"};
    private static final String[] TOOLS = {"pickaxe", "axe", "shovel", "hoe", "sword", "paxel"};
    private static final String[] ARMOR = {"helmet", "chestplate", "leggings", "boots"};

    public EMItemModelProvider(PackOutput output, ExistingFileHelper existing) {
        super(output, EvolvedMekanism.MODID, existing);
    }

    @Override
    protected void registerModels() {
        for (Item item : ForgeRegistries.ITEMS) {
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
            if (id == null || !EvolvedMekanism.MODID.equals(id.getNamespace())) {
                continue;
            }
            String path = id.getPath();
            if (item instanceof BlockItem) {
                if (path.endsWith("_energy_cube")) {
                    energyCube(path);
                } else if (path.endsWith("_fluid_tank")) {
                    fluidTank(path);
                }
                continue;
            }
            if (path.endsWith("_shield")) {
                continue;
            }
            if (path.startsWith("qio_drive_")) {
                withExistingParent(path, "minecraft:item/generated")
                        .texture("layer0", modLoc("item/qio_" + path.substring("qio_drive_".length())));
                continue;
            }
            if (path.startsWith("mold_")) {
                ResourceLocation texture = path.equals("mold_storage_block")
                        ? modLoc("item/mold_block")
                        : modLoc("item/" + path);
                withExistingParent(path, "minecraft:item/generated").texture("layer0", texture);
                continue;
            }
            if (existingFileHelper.exists(modLoc("item/" + path), TEXTURE)) {
                basicItem(item);
            }
        }
        for (Fluid fluid : ForgeRegistries.FLUIDS) {
            ResourceLocation id = ForgeRegistries.FLUIDS.getKey(fluid);
            if (id == null || !EvolvedMekanism.MODID.equals(id.getNamespace()) || id.getPath().startsWith("flowing_")) {
                continue;
            }
            withExistingParent(id.getPath() + "_bucket", new ResourceLocation("forge", "item/bucket"))
                    .customLoader(DynamicFluidContainerModelBuilder::begin)
                    .fluid(fluid)
                    .end();
        }
        if (ModsCompats.MEKANISMTOOLS.isLoaded()) {
            for (String material : TOOL_MATERIALS) {
                handheldTool(material);
                armor(material);
                shield(material);
            }
        }
    }

    private void handheldTool(String material) {
        for (String tool : TOOLS) {
            String path = material + "_" + tool;
            if (BuiltInRegistries.ITEM.get(EvolvedMekanism.rl(path)).asItem() == net.minecraft.world.item.Items.AIR) {
                continue;
            }
            withExistingParent(path, "minecraft:item/handheld")
                    .texture("layer0", modLoc("item/tools/" + material + "/" + tool));
        }
    }

    private void armor(String material) {
        for (String slot : ARMOR) {
            String path = material + "_" + slot;
            if (BuiltInRegistries.ITEM.get(EvolvedMekanism.rl(path)).asItem() == net.minecraft.world.item.Items.AIR) {
                continue;
            }
            ResourceLocation layer0 = modLoc("item/tools/" + material + "/" + slot);
            ItemModelBuilder base = withExistingParent(path, "minecraft:item/generated").texture("layer0", layer0);
            for (int i = 0; i < TRIM_NAMES.length; i++) {
                String trimPath = path + "_" + TRIM_NAMES[i] + "_trim";
                ResourceLocation trimTexture = mcLoc("trims/items/" + slot + "_trim_" + TRIM_NAMES[i]);
                existingFileHelper.trackGenerated(trimTexture, TEXTURE);
                withExistingParent(trimPath, "minecraft:item/generated")
                        .texture("layer0", layer0)
                        .texture("layer1", trimTexture);
                base.override()
                        .predicate(mcLoc("trim_type"), TRIM_VALUES[i])
                        .model(new ModelFile.UncheckedModelFile(modLoc("item/" + trimPath)))
                        .end();
            }
        }
    }

    private void shield(String material) {
        String path = material + "_shield";
        if (BuiltInRegistries.ITEM.get(EvolvedMekanism.rl(path)).asItem() == net.minecraft.world.item.Items.AIR) {
            return;
        }
        ResourceLocation particle = modLoc("block/block_" + material);
        ModelFile blocking = getBuilder(path + "_blocking")
                .parent(new ModelFile.UncheckedModelFile(mcLoc("item/shield_blocking")))
                .texture("particle", particle);
        getBuilder(path)
                .parent(new ModelFile.UncheckedModelFile(mcLoc("item/shield")))
                .texture("particle", particle)
                .override()
                .predicate(new ResourceLocation("mekanismtools", "blocking"), 1)
                .model(blocking)
                .end();
    }

    private void energyCube(String path) {
        withExistingParent(path, new ResourceLocation("mekanism", "item/basic_energy_cube"));
    }

    private void fluidTank(String path) {
        withExistingParent(path, new ResourceLocation("mekanism", "item/basic_fluid_tank"));
    }
}
