package fr.iglee42.evolvedmekanism.datagen;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMFeatures;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import fr.iglee42.evolvedmekanism.registries.EMOreType;
import fr.iglee42.evolvedmekanism.world.EMResizableOreFeatureConfig;
import mekanism.common.registries.MekanismFeatures;
import mekanism.common.resource.ore.OreType;
import mekanism.common.world.ResizableOreFeatureConfig;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

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

    private static void configured(BootstrapContext<ConfiguredFeature<?, ?>> context) {
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
        EMOreType.OreVeinType noctis = new EMOreType.OreVeinType(EMOreType.NOCTIS_ROZULI, 0);
        EMOreType.OreVeinType noctisBuried = new EMOreType.OreVeinType(EMOreType.NOCTIS_ROZULI, 1);
        context.register(configuredKey("ore_noctis"), new ConfiguredFeature<>(EMFeatures.ORE.get(), new EMResizableOreFeatureConfig(
                List.of(
                        emTarget("noctis_rozuli_ore_natural", "minecraft:stone_ore_replaceables"),
                        emTarget("deepslate_noctis_rozuli_ore_natural", "minecraft:deepslate_ore_replaceables")
                ), noctis, () -> 0, () -> 0F)));
        context.register(configuredKey("ore_noctis_buried"), new ConfiguredFeature<>(EMFeatures.ORE.get(), new EMResizableOreFeatureConfig(
                List.of(
                        emTarget("noctis_rozuli_ore_natural", "minecraft:stone_ore_replaceables"),
                        emTarget("deepslate_noctis_rozuli_ore_natural", "minecraft:deepslate_ore_replaceables")
                ), noctisBuried, () -> 0, () -> 0F)));
    }

    private static void registerDim(BootstrapContext<ConfiguredFeature<?, ?>> context, String name, OreType.OreVeinType vein,
                                    List<OreConfiguration.TargetBlockState> targets) {
        context.register(configuredKey(name), new ConfiguredFeature<>(MekanismFeatures.ORE.get(),
                new ResizableOreFeatureConfig(targets, vein, () -> 0, () -> 0F)));
    }

    private static void trims(BootstrapContext<TrimMaterial> context) {
        context.register(trim("better_gold"), trimMaterial("better_gold", EMItems.BETTER_GOLD_INGOT.get(), 0.6F, 0xFDF147));
        context.register(trim("plaslitherite"), trimMaterial("plaslitherite", EMItems.PLASLITHERITE_INGOT.get(), 0.3F, 0x5C6263));
        context.register(trim("refined_redstone"), trimMaterial("refined_redstone", EMItems.REFINED_REDSTONE_INGOT.get(), 0.4F, 0x96121F));
        context.register(trim("noctis_rozuli"), trimMaterial("noctis_rozuli", EMItems.NOCTIS_ROZULI.get(), 1.0F, 0xFA6AB2));
    }

    private static TrimMaterial trimMaterial(String asset, net.minecraft.world.item.Item item, float index, int color) {
        return new TrimMaterial(asset, BuiltInRegistries.ITEM.wrapAsHolder(item), index, Map.of(),
                Component.translatable("trim_material.evolvedmekanism." + asset).withStyle(style -> style.withColor(color)));
    }

    private static OreConfiguration.TargetBlockState target(String blockPath, String tag) {
        Block block = BuiltInRegistries.BLOCK.get(EvolvedMekanism.rl(blockPath));
        return OreConfiguration.target(new TagMatchTest(TagKey.create(Registries.BLOCK, ResourceLocation.parse(tag))), block.defaultBlockState());
    }

    private static OreConfiguration.TargetBlockState emTarget(String blockPath, String tag) {
        return target(blockPath, tag);
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
