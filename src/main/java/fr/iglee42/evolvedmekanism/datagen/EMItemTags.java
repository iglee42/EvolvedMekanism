package fr.iglee42.evolvedmekanism.datagen;

import fr.iglee42.emtools.registries.EMToolsItems;
import fr.iglee42.emtools.registries.EMToolsTags;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import fr.iglee42.evolvedmekanism.registries.EMTags;
import fr.iglee42.evolvedmekanism.utils.ModsCompats;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EMItemTags extends ItemTagsProvider {

    public EMItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup,
                      CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existing) {
        super(output, lookup, blockTags, EvolvedMekanism.MODID, existing);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(EMTags.Items.INGOTS_BETTER_GOLD).add(EMItems.BETTER_GOLD_INGOT.get());
        tag(EMTags.Items.INGOTS_PLASLITHERITE).add(EMItems.PLASLITHERITE_INGOT.get());
        tag(EMTags.Items.INGOTS_REFINED_REDSTONE).add(EMItems.REFINED_REDSTONE_INGOT.get());
        tag(EMTags.Items.NUGGETS_BETTER_GOLD).add(EMItems.BETTER_GOLD_NUGGET.get());
        tag(EMTags.Items.NUGGETS_PLASLITHERITE).add(EMItems.PLASLITHERITE_NUGGET.get());
        tag(EMTags.Items.NUGGETS_REFINED_REDSTONE).add(EMItems.REFINED_REDSTONE_NUGGET.get());
        tag(EMTags.Items.DUSTS_BETTER_GOLD).add(EMItems.BETTER_GOLD_DUST.get());
        tag(EMTags.Items.DUSTS_PLASLITHERITE).add(EMItems.PLASLITHERITE_DUST.get());

        copy(EMTags.Blocks.STORAGE_BLOCKS_BETTER_GOLD, EMTags.Items.STORAGE_BLOCKS_BETTER_GOLD);
        copy(EMTags.Blocks.STORAGE_BLOCKS_PLASLITHERITE, EMTags.Items.STORAGE_BLOCKS_PLASLITHERITE);
        copy(EMTags.Blocks.STORAGE_BLOCKS_REFINED_REDSTONE, EMTags.Items.STORAGE_BLOCKS_REFINED_REDSTONE);
        copy(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS, EMDatagenTags.forgeItem("storage_blocks/alloys"));
        copy(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_INFUSED, EMDatagenTags.forgeItem("storage_blocks/alloys/infused"));
        copy(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_REINFORCED, EMDatagenTags.forgeItem("storage_blocks/alloys/reinforced"));
        copy(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_ATOMIC, EMDatagenTags.forgeItem("storage_blocks/alloys/atomic"));
        copy(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_HYPERCHARGED, EMDatagenTags.forgeItem("storage_blocks/alloys/hypercharged"));
        copy(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_SUBATOMIC, EMDatagenTags.forgeItem("storage_blocks/alloys/subatomic"));
        copy(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_SINGULAR, EMDatagenTags.forgeItem("storage_blocks/alloys/singular"));
        copy(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_EXOVERSAL, EMDatagenTags.forgeItem("storage_blocks/alloys/exoversal"));
        copy(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_CREATIVE, EMDatagenTags.forgeItem("storage_blocks/alloys/creative"));
        copy(EMDatagenTags.forgeBlock("storage_blocks"), EMDatagenTags.forgeItem("storage_blocks"));
        copy(EMDatagenTags.forgeBlock("storage_blocks/amethyst"), EMDatagenTags.forgeItem("storage_blocks/amethyst"));
        copy(EMDatagenTags.forgeBlock("storage_blocks/glowstone"), EMDatagenTags.forgeItem("storage_blocks/glowstone"));
        copy(EMDatagenTags.forgeBlock("storage_blocks/quartz"), EMDatagenTags.forgeItem("storage_blocks/quartz"));

        tag(EMDatagenTags.forgeItem("ingots")).addTags(EMTags.Items.INGOTS_BETTER_GOLD, EMTags.Items.INGOTS_PLASLITHERITE, EMTags.Items.INGOTS_REFINED_REDSTONE);
        tag(EMDatagenTags.forgeItem("nuggets")).addTags(EMTags.Items.NUGGETS_BETTER_GOLD, EMTags.Items.NUGGETS_PLASLITHERITE, EMTags.Items.NUGGETS_REFINED_REDSTONE);
        tag(EMDatagenTags.forgeItem("dusts")).addTags(EMTags.Items.DUSTS_BETTER_GOLD, EMTags.Items.DUSTS_PLASLITHERITE);
        tag(EMDatagenTags.forgeItem("gems/coal")).add(Items.COAL);

        tag(EMTags.Items.ALLOYS_HYPERCHARGED).add(EMItems.HYPERCHARGED_ALLOY.get());
        tag(EMTags.Items.ALLOYS_SUBATOMIC).add(EMItems.SUBATOMIC_ALLOY.get());
        tag(EMTags.Items.ALLOYS_SINGULAR).add(EMItems.SINGULAR_ALLOY.get());
        tag(EMTags.Items.ALLOYS_EXOVERSAL).add(EMItems.EXOVERSAL_ALLOY.get());
        tag(EMTags.Items.ALLOYS_CREATIVE).add(EMItems.CREATIVE_ALLOY.get());
        tag(EMTags.Items.ALLOYS).addTags(EMTags.Items.ALLOYS_HYPERCHARGED, EMTags.Items.ALLOYS_SUBATOMIC,
                EMTags.Items.ALLOYS_SINGULAR, EMTags.Items.ALLOYS_EXOVERSAL, EMTags.Items.ALLOYS_CREATIVE);
        tag(EMTags.Items.ALLOYS_OVERCLOCKED).add(EMItems.HYPERCHARGED_ALLOY.get());
        tag(EMTags.Items.ALLOYS_QUANTUM).add(EMItems.SUBATOMIC_ALLOY.get());
        tag(EMTags.Items.ALLOYS_DENSE).add(EMItems.SINGULAR_ALLOY.get());
        tag(EMTags.Items.ALLOYS_MULTIVERSAL).add(EMItems.EXOVERSAL_ALLOY.get());
        tag(EMTags.Items.ALLOYS_CREATIVE_FORGE).add(EMItems.CREATIVE_ALLOY.get());
        tag(EMDatagenTags.forgeItem("alloys")).addTags(EMTags.Items.ALLOYS_OVERCLOCKED, EMTags.Items.ALLOYS_QUANTUM,
                EMTags.Items.ALLOYS_DENSE, EMTags.Items.ALLOYS_MULTIVERSAL, EMTags.Items.ALLOYS_CREATIVE_FORGE);

        tag(EMTags.Items.CIRCUITS_OVERCLOCKED).add(EMItems.OVERCLOCKED_CONTROL_CIRCUIT.get());
        tag(EMTags.Items.CIRCUITS_QUANTUM).add(EMItems.QUANTUM_CONTROL_CIRCUIT.get());
        tag(EMTags.Items.CIRCUITS_DENSE).add(EMItems.DENSE_CONTROL_CIRCUIT.get());
        tag(EMTags.Items.CIRCUITS_MULTIVERSAL).add(EMItems.MULTIVERSAL_CONTROL_CIRCUIT.get());
        tag(EMTags.Items.CIRCUITS_CREATIVE_FORGE).add(EMItems.CREATIVE_CONTROL_CIRCUIT.get());
        tag(EMDatagenTags.forgeItem("circuits")).addTags(EMTags.Items.CIRCUITS_OVERCLOCKED, EMTags.Items.CIRCUITS_QUANTUM,
                EMTags.Items.CIRCUITS_DENSE, EMTags.Items.CIRCUITS_MULTIVERSAL, EMTags.Items.CIRCUITS_CREATIVE_FORGE);

        tag(EMTags.Items.ENRICHED_BETTER_GOLD).add(EMItems.ENRICHED_BETTER_GOLD.get());
        tag(EMTags.Items.ENRICHED_PLASLITHERITE).add(EMItems.ENRICHED_PLASLITHERITE.get());
        tag(EMTags.Items.ENRICHED_URANIUM).add(EMItems.ENRICHED_URANIUM.get());
        tag(EMTags.Items.ENRICHED).addTags(EMTags.Items.ENRICHED_BETTER_GOLD, EMTags.Items.ENRICHED_PLASLITHERITE, EMTags.Items.ENRICHED_URANIUM);

        tag(EMDatagenTags.item(EvolvedMekanism.MODID, "unit")).add(
                EMItems.AIR_AFFINITY.get(), EMItems.AQUA_AFFINITY.get(), EMItems.CAPTURING.get(), EMItems.LUCK.get());

        copy(EMDatagenTags.forgeBlock("ores/fluorite"), EMDatagenTags.forgeItem("ores/fluorite"));
        copy(EMDatagenTags.forgeBlock("ores/lead"), EMDatagenTags.forgeItem("ores/lead"));
        copy(EMDatagenTags.forgeBlock("ores/osmium"), EMDatagenTags.forgeItem("ores/osmium"));
        copy(EMDatagenTags.forgeBlock("ores/tin"), EMDatagenTags.forgeItem("ores/tin"));
        copy(EMDatagenTags.forgeBlock("ores/uranium"), EMDatagenTags.forgeItem("ores/uranium"));
        copy(EMDatagenTags.forgeBlock("ore_rates/dense"), EMDatagenTags.forgeItem("ore_rates/dense"));
        copy(EMDatagenTags.forgeBlock("ores_in_ground/netherrack"), EMDatagenTags.forgeItem("ores_in_ground/netherrack"));
        copy(EMDatagenTags.forgeBlock("ores_in_ground/end_stone"), EMDatagenTags.forgeItem("ores_in_ground/end_stone"));
        copy(EMDatagenTags.forgeBlock("ores_in_ground/holystone"), EMDatagenTags.forgeItem("ores_in_ground/holystone"));
        copy(EMDatagenTags.forgeBlock("ores_in_ground/depthrock"), EMDatagenTags.forgeItem("ores_in_ground/depthrock"));
        copy(EMDatagenTags.forgeBlock("ores_in_ground/shiverstone"), EMDatagenTags.forgeItem("ores_in_ground/shiverstone"));
        tag(EMDatagenTags.forgeItem("ore_rates/singular"))
                .addTag(EMDatagenTags.forgeItem("ores/tin"))
                .addTag(EMDatagenTags.forgeItem("ores/osmium"))
                .addTag(EMDatagenTags.forgeItem("ores/uranium"))
                .addTag(EMDatagenTags.forgeItem("ores/lead"));

        for (Block block : ForgeRegistries.BLOCKS) {
            ResourceLocation id = ForgeRegistries.BLOCKS.getKey(block);
            if (id != null && EvolvedMekanism.MODID.equals(id.getNamespace()) && !(block instanceof LiquidBlock)
                    && (id.getPath().endsWith("_personal_barrel") || id.getPath().endsWith("_personal_chest"))) {
                tag(EMDatagenTags.mekItem("personal_storage")).add(block.asItem());
            }
        }

        ForgeRegistries.ITEMS.getKeys().stream()
                .filter(id -> EvolvedMekanism.MODID.equals(id.getNamespace()) && id.getPath().endsWith("_bucket"))
                .forEach(id -> {
                    String fluid = id.getPath().substring(0, id.getPath().length() - "_bucket".length());
                    tag(EMDatagenTags.forgeItem("buckets/" + fluid)).addOptional(id);
                });

        tag(ItemTags.BEACON_PAYMENT_ITEMS).add(EMItems.BETTER_GOLD_INGOT.get(), EMItems.PLASLITHERITE_INGOT.get(),
                EMItems.REFINED_REDSTONE_INGOT.get());
        tag(ItemTags.TRIM_MATERIALS).add(EMItems.BETTER_GOLD_INGOT.get(), EMItems.PLASLITHERITE_INGOT.get(),
                EMItems.REFINED_REDSTONE_INGOT.get());

        if (ModsCompats.MEKANISMTOOLS.isLoaded()) {
            addToolTags();
        }
    }

    private void addToolTags() {
        tag(EMToolsTags.Items.TOOLS_PICKAXES_BETTER_GOLD).add(EMToolsItems.BETTER_GOLD_PICKAXE.get());
        tag(EMToolsTags.Items.TOOLS_PICKAXES_PLASLITHERITE).add(EMToolsItems.PLASLITHERITE_PICKAXE.get());
        tag(EMToolsTags.Items.TOOLS_PICKAXES_REFINED_REDSTONE).add(EMToolsItems.REFINED_REDSTONE_PICKAXE.get());
        tag(ItemTags.PICKAXES).add(EMToolsItems.BETTER_GOLD_PICKAXE.get(), EMToolsItems.PLASLITHERITE_PICKAXE.get(), EMToolsItems.REFINED_REDSTONE_PICKAXE.get());
        tag(ItemTags.AXES).add(EMToolsItems.BETTER_GOLD_AXE.get(), EMToolsItems.PLASLITHERITE_AXE.get(), EMToolsItems.REFINED_REDSTONE_AXE.get());
        tag(ItemTags.SHOVELS).add(EMToolsItems.BETTER_GOLD_SHOVEL.get(), EMToolsItems.PLASLITHERITE_SHOVEL.get(), EMToolsItems.REFINED_REDSTONE_SHOVEL.get());
        tag(ItemTags.HOES).add(EMToolsItems.BETTER_GOLD_HOE.get(), EMToolsItems.PLASLITHERITE_HOE.get(), EMToolsItems.REFINED_REDSTONE_HOE.get());
        tag(ItemTags.SWORDS).add(EMToolsItems.BETTER_GOLD_SWORD.get(), EMToolsItems.PLASLITHERITE_SWORD.get(), EMToolsItems.REFINED_REDSTONE_SWORD.get());
        tag(ItemTags.TRIMMABLE_ARMOR)
                .add(EMToolsItems.BETTER_GOLD_HELMET.get(), EMToolsItems.BETTER_GOLD_CHESTPLATE.get(),
                        EMToolsItems.BETTER_GOLD_LEGGINGS.get(), EMToolsItems.BETTER_GOLD_BOOTS.get())
                .add(EMToolsItems.PLASLITHERITE_HELMET.get(), EMToolsItems.PLASLITHERITE_CHESTPLATE.get(),
                        EMToolsItems.PLASLITHERITE_LEGGINGS.get(), EMToolsItems.PLASLITHERITE_BOOTS.get())
                .add(EMToolsItems.REFINED_REDSTONE_HELMET.get(), EMToolsItems.REFINED_REDSTONE_CHESTPLATE.get(),
                        EMToolsItems.REFINED_REDSTONE_LEGGINGS.get(), EMToolsItems.REFINED_REDSTONE_BOOTS.get());
        tag(EMToolsTags.Items.ARMORS_HELMETS_BETTER_GOLD).add(EMToolsItems.BETTER_GOLD_HELMET.get());
        tag(EMToolsTags.Items.ARMORS_HELMETS_PLASLITHERITE).add(EMToolsItems.PLASLITHERITE_HELMET.get());
        tag(EMToolsTags.Items.ARMORS_HELMETS_REFINED_REDSTONE).add(EMToolsItems.REFINED_REDSTONE_HELMET.get());
        tag(EMToolsTags.Items.ARMORS_CHESTPLATES_BETTER_GOLD).add(EMToolsItems.BETTER_GOLD_CHESTPLATE.get());
        tag(EMToolsTags.Items.ARMORS_CHESTPLATES_PLASLITHERITE).add(EMToolsItems.PLASLITHERITE_CHESTPLATE.get());
        tag(EMToolsTags.Items.ARMORS_CHESTPLATES_REFINED_REDSTONE).add(EMToolsItems.REFINED_REDSTONE_CHESTPLATE.get());
        tag(EMToolsTags.Items.ARMORS_LEGGINGS_BETTER_GOLD).add(EMToolsItems.BETTER_GOLD_LEGGINGS.get());
        tag(EMToolsTags.Items.ARMORS_LEGGINGS_PLASLITHERITE).add(EMToolsItems.PLASLITHERITE_LEGGINGS.get());
        tag(EMToolsTags.Items.ARMORS_LEGGINGS_REFINED_REDSTONE).add(EMToolsItems.REFINED_REDSTONE_LEGGINGS.get());
        tag(EMToolsTags.Items.ARMORS_BOOTS_BETTER_GOLD).add(EMToolsItems.BETTER_GOLD_BOOTS.get());
        tag(EMToolsTags.Items.ARMORS_BOOTS_PLASLITHERITE).add(EMToolsItems.PLASLITHERITE_BOOTS.get());
        tag(EMToolsTags.Items.ARMORS_BOOTS_REFINED_REDSTONE).add(EMToolsItems.REFINED_REDSTONE_BOOTS.get());
        tag(EMToolsTags.Items.TOOLS_AXES_BETTER_GOLD).add(EMToolsItems.BETTER_GOLD_AXE.get());
        tag(EMToolsTags.Items.TOOLS_AXES_PLASLITHERITE).add(EMToolsItems.PLASLITHERITE_AXE.get());
        tag(EMToolsTags.Items.TOOLS_AXES_REFINED_REDSTONE).add(EMToolsItems.REFINED_REDSTONE_AXE.get());
        tag(EMToolsTags.Items.TOOLS_HOES_BETTER_GOLD).add(EMToolsItems.BETTER_GOLD_HOE.get());
        tag(EMToolsTags.Items.TOOLS_HOES_PLASLITHERITE).add(EMToolsItems.PLASLITHERITE_HOE.get());
        tag(EMToolsTags.Items.TOOLS_HOES_REFINED_REDSTONE).add(EMToolsItems.REFINED_REDSTONE_HOE.get());
        tag(EMToolsTags.Items.TOOLS_SHOVELS_BETTER_GOLD).add(EMToolsItems.BETTER_GOLD_SHOVEL.get());
        tag(EMToolsTags.Items.TOOLS_SHOVELS_PLASLITHERITE).add(EMToolsItems.PLASLITHERITE_SHOVEL.get());
        tag(EMToolsTags.Items.TOOLS_SHOVELS_REFINED_REDSTONE).add(EMToolsItems.REFINED_REDSTONE_SHOVEL.get());
        tag(EMToolsTags.Items.TOOLS_SWORDS_BETTER_GOLD).add(EMToolsItems.BETTER_GOLD_SWORD.get());
        tag(EMToolsTags.Items.TOOLS_SWORDS_PLASLITHERITE).add(EMToolsItems.PLASLITHERITE_SWORD.get());
        tag(EMToolsTags.Items.TOOLS_SWORDS_REFINED_REDSTONE).add(EMToolsItems.REFINED_REDSTONE_SWORD.get());
        tag(EMToolsTags.Items.TOOLS_PAXELS_BETTER_GOLD).add(EMToolsItems.BETTER_GOLD_PAXEL.get());
        tag(EMToolsTags.Items.TOOLS_PAXELS_PLASLITHERITE).add(EMToolsItems.PLASLITHERITE_PAXEL.get());
        tag(EMToolsTags.Items.TOOLS_PAXELS_REFINED_REDSTONE).add(EMToolsItems.REFINED_REDSTONE_PAXEL.get());
        tag(EMToolsTags.Items.TOOLS_SHIELDS_BETTER_GOLD).add(EMToolsItems.BETTER_GOLD_SHIELD.get());
        tag(EMToolsTags.Items.TOOLS_SHIELDS_PLASLITHERITE).add(EMToolsItems.PLASLITHERITE_SHIELD.get());
        tag(EMToolsTags.Items.TOOLS_SHIELDSREFINED_REDSTONE).add(EMToolsItems.REFINED_REDSTONE_SHIELD.get());
        tag(EMDatagenTags.forgeItem("tools/paxel")).add(EMToolsItems.BETTER_GOLD_PAXEL.get(), EMToolsItems.PLASLITHERITE_PAXEL.get(), EMToolsItems.REFINED_REDSTONE_PAXEL.get());
        tag(EMDatagenTags.forgeItem("tools/shields")).add(EMToolsItems.BETTER_GOLD_SHIELD.get(), EMToolsItems.PLASLITHERITE_SHIELD.get(), EMToolsItems.REFINED_REDSTONE_SHIELD.get());
        tag(ItemTags.CLUSTER_MAX_HARVESTABLES)
                .add(EMToolsItems.BETTER_GOLD_PICKAXE.get(), EMToolsItems.BETTER_GOLD_PAXEL.get())
                .add(EMToolsItems.PLASLITHERITE_PICKAXE.get(), EMToolsItems.PLASLITHERITE_PAXEL.get())
                .add(EMToolsItems.REFINED_REDSTONE_PICKAXE.get(), EMToolsItems.REFINED_REDSTONE_PAXEL.get());
    }
}
