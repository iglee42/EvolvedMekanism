package fr.iglee42.evolvedmekanism.datagen;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.utils.ModsCompats;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class EMItemModelProvider extends ItemModelProvider {

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
            if (path.contains("shield") || path.endsWith("_bucket")
                    || path.endsWith("_helmet") || path.endsWith("_chestplate")
                    || path.endsWith("_leggings") || path.endsWith("_boots")) {
                continue;
            }
            if (path.startsWith("qio_drive_")) {
                withExistingParent(path, "minecraft:item/generated")
                        .texture("layer0", modLoc("item/qio_" + path.substring("qio_drive_".length())));
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
            String bucketPath = id.getPath() + "_bucket";
            withExistingParent(bucketPath, new ResourceLocation("forge", "item/bucket"))
                    .customLoader(DynamicFluidContainerModelBuilder::begin)
                    .fluid(fluid)
                    .end();
        }
        if (ModsCompats.MEKANISMTOOLS.isLoaded()) {
            handheldTool("better_gold");
            handheldTool("plaslitherite");
            handheldTool("refined_redstone");
            handheldTool("noctis_rozuli");
        }
    }

    private void handheldTool(String material) {
        for (String tool : new String[]{"pickaxe", "axe", "shovel", "hoe", "sword", "paxel"}) {
            String path = material + "_" + tool;
            if (BuiltInRegistries.ITEM.get(EvolvedMekanism.rl(path)) == net.minecraft.world.item.Items.AIR) {
                continue;
            }
            withExistingParent(path, "minecraft:item/handheld")
                    .texture("layer0", modLoc("item/tools/" + material + "/" + tool));
        }
    }
}
