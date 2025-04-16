package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTCasing;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTPort;
import fr.iglee42.evolvedmekanism.tiers.PersonalStorageTier;
import fr.iglee42.evolvedmekanism.tiles.TileEntityTieredPersonalBarrel;
import fr.iglee42.evolvedmekanism.tiles.TileEntityTieredPersonalChest;
import fr.iglee42.evolvedmekanism.tiles.TileEntityTieredPersonalStorage;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityAlloyer;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityChemixer;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tile.*;
import mekanism.common.tile.multiblock.TileEntityInductionCell;
import mekanism.common.tile.multiblock.TileEntityInductionProvider;
import mekanism.common.tile.multiblock.TileEntitySPSCasing;
import mekanism.common.tile.multiblock.TileEntitySPSPort;
import mekanism.common.tile.transmitter.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class EMTileEntityTypes {

    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(EvolvedMekanism.MODID);

    public static final TileEntityTypeRegistryObject<TileEntityAPTCasing> APT_CASING = TILE_ENTITY_TYPES.register(EMBlocks.APT_CASING, TileEntityAPTCasing::new);
    public static final TileEntityTypeRegistryObject<TileEntityAPTPort> APT_PORT = TILE_ENTITY_TYPES.register(EMBlocks.APT_PORT, TileEntityAPTPort::new);


    //Logistic Transporters
    public static final TileEntityTypeRegistryObject<TileEntityLogisticalTransporter> OVERCLOCKED_LOGISTICAL_TRANSPORTER = TILE_ENTITY_TYPES.builder(EMBlocks.OVERCLOCKED_LOGISTICAL_TRANSPORTER, (pos, state) -> new TileEntityLogisticalTransporter(EMBlocks.OVERCLOCKED_LOGISTICAL_TRANSPORTER, pos, state)).clientTicker(TileEntityLogisticalTransporterBase::tickClient).serverTicker(TileEntityTransmitter::tickServer).build();
    public static final TileEntityTypeRegistryObject<TileEntityLogisticalTransporter> QUANTUM_LOGISTICAL_TRANSPORTER = TILE_ENTITY_TYPES.builder(EMBlocks.QUANTUM_LOGISTICAL_TRANSPORTER, (pos, state) -> new TileEntityLogisticalTransporter(EMBlocks.QUANTUM_LOGISTICAL_TRANSPORTER, pos, state)).clientTicker(TileEntityLogisticalTransporterBase::tickClient).serverTicker(TileEntityTransmitter::tickServer).build();
    public static final TileEntityTypeRegistryObject<TileEntityLogisticalTransporter> DENSE_LOGISTICAL_TRANSPORTER = TILE_ENTITY_TYPES.builder(EMBlocks.DENSE_LOGISTICAL_TRANSPORTER, (pos, state) -> new TileEntityLogisticalTransporter(EMBlocks.DENSE_LOGISTICAL_TRANSPORTER, pos, state)).clientTicker(TileEntityLogisticalTransporterBase::tickClient).serverTicker(TileEntityTransmitter::tickServer).build();
    public static final TileEntityTypeRegistryObject<TileEntityLogisticalTransporter> MULTIVERSAL_LOGISTICAL_TRANSPORTER = TILE_ENTITY_TYPES.builder(EMBlocks.MULTIVERSAL_LOGISTICAL_TRANSPORTER, (pos, state) -> new TileEntityLogisticalTransporter(EMBlocks.MULTIVERSAL_LOGISTICAL_TRANSPORTER, pos, state)).clientTicker(TileEntityLogisticalTransporterBase::tickClient).serverTicker(TileEntityTransmitter::tickServer).build();
    public static final TileEntityTypeRegistryObject<TileEntityLogisticalTransporter> CREATIVE_LOGISTICAL_TRANSPORTER = TILE_ENTITY_TYPES.builder(EMBlocks.CREATIVE_LOGISTICAL_TRANSPORTER, (pos, state) -> new TileEntityLogisticalTransporter(EMBlocks.CREATIVE_LOGISTICAL_TRANSPORTER, pos, state)).clientTicker(TileEntityLogisticalTransporterBase::tickClient).serverTicker(TileEntityTransmitter::tickServer).build();
    //Mechanical Pipes
    public static final TileEntityTypeRegistryObject<TileEntityMechanicalPipe> OVERCLOCKED_MECHANICAL_PIPE = registerTransmitter(EMBlocks.OVERCLOCKED_MECHANICAL_PIPE, (pos, state) -> new TileEntityMechanicalPipe(EMBlocks.OVERCLOCKED_MECHANICAL_PIPE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityMechanicalPipe> QUANTUM_MECHANICAL_PIPE = registerTransmitter(EMBlocks.QUANTUM_MECHANICAL_PIPE, (pos, state) -> new TileEntityMechanicalPipe(EMBlocks.QUANTUM_MECHANICAL_PIPE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityMechanicalPipe> DENSE_MECHANICAL_PIPE = registerTransmitter(EMBlocks.DENSE_MECHANICAL_PIPE, (pos, state) -> new TileEntityMechanicalPipe(EMBlocks.DENSE_MECHANICAL_PIPE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityMechanicalPipe> MULTIVERSAL_MECHANICAL_PIPE = registerTransmitter(EMBlocks.MULTIVERSAL_MECHANICAL_PIPE, (pos, state) -> new TileEntityMechanicalPipe(EMBlocks.MULTIVERSAL_MECHANICAL_PIPE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityMechanicalPipe> CREATIVE_MECHANICAL_PIPE = registerTransmitter(EMBlocks.CREATIVE_MECHANICAL_PIPE, (pos, state) -> new TileEntityMechanicalPipe(EMBlocks.CREATIVE_MECHANICAL_PIPE, pos, state));
    //Pressurized Tubes
    public static final TileEntityTypeRegistryObject<TileEntityPressurizedTube> OVERCLOCKED_PRESSURIZED_TUBE = registerTransmitter(EMBlocks.OVERCLOCKED_PRESSURIZED_TUBE, (pos, state) -> new TileEntityPressurizedTube(EMBlocks.OVERCLOCKED_PRESSURIZED_TUBE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityPressurizedTube> QUANTUM_PRESSURIZED_TUBE = registerTransmitter(EMBlocks.QUANTUM_PRESSURIZED_TUBE, (pos, state) -> new TileEntityPressurizedTube(EMBlocks.QUANTUM_PRESSURIZED_TUBE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityPressurizedTube> DENSE_PRESSURIZED_TUBE = registerTransmitter(EMBlocks.DENSE_PRESSURIZED_TUBE, (pos, state) -> new TileEntityPressurizedTube(EMBlocks.DENSE_PRESSURIZED_TUBE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityPressurizedTube> MULTIVERSAL_PRESSURIZED_TUBE = registerTransmitter(EMBlocks.MULTIVERSAL_PRESSURIZED_TUBE, (pos, state) -> new TileEntityPressurizedTube(EMBlocks.MULTIVERSAL_PRESSURIZED_TUBE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityPressurizedTube> CREATIVE_PRESSURIZED_TUBE = registerTransmitter(EMBlocks.CREATIVE_PRESSURIZED_TUBE, (pos, state) -> new TileEntityPressurizedTube(EMBlocks.CREATIVE_PRESSURIZED_TUBE, pos, state));
    //Thermodynamic Conductors
    public static final TileEntityTypeRegistryObject<TileEntityThermodynamicConductor> OVERCLOCKED_THERMODYNAMIC_CONDUCTOR = registerTransmitter(EMBlocks.OVERCLOCKED_THERMODYNAMIC_CONDUCTOR, (pos, state) -> new TileEntityThermodynamicConductor(EMBlocks.OVERCLOCKED_THERMODYNAMIC_CONDUCTOR, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityThermodynamicConductor> QUANTUM_THERMODYNAMIC_CONDUCTOR = registerTransmitter(EMBlocks.QUANTUM_THERMODYNAMIC_CONDUCTOR, (pos, state) -> new TileEntityThermodynamicConductor(EMBlocks.QUANTUM_THERMODYNAMIC_CONDUCTOR, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityThermodynamicConductor> DENSE_THERMODYNAMIC_CONDUCTOR = registerTransmitter(EMBlocks.DENSE_THERMODYNAMIC_CONDUCTOR, (pos, state) -> new TileEntityThermodynamicConductor(EMBlocks.DENSE_THERMODYNAMIC_CONDUCTOR, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityThermodynamicConductor> MULTIVERSAL_THERMODYNAMIC_CONDUCTOR = registerTransmitter(EMBlocks.MULTIVERSAL_THERMODYNAMIC_CONDUCTOR, (pos, state) -> new TileEntityThermodynamicConductor(EMBlocks.MULTIVERSAL_THERMODYNAMIC_CONDUCTOR, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityThermodynamicConductor> CREATIVE_THERMODYNAMIC_CONDUCTOR = registerTransmitter(EMBlocks.CREATIVE_THERMODYNAMIC_CONDUCTOR, (pos, state) -> new TileEntityThermodynamicConductor(EMBlocks.CREATIVE_THERMODYNAMIC_CONDUCTOR, pos, state));
    //Universal Cables
    public static final TileEntityTypeRegistryObject<TileEntityUniversalCable> OVERCLOCKED_UNIVERSAL_CABLE = registerTransmitter(EMBlocks.OVERCLOCKED_UNIVERSAL_CABLE, (pos, state) -> new TileEntityUniversalCable(EMBlocks.OVERCLOCKED_UNIVERSAL_CABLE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityUniversalCable> QUANTUM_UNIVERSAL_CABLE = registerTransmitter(EMBlocks.QUANTUM_UNIVERSAL_CABLE, (pos, state) -> new TileEntityUniversalCable(EMBlocks.QUANTUM_UNIVERSAL_CABLE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityUniversalCable> DENSE_UNIVERSAL_CABLE = registerTransmitter(EMBlocks.DENSE_UNIVERSAL_CABLE, (pos, state) -> new TileEntityUniversalCable(EMBlocks.DENSE_UNIVERSAL_CABLE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityUniversalCable> MULTIVERSAL_UNIVERSAL_CABLE = registerTransmitter(EMBlocks.MULTIVERSAL_UNIVERSAL_CABLE, (pos, state) -> new TileEntityUniversalCable(EMBlocks.MULTIVERSAL_UNIVERSAL_CABLE, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityUniversalCable> CREATIVE_UNIVERSAL_CABLE = registerTransmitter(EMBlocks.CREATIVE_UNIVERSAL_CABLE, (pos, state) -> new TileEntityUniversalCable(EMBlocks.CREATIVE_UNIVERSAL_CABLE, pos, state));

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
    public static final TileEntityTypeRegistryObject<TileEntityInductionCell> CREATIVE_INDUCTION_CELL = TILE_ENTITY_TYPES.register(EMBlocks.CREATIVE_INDUCTION_CELL, (pos, state) -> new TileEntityInductionCell(EMBlocks.CREATIVE_INDUCTION_CELL, pos, state));
    //Induction Providers
    public static final TileEntityTypeRegistryObject<TileEntityInductionProvider> OVERCLOCKED_INDUCTION_PROVIDER = TILE_ENTITY_TYPES.register(EMBlocks.OVERCLOCKED_INDUCTION_PROVIDER, (pos, state) -> new TileEntityInductionProvider(EMBlocks.OVERCLOCKED_INDUCTION_PROVIDER, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityInductionProvider> QUANTUM_INDUCTION_PROVIDER = TILE_ENTITY_TYPES.register(EMBlocks.QUANTUM_INDUCTION_PROVIDER, (pos, state) -> new TileEntityInductionProvider(EMBlocks.QUANTUM_INDUCTION_PROVIDER, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityInductionProvider> DENSE_INDUCTION_PROVIDER = TILE_ENTITY_TYPES.register(EMBlocks.DENSE_INDUCTION_PROVIDER, (pos, state) -> new TileEntityInductionProvider(EMBlocks.DENSE_INDUCTION_PROVIDER, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityInductionProvider> MULTIVERSAL_INDUCTION_PROVIDER = TILE_ENTITY_TYPES.register(EMBlocks.MULTIVERSAL_INDUCTION_PROVIDER, (pos, state) -> new TileEntityInductionProvider(EMBlocks.MULTIVERSAL_INDUCTION_PROVIDER, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityInductionProvider> CREATIVE_INDUCTION_PROVIDER = TILE_ENTITY_TYPES.register(EMBlocks.CREATIVE_INDUCTION_PROVIDER, (pos, state) -> new TileEntityInductionProvider(EMBlocks.CREATIVE_INDUCTION_PROVIDER, pos, state));

    public static final TileEntityTypeRegistryObject<TileEntityTieredPersonalBarrel> ADVANCED_PERSONAL_BARREL = registerTieredStorage(EMBlocks.ADVANCED_PERSONAL_BARREL,PersonalStorageTier.ADVANCED, TileEntityTieredPersonalBarrel::new);
    public static final TileEntityTypeRegistryObject<TileEntityTieredPersonalBarrel> ELITE_PERSONAL_BARREL = registerTieredStorage(EMBlocks.ELITE_PERSONAL_BARREL,PersonalStorageTier.ELITE, TileEntityTieredPersonalBarrel::new);
    public static final TileEntityTypeRegistryObject<TileEntityTieredPersonalBarrel> ULTIMATE_PERSONAL_BARREL = registerTieredStorage(EMBlocks.ULTIMATE_PERSONAL_BARREL,PersonalStorageTier.ULTIMATE, TileEntityTieredPersonalBarrel::new);
    public static final TileEntityTypeRegistryObject<TileEntityTieredPersonalBarrel> OVERCLOCKED_PERSONAL_BARREL = registerTieredStorage(EMBlocks.OVERCLOCKED_PERSONAL_BARREL,PersonalStorageTier.OVERCLOCKED, TileEntityTieredPersonalBarrel::new);
    public static final TileEntityTypeRegistryObject<TileEntityTieredPersonalBarrel> QUANTUM_PERSONAL_BARREL = registerTieredStorage(EMBlocks.QUANTUM_PERSONAL_BARREL,PersonalStorageTier.QUANTUM, TileEntityTieredPersonalBarrel::new);
    public static final TileEntityTypeRegistryObject<TileEntityTieredPersonalBarrel> DENSE_PERSONAL_BARREL = registerTieredStorage(EMBlocks.DENSE_PERSONAL_BARREL,PersonalStorageTier.DENSE, TileEntityTieredPersonalBarrel::new);
    public static final TileEntityTypeRegistryObject<TileEntityTieredPersonalBarrel> MULTIVERSAL_PERSONAL_BARREL = registerTieredStorage(EMBlocks.MULTIVERSAL_PERSONAL_BARREL,PersonalStorageTier.MULTIVERSAL, TileEntityTieredPersonalBarrel::new);
    public static final TileEntityTypeRegistryObject<TileEntityTieredPersonalBarrel> CREATIVE_PERSONAL_BARREL = registerTieredStorage(EMBlocks.CREATIVE_PERSONAL_BARREL,PersonalStorageTier.CREATIVE, TileEntityTieredPersonalBarrel::new);

    public static final TileEntityTypeRegistryObject<TileEntityTieredPersonalChest> ADVANCED_PERSONAL_CHEST = registerTieredStorage(EMBlocks.ADVANCED_PERSONAL_CHEST, PersonalStorageTier.ADVANCED, TileEntityTieredPersonalChest::new);
    public static final TileEntityTypeRegistryObject<TileEntityTieredPersonalChest> ELITE_PERSONAL_CHEST = registerTieredStorage(EMBlocks.ELITE_PERSONAL_CHEST, PersonalStorageTier.ELITE, TileEntityTieredPersonalChest::new);
    public static final TileEntityTypeRegistryObject<TileEntityTieredPersonalChest> ULTIMATE_PERSONAL_CHEST = registerTieredStorage(EMBlocks.ULTIMATE_PERSONAL_CHEST, PersonalStorageTier.ULTIMATE, TileEntityTieredPersonalChest::new);
    public static final TileEntityTypeRegistryObject<TileEntityTieredPersonalChest> OVERCLOCKED_PERSONAL_CHEST = registerTieredStorage(EMBlocks.OVERCLOCKED_PERSONAL_CHEST, PersonalStorageTier.OVERCLOCKED, TileEntityTieredPersonalChest::new);
    public static final TileEntityTypeRegistryObject<TileEntityTieredPersonalChest> QUANTUM_PERSONAL_CHEST = registerTieredStorage(EMBlocks.QUANTUM_PERSONAL_CHEST, PersonalStorageTier.QUANTUM, TileEntityTieredPersonalChest::new);
    public static final TileEntityTypeRegistryObject<TileEntityTieredPersonalChest> DENSE_PERSONAL_CHEST = registerTieredStorage(EMBlocks.DENSE_PERSONAL_CHEST, PersonalStorageTier.DENSE, TileEntityTieredPersonalChest::new);
    public static final TileEntityTypeRegistryObject<TileEntityTieredPersonalChest> MULTIVERSAL_PERSONAL_CHEST = registerTieredStorage(EMBlocks.MULTIVERSAL_PERSONAL_CHEST, PersonalStorageTier.MULTIVERSAL, TileEntityTieredPersonalChest::new);
    public static final TileEntityTypeRegistryObject<TileEntityTieredPersonalChest> CREATIVE_PERSONAL_CHEST = registerTieredStorage(EMBlocks.CREATIVE_PERSONAL_CHEST, PersonalStorageTier.CREATIVE, TileEntityTieredPersonalChest::new);

    public static final TileEntityTypeRegistryObject<TileEntityAlloyer> ALLOYER = TILE_ENTITY_TYPES.register(EMBlocks.ALLOYER, TileEntityAlloyer::new);
    public static final TileEntityTypeRegistryObject<TileEntityChemixer> CHEMIXER = TILE_ENTITY_TYPES.register(EMBlocks.CHEMIXER, TileEntityChemixer::new);


    private static <BE extends TileEntityTransmitter> TileEntityTypeRegistryObject<BE> registerTransmitter(BlockRegistryObject<?, ?> block,
                                                                                                           BlockEntityType.BlockEntitySupplier<? extends BE> factory) {
        //Note: There is no data fixer type as forge does not currently have a way exposing data fixers to mods yet
        return TILE_ENTITY_TYPES.<BE>builder(block, factory).serverTicker(TileEntityTransmitter::tickServer).build();
    }

    private static <BE extends TileEntityTieredPersonalStorage> TileEntityTypeRegistryObject<BE> registerTieredStorage(BlockRegistryObject<?, ?> block, PersonalStorageTier tier,
                                                                                                                       BlockEntitySupplier<? extends BE> factory){
        return TILE_ENTITY_TYPES.register(block,(pos,state)->factory.create(block,pos,state,tier));
    }

    @FunctionalInterface
    public interface BlockEntitySupplier<T extends BlockEntity> {
        T create(IBlockProvider block, BlockPos pos, BlockState state, PersonalStorageTier tier);
    }
}
