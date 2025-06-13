package fr.iglee42.emgenerators.registries;

import java.util.EnumSet;
import java.util.function.Supplier;

import fr.iglee42.emgenerators.tiers.AdvancedSolarPanelTier;
import fr.iglee42.emgenerators.tile.TileEntityTieredAdvancedSolarGenerator;
import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.registries.EMUpgrades;
import mekanism.api.Upgrade;
import mekanism.api.text.ILangEntry;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.block.attribute.AttributeUpgradeable;
import mekanism.common.block.attribute.Attributes;
import mekanism.common.block.prefab.BlockBase;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import mekanism.generators.common.content.blocktype.BlockShapes;
import mekanism.generators.common.content.blocktype.Generator;
import mekanism.generators.common.content.blocktype.Generator.GeneratorBuilder;
import mekanism.generators.common.registries.GeneratorsSounds;

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


    public static Generator<TileEntityTieredAdvancedSolarGenerator> createTieredSolarGenerator(
            AdvancedSolarPanelTier tier,
            Supplier<TileEntityTypeRegistryObject<TileEntityTieredAdvancedSolarGenerator>> tileEntityRegistrar,
            ILangEntry description,
            Supplier<BlockRegistryObject<?,?>> upgradeBlock
    ) {
        return GeneratorBuilder
                .createGenerator(tileEntityRegistrar, description)
                .withGui(() -> EMGenContainerTypes.TIERED_ADVANCED_SOLAR_GENERATOR)
                .withEnergyConfig(() -> MekanismGeneratorsConfig.storageConfig.advancedSolarGenerator.get().multiply(tier.getMultiplier()))
                .withCustomShape(BlockShapes.ADVANCED_SOLAR_GENERATOR)
                .withSound(GeneratorsSounds.SOLAR_GENERATOR)
                .withSupportedUpgrades(EnumSet.of(Upgrade.MUFFLING, EMUpgrades.SOLAR_UPGRADE))
                .withBounding((pos, state, builder) -> {
                    builder.add(pos.above());
                    for (int x = -1; x <= 1; x++) {
                        for (int z = -1; z <= 1; z++) {
                            builder.add(pos.offset(x, 2, z));
                        }
                    }
                })
                .withComputerSupport("advancedSolarGenerator")
                .with(new AttributeTier<>(tier), new AttributeUpgradeable(upgradeBlock))
                .replace(Attributes.ACTIVE)
                .build();
    }


    public static void register() {
    }
}
