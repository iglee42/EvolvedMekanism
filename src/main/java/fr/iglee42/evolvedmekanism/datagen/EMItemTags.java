package fr.iglee42.evolvedmekanism.datagen;

import fr.iglee42.emtools.registries.EMToolsItems;
import fr.iglee42.emtools.registries.EMToolsTags;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.curios.CuriosSlots;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMFluids;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import fr.iglee42.evolvedmekanism.registries.EMTags;
import fr.iglee42.evolvedmekanism.utils.ModsCompats;
import mekanism.common.registries.MekanismItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EMItemTags extends ItemTagsProvider {

    public EMItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup,
                      CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existing) {
        super(output, lookup, blockTags, EvolvedMekanism.MODID, existing);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(EMTags.Items.INGOTS_BETTER_GOLD).add(EMItems.BETTER_GOLD_INGOT.asItem());
        tag(EMTags.Items.INGOTS_PLASLITHERITE).add(EMItems.PLASLITHERITE_INGOT.asItem());
        tag(EMTags.Items.INGOTS_REFINED_REDSTONE).add(EMItems.REFINED_REDSTONE_INGOT.asItem());
        tag(EMTags.Items.GEMS_NOCTIS_ROZULI).add(EMItems.NOCTIS_ROZULI.asItem());
        tag(EMTags.Items.NUGGETS_BETTER_GOLD).add(EMItems.BETTER_GOLD_NUGGET.asItem());
        tag(EMTags.Items.NUGGETS_PLASLITHERITE).add(EMItems.PLASLITHERITE_NUGGET.asItem());
        tag(EMTags.Items.NUGGETS_REFINED_REDSTONE).add(EMItems.REFINED_REDSTONE_NUGGET.asItem());
        tag(EMTags.Items.DUSTS_BETTER_GOLD).add(EMItems.BETTER_GOLD_DUST.asItem());
        tag(EMTags.Items.DUSTS_PLASLITHERITE).add(EMItems.PLASLITHERITE_DUST.asItem());
        tag(EMTags.Items.DUSTS_NOCTIS_ROZULI).add(EMItems.NOCTIS_ROZULI_DUST.asItem());

        copy(EMTags.Blocks.STORAGE_BLOCKS_BETTER_GOLD, EMTags.Items.STORAGE_BLOCKS_BETTER_GOLD);
        copy(EMTags.Blocks.STORAGE_BLOCKS_PLASLITHERITE, EMTags.Items.STORAGE_BLOCKS_PLASLITHERITE);
        copy(EMTags.Blocks.STORAGE_BLOCKS_REFINED_REDSTONE, EMTags.Items.STORAGE_BLOCKS_REFINED_REDSTONE);
        copy(EMDatagenTags.cBlock("storage_blocks/noctis_rozuli"), EMTags.Items.STORAGE_BLOCKS_NOCTIS_ROZULI);
        copy(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS, EMDatagenTags.cItem("storage_blocks/alloys"));
        copy(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_INFUSED, EMDatagenTags.cItem("storage_blocks/alloys/infused"));
        copy(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_REINFORCED, EMDatagenTags.cItem("storage_blocks/alloys/reinforced"));
        copy(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_ATOMIC, EMDatagenTags.cItem("storage_blocks/alloys/atomic"));
        copy(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_HYPERCHARGED, EMDatagenTags.cItem("storage_blocks/alloys/hypercharged"));
        copy(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_SUBATOMIC, EMDatagenTags.cItem("storage_blocks/alloys/subatomic"));
        copy(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_SINGULAR, EMDatagenTags.cItem("storage_blocks/alloys/singular"));
        copy(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_EXOVERSAL, EMDatagenTags.cItem("storage_blocks/alloys/exoversal"));
        copy(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_CREATIVE, EMDatagenTags.cItem("storage_blocks/alloys/creative"));
        copy(EMDatagenTags.cBlock("storage_blocks"), EMDatagenTags.cItem("storage_blocks"));
        copy(EMDatagenTags.cBlock("storage_blocks/amethyst"), EMDatagenTags.cItem("storage_blocks/amethyst"));
        copy(EMDatagenTags.cBlock("storage_blocks/glowstone"), EMDatagenTags.cItem("storage_blocks/glowstone"));
        copy(EMDatagenTags.cBlock("storage_blocks/quartz"), EMDatagenTags.cItem("storage_blocks/quartz"));

        tag(EMDatagenTags.cItem("ingots")).addTags(EMTags.Items.INGOTS_BETTER_GOLD, EMTags.Items.INGOTS_PLASLITHERITE, EMTags.Items.INGOTS_REFINED_REDSTONE);
        tag(EMDatagenTags.cItem("nuggets")).addTags(EMTags.Items.NUGGETS_BETTER_GOLD, EMTags.Items.NUGGETS_PLASLITHERITE, EMTags.Items.NUGGETS_REFINED_REDSTONE);
        tag(EMDatagenTags.cItem("dusts")).addTags(EMTags.Items.DUSTS_BETTER_GOLD, EMTags.Items.DUSTS_PLASLITHERITE);
        tag(EMDatagenTags.cItem("gems/coal")).add(Items.COAL);

        tag(EMTags.Items.ALLOYS_HYPERCHARGED).add(EMItems.HYPERCHARGED_ALLOY.asItem());
        tag(EMTags.Items.ALLOYS_SUBATOMIC).add(EMItems.SUBATOMIC_ALLOY.asItem());
        tag(EMTags.Items.ALLOYS_SINGULAR).add(EMItems.SINGULAR_ALLOY.asItem());
        tag(EMTags.Items.ALLOYS_EXOVERSAL).add(EMItems.EXOVERSAL_ALLOY.asItem());
        tag(EMTags.Items.ALLOYS_CREATIVE).add(EMItems.CREATIVE_ALLOY.asItem());
        tag(EMTags.Items.ALLOYS).addTags(EMTags.Items.ALLOYS_HYPERCHARGED, EMTags.Items.ALLOYS_SUBATOMIC,
                EMTags.Items.ALLOYS_SINGULAR, EMTags.Items.ALLOYS_EXOVERSAL, EMTags.Items.ALLOYS_CREATIVE);
        tag(EMTags.Items.ALLOYS_OVERCLOCKED).add(EMItems.HYPERCHARGED_ALLOY.asItem());
        tag(EMTags.Items.ALLOYS_QUANTUM).add(EMItems.SUBATOMIC_ALLOY.asItem());
        tag(EMTags.Items.ALLOYS_DENSE).add(EMItems.SINGULAR_ALLOY.asItem());
        tag(EMTags.Items.ALLOYS_MULTIVERSAL).add(EMItems.EXOVERSAL_ALLOY.asItem());
        tag(EMTags.Items.ALLOYS_CREATIVE_FORGE).add(EMItems.CREATIVE_ALLOY.asItem());
        tag(EMDatagenTags.cItem("alloys")).addTags(EMTags.Items.ALLOYS_OVERCLOCKED, EMTags.Items.ALLOYS_QUANTUM,
                EMTags.Items.ALLOYS_DENSE, EMTags.Items.ALLOYS_MULTIVERSAL, EMTags.Items.ALLOYS_CREATIVE_FORGE);

        tag(EMTags.Items.CIRCUITS_OVERCLOCKED).add(EMItems.OVERCLOCKED_CONTROL_CIRCUIT.asItem());
        tag(EMTags.Items.CIRCUITS_QUANTUM).add(EMItems.QUANTUM_CONTROL_CIRCUIT.asItem());
        tag(EMTags.Items.CIRCUITS_DENSE).add(EMItems.DENSE_CONTROL_CIRCUIT.asItem());
        tag(EMTags.Items.CIRCUITS_MULTIVERSAL).add(EMItems.MULTIVERSAL_CONTROL_CIRCUIT.asItem());
        tag(EMTags.Items.CIRCUITS_CREATIVE_FORGE).add(EMItems.CREATIVE_CONTROL_CIRCUIT.asItem());
        tag(EMDatagenTags.cItem("circuits")).addTags(EMTags.Items.CIRCUITS_OVERCLOCKED, EMTags.Items.CIRCUITS_QUANTUM,
                EMTags.Items.CIRCUITS_DENSE, EMTags.Items.CIRCUITS_MULTIVERSAL, EMTags.Items.CIRCUITS_CREATIVE_FORGE);

        tag(EMTags.Items.ENRICHED_BETTER_GOLD).add(EMItems.ENRICHED_BETTER_GOLD.asItem());
        tag(EMTags.Items.ENRICHED_PLASLITHERITE).add(EMItems.ENRICHED_PLASLITHERITE.asItem());
        tag(EMTags.Items.ENRICHED_URANIUM).add(EMItems.ENRICHED_URANIUM.asItem());
        tag(EMTags.Items.ENRICHED).addTags(EMTags.Items.ENRICHED_BETTER_GOLD, EMTags.Items.ENRICHED_PLASLITHERITE, EMTags.Items.ENRICHED_URANIUM);

        tag(EMDatagenTags.item(EvolvedMekanism.MODID, "unit")).add(
                EMItems.AIR_AFFINITY.asItem(), EMItems.AQUA_AFFINITY.asItem(), EMItems.CAPTURING.asItem(), EMItems.LUCK.asItem());

        copy(EMDatagenTags.cBlock("ores/fluorite"), EMDatagenTags.cItem("ores/fluorite"));
        copy(EMDatagenTags.cBlock("ores/lead"), EMDatagenTags.cItem("ores/lead"));
        copy(EMDatagenTags.cBlock("ores/osmium"), EMDatagenTags.cItem("ores/osmium"));
        copy(EMDatagenTags.cBlock("ores/tin"), EMDatagenTags.cItem("ores/tin"));
        copy(EMDatagenTags.cBlock("ores/uranium"), EMDatagenTags.cItem("ores/uranium"));
        copy(EMDatagenTags.cBlock("ore_rates/dense"), EMDatagenTags.cItem("ore_rates/dense"));
        copy(EMDatagenTags.cBlock("ores_in_ground/netherrack"), EMDatagenTags.cItem("ores_in_ground/netherrack"));
        copy(EMDatagenTags.cBlock("ores_in_ground/end_stone"), EMDatagenTags.cItem("ores_in_ground/end_stone"));
        copy(EMDatagenTags.cBlock("ores_in_ground/holystone"), EMDatagenTags.cItem("ores_in_ground/holystone"));
        copy(EMDatagenTags.cBlock("ores_in_ground/depthrock"), EMDatagenTags.cItem("ores_in_ground/depthrock"));
        copy(EMDatagenTags.cBlock("ores_in_ground/shiverstone"), EMDatagenTags.cItem("ores_in_ground/shiverstone"));

        tag(EMDatagenTags.cItem("ores/noctis_rozuli")).add(EMBlocks.ORES.get(fr.iglee42.evolvedmekanism.registries.EMOreType.NOCTIS_ROZULI).stone().asItem(),
                EMBlocks.ORES.get(fr.iglee42.evolvedmekanism.registries.EMOreType.NOCTIS_ROZULI).deepslate().asItem());
        tag(EMDatagenTags.cItem("ores_in_ground/stone")).add(EMBlocks.ORES.get(fr.iglee42.evolvedmekanism.registries.EMOreType.NOCTIS_ROZULI).stone().asItem());
        tag(EMDatagenTags.cItem("ores_in_ground/deepslate")).add(EMBlocks.ORES.get(fr.iglee42.evolvedmekanism.registries.EMOreType.NOCTIS_ROZULI).deepslate().asItem());
        tag(EMDatagenTags.cItem("ore_rates/singular"))
                .addTag(EMDatagenTags.cItem("ores/tin"))
                .addTag(EMDatagenTags.cItem("ores/osmium"))
                .addTag(EMDatagenTags.cItem("ores/uranium"))
                .addTag(EMDatagenTags.cItem("ores/lead"))
                .addTag(EMDatagenTags.cItem("ores/noctis_rozuli"));

        EMBlocks.BLOCKS.getPrimaryEntries().forEach(holder -> {
            String path = holder.getId().getPath();
            if (path.endsWith("_personal_barrel") || path.endsWith("_personal_chest")) {
                tag(EMDatagenTags.mekItem("personal_storage")).add(holder.get().asItem());
            }
        });

        EMFluids.FLUIDS.getBucketEntries().forEach(holder -> {
            String path = holder.getId().getPath();
            if (path.endsWith("_bucket")) {
                String fluid = path.substring(0, path.length() - "_bucket".length());
                tag(EMDatagenTags.cItem("buckets/" + fluid)).add(holder.get());
            }
        });

        tag(ItemTags.BEACON_PAYMENT_ITEMS).add(EMItems.BETTER_GOLD_INGOT.asItem(), EMItems.PLASLITHERITE_INGOT.asItem(),
                EMItems.REFINED_REDSTONE_INGOT.asItem(), EMItems.NOCTIS_ROZULI.asItem());
        tag(ItemTags.TRIM_MATERIALS).addOptional(EMItems.BETTER_GOLD_INGOT.getId())
                .addOptional(EMItems.PLASLITHERITE_INGOT.getId())
                .addOptional(EMItems.REFINED_REDSTONE_INGOT.getId())
                .addOptional(EMItems.NOCTIS_ROZULI.getId());

        if (ModsCompats.MEKANISMTOOLS.isLoaded()) {
            addToolTags();
        }

        addCuriosTags();
    }

    private void addCuriosTags() {
        curiosSlot(CuriosSlots.JETPACK, MekanismItems.JETPACK, MekanismItems.ARMORED_JETPACK);
        curiosSlot(CuriosSlots.HDPE_ELYTRA, MekanismItems.HDPE_REINFORCED_ELYTRA);
        curiosSlot(CuriosSlots.SCUBA_MASK, MekanismItems.SCUBA_MASK);
        curiosSlot(CuriosSlots.SCUBA_TANK, MekanismItems.SCUBA_TANK);
        curiosSlot(CuriosSlots.FREE_RUNNERS, MekanismItems.FREE_RUNNERS, MekanismItems.ARMORED_FREE_RUNNERS);
        curiosSlot(CuriosSlots.PORTABLE_HAZMAT_SUIT, EMItems.PORTABLE_HAZMAT_SUIT);
        curiosSlot(CuriosSlots.CANTEEN, MekanismItems.CANTEEN);
        curiosSlot(CuriosSlots.ENERGY_TABLET, MekanismItems.ENERGY_TABLET);
        curiosSlot(CuriosSlots.PORTABLE_QIO_DASHBOARD, MekanismItems.PORTABLE_QIO_DASHBOARD);
        curiosSlot(CuriosSlots.PORTABLE_TELEPORTER, MekanismItems.PORTABLE_TELEPORTER);
        curiosSlot(CuriosSlots.DOSIMETER, MekanismItems.DOSIMETER);
        curiosSlot(CuriosSlots.GEIGER_COUNTER, MekanismItems.GEIGER_COUNTER);
    }

    private void curiosSlot(String slot, ItemLike... items) {
        IntrinsicTagAppender<Item> tag = tag(EMDatagenTags.item("curios", slot));
        for (ItemLike item : items) {
            tag.add(item.asItem());
        }
    }

    private void addToolTags() {
        tag(EMToolsTags.Items.TOOLS_PICKAXES_BETTER_GOLD).addOptional(EMToolsItems.BETTER_GOLD_PICKAXE.getId());
        tag(EMToolsTags.Items.TOOLS_PICKAXES_PLASLITHERITE).addOptional(EMToolsItems.PLASLITHERITE_PICKAXE.getId());
        tag(EMToolsTags.Items.TOOLS_PICKAXES_REFINED_REDSTONE).addOptional(EMToolsItems.REFINED_REDSTONE_PICKAXE.getId());
        tag(EMToolsTags.Items.TOOLS_PICKAXES_NOCTIS_ROZULI).addOptional(EMToolsItems.NOCTIS_ROZULI_PICKAXE.getId());
        tag(ItemTags.PICKAXES)
                .addOptional(EMToolsItems.BETTER_GOLD_PICKAXE.getId())
                .addOptional(EMToolsItems.PLASLITHERITE_PICKAXE.getId())
                .addOptional(EMToolsItems.REFINED_REDSTONE_PICKAXE.getId())
                .addOptional(EMToolsItems.NOCTIS_ROZULI_PICKAXE.getId());
        tag(ItemTags.AXES)
                .addOptional(EMToolsItems.BETTER_GOLD_AXE.getId())
                .addOptional(EMToolsItems.PLASLITHERITE_AXE.getId())
                .addOptional(EMToolsItems.REFINED_REDSTONE_AXE.getId())
                .addOptional(EMToolsItems.NOCTIS_ROZULI_AXE.getId());
        tag(ItemTags.SHOVELS)
                .addOptional(EMToolsItems.BETTER_GOLD_SHOVEL.getId())
                .addOptional(EMToolsItems.PLASLITHERITE_SHOVEL.getId())
                .addOptional(EMToolsItems.REFINED_REDSTONE_SHOVEL.getId())
                .addOptional(EMToolsItems.NOCTIS_ROZULI_SHOVEL.getId());
        tag(ItemTags.HOES)
                .addOptional(EMToolsItems.BETTER_GOLD_HOE.getId())
                .addOptional(EMToolsItems.PLASLITHERITE_HOE.getId())
                .addOptional(EMToolsItems.REFINED_REDSTONE_HOE.getId())
                .addOptional(EMToolsItems.NOCTIS_ROZULI_HOE.getId());
        tag(ItemTags.SWORDS)
                .addOptional(EMToolsItems.BETTER_GOLD_SWORD.getId())
                .addOptional(EMToolsItems.PLASLITHERITE_SWORD.getId())
                .addOptional(EMToolsItems.REFINED_REDSTONE_SWORD.getId())
                .addOptional(EMToolsItems.NOCTIS_ROZULI_SWORD.getId());
        tag(ItemTags.HEAD_ARMOR)
                .addOptional(EMToolsItems.BETTER_GOLD_HELMET.getId())
                .addOptional(EMToolsItems.PLASLITHERITE_HELMET.getId())
                .addOptional(EMToolsItems.REFINED_REDSTONE_HELMET.getId())
                .addOptional(EMToolsItems.NOCTIS_ROZULI_HELMET.getId());
        tag(ItemTags.CHEST_ARMOR)
                .addOptional(EMToolsItems.BETTER_GOLD_CHESTPLATE.getId())
                .addOptional(EMToolsItems.PLASLITHERITE_CHESTPLATE.getId())
                .addOptional(EMToolsItems.REFINED_REDSTONE_CHESTPLATE.getId())
                .addOptional(EMToolsItems.NOCTIS_ROZULI_CHESTPLATE.getId());
        tag(ItemTags.LEG_ARMOR)
                .addOptional(EMToolsItems.BETTER_GOLD_LEGGINGS.getId())
                .addOptional(EMToolsItems.PLASLITHERITE_LEGGINGS.getId())
                .addOptional(EMToolsItems.REFINED_REDSTONE_LEGGINGS.getId())
                .addOptional(EMToolsItems.NOCTIS_ROZULI_LEGGINGS.getId());
        tag(ItemTags.FOOT_ARMOR)
                .addOptional(EMToolsItems.BETTER_GOLD_BOOTS.getId())
                .addOptional(EMToolsItems.PLASLITHERITE_BOOTS.getId())
                .addOptional(EMToolsItems.REFINED_REDSTONE_BOOTS.getId())
                .addOptional(EMToolsItems.NOCTIS_ROZULI_BOOTS.getId());
        tag(ItemTags.TRIMMABLE_ARMOR)
                .addOptional(EMToolsItems.BETTER_GOLD_HELMET.getId()).addOptional(EMToolsItems.BETTER_GOLD_CHESTPLATE.getId())
                .addOptional(EMToolsItems.BETTER_GOLD_LEGGINGS.getId()).addOptional(EMToolsItems.BETTER_GOLD_BOOTS.getId())
                .addOptional(EMToolsItems.PLASLITHERITE_HELMET.getId()).addOptional(EMToolsItems.PLASLITHERITE_CHESTPLATE.getId())
                .addOptional(EMToolsItems.PLASLITHERITE_LEGGINGS.getId()).addOptional(EMToolsItems.PLASLITHERITE_BOOTS.getId())
                .addOptional(EMToolsItems.REFINED_REDSTONE_HELMET.getId()).addOptional(EMToolsItems.REFINED_REDSTONE_CHESTPLATE.getId())
                .addOptional(EMToolsItems.REFINED_REDSTONE_LEGGINGS.getId()).addOptional(EMToolsItems.REFINED_REDSTONE_BOOTS.getId())
                .addOptional(EMToolsItems.NOCTIS_ROZULI_HELMET.getId()).addOptional(EMToolsItems.NOCTIS_ROZULI_CHESTPLATE.getId())
                .addOptional(EMToolsItems.NOCTIS_ROZULI_LEGGINGS.getId()).addOptional(EMToolsItems.NOCTIS_ROZULI_BOOTS.getId());
        tag(EMDatagenTags.cItem("tools/paxel"))
                .addOptional(EMToolsItems.BETTER_GOLD_PAXEL.getId())
                .addOptional(EMToolsItems.PLASLITHERITE_PAXEL.getId())
                .addOptional(EMToolsItems.REFINED_REDSTONE_PAXEL.getId())
                .addOptional(EMToolsItems.NOCTIS_ROZULI_PAXEL.getId());
        tag(EMDatagenTags.cItem("tools/shield"))
                .addOptional(EMToolsItems.BETTER_GOLD_SHIELD.getId())
                .addOptional(EMToolsItems.PLASLITHERITE_SHIELD.getId())
                .addOptional(EMToolsItems.REFINED_REDSTONE_SHIELD.getId())
                .addOptional(EMToolsItems.NOCTIS_ROZULI_SHIELD.getId());
        tag(EMDatagenTags.cItem("tools/melee_weapon"))
                .addOptional(EMToolsItems.BETTER_GOLD_AXE.getId()).addOptional(EMToolsItems.BETTER_GOLD_SWORD.getId()).addOptional(EMToolsItems.BETTER_GOLD_PAXEL.getId())
                .addOptional(EMToolsItems.PLASLITHERITE_AXE.getId()).addOptional(EMToolsItems.PLASLITHERITE_SWORD.getId()).addOptional(EMToolsItems.PLASLITHERITE_PAXEL.getId())
                .addOptional(EMToolsItems.REFINED_REDSTONE_AXE.getId()).addOptional(EMToolsItems.REFINED_REDSTONE_SWORD.getId()).addOptional(EMToolsItems.REFINED_REDSTONE_PAXEL.getId());
        tag(ItemTags.CLUSTER_MAX_HARVESTABLES)
                .addOptional(EMToolsItems.BETTER_GOLD_PICKAXE.getId()).addOptional(EMToolsItems.BETTER_GOLD_PAXEL.getId())
                .addOptional(EMToolsItems.PLASLITHERITE_PICKAXE.getId()).addOptional(EMToolsItems.PLASLITHERITE_PAXEL.getId())
                .addOptional(EMToolsItems.REFINED_REDSTONE_PICKAXE.getId()).addOptional(EMToolsItems.REFINED_REDSTONE_PAXEL.getId())
                .addOptional(EMToolsItems.NOCTIS_ROZULI_PICKAXE.getId()).addOptional(EMToolsItems.NOCTIS_ROZULI_PAXEL.getId());
        tag(ItemTags.DURABILITY_ENCHANTABLE)
                .addOptional(EMToolsItems.BETTER_GOLD_SHIELD.getId())
                .addOptional(EMToolsItems.PLASLITHERITE_SHIELD.getId())
                .addOptional(EMToolsItems.REFINED_REDSTONE_SHIELD.getId());
    }
}
