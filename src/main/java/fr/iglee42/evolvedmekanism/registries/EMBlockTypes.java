package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.blocks.BlockTieredPersonalStorage;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTCasing;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTPort;
import fr.iglee42.evolvedmekanism.tiers.PersonalStorageTier;
import fr.iglee42.evolvedmekanism.tiers.storage.*;
import fr.iglee42.evolvedmekanism.tiles.TileEntityTieredPersonalBarrel;
import fr.iglee42.evolvedmekanism.tiles.TileEntityTieredPersonalChest;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityAlloyer;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityChemixer;
import mekanism.api.Upgrade;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.*;
import mekanism.common.block.attribute.Attributes.AttributeRedstone;
import mekanism.common.block.attribute.Attributes.AttributeSecurity;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.blocktype.BlockShapes;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.BlockTypeTile.BlockTileBuilder;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.content.blocktype.Machine.MachineBuilder;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.registries.MekanismContainerTypes;
import mekanism.common.registries.MekanismSounds;
import mekanism.common.registries.MekanismTileEntityTypes;
import mekanism.common.tier.*;
import mekanism.common.tile.*;
import mekanism.common.tile.multiblock.TileEntityInductionCell;
import mekanism.common.tile.multiblock.TileEntityInductionProvider;
import mekanism.common.tile.multiblock.TileEntitySPSPort;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Supplier;

public class EMBlockTypes {

    public static final Machine.FactoryMachine<TileEntityAlloyer> ALLOYER = MachineBuilder
            .createFactoryMachine(() -> EMTileEntityTypes.ALLOYER, EvolvedMekanismLang.DESCRIPTION_ALLOYER, EMFactoryType.ALLOYING)
            .withGui(() -> EMContainerTypes.ALLOYER)
            .withSound(MekanismSounds.COMBINER)
            .withEnergyConfig(MekanismConfig.usage.combiner, MekanismConfig.storage.combiner)
            .withSupportedUpgrades(EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.MUFFLING))
            .withComputerSupport("alloyer")
            .build();

    public static final Machine<TileEntityChemixer> CHEMIXER = MachineBuilder
            .createMachine(() -> EMTileEntityTypes.CHEMIXER, MekanismLang.DESCRIPTION_PRESSURIZED_REACTION_CHAMBER)
            .withGui(() -> EMContainerTypes.CHEMIXER)
            .withSound(MekanismSounds.PRESSURIZED_REACTION_CHAMBER)
            .withEnergyConfig(MekanismConfig.usage.combiner, MekanismConfig.storage.combiner)
            .withSupportedUpgrades(EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.MUFFLING,EMUpgrades.RADIOACTIVE_UPGRADE))
            .withComputerSupport("chemixer")
            .build();

    // APT Casing
    public static final BlockTypeTile<TileEntityAPTCasing> APT_CASING = BlockTileBuilder
            .createBlock(() -> EMTileEntityTypes.APT_CASING, EvolvedMekanismLang.DESCRIPTION_APT_CASING)
            .withGui(() -> EMContainerTypes.APT, EvolvedMekanismLang.APT)
            .withSound(MekanismSounds.SPS)
            .externalMultiblock()
            .build();
    // APT Port
    public static final BlockTypeTile<TileEntityAPTPort> APT_PORT = BlockTileBuilder
            .createBlock(() -> EMTileEntityTypes.APT_PORT, EvolvedMekanismLang.DESCRIPTION_APT_PORT)
            .withGui(() -> EMContainerTypes.APT, EvolvedMekanismLang.APT)
            .withSound(MekanismSounds.SPS)
            .with(Attributes.ACTIVE, Attributes.COMPARATOR)
            .externalMultiblock()
            .withComputerSupport("aptPort")
            .build();

    // Induction Cells
   public static final BlockTypeTile<TileEntityInductionCell> OVERCLOCKED_INDUCTION_CELL = createInductionCell(EMInductionCellTier.OVERCLOCKED, () -> EMTileEntityTypes.OVERCLOCKED_INDUCTION_CELL);
   public static final BlockTypeTile<TileEntityInductionCell> QUANTUM_INDUCTION_CELL = createInductionCell(EMInductionCellTier.QUANTUM, () -> EMTileEntityTypes.QUANTUM_INDUCTION_CELL);
   public static final BlockTypeTile<TileEntityInductionCell> DENSE_INDUCTION_CELL = createInductionCell(EMInductionCellTier.DENSE, () -> EMTileEntityTypes.DENSE_INDUCTION_CELL);
   public static final BlockTypeTile<TileEntityInductionCell> MULTIVERSAL_INDUCTION_CELL = createInductionCell(EMInductionCellTier.MULTIVERSAL, () -> EMTileEntityTypes.MULTIVERSAL_INDUCTION_CELL);
   public static final BlockTypeTile<TileEntityInductionCell> CREATIVE_INDUCTION_CELL = createInductionCell(EMInductionCellTier.CREATIVE, () -> EMTileEntityTypes.CREATIVE_INDUCTION_CELL);

    // Induction Provider
   public static final BlockTypeTile<TileEntityInductionProvider> OVERCLOCKED_INDUCTION_PROVIDER = createInductionProvider(EMInductionProviderTier.OVERCLOCKED, () -> EMTileEntityTypes.OVERCLOCKED_INDUCTION_PROVIDER);
   public static final BlockTypeTile<TileEntityInductionProvider> QUANTUM_INDUCTION_PROVIDER = createInductionProvider(EMInductionProviderTier.QUANTUM, () -> EMTileEntityTypes.QUANTUM_INDUCTION_PROVIDER);
   public static final BlockTypeTile<TileEntityInductionProvider> DENSE_INDUCTION_PROVIDER = createInductionProvider(EMInductionProviderTier.DENSE, () -> EMTileEntityTypes.DENSE_INDUCTION_PROVIDER);
   public static final BlockTypeTile<TileEntityInductionProvider> MULTIVERSAL_INDUCTION_PROVIDER = createInductionProvider(EMInductionProviderTier.MULTIVERSAL, () -> EMTileEntityTypes.MULTIVERSAL_INDUCTION_PROVIDER);
   public static final BlockTypeTile<TileEntityInductionProvider> CREATIVE_INDUCTION_PROVIDER = createInductionProvider(EMInductionProviderTier.CREATIVE, () -> EMTileEntityTypes.CREATIVE_INDUCTION_PROVIDER);

    // Bins
    public static final Machine<TileEntityBin> OVERCLOCKED_BIN = createBin(EMBinTier.OVERCLOCKED, () -> EMTileEntityTypes.OVERCLOCKED_BIN, () -> EMBlocks.QUANTUM_BIN);
    public static final Machine<TileEntityBin> QUANTUM_BIN = createBin(EMBinTier.QUANTUM, () -> EMTileEntityTypes.QUANTUM_BIN, () -> EMBlocks.DENSE_BIN);
    public static final Machine<TileEntityBin> DENSE_BIN = createBin(EMBinTier.DENSE, () -> EMTileEntityTypes.DENSE_BIN, () -> EMBlocks.MULTIVERSAL_BIN);
    public static final Machine<TileEntityBin> MULTIVERSAL_BIN = createBin(EMBinTier.MULTIVERSAL, () -> EMTileEntityTypes.MULTIVERSAL_BIN, null);

    // Energy Cubes
    public static final Machine<TileEntityEnergyCube> OVERCLOCKED_ENERGY_CUBE = createEnergyCube(EMEnergyCubeTier.OVERCLOCKED, () -> EMTileEntityTypes.OVERCLOCKED_ENERGY_CUBE, () -> EMBlocks.QUANTUM_ENERGY_CUBE);
    public static final Machine<TileEntityEnergyCube> QUANTUM_ENERGY_CUBE = createEnergyCube(EMEnergyCubeTier.QUANTUM, () -> EMTileEntityTypes.QUANTUM_ENERGY_CUBE, () -> EMBlocks.DENSE_ENERGY_CUBE);
    public static final Machine<TileEntityEnergyCube> DENSE_ENERGY_CUBE = createEnergyCube(EMEnergyCubeTier.DENSE, () -> EMTileEntityTypes.DENSE_ENERGY_CUBE, () -> EMBlocks.MULTIVERSAL_ENERGY_CUBE);
    public static final Machine<TileEntityEnergyCube> MULTIVERSAL_ENERGY_CUBE = createEnergyCube(EMEnergyCubeTier.MULTIVERSAL, () -> EMTileEntityTypes.MULTIVERSAL_ENERGY_CUBE, null);

    // Fluid Tanks
    public static final Machine<TileEntityFluidTank> OVERCLOCKED_FLUID_TANK = createFluidTank(EMFluidTankTier.OVERCLOCKED, () -> EMTileEntityTypes.OVERCLOCKED_FLUID_TANK, () -> EMBlocks.QUANTUM_FLUID_TANK);
    public static final Machine<TileEntityFluidTank> QUANTUM_FLUID_TANK = createFluidTank(EMFluidTankTier.QUANTUM, () -> EMTileEntityTypes.QUANTUM_FLUID_TANK, () -> EMBlocks.DENSE_FLUID_TANK);
    public static final Machine<TileEntityFluidTank> DENSE_FLUID_TANK = createFluidTank(EMFluidTankTier.DENSE, () -> EMTileEntityTypes.DENSE_FLUID_TANK, () -> EMBlocks.MULTIVERSAL_FLUID_TANK);
    public static final Machine<TileEntityFluidTank> MULTIVERSAL_FLUID_TANK = createFluidTank(EMFluidTankTier.MULTIVERSAL, () -> EMTileEntityTypes.MULTIVERSAL_FLUID_TANK, null);

    // Chemical Tanks
    public static final Machine<TileEntityChemicalTank> OVERCLOCKED_CHEMICAL_TANK = createChemicalTank(EMChemicalTankTier.OVERCLOCKED, () -> EMTileEntityTypes.OVERCLOCKED_CHEMICAL_TANK, () -> EMBlocks.QUANTUM_CHEMICAL_TANK);
    public static final Machine<TileEntityChemicalTank> QUANTUM_CHEMICAL_TANK = createChemicalTank(EMChemicalTankTier.QUANTUM, () -> EMTileEntityTypes.QUANTUM_CHEMICAL_TANK, () -> EMBlocks.DENSE_CHEMICAL_TANK);
    public static final Machine<TileEntityChemicalTank> DENSE_CHEMICAL_TANK = createChemicalTank(EMChemicalTankTier.DENSE, () -> EMTileEntityTypes.DENSE_CHEMICAL_TANK, () -> EMBlocks.MULTIVERSAL_CHEMICAL_TANK);
    public static final Machine<TileEntityChemicalTank> MULTIVERSAL_CHEMICAL_TANK = createChemicalTank(EMChemicalTankTier.MULTIVERSAL, () -> EMTileEntityTypes.MULTIVERSAL_CHEMICAL_TANK, null);

    // Personal Barrels
    public static final BlockTypeTile<TileEntityTieredPersonalBarrel> ADVANCED_PERSONAL_BARREL = createPersonalBarrel(()-> EMTileEntityTypes.ADVANCED_PERSONAL_BARREL,PersonalStorageTier.ADVANCED,()->EMBlocks.ELITE_PERSONAL_BARREL);
    public static final BlockTypeTile<TileEntityTieredPersonalBarrel> ELITE_PERSONAL_BARREL = createPersonalBarrel(()-> EMTileEntityTypes.ELITE_PERSONAL_BARREL,PersonalStorageTier.ELITE,()->EMBlocks.ULTIMATE_PERSONAL_BARREL);
    public static final BlockTypeTile<TileEntityTieredPersonalBarrel> ULTIMATE_PERSONAL_BARREL = createPersonalBarrel(()-> EMTileEntityTypes.ULTIMATE_PERSONAL_BARREL,PersonalStorageTier.ULTIMATE,()->EMBlocks.OVERCLOCKED_PERSONAL_BARREL);
    public static final BlockTypeTile<TileEntityTieredPersonalBarrel> OVERCLOCKED_PERSONAL_BARREL = createPersonalBarrel(()-> EMTileEntityTypes.OVERCLOCKED_PERSONAL_BARREL,PersonalStorageTier.OVERCLOCKED,()->EMBlocks.QUANTUM_PERSONAL_BARREL);
    public static final BlockTypeTile<TileEntityTieredPersonalBarrel> QUANTUM_PERSONAL_BARREL = createPersonalBarrel(()-> EMTileEntityTypes.QUANTUM_PERSONAL_BARREL,PersonalStorageTier.QUANTUM,()->EMBlocks.DENSE_PERSONAL_BARREL);
    public static final BlockTypeTile<TileEntityTieredPersonalBarrel> DENSE_PERSONAL_BARREL = createPersonalBarrel(()-> EMTileEntityTypes.DENSE_PERSONAL_BARREL,PersonalStorageTier.DENSE,()->EMBlocks.MULTIVERSAL_PERSONAL_BARREL);
    public static final BlockTypeTile<TileEntityTieredPersonalBarrel> MULTIVERSAL_PERSONAL_BARREL = createPersonalBarrel(()-> EMTileEntityTypes.MULTIVERSAL_PERSONAL_BARREL,PersonalStorageTier.MULTIVERSAL,()->EMBlocks.CREATIVE_PERSONAL_BARREL);
    public static final BlockTypeTile<TileEntityTieredPersonalBarrel> CREATIVE_PERSONAL_BARREL = createPersonalBarrel(()-> EMTileEntityTypes.CREATIVE_PERSONAL_BARREL,PersonalStorageTier.CREATIVE,null);

    // Personal Chests
    public static final BlockTypeTile<TileEntityTieredPersonalChest> ADVANCED_PERSONAL_CHEST = createPersonalChest(()-> EMTileEntityTypes.ADVANCED_PERSONAL_CHEST,PersonalStorageTier.ADVANCED,()->EMBlocks.ELITE_PERSONAL_CHEST);
    public static final BlockTypeTile<TileEntityTieredPersonalChest> ELITE_PERSONAL_CHEST = createPersonalChest(()-> EMTileEntityTypes.ELITE_PERSONAL_CHEST,PersonalStorageTier.ELITE,()->EMBlocks.ULTIMATE_PERSONAL_CHEST);
    public static final BlockTypeTile<TileEntityTieredPersonalChest> ULTIMATE_PERSONAL_CHEST = createPersonalChest(()-> EMTileEntityTypes.ULTIMATE_PERSONAL_CHEST,PersonalStorageTier.ULTIMATE,()->EMBlocks.OVERCLOCKED_PERSONAL_CHEST);
    public static final BlockTypeTile<TileEntityTieredPersonalChest> OVERCLOCKED_PERSONAL_CHEST = createPersonalChest(()-> EMTileEntityTypes.OVERCLOCKED_PERSONAL_CHEST,PersonalStorageTier.OVERCLOCKED,()->EMBlocks.QUANTUM_PERSONAL_CHEST);
    public static final BlockTypeTile<TileEntityTieredPersonalChest> QUANTUM_PERSONAL_CHEST = createPersonalChest(()-> EMTileEntityTypes.QUANTUM_PERSONAL_CHEST,PersonalStorageTier.QUANTUM,()->EMBlocks.DENSE_PERSONAL_CHEST);
    public static final BlockTypeTile<TileEntityTieredPersonalChest> DENSE_PERSONAL_CHEST = createPersonalChest(()-> EMTileEntityTypes.DENSE_PERSONAL_CHEST,PersonalStorageTier.DENSE,()->EMBlocks.MULTIVERSAL_PERSONAL_CHEST);
    public static final BlockTypeTile<TileEntityTieredPersonalChest> MULTIVERSAL_PERSONAL_CHEST = createPersonalChest(()-> EMTileEntityTypes.MULTIVERSAL_PERSONAL_CHEST,PersonalStorageTier.MULTIVERSAL,()->EMBlocks.CREATIVE_PERSONAL_CHEST);
    public static final BlockTypeTile<TileEntityTieredPersonalChest> CREATIVE_PERSONAL_CHEST = createPersonalChest(()-> EMTileEntityTypes.CREATIVE_PERSONAL_CHEST,PersonalStorageTier.CREATIVE,null);


    private static BlockTypeTile<TileEntityTieredPersonalBarrel> createPersonalBarrel(Supplier<TileEntityTypeRegistryObject<TileEntityTieredPersonalBarrel>> teType, PersonalStorageTier tier,Supplier<BlockRegistryObject<?, ?>> upgradeBlock){
        return BlockTileBuilder
                .createBlock(teType, MekanismLang.DESCRIPTION_PERSONAL_BARREL)
                .withGui(() -> EMContainerTypes.TIERED_PERSONAL_STORAGE_BLOCK)
                .with(new AttributeTier<>(tier), new AttributeUpgradeable(upgradeBlock))
                .with(Attributes.SECURITY, BlockTieredPersonalStorage.TIERED_PERSONAL_STORAGE_INVENTORY, AttributeStateOpen.INSTANCE, new AttributeStateFacing(BlockStateProperties.FACING), new Attributes.AttributeCustomResistance(-1))
                .withComputerSupport("personalBarrel")
                .build();
    }
    private static BlockTypeTile<TileEntityTieredPersonalChest> createPersonalChest(Supplier<TileEntityTypeRegistryObject<TileEntityTieredPersonalChest>> teType, PersonalStorageTier tier,Supplier<BlockRegistryObject<?, ?>> upgradeBlock){
        return  BlockTileBuilder.createBlock(teType, MekanismLang.DESCRIPTION_PERSONAL_CHEST)
                .withGui(() -> EMContainerTypes.TIERED_PERSONAL_STORAGE_BLOCK)
                .with(new AttributeTier<>(tier), new AttributeUpgradeable(upgradeBlock))
                .with(Attributes.SECURITY, BlockTieredPersonalStorage.TIERED_PERSONAL_STORAGE_INVENTORY, new AttributeStateFacing(), new Attributes.AttributeCustomResistance(-1))
                .withCustomShape(BlockShapes.PERSONAL_CHEST)
                .withComputerSupport("personalChest")
                .build();
    }

    private static <TILE extends TileEntityInductionCell> BlockTypeTile<TILE> createInductionCell(InductionCellTier tier, Supplier<TileEntityTypeRegistryObject<TILE>> tile) {
        return BlockTileBuilder.createBlock(tile, MekanismLang.DESCRIPTION_INDUCTION_CELL)
                .withEnergyConfig(tier::getMaxEnergy)
                .with(new AttributeTier<>(tier))
                .internalMultiblock()
                .build();
    }

    private static <TILE extends TileEntityInductionProvider> BlockTypeTile<TILE> createInductionProvider(InductionProviderTier tier, Supplier<TileEntityTypeRegistryObject<TILE>> tile) {
        return BlockTileBuilder.createBlock(tile, MekanismLang.DESCRIPTION_INDUCTION_PROVIDER)
                .with(new AttributeTier<>(tier))
                .internalMultiblock()
                .build();
    }

    private static <TILE extends TileEntityBin> Machine<TILE> createBin(BinTier tier, Supplier<TileEntityTypeRegistryObject<TILE>> tile, Supplier<BlockRegistryObject<?, ?>> upgradeBlock) {
        return MachineBuilder.createMachine(tile, MekanismLang.DESCRIPTION_BIN)
                .with(new AttributeTier<>(tier), new AttributeUpgradeable(upgradeBlock))
                .without(AttributeParticleFX.class, AttributeSecurity.class, AttributeUpgradeSupport.class, AttributeRedstone.class)
                .withComputerSupport(tier, "Bin")
                .build();
    }

    private static <TILE extends TileEntityEnergyCube> Machine<TILE> createEnergyCube(EnergyCubeTier tier, Supplier<TileEntityTypeRegistryObject<TILE>> tile, Supplier<BlockRegistryObject<?, ?>> upgradeBlock) {
        return MachineBuilder.createMachine(tile, MekanismLang.DESCRIPTION_ENERGY_CUBE)
                .withGui(() -> MekanismContainerTypes.ENERGY_CUBE)
                .withEnergyConfig(tier::getMaxEnergy)
                .with(new AttributeTier<>(tier), new AttributeUpgradeable(upgradeBlock), new AttributeStateFacing(BlockStateProperties.FACING))
                .without(AttributeParticleFX.class, AttributeStateActive.class, AttributeUpgradeSupport.class)
                .withComputerSupport(tier, "EnergyCube")
                .build();
    }

    private static <TILE extends TileEntityFluidTank> Machine<TILE> createFluidTank(FluidTankTier tier, Supplier<TileEntityTypeRegistryObject<TILE>> tile, Supplier<BlockRegistryObject<?, ?>> upgradeBlock) {
        return MachineBuilder.createMachine(tile, MekanismLang.DESCRIPTION_FLUID_TANK)
                .withGui(() -> MekanismContainerTypes.FLUID_TANK)
                .withCustomShape(BlockShapes.FLUID_TANK)
                .with(new AttributeTier<>(tier), new AttributeUpgradeable(upgradeBlock))
                .without(AttributeParticleFX.class, AttributeStateFacing.class, AttributeRedstone.class, AttributeUpgradeSupport.class)
                .withComputerSupport(tier, "FluidTank")
                .build();
    }

    private static <TILE extends TileEntityChemicalTank> Machine<TILE> createChemicalTank(ChemicalTankTier tier, Supplier<TileEntityTypeRegistryObject<TILE>> tile, Supplier<BlockRegistryObject<?, ?>> upgradeBlock) {
        return MachineBuilder.createMachine(tile, MekanismLang.DESCRIPTION_CHEMICAL_TANK)
                .withGui(() -> MekanismContainerTypes.CHEMICAL_TANK)
                .withCustomShape(BlockShapes.CHEMICAL_TANK)
                .with(new AttributeTier<>(tier), new AttributeUpgradeable(upgradeBlock))
                .without(AttributeParticleFX.class, AttributeStateActive.class, AttributeUpgradeSupport.class)
                .withComputerSupport(tier, "ChemicalTank")
                .build();
    }
}
