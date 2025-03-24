package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.tiers.storage.*;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.*;
import mekanism.common.block.attribute.Attributes.AttributeRedstone;
import mekanism.common.block.attribute.Attributes.AttributeSecurity;
import mekanism.common.content.blocktype.BlockShapes;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.BlockTypeTile.BlockTileBuilder;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.content.blocktype.Machine.MachineBuilder;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.registries.MekanismContainerTypes;
import mekanism.common.tier.*;
import mekanism.common.tile.TileEntityBin;
import mekanism.common.tile.TileEntityChemicalTank;
import mekanism.common.tile.TileEntityEnergyCube;
import mekanism.common.tile.TileEntityFluidTank;
import mekanism.common.tile.multiblock.TileEntityInductionCell;
import mekanism.common.tile.multiblock.TileEntityInductionProvider;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.Supplier;

public class EMBlockTypes {





    // Induction Cells
   public static final BlockTypeTile<TileEntityInductionCell> OVERCLOCKED_INDUCTION_CELL = createInductionCell(EMInductionCellTier.OVERCLOCKED, () -> EMBlockEntityTypes.OVERCLOCKED_INDUCTION_CELL);
   public static final BlockTypeTile<TileEntityInductionCell> QUANTUM_INDUCTION_CELL = createInductionCell(EMInductionCellTier.QUANTUM, () -> EMBlockEntityTypes.QUANTUM_INDUCTION_CELL);
   public static final BlockTypeTile<TileEntityInductionCell> DENSE_INDUCTION_CELL = createInductionCell(EMInductionCellTier.DENSE, () -> EMBlockEntityTypes.DENSE_INDUCTION_CELL);
   public static final BlockTypeTile<TileEntityInductionCell> MULTIVERSAL_INDUCTION_CELL = createInductionCell(EMInductionCellTier.MULTIVERSAL, () -> EMBlockEntityTypes.MULTIVERSAL_INDUCTION_CELL);

    // Induction Provider
   public static final BlockTypeTile<TileEntityInductionProvider> OVERCLOCKED_INDUCTION_PROVIDER = createInductionProvider(EMInductionProviderTier.OVERCLOCKED, () -> EMBlockEntityTypes.OVERCLOCKED_INDUCTION_PROVIDER);
   public static final BlockTypeTile<TileEntityInductionProvider> QUANTUM_INDUCTION_PROVIDER = createInductionProvider(EMInductionProviderTier.QUANTUM, () -> EMBlockEntityTypes.QUANTUM_INDUCTION_PROVIDER);
   public static final BlockTypeTile<TileEntityInductionProvider> DENSE_INDUCTION_PROVIDER = createInductionProvider(EMInductionProviderTier.DENSE, () -> EMBlockEntityTypes.DENSE_INDUCTION_PROVIDER);
   public static final BlockTypeTile<TileEntityInductionProvider> MULTIVERSAL_INDUCTION_PROVIDER = createInductionProvider(EMInductionProviderTier.MULTIVERSAL, () -> EMBlockEntityTypes.MULTIVERSAL_INDUCTION_PROVIDER);

    // Bins
    public static final Machine<TileEntityBin> OVERCLOCKED_BIN = createBin(EMBinTier.OVERCLOCKED, () -> EMBlockEntityTypes.OVERCLOCKED_BIN, () -> EMBlocks.QUANTUM_BIN);
    public static final Machine<TileEntityBin> QUANTUM_BIN = createBin(EMBinTier.QUANTUM, () -> EMBlockEntityTypes.QUANTUM_BIN, () -> EMBlocks.DENSE_BIN);
    public static final Machine<TileEntityBin> DENSE_BIN = createBin(EMBinTier.DENSE, () -> EMBlockEntityTypes.DENSE_BIN, () -> EMBlocks.MULTIVERSAL_BIN);
    public static final Machine<TileEntityBin> MULTIVERSAL_BIN = createBin(EMBinTier.MULTIVERSAL, () -> EMBlockEntityTypes.MULTIVERSAL_BIN, null);

    // Energy Cubes
    public static final Machine<TileEntityEnergyCube> OVERCLOCKED_ENERGY_CUBE = createEnergyCube(EMEnergyCubeTier.OVERCLOCKED, () -> EMBlockEntityTypes.OVERCLOCKED_ENERGY_CUBE, () -> EMBlocks.QUANTUM_ENERGY_CUBE);
    public static final Machine<TileEntityEnergyCube> QUANTUM_ENERGY_CUBE = createEnergyCube(EMEnergyCubeTier.QUANTUM, () -> EMBlockEntityTypes.QUANTUM_ENERGY_CUBE, () -> EMBlocks.DENSE_ENERGY_CUBE);
    public static final Machine<TileEntityEnergyCube> DENSE_ENERGY_CUBE = createEnergyCube(EMEnergyCubeTier.DENSE, () -> EMBlockEntityTypes.DENSE_ENERGY_CUBE, () -> EMBlocks.MULTIVERSAL_ENERGY_CUBE);
    public static final Machine<TileEntityEnergyCube> MULTIVERSAL_ENERGY_CUBE = createEnergyCube(EMEnergyCubeTier.MULTIVERSAL, () -> EMBlockEntityTypes.MULTIVERSAL_ENERGY_CUBE, null);

    // Fluid Tanks
    public static final Machine<TileEntityFluidTank> OVERCLOCKED_FLUID_TANK = createFluidTank(EMFluidTankTier.OVERCLOCKED, () -> EMBlockEntityTypes.OVERCLOCKED_FLUID_TANK, () -> EMBlocks.QUANTUM_FLUID_TANK);
    public static final Machine<TileEntityFluidTank> QUANTUM_FLUID_TANK = createFluidTank(EMFluidTankTier.QUANTUM, () -> EMBlockEntityTypes.QUANTUM_FLUID_TANK, () -> EMBlocks.DENSE_FLUID_TANK);
    public static final Machine<TileEntityFluidTank> DENSE_FLUID_TANK = createFluidTank(EMFluidTankTier.DENSE, () -> EMBlockEntityTypes.DENSE_FLUID_TANK, () -> EMBlocks.MULTIVERSAL_FLUID_TANK);
    public static final Machine<TileEntityFluidTank> MULTIVERSAL_FLUID_TANK = createFluidTank(EMFluidTankTier.MULTIVERSAL, () -> EMBlockEntityTypes.MULTIVERSAL_FLUID_TANK, null);

    // Chemical Tanks
    public static final Machine<TileEntityChemicalTank> OVERCLOCKED_CHEMICAL_TANK = createChemicalTank(EMChemicalTankTier.OVERCLOCKED, () -> EMBlockEntityTypes.OVERCLOCKED_CHEMICAL_TANK, () -> EMBlocks.QUANTUM_CHEMICAL_TANK);
    public static final Machine<TileEntityChemicalTank> QUANTUM_CHEMICAL_TANK = createChemicalTank(EMChemicalTankTier.QUANTUM, () -> EMBlockEntityTypes.QUANTUM_CHEMICAL_TANK, () -> EMBlocks.DENSE_CHEMICAL_TANK);
    public static final Machine<TileEntityChemicalTank> DENSE_CHEMICAL_TANK = createChemicalTank(EMChemicalTankTier.DENSE, () -> EMBlockEntityTypes.DENSE_CHEMICAL_TANK, () -> EMBlocks.MULTIVERSAL_CHEMICAL_TANK);
    public static final Machine<TileEntityChemicalTank> MULTIVERSAL_CHEMICAL_TANK = createChemicalTank(EMChemicalTankTier.MULTIVERSAL, () -> EMBlockEntityTypes.MULTIVERSAL_CHEMICAL_TANK, null);


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
