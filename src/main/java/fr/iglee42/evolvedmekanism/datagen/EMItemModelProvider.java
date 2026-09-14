package fr.iglee42.evolvedmekanism.datagen;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMFluids;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import fr.iglee42.evolvedmekanism.utils.ModsCompats;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class EMItemModelProvider extends ItemModelProvider {

    public EMItemModelProvider(PackOutput output, ExistingFileHelper existing) {
        super(output, EvolvedMekanism.MODID, existing);
    }

    @Override
    protected void registerModels() {
        EMItems.ITEMS.getEntries().forEach(holder -> {
            String path = holder.getId().getPath();
            if (path.contains("shield")) {
                return;
            }
            if (path.startsWith("qio_drive_")) {
                withExistingParent(path, "minecraft:item/generated")
                        .texture("layer0", modLoc("item/qio_" + path.substring("qio_drive_".length())));
                return;
            }
            if (existingFileHelper.exists(modLoc("item/" + path), TEXTURE)) {
                basicItem(holder.get());
            }
        });
        EMFluids.FLUIDS.getBucketEntries().forEach(holder ->
                withExistingParent(holder.getId().getPath(), ResourceLocation.fromNamespaceAndPath("neoforge", "item/bucket"))
                        .customLoader(DynamicFluidContainerModelBuilder::begin)
                        .fluid(BuiltInRegistries.FLUID.get(EvolvedMekanism.rl(holder.getId().getPath().replace("_bucket", ""))))
                        .end());
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
