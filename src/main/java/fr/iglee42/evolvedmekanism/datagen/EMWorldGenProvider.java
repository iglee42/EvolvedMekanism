package fr.iglee42.evolvedmekanism.datagen;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import mekanism.common.registries.MekanismFeatures;
import mekanism.common.resource.ore.OreType;
import mekanism.common.world.ResizableOreFeatureConfig;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class EMWorldGenProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, EMWorldGenProvider::configured)
            .add(Registries.TRIM_MATERIAL, EMWorldGenProvider::trims);

    public EMWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup, BUILDER, Set.of(EvolvedMekanism.MODID));
    }

    private static void configured(BootstapContext<ConfiguredFeature<?, ?>> context) {
        for (OreType type : OreType.values()) {
            for (int index = 0; index < type.getBaseConfigs().size(); index++) {
                OreType.OreVeinType vein = new OreType.OreVeinType(type, index);
                String base = vein.name();
                registerDim(context, base + "_nether", vein, List.of(target("netherrack_" + oreSuffix(type), "minecraft:netherrack_ore_replaceables")));
                registerDim(context, base + "_end", vein, List.of(target("end_stone_" + oreSuffix(type), "minecraft:end_stone_ore_replaceables")));
                registerDim(context, base + "_aether", vein, List.of(target("holystone_" + oreSuffix(type), "aether:holystone_ore_replaceables")));
                registerDim(context, base + "_undergarden", vein, List.of(
                        target("depthrock_" + oreSuffix(type), "undergarden:depthrock_ore_replaceables"),
                        target("shiverstone_" + oreSuffix(type), "undergarden:shiverstone_ore_replaceables")
                ));
            }
        }
    }

    private static void registerDim(BootstapContext<ConfiguredFeature<?, ?>> context, String name, OreType.OreVeinType vein,
                                    List<OreConfiguration.TargetBlockState> targets) {
        context.register(configuredKey(name), new ConfiguredFeature<>(MekanismFeatures.ORE.get(),
                new ResizableOreFeatureConfig(targets, vein, () -> 0, () -> 0F)));
    }

    private static void trims(BootstapContext<TrimMaterial> context) {
        context.register(trim("better_gold"), trimMaterial("better_gold", EMItems.BETTER_GOLD_INGOT.get(), 0.6F, 0xFDF147));
        context.register(trim("plaslitherite"), trimMaterial("plaslitherite", EMItems.PLASLITHERITE_INGOT.get(), 0.3F, 0x5C6263));
        context.register(trim("refined_redstone"), trimMaterial("refined_redstone", EMItems.REFINED_REDSTONE_INGOT.get(), 0.4F, 0x96121F));
    }

    private static TrimMaterial trimMaterial(String asset, net.minecraft.world.item.Item item, float index, int color) {
        return new TrimMaterial(asset, item.builtInRegistryHolder(), index, Map.of(),
                Component.translatable("trim_material.evolvedmekanism." + asset).withStyle(style -> style.withColor(color)));
    }

    private static OreConfiguration.TargetBlockState target(String blockPath, String tag) {
        Block block = BuiltInRegistries.BLOCK.get(EvolvedMekanism.rl(blockPath));
        return OreConfiguration.target(new TagMatchTest(TagKey.create(Registries.BLOCK, new ResourceLocation(tag))), block.defaultBlockState());
    }

    private static String oreSuffix(OreType type) {
        return type.getResource().getRegistrySuffix() + "_ore";
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> configuredKey(String path) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, EvolvedMekanism.rl(path));
    }

    private static ResourceKey<TrimMaterial> trim(String path) {
        return ResourceKey.create(Registries.TRIM_MATERIAL, EvolvedMekanism.rl(path));
    }
}
