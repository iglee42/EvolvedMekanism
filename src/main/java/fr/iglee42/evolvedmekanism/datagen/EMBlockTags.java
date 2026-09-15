package fr.iglee42.evolvedmekanism.datagen;

import fr.iglee42.emgenerators.registries.EMGenBlocks;
import fr.iglee42.emtools.registries.EMToolsTags;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMTags;
import fr.iglee42.evolvedmekanism.utils.ModsCompats;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EMBlockTags extends BlockTagsProvider {

    public EMBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper existing) {
        super(output, lookup, EvolvedMekanism.MODID, existing);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(EMTags.Blocks.STORAGE_BLOCKS_BETTER_GOLD).add(EMBlocks.BETTER_GOLD_BLOCK.getBlock());
        tag(EMTags.Blocks.STORAGE_BLOCKS_PLASLITHERITE).add(EMBlocks.PLASLITHERITE_BLOCK.getBlock());
        tag(EMTags.Blocks.STORAGE_BLOCKS_REFINED_REDSTONE).add(EMBlocks.REFINED_REDSTONE_BLOCK.getBlock());
        tag(EMTags.Blocks.STORAGE_BLOCKS_NOCTIS_ROZULI).add(EMBlocks.NOCTIS_ROZULI_BLOCK.getBlock());

        tag(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_INFUSED).add(EMBlocks.INFUSED_ALLOY_BLOCK.getBlock());
        tag(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_REINFORCED).add(EMBlocks.REINFORCED_ALLOY_BLOCK.getBlock());
        tag(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_ATOMIC).add(EMBlocks.ATOMIC_ALLOY_BLOCK.getBlock());
        tag(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_HYPERCHARGED).add(EMBlocks.HYPERCHARGED_ALLOY_BLOCK.getBlock());
        tag(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_SUBATOMIC).add(EMBlocks.SUBATOMIC_ALLOY_BLOCK.getBlock());
        tag(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_SINGULAR).add(EMBlocks.SINGULAR_ALLOY_BLOCK.getBlock());
        tag(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_EXOVERSAL).add(EMBlocks.EXOVERSAL_ALLOY_BLOCK.getBlock());
        tag(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_CREATIVE).add(EMBlocks.CREATIVE_ALLOY_BLOCK.getBlock());
        tag(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS)
                .addTags(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_INFUSED, EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_REINFORCED,
                        EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_ATOMIC, EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_HYPERCHARGED,
                        EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_SUBATOMIC, EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_SINGULAR,
                        EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_EXOVERSAL, EMTags.Blocks.STORAGE_BLOCKS_ALLOYS_CREATIVE);
        tag(EMDatagenTags.forgeBlock("storage_blocks"))
                .addTags(EMTags.Blocks.STORAGE_BLOCKS_BETTER_GOLD, EMTags.Blocks.STORAGE_BLOCKS_PLASLITHERITE,
                        EMTags.Blocks.STORAGE_BLOCKS_REFINED_REDSTONE, EMTags.Blocks.STORAGE_BLOCKS_NOCTIS_ROZULI,
                        EMTags.Blocks.STORAGE_BLOCKS_ALLOYS);
        tag(EMDatagenTags.forgeBlock("storage_blocks/noctis")).addTag(EMTags.Blocks.STORAGE_BLOCKS_NOCTIS_ROZULI);
        tag(EMDatagenTags.forgeBlock("ores/noctis")).addTag(EMTags.Blocks.ORES_NOCTIS_ROZULI);
        tag(EMDatagenTags.forgeBlock("storage_blocks/amethyst")).add(Blocks.AMETHYST_BLOCK);
        tag(EMDatagenTags.forgeBlock("storage_blocks/glowstone")).add(Blocks.GLOWSTONE);
        tag(EMDatagenTags.forgeBlock("storage_blocks/quartz")).add(Blocks.QUARTZ_BLOCK);

        var personalBarrel = tag(EMDatagenTags.forgeBlock("barrels/personal"));
        var personalChest = tag(EMDatagenTags.forgeBlock("chests/personal"));
        var electricChest = tag(EMDatagenTags.forgeBlock("chests/electric"));
        var piglin = tag(BlockTags.GUARDED_BY_PIGLINS);
        var relocation = tag(EMDatagenTags.forgeBlock("relocation_not_supported"));
        var mineable = tag(BlockTags.MINEABLE_WITH_PICKAXE);
        for (Block block : ForgeRegistries.BLOCKS) {
            ResourceLocation id = ForgeRegistries.BLOCKS.getKey(block);
            if (id == null || !EvolvedMekanism.MODID.equals(id.getNamespace())) {
                continue;
            }
            if (block instanceof LiquidBlock) {
                tag(BlockTags.REPLACEABLE).add(block);
                continue;
            }
            mineable.add(block);
            String path = id.getPath();
            if (path.endsWith("_personal_barrel")) {
                personalBarrel.add(block);
                piglin.add(block);
            } else if (path.endsWith("_personal_chest")) {
                personalChest.add(block);
                electricChest.add(block);
                piglin.add(block);
            } else if (path.endsWith("_pressurized_tube") || path.endsWith("_mechanical_pipe") || path.endsWith("_universal_cable")) {
                relocation.add(block);
            }
            classifyOre(block, path);
        }
        if (ModsCompats.MEKANISMGENERATORS.isLoaded()) {
            for (Block block : ForgeRegistries.BLOCKS) {
                ResourceLocation id = ForgeRegistries.BLOCKS.getKey(block);
                if (id != null && EvolvedMekanism.MODID.equals(id.getNamespace()) && (id.getPath().contains("solar_generator") || id.getPath().contains("lunar_generator"))) {
                    mineable.addOptional(id);
                }
            }
        }
        mineable.addOptional(new ResourceLocation("mekanism", "basic_alloying_factory"));
        mineable.addOptional(new ResourceLocation("mekanism", "advanced_alloying_factory"));
        mineable.addOptional(new ResourceLocation("mekanism", "elite_alloying_factory"));
        mineable.addOptional(new ResourceLocation("mekanism", "ultimate_alloying_factory"));

        tag(EMDatagenTags.block("minecraft", "netherrack_ore_replaceables")).add(Blocks.NETHERRACK);
        tag(EMDatagenTags.block("minecraft", "end_stone_ore_replaceables")).add(Blocks.END_STONE);

        if (ModsCompats.MEKANISMTOOLS.isLoaded()) {
            tag(EMToolsTags.Blocks.NEEDS_BETTER_GOLD_TOOL);
            tag(EMToolsTags.Blocks.NEEDS_PLASLITHERITE_TOOL);
            tag(EMToolsTags.Blocks.NEEDS_REFINED_REDSTONE_TOOL);
            tag(EMToolsTags.Blocks.NEEDS_NOCTIS_ROZULI_TOOL);
        }
    }

    private void classifyOre(Block block, String path) {
        if (path.endsWith("_ore_natural")) {
            tag(BlockTags.NEEDS_STONE_TOOL).add(block);
            return;
        }
        if (!path.endsWith("_ore")) {
            return;
        }
        tag(BlockTags.NEEDS_STONE_TOOL).add(block);
        String resource = resourceFromOre(path);
        if (resource != null) {
            tag(EMDatagenTags.forgeBlock("ores/" + resource)).add(block);
        }
        if (path.startsWith("netherrack_")) {
            tag(EMDatagenTags.forgeBlock("ores_in_ground/netherrack")).add(block);
        } else if (path.startsWith("end_stone_")) {
            tag(EMDatagenTags.forgeBlock("ores_in_ground/end_stone")).add(block);
        } else if (path.startsWith("holystone_")) {
            tag(EMDatagenTags.forgeBlock("ores_in_ground/holystone")).add(block);
        } else if (path.startsWith("depthrock_")) {
            tag(EMDatagenTags.forgeBlock("ores_in_ground/depthrock")).add(block);
        } else if (path.startsWith("shiverstone_")) {
            tag(EMDatagenTags.forgeBlock("ores_in_ground/shiverstone")).add(block);
        } else if (path.startsWith("deepslate_")) {
            tag(EMDatagenTags.forgeBlock("ores_in_ground/deepslate")).add(block);
        } else if (resource != null && path.equals(resource + "_ore")) {
            tag(EMDatagenTags.forgeBlock("ores_in_ground/stone")).add(block);
        }
        if (path.contains("fluorite")) {
            tag(EMDatagenTags.forgeBlock("ore_rates/dense")).add(block);
        } else if (resource != null) {
            tag(EMDatagenTags.forgeBlock("ore_rates/singular")).add(block);
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
