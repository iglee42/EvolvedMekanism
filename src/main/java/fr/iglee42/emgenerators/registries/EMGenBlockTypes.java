package fr.iglee42.emgenerators.registries;

import java.util.EnumSet;
import java.util.function.Supplier;

import fr.iglee42.emgenerators.tiers.AdvancedLunarPanelTier;
import fr.iglee42.emgenerators.tiers.AdvancedSolarPanelTier;
import fr.iglee42.emgenerators.tile.TileEntityLunarGenerator;
import fr.iglee42.emgenerators.tile.TileEntityTieredAdvancedLunarGenerator;
import fr.iglee42.emgenerators.tile.TileEntityTieredAdvancedSolarGenerator;
import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.registries.EMTileEntityTypes;
import fr.iglee42.evolvedmekanism.registries.EMUpgrades;
import mekanism.api.Upgrade;
import mekanism.api.text.ILangEntry;
import mekanism.common.block.attribute.*;
import mekanism.common.block.prefab.BlockBase;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.generators.common.GeneratorsLang;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import mekanism.generators.common.content.blocktype.BlockShapes;
import mekanism.generators.common.content.blocktype.Generator;
import mekanism.generators.common.content.blocktype.Generator.GeneratorBuilder;
import mekanism.generators.common.registries.GeneratorsContainerTypes;
import mekanism.generators.common.registries.GeneratorsSounds;
import mekanism.generators.common.registries.GeneratorsTileEntityTypes;
import mekanism.generators.common.tile.TileEntitySolarGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class EMGenBlockTypes {

    public static final Generator<TileEntityTieredAdvancedSolarGenerator> ADVANCED_SOLAR_GENERATOR =
            createTieredSolarGenerator(AdvancedSolarPanelTier.ADVANCED, () -> EMGenTileEntityTypes.ADVANCED_SOLAR_PANEL, EvolvedMekanismLang.DESCRIPTION_ADVANCED_SOLAR_GENERATOR, () -> EMGenBlocks.ELITE_SOLAR_GENERATOR);

    public static final Generator<TileEntityTieredAdvancedSolarGenerator> ELITE_SOLAR_GENERATOR =
            createTieredSolarGenerator(AdvancedSolarPanelTier.ELITE, () -> EMGenTileEntityTypes.ELITE_SOLAR_PANEL, EvolvedMekanismLang.DESCRIPTION_ELITE_SOLAR_GENERATOR, () -> EMGenBlocks.ULTIMATE_SOLAR_GENERATOR);

    public static final Generator<TileEntityTieredAdvancedSolarGenerator> ULTIMATE_SOLAR_GENERATOR =
            createTieredSolarGenerator(AdvancedSolarPanelTier.ULTIMATE, () -> EMGenTileEntityTypes.ULTIMATE_SOLAR_PANEL, EvolvedMekanismLang.DESCRIPTION_ULTIMATE_SOLAR_GENERATOR, () -> EMGenBlocks.OVERCLOCKED_SOLAR_GENERATOR);

    public static final Generator<TileEntityTieredAdvancedSolarGenerator> OVERCLOCKED_SOLAR_GENERATOR =
            createTieredSolarGenerator(AdvancedSolarPanelTier.OVERCLOCKED, () -> EMGenTileEntityTypes.OVERCLOCKED_SOLAR_PANEL, EvolvedMekanismLang.DESCRIPTION_OVERCLOCKED_SOLAR_GENERATOR, () -> EMGenBlocks.QUANTUM_SOLAR_GENERATOR);

    public static final Generator<TileEntityTieredAdvancedSolarGenerator> QUANTUM_SOLAR_GENERATOR =
            createTieredSolarGenerator(AdvancedSolarPanelTier.QUANTUM, () -> EMGenTileEntityTypes.QUANTUM_SOLAR_PANEL, EvolvedMekanismLang.DESCRIPTION_QUANTUM_SOLAR_GENERATOR, () -> EMGenBlocks.DENSE_SOLAR_GENERATOR);

    public static final Generator<TileEntityTieredAdvancedSolarGenerator> DENSE_SOLAR_GENERATOR =
            createTieredSolarGenerator(AdvancedSolarPanelTier.DENSE, () -> EMGenTileEntityTypes.DENSE_SOLAR_PANEL, EvolvedMekanismLang.DESCRIPTION_DENSE_SOLAR_GENERATOR, () -> EMGenBlocks.MULTIVERSAL_SOLAR_GENERATOR);

    public static final Generator<TileEntityTieredAdvancedSolarGenerator> MULTIVERSAL_SOLAR_GENERATOR =
            createTieredSolarGenerator(AdvancedSolarPanelTier.MULTIVERSAL, () -> EMGenTileEntityTypes.MULTIVERSAL_SOLAR_PANEL, EvolvedMekanismLang.DESCRIPTION_MULTIVERSAL_SOLAR_GENERATOR, ()->null);
    public static final Generator<TileEntityTieredAdvancedSolarGenerator> CREATIVE_SOLAR_GENERATOR =
            createTieredSolarGenerator(AdvancedSolarPanelTier.CREATIVE, () -> EMGenTileEntityTypes.CREATIVE_SOLAR_PANEL, EvolvedMekanismLang.DESCRIPTION_CREATIVE_SOLAR_GENERATOR, ()->null);

    public static final Generator<TileEntityLunarGenerator> LUNAR_GENERATOR = GeneratorBuilder
            .createGenerator(() -> EMGenTileEntityTypes.LUNAR_GENERATOR, EvolvedMekanismLang.DESCRIPTION_LUNAR_GENERATOR)
            .withGui(() -> EMGenContainerTypes.LUNAR_GENERATOR)
            .withEnergyConfig(MekanismGeneratorsConfig.storageConfig.solarGenerator)
            .withCustomShape(BlockShapes.SOLAR_GENERATOR)
            .withSound(GeneratorsSounds.SOLAR_GENERATOR)
            .withSupportedUpgrades(Upgrade.MUFFLING,EMUpgrades.LUNAR_UPGRADE)
            .withComputerSupport("lunarGenerator")
            .replace(Attributes.ACTIVE)
            .build();

    public static final Generator<TileEntityTieredAdvancedLunarGenerator> BASIC_ADVANCED_LUNAR_GENERATOR =
            createTieredLunarGenerator(AdvancedLunarPanelTier.ADVANCED, () -> EMGenTileEntityTypes.BASIC_ADVANCED_LUNAR_PANEL, EvolvedMekanismLang.DESCRIPTION_BASIC_ADVANCED_LUNAR_GENERATOR, () -> EMGenBlocks.BASIC_ADVANCED_LUNAR_GENERATOR);

    public static final Generator<TileEntityTieredAdvancedLunarGenerator> ADVANCED_LUNAR_GENERATOR =
            createTieredLunarGenerator(AdvancedLunarPanelTier.ADVANCED, () -> EMGenTileEntityTypes.ADVANCED_LUNAR_PANEL, EvolvedMekanismLang.DESCRIPTION_ADVANCED_LUNAR_GENERATOR, () -> EMGenBlocks.ELITE_LUNAR_GENERATOR);

    public static final Generator<TileEntityTieredAdvancedLunarGenerator> ELITE_LUNAR_GENERATOR =
            createTieredLunarGenerator(AdvancedLunarPanelTier.ELITE, () -> EMGenTileEntityTypes.ELITE_LUNAR_PANEL, EvolvedMekanismLang.DESCRIPTION_ELITE_LUNAR_GENERATOR, () -> EMGenBlocks.ULTIMATE_LUNAR_GENERATOR);

    public static final Generator<TileEntityTieredAdvancedLunarGenerator> ULTIMATE_LUNAR_GENERATOR =
            createTieredLunarGenerator(AdvancedLunarPanelTier.ULTIMATE, () -> EMGenTileEntityTypes.ULTIMATE_LUNAR_PANEL, EvolvedMekanismLang.DESCRIPTION_ULTIMATE_LUNAR_GENERATOR, () -> EMGenBlocks.OVERCLOCKED_LUNAR_GENERATOR);

    public static final Generator<TileEntityTieredAdvancedLunarGenerator> OVERCLOCKED_LUNAR_GENERATOR =
            createTieredLunarGenerator(AdvancedLunarPanelTier.OVERCLOCKED, () -> EMGenTileEntityTypes.OVERCLOCKED_LUNAR_PANEL, EvolvedMekanismLang.DESCRIPTION_OVERCLOCKED_LUNAR_GENERATOR, () -> EMGenBlocks.QUANTUM_LUNAR_GENERATOR);

    public static final Generator<TileEntityTieredAdvancedLunarGenerator> QUANTUM_LUNAR_GENERATOR =
            createTieredLunarGenerator(AdvancedLunarPanelTier.QUANTUM, () -> EMGenTileEntityTypes.QUANTUM_LUNAR_PANEL, EvolvedMekanismLang.DESCRIPTION_QUANTUM_LUNAR_GENERATOR, () -> EMGenBlocks.DENSE_LUNAR_GENERATOR);

    public static final Generator<TileEntityTieredAdvancedLunarGenerator> DENSE_LUNAR_GENERATOR =
            createTieredLunarGenerator(AdvancedLunarPanelTier.DENSE, () -> EMGenTileEntityTypes.DENSE_LUNAR_PANEL, EvolvedMekanismLang.DESCRIPTION_DENSE_LUNAR_GENERATOR, () -> EMGenBlocks.MULTIVERSAL_LUNAR_GENERATOR);

    public static final Generator<TileEntityTieredAdvancedLunarGenerator> MULTIVERSAL_LUNAR_GENERATOR =
            createTieredLunarGenerator(AdvancedLunarPanelTier.MULTIVERSAL, () -> EMGenTileEntityTypes.MULTIVERSAL_LUNAR_PANEL, EvolvedMekanismLang.DESCRIPTION_MULTIVERSAL_LUNAR_GENERATOR, ()->null);
    public static final Generator<TileEntityTieredAdvancedLunarGenerator> CREATIVE_LUNAR_GENERATOR =
            createTieredLunarGenerator(AdvancedLunarPanelTier.CREATIVE, () -> EMGenTileEntityTypes.CREATIVE_LUNAR_PANEL, EvolvedMekanismLang.DESCRIPTION_CREATIVE_LUNAR_GENERATOR, ()->null);

    public static Generator<TileEntityTieredAdvancedSolarGenerator> createTieredSolarGenerator(
            AdvancedSolarPanelTier tier,
            Supplier<TileEntityTypeRegistryObject<TileEntityTieredAdvancedSolarGenerator>> tileEntityRegistrar,
            ILangEntry description,
            Supplier<BlockRegistryObject<?,?>> upgradeBlock
    ) {
        return GeneratorBuilder
                .createGenerator(tileEntityRegistrar, description)
                .withGui(() -> EMGenContainerTypes.TIERED_ADVANCED_SOLAR_GENERATOR)
                .withEnergyConfig(() -> MekanismGeneratorsConfig.storageConfig.advancedSolarGenerator.get() * tier.getMultiplier())
                .withCustomShape(BlockShapes.ADVANCED_SOLAR_GENERATOR)
                .withSound(GeneratorsSounds.SOLAR_GENERATOR)
                .withSupportedUpgrades(Upgrade.MUFFLING, EMUpgrades.SOLAR_UPGRADE)
                .withBounding(new AttributeHasBounding.HandleBoundingBlock() {
                    @Override
                    public <DATA> boolean handle(Level level, BlockPos pos, BlockState state, DATA data, AttributeHasBounding.TriBooleanFunction<Level, BlockPos, DATA> consumer) {
                        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
                        if (!consumer.accept(level, mutable, data)) {
                            return false;
                        }
                        for (int x = -1; x <= 1; x++) {
                            for (int z = -1; z <= 1; z++) {
                                mutable.setWithOffset(pos, x, 2, z);
                                if (!consumer.accept(level, mutable, data)) {
                                    return false;
                                }
                            }
                        }
                        return true;
                    }
                })
                .withComputerSupport("advancedSolarGenerator")
                .with(new AttributeTier<>(tier), new AttributeUpgradeable(upgradeBlock))
                .replace(Attributes.ACTIVE)
                .build();
    }


    public static Generator<TileEntityTieredAdvancedLunarGenerator> createTieredLunarGenerator(
            AdvancedLunarPanelTier tier,
            Supplier<TileEntityTypeRegistryObject<TileEntityTieredAdvancedLunarGenerator>> tileEntityRegistrar,
            ILangEntry description,
            Supplier<BlockRegistryObject<?,?>> upgradeBlock
    ) {
        return GeneratorBuilder
                .createGenerator(tileEntityRegistrar, description)
                .withGui(() -> EMGenContainerTypes.TIERED_ADVANCED_LUNAR_GENERATOR)
                .withEnergyConfig(() -> MekanismGeneratorsConfig.storageConfig.advancedSolarGenerator.get() * tier.getMultiplier())
                .withCustomShape(BlockShapes.ADVANCED_SOLAR_GENERATOR)
                .withSound(GeneratorsSounds.SOLAR_GENERATOR)
                .withSupportedUpgrades(Upgrade.MUFFLING, EMUpgrades.LUNAR_UPGRADE)
                .withBounding(new AttributeHasBounding.HandleBoundingBlock() {
                    @Override
                    public <DATA> boolean handle(Level level, BlockPos pos, BlockState state, DATA data, AttributeHasBounding.TriBooleanFunction<Level, BlockPos, DATA> consumer) {
                        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
                        if (!consumer.accept(level, mutable, data)) {
                            return false;
                        }
                        for (int x = -1; x <= 1; x++) {
                            for (int z = -1; z <= 1; z++) {
                                mutable.setWithOffset(pos, x, 2, z);
                                if (!consumer.accept(level, mutable, data)) {
                                    return false;
                                }
                            }
                        }
                        return true;
                    }
                })
                .withComputerSupport("advancedLunarGenerator")
                .with(new AttributeTier<>(tier), new AttributeUpgradeable(upgradeBlock))
                .replace(Attributes.ACTIVE)
                .build();
    }


    public static void register() {
    }
}
