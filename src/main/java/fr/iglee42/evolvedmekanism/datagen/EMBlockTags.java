package fr.iglee42.evolvedmekanism.datagen;

import fr.iglee42.emgenerators.registries.EMGenBlocks;
import fr.iglee42.emtools.registries.EMToolsTags;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMFluids;
import fr.iglee42.evolvedmekanism.registries.EMTags;
import fr.iglee42.evolvedmekanism.utils.ModsCompats;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EMBlockTags extends BlockTagsProvider {

    public EMBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper existing) {
        super(output, lookup, EvolvedMekanism.MODID, existing);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(EMTags.Blocks.STORAGE_BLOCKS_BETTER_GOLD).add(EMBlocks.BETTER_GOLD_BLOCK.get());
        tag(EMTags.Blocks.STORAGE_BLOCKS_PLASLITHERITE).add(EMBlocks.PLASLITHERITE_BLOCK.get());
        tag(EMTags.Blocks.STORAGE_BLOCKS_REFINED_REDSTONE).add(EMBlocks.REFINED_REDSTONE_BLOCK.get());
        tag(EMDatagenTags.cBlock("storage_blocks/noctis_rozuli")).add(EMBlocks.NOCTIS_ROZULI_BLOCK.get());

        tag(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_INFUSED).add(EMBlocks.INFUSED_ALLOY_BLOCK.get());
        tag(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_REINFORCED).add(EMBlocks.REINFORCED_ALLOY_BLOCK.get());
        tag(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_ATOMIC).add(EMBlocks.ATOMIC_ALLOY_BLOCK.get());
        tag(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_HYPERCHARGED).add(EMBlocks.HYPERCHARGED_ALLOY_BLOCK.get());
        tag(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_SUBATOMIC).add(EMBlocks.SUBATOMIC_ALLOY_BLOCK.get());
        tag(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_SINGULAR).add(EMBlocks.SINGULAR_ALLOY_BLOCK.get());
        tag(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_EXOVERSAL).add(EMBlocks.EXOVERSAL_ALLOY_BLOCK.get());
        tag(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_CREATIVE).add(EMBlocks.CREATIVE_ALLOY_BLOCK.get());
        tag(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS)
                .addTags(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_INFUSED, EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_REINFORCED,
                        EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_ATOMIC, EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_HYPERCHARGED,
                        EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_SUBATOMIC, EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_SINGULAR,
                        EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_EXOVERSAL, EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_CREATIVE);
        tag(EMDatagenTags.cBlock("storage_blocks"))
                .addTags(EMTags.Blocks.STORAGE_BLOCKS_BETTER_GOLD, EMTags.Blocks.STORAGE_BLOCKS_PLASLITHERITE,
                        EMTags.Blocks.STORAGE_BLOCKS_REFINED_REDSTONE, EMTags.Blocks.STORAGE_BLOCKS_ALLOYS);
        tag(EMDatagenTags.cBlock("storage_blocks/amethyst")).add(Blocks.AMETHYST_BLOCK);
        tag(EMDatagenTags.cBlock("storage_blocks/glowstone")).add(Blocks.GLOWSTONE);
        tag(EMDatagenTags.cBlock("storage_blocks/quartz")).add(Blocks.QUARTZ_BLOCK);

        var personalBarrel = tag(EMDatagenTags.cBlock("barrels/personal"));
        var personalChest = tag(EMDatagenTags.cBlock("chests/personal"));
        var electricChest = tag(EMDatagenTags.cBlock("chests/electric"));
        var piglin = tag(BlockTags.GUARDED_BY_PIGLINS);
        var relocation = tag(EMDatagenTags.cBlock("relocation_not_supported"));
        var mineable = tag(BlockTags.MINEABLE_WITH_PICKAXE);
        EMBlocks.BLOCKS.getPrimaryEntries().forEach(holder -> {
            Block block = holder.get();
            if (block instanceof LiquidBlock) {
                return;
            }
            mineable.add(block);
            String path = holder.getId().getPath();
            if (path.endsWith("_personal_barrel")) {
                personalBarrel.add(block);
                piglin.add(block);
            } else if (path.endsWith("_personal_chest")) {
                personalChest.add(block);
                electricChest.add(block);
                piglin.add(block);
            } else if (path.endsWith("_pressurized_tube") || path.endsWith("_mechanical_pipe")
                    || path.endsWith("_universal_cable") || path.equals("lunar_neutron_activator")) {
                relocation.add(block);
            }
            classifyOre(block, path);
        });
        EMBlocks.BLOCKS_NO_ITEMS.getEntries().forEach(holder -> {
            Block block = holder.get();
            if (!(block instanceof LiquidBlock)) {
                mineable.add(block);
            }
        });
        if (ModsCompats.MEKANISMGENERATORS.isLoaded()) {
            EMGenBlocks.BLOCKS.getPrimaryEntries().forEach(holder -> mineable.addOptional(holder.getId()));
        }

        tag(EMDatagenTags.block("minecraft", "netherrack_ore_replaceables")).add(Blocks.NETHERRACK);
        tag(EMDatagenTags.block("minecraft", "end_stone_ore_replaceables")).add(Blocks.END_STONE);
        tag(BlockTags.REPLACEABLE).add(EMFluids.NITROGEN.getBlock(), EMFluids.CRYONOCTIS.getBlock());

        if (ModsCompats.MEKANISMTOOLS.isLoaded()) {
            tag(EMToolsTags.Blocks.INCORRECT_FOR_NEEDS_BETTER_GOLD_TOOL).addTag(BlockTags.INCORRECT_FOR_NETHERITE_TOOL);
            tag(EMToolsTags.Blocks.INCORRECT_FOR_PLASLITHERITE_TOOL).addTag(BlockTags.INCORRECT_FOR_NETHERITE_TOOL);
            tag(EMToolsTags.Blocks.INCORRECT_FOR_REFINED_REDSTONE_TOOL).addTag(BlockTags.INCORRECT_FOR_NETHERITE_TOOL);
        }
        tag(EMDatagenTags.block(EvolvedMekanism.MODID, "needs_noctis_rozuli_tool"));
    }

    private void classifyOre(Block block, String path) {
        if (!path.endsWith("_ore") || path.endsWith("_natural")) {
            return;
        }
        tag(BlockTags.NEEDS_STONE_TOOL).add(block);
        String resource = resourceFromOre(path);
        if (resource != null) {
            tag(EMDatagenTags.cBlock("ores/" + resource)).add(block);
        }
        if (path.startsWith("netherrack_")) {
            tag(EMDatagenTags.cBlock("ores_in_ground/netherrack")).add(block);
        } else if (path.startsWith("end_stone_")) {
            tag(EMDatagenTags.cBlock("ores_in_ground/end_stone")).add(block);
        } else if (path.startsWith("holystone_")) {
            tag(EMDatagenTags.cBlock("ores_in_ground/holystone")).add(block);
        } else if (path.startsWith("depthrock_")) {
            tag(EMDatagenTags.cBlock("ores_in_ground/depthrock")).add(block);
        } else if (path.startsWith("shiverstone_")) {
            tag(EMDatagenTags.cBlock("ores_in_ground/shiverstone")).add(block);
        }
        if (path.contains("fluorite")) {
            tag(EMDatagenTags.cBlock("ore_rates/dense")).add(block);
        } else if (resource != null && !"noctis_rozuli".equals(resource)) {
            tag(EMDatagenTags.cBlock("ore_rates/singular")).add(block);
        }
    }

    static String resourceFromOre(String path) {
        String name = path.endsWith("_ore") ? path.substring(0, path.length() - 4) : path;
        for (String prefix : new String[]{"netherrack_", "end_stone_", "holystone_", "depthrock_", "shiverstone_", "deepslate_"}) {
            if (name.startsWith(prefix)) {
                return name.substring(prefix.length());
            }
        }
        return name;
    }
}
