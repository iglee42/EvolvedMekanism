package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.TileEntityBin;
import mekanism.common.tile.TileEntityChemicalTank;
import mekanism.common.tile.TileEntityEnergyCube;
import mekanism.common.tile.TileEntityFluidTank;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.multiblock.TileEntityInductionCell;
import mekanism.common.tile.multiblock.TileEntityInductionProvider;
import mekanism.common.tile.transmitter.*;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class EMBlockEntityTypes {

    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(EvolvedMekanism.MODID);

    //Logistic Transporters
    public static final TileEntityTypeRegistryObject<TileEntityLogisticalTransporter> OVERCLOCKED_LOGISTICAL_TRANSPORTER = TILE_ENTITY_TYPES.builder(EMBlocks.OVERCLOCKED_LOGISTICAL_TRANSPORTER, (pos, state) -> new TileEntityLogisticalTransporter(EMBlocks.OVERCLOCKED_LOGISTICAL_TRANSPORTER, pos, state)).clientTicker(TileEntityLogisticalTransporterBase::tickClient).serverTicker(TileEntityTransmitter::tickServer).build();
    public static final TileEntityTypeRegistryObject<TileEntityLogisticalTransporter> QUANTUM_LOGISTICAL_TRANSPORTER = TILE_ENTITY_TYPES.builder(EMBlocks.QUANTUM_LOGISTICAL_TRANSPORTER, (pos, state) -> new TileEntityLogisticalTransporter(EMBlocks.QUANTUM_LOGISTICAL_TRANSPORTER, pos, state)).clientTicker(TileEntityLogisticalTransporterBase::tickClient).serverTicker(TileEntityTransmitter::tickServer).build();
    public static final TileEntityTypeRegistryObject<TileEntityLogisticalTransporter> DENSE_LOGISTICAL_TRANSPORTER = TILE_ENTITY_TYPES.builder(EMBlocks.DENSE_LOGISTICAL_TRANSPORTER, (pos, state) -> new TileEntityLogisticalTransporter(EMBlocks.DENSE_LOGISTICAL_TRANSPORTER, pos, state)).clientTicker(TileEntityLogisticalTransporterBase::tickClient).serverTicker(TileEntityTransmitter::tickServer).build();
    public static final TileEntityTypeRegistryObject<TileEntityLogisticalTransporter> MULTIVERSAL_LOGISTICAL_TRANSPORTER = TILE_ENTITY_TYPES.builder(EMBlocks.MULTIVERSAL_LOGISTICAL_TRANSPORTER, (pos, state) -> new TileEntityLogisticalTransporter(EMBlocks.MULTIVERSAL_LOGISTICAL_TRANSPORTER, pos, state)).clientTicker(TileEntityLogisticalTransporterBase::tickClient).serverTicker(TileEntityTransmitter::tickServer).build();
    //Mechanical Pipes
    public static final TileEntityTypeRegistryObject<TileEntityMechanicalPipe> OVERCLOCKED_MECHANICAL_PIPE = registerTransmitter(EMBlocks.OVERCLOCKED_MECHANICAL_PIPE, (pos, state) -> new TileEntityMechanicalPipe(EMBlocks.OVERCLOCKED_MECHANICAL_PIPE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityMechanicalPipe> QUANTUM_MECHANICAL_PIPE = registerTransmitter(EMBlocks.QUANTUM_MECHANICAL_PIPE, (pos, state) -> new TileEntityMechanicalPipe(EMBlocks.QUANTUM_MECHANICAL_PIPE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityMechanicalPipe> DENSE_MECHANICAL_PIPE = registerTransmitter(EMBlocks.DENSE_MECHANICAL_PIPE, (pos, state) -> new TileEntityMechanicalPipe(EMBlocks.DENSE_MECHANICAL_PIPE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityMechanicalPipe> MULTIVERSAL_MECHANICAL_PIPE = registerTransmitter(EMBlocks.MULTIVERSAL_MECHANICAL_PIPE, (pos, state) -> new TileEntityMechanicalPipe(EMBlocks.MULTIVERSAL_MECHANICAL_PIPE, pos, state));
    //Pressurized Tubes
    public static final TileEntityTypeRegistryObject<TileEntityPressurizedTube> OVERCLOCKED_PRESSURIZED_TUBE = registerTransmitter(EMBlocks.OVERCLOCKED_PRESSURIZED_TUBE, (pos, state) -> new TileEntityPressurizedTube(EMBlocks.OVERCLOCKED_PRESSURIZED_TUBE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityPressurizedTube> QUANTUM_PRESSURIZED_TUBE = registerTransmitter(EMBlocks.QUANTUM_PRESSURIZED_TUBE, (pos, state) -> new TileEntityPressurizedTube(EMBlocks.QUANTUM_PRESSURIZED_TUBE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityPressurizedTube> DENSE_PRESSURIZED_TUBE = registerTransmitter(EMBlocks.DENSE_PRESSURIZED_TUBE, (pos, state) -> new TileEntityPressurizedTube(EMBlocks.DENSE_PRESSURIZED_TUBE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityPressurizedTube> MULTIVERSAL_PRESSURIZED_TUBE = registerTransmitter(EMBlocks.MULTIVERSAL_PRESSURIZED_TUBE, (pos, state) -> new TileEntityPressurizedTube(EMBlocks.MULTIVERSAL_PRESSURIZED_TUBE, pos, state));
    //Thermodynamic Conductors
    public static final TileEntityTypeRegistryObject<TileEntityThermodynamicConductor> OVERCLOCKED_THERMODYNAMIC_CONDUCTOR = registerTransmitter(EMBlocks.OVERCLOCKED_THERMODYNAMIC_CONDUCTOR, (pos, state) -> new TileEntityThermodynamicConductor(EMBlocks.OVERCLOCKED_THERMODYNAMIC_CONDUCTOR, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityThermodynamicConductor> QUANTUM_THERMODYNAMIC_CONDUCTOR = registerTransmitter(EMBlocks.QUANTUM_THERMODYNAMIC_CONDUCTOR, (pos, state) -> new TileEntityThermodynamicConductor(EMBlocks.QUANTUM_THERMODYNAMIC_CONDUCTOR, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityThermodynamicConductor> DENSE_THERMODYNAMIC_CONDUCTOR = registerTransmitter(EMBlocks.DENSE_THERMODYNAMIC_CONDUCTOR, (pos, state) -> new TileEntityThermodynamicConductor(EMBlocks.DENSE_THERMODYNAMIC_CONDUCTOR, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityThermodynamicConductor> MULTIVERSAL_THERMODYNAMIC_CONDUCTOR = registerTransmitter(EMBlocks.MULTIVERSAL_THERMODYNAMIC_CONDUCTOR, (pos, state) -> new TileEntityThermodynamicConductor(EMBlocks.MULTIVERSAL_THERMODYNAMIC_CONDUCTOR, pos, state));
    //Universal Cables
    public static final TileEntityTypeRegistryObject<TileEntityUniversalCable> OVERCLOCKED_UNIVERSAL_CABLE = registerTransmitter(EMBlocks.OVERCLOCKED_UNIVERSAL_CABLE, (pos, state) -> new TileEntityUniversalCable(EMBlocks.OVERCLOCKED_UNIVERSAL_CABLE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityUniversalCable> QUANTUM_UNIVERSAL_CABLE = registerTransmitter(EMBlocks.QUANTUM_UNIVERSAL_CABLE, (pos, state) -> new TileEntityUniversalCable(EMBlocks.QUANTUM_UNIVERSAL_CABLE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityUniversalCable> DENSE_UNIVERSAL_CABLE = registerTransmitter(EMBlocks.DENSE_UNIVERSAL_CABLE, (pos, state) -> new TileEntityUniversalCable(EMBlocks.DENSE_UNIVERSAL_CABLE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityUniversalCable> MULTIVERSAL_UNIVERSAL_CABLE = registerTransmitter(EMBlocks.MULTIVERSAL_UNIVERSAL_CABLE, (pos, state) -> new TileEntityUniversalCable(EMBlocks.MULTIVERSAL_UNIVERSAL_CABLE, pos, state));

    //Tiered Tiles
    //Energy Cubes
    public static final TileEntityTypeRegistryObject<TileEntityEnergyCube> OVERCLOCKED_ENERGY_CUBE = TILE_ENTITY_TYPES.register(EMBlocks.OVERCLOCKED_ENERGY_CUBE, (pos, state) -> new TileEntityEnergyCube(EMBlocks.OVERCLOCKED_ENERGY_CUBE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityEnergyCube> QUANTUM_ENERGY_CUBE = TILE_ENTITY_TYPES.register(EMBlocks.QUANTUM_ENERGY_CUBE, (pos, state) -> new TileEntityEnergyCube(EMBlocks.QUANTUM_ENERGY_CUBE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityEnergyCube> DENSE_ENERGY_CUBE = TILE_ENTITY_TYPES.register(EMBlocks.DENSE_ENERGY_CUBE, (pos, state) -> new TileEntityEnergyCube(EMBlocks.DENSE_ENERGY_CUBE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityEnergyCube> MULTIVERSAL_ENERGY_CUBE = TILE_ENTITY_TYPES.register(EMBlocks.MULTIVERSAL_ENERGY_CUBE, (pos, state) -> new TileEntityEnergyCube(EMBlocks.MULTIVERSAL_ENERGY_CUBE, pos, state));
    //Chemical Tanks
    public static final TileEntityTypeRegistryObject<TileEntityChemicalTank> OVERCLOCKED_CHEMICAL_TANK = TILE_ENTITY_TYPES.register(EMBlocks.OVERCLOCKED_CHEMICAL_TANK, (pos, state) -> new TileEntityChemicalTank(EMBlocks.OVERCLOCKED_CHEMICAL_TANK, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityChemicalTank> QUANTUM_CHEMICAL_TANK = TILE_ENTITY_TYPES.register(EMBlocks.QUANTUM_CHEMICAL_TANK, (pos, state) -> new TileEntityChemicalTank(EMBlocks.QUANTUM_CHEMICAL_TANK, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityChemicalTank> DENSE_CHEMICAL_TANK = TILE_ENTITY_TYPES.register(EMBlocks.DENSE_CHEMICAL_TANK, (pos, state) -> new TileEntityChemicalTank(EMBlocks.DENSE_CHEMICAL_TANK, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityChemicalTank> MULTIVERSAL_CHEMICAL_TANK = TILE_ENTITY_TYPES.register(EMBlocks.MULTIVERSAL_CHEMICAL_TANK, (pos, state) -> new TileEntityChemicalTank(EMBlocks.MULTIVERSAL_CHEMICAL_TANK, pos, state));
    //Fluid Tanks
    public static final TileEntityTypeRegistryObject<TileEntityFluidTank> OVERCLOCKED_FLUID_TANK = TILE_ENTITY_TYPES.register(EMBlocks.OVERCLOCKED_FLUID_TANK, (pos, state) -> new TileEntityFluidTank(EMBlocks.OVERCLOCKED_FLUID_TANK, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityFluidTank> QUANTUM_FLUID_TANK = TILE_ENTITY_TYPES.register(EMBlocks.QUANTUM_FLUID_TANK, (pos, state) -> new TileEntityFluidTank(EMBlocks.QUANTUM_FLUID_TANK, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityFluidTank> DENSE_FLUID_TANK = TILE_ENTITY_TYPES.register(EMBlocks.DENSE_FLUID_TANK, (pos, state) -> new TileEntityFluidTank(EMBlocks.DENSE_FLUID_TANK, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityFluidTank> MULTIVERSAL_FLUID_TANK = TILE_ENTITY_TYPES.register(EMBlocks.MULTIVERSAL_FLUID_TANK, (pos, state) -> new TileEntityFluidTank(EMBlocks.MULTIVERSAL_FLUID_TANK, pos, state));
    //Bins
    public static final TileEntityTypeRegistryObject<TileEntityBin> OVERCLOCKED_BIN = TILE_ENTITY_TYPES.register(EMBlocks.OVERCLOCKED_BIN, (pos, state) -> new TileEntityBin(EMBlocks.OVERCLOCKED_BIN, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityBin> QUANTUM_BIN = TILE_ENTITY_TYPES.register(EMBlocks.QUANTUM_BIN, (pos, state) -> new TileEntityBin(EMBlocks.QUANTUM_BIN, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityBin> DENSE_BIN = TILE_ENTITY_TYPES.register(EMBlocks.DENSE_BIN, (pos, state) -> new TileEntityBin(EMBlocks.DENSE_BIN, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityBin> MULTIVERSAL_BIN = TILE_ENTITY_TYPES.register(EMBlocks.MULTIVERSAL_BIN, (pos, state) -> new TileEntityBin(EMBlocks.MULTIVERSAL_BIN, pos, state));
    //Induction Cells
    public static final TileEntityTypeRegistryObject<TileEntityInductionCell> OVERCLOCKED_INDUCTION_CELL = TILE_ENTITY_TYPES.register(EMBlocks.OVERCLOCKED_INDUCTION_CELL, (pos, state) -> new TileEntityInductionCell(EMBlocks.OVERCLOCKED_INDUCTION_CELL, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityInductionCell> QUANTUM_INDUCTION_CELL = TILE_ENTITY_TYPES.register(EMBlocks.QUANTUM_INDUCTION_CELL, (pos, state) -> new TileEntityInductionCell(EMBlocks.QUANTUM_INDUCTION_CELL, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityInductionCell> DENSE_INDUCTION_CELL = TILE_ENTITY_TYPES.register(EMBlocks.DENSE_INDUCTION_CELL, (pos, state) -> new TileEntityInductionCell(EMBlocks.DENSE_INDUCTION_CELL, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityInductionCell> MULTIVERSAL_INDUCTION_CELL = TILE_ENTITY_TYPES.register(EMBlocks.MULTIVERSAL_INDUCTION_CELL, (pos, state) -> new TileEntityInductionCell(EMBlocks.MULTIVERSAL_INDUCTION_CELL, pos, state));
    //Induction Providers
    public static final TileEntityTypeRegistryObject<TileEntityInductionProvider> OVERCLOCKED_INDUCTION_PROVIDER = TILE_ENTITY_TYPES.register(EMBlocks.OVERCLOCKED_INDUCTION_PROVIDER, (pos, state) -> new TileEntityInductionProvider(EMBlocks.OVERCLOCKED_INDUCTION_PROVIDER, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityInductionProvider> QUANTUM_INDUCTION_PROVIDER = TILE_ENTITY_TYPES.register(EMBlocks.QUANTUM_INDUCTION_PROVIDER, (pos, state) -> new TileEntityInductionProvider(EMBlocks.QUANTUM_INDUCTION_PROVIDER, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityInductionProvider> DENSE_INDUCTION_PROVIDER = TILE_ENTITY_TYPES.register(EMBlocks.DENSE_INDUCTION_PROVIDER, (pos, state) -> new TileEntityInductionProvider(EMBlocks.DENSE_INDUCTION_PROVIDER, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityInductionProvider> MULTIVERSAL_INDUCTION_PROVIDER = TILE_ENTITY_TYPES.register(EMBlocks.MULTIVERSAL_INDUCTION_PROVIDER, (pos, state) -> new TileEntityInductionProvider(EMBlocks.MULTIVERSAL_INDUCTION_PROVIDER, pos, state));


    private static <BE extends TileEntityTransmitter> TileEntityTypeRegistryObject<BE> registerTransmitter(BlockRegistryObject<?, ?> block,
                                                                                                           BlockEntityType.BlockEntitySupplier<? extends BE> factory) {
        //Note: There is no data fixer type as forge does not currently have a way exposing data fixers to mods yet
        return TILE_ENTITY_TYPES.<BE>builder(block, factory).serverTicker(TileEntityTransmitter::tickServer).build();
    }
}
