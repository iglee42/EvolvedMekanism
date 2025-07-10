package fr.iglee42.evolvedmekanism.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTCasing;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTPort;
import fr.iglee42.evolvedmekanism.tiers.EMFactoryTier;
import fr.iglee42.evolvedmekanism.tiers.PersonalStorageTier;
import fr.iglee42.evolvedmekanism.tiles.TileEntitySuperchargingElement;
import fr.iglee42.evolvedmekanism.tiles.TileEntityTieredPersonalBarrel;
import fr.iglee42.evolvedmekanism.tiles.TileEntityTieredPersonalChest;
import fr.iglee42.evolvedmekanism.tiles.TileEntityTieredPersonalStorage;
import fr.iglee42.evolvedmekanism.tiles.factory.TileEntityAlloyingFactory;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityAlloyer;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityChemixer;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityMelter;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntitySolidifier;
import mekanism.common.block.BlockEnergyCube;
import mekanism.common.block.basic.BlockBin;
import mekanism.common.block.basic.BlockFluidTank;
import mekanism.common.block.prefab.BlockFactoryMachine;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.content.blocktype.FactoryType;
import mekanism.common.item.block.ItemBlockChemicalTank;
import mekanism.common.item.block.machine.ItemBlockFactory;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.registries.MekanismTileEntityTypes;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.TileEntityBin;
import mekanism.common.tile.TileEntityChemicalTank;
import mekanism.common.tile.TileEntityEnergyCube;
import mekanism.common.tile.TileEntityFluidTank;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.factory.*;
import mekanism.common.tile.multiblock.TileEntityInductionCell;
import mekanism.common.tile.multiblock.TileEntityInductionProvider;
import mekanism.common.tile.transmitter.*;
import mekanism.common.util.EnumUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EMTileEntityTypes {

    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(EvolvedMekanism.MODID);

    private static final Table<FactoryTier, FactoryType, TileEntityTypeRegistryObject<? extends TileEntityFactory<?>>> FACTORIES = HashBasedTable.create();

    static {
        for (FactoryTier tier : Arrays.asList(EMFactoryTier.OVERCLOCKED,EMFactoryTier.QUANTUM,EMFactoryTier.DENSE,EMFactoryTier.MULTIVERSAL,EMFactoryTier.CREATIVE)) {
            registerFactory(tier, FactoryType.COMBINING, TileEntityCombiningFactory::new);
            registerFactory(tier, FactoryType.COMPRESSING, TileEntityItemStackChemicalToItemStackFactory::new);
            registerFactory(tier, FactoryType.CRUSHING, TileEntityItemStackToItemStackFactory::new);
            registerFactory(tier, FactoryType.ENRICHING, TileEntityItemStackToItemStackFactory::new);
            registerFactory(tier, FactoryType.INFUSING, TileEntityItemStackChemicalToItemStackFactory::new);
            registerFactory(tier, FactoryType.INJECTING, TileEntityItemStackChemicalToItemStackFactory::new);
            registerFactory(tier, FactoryType.PURIFYING, TileEntityItemStackChemicalToItemStackFactory::new);
            registerFactory(tier, FactoryType.SAWING, TileEntitySawingFactory::new);
            registerFactory(tier, FactoryType.SMELTING, TileEntityItemStackToItemStackFactory::new);
        }
        List<FactoryTier> allTiers = new ArrayList<>();
        allTiers.addAll(Arrays.stream(EnumUtils.FACTORY_TIERS).toList());
        allTiers.addAll( Arrays.asList(EMFactoryTier.OVERCLOCKED,EMFactoryTier.QUANTUM,EMFactoryTier.DENSE,EMFactoryTier.MULTIVERSAL,EMFactoryTier.CREATIVE));
        for (FactoryTier tier : allTiers){
            registerFactory(tier,EMFactoryType.ALLOYING, TileEntityAlloyingFactory::new);
        }
    }

    private static void registerFactory(FactoryTier tier, FactoryType type, BlockEntityFactory<? extends TileEntityFactory<?>> factoryConstructor) {
        BlockRegistryObject<BlockFactoryMachine.BlockFactory<?>, ItemBlockFactory> block = EMBlocks.getFactory(tier, type);
        TileEntityTypeRegistryObject<? extends TileEntityFactory<?>> tileRO = TILE_ENTITY_TYPES.mekBuilder(block, (pos, state) -> factoryConstructor.create(block, pos, state))
                .clientTicker(TileEntityMekanism::tickClient)
                .serverTicker(TileEntityMekanism::tickServer)
                .withSimple(Capabilities.CONFIG_CARD)
                .build();
        FACTORIES.put(tier, type, tileRO);
    }
    public static final TileEntityTypeRegistryObject<TileEntityAPTCasing> APT_CASING = TILE_ENTITY_TYPES.mekBuilder(EMBlocks.APT_CASING, TileEntityAPTCasing::new)
                      .clientTicker(TileEntityMekanism::tickClient)
          .serverTicker(TileEntityMekanism::tickServer)
          .withSimple(Capabilities.CONFIGURABLE).build();
    public static final TileEntityTypeRegistryObject<TileEntityAPTPort> APT_PORT = TILE_ENTITY_TYPES.mekBuilder(EMBlocks.APT_PORT, TileEntityAPTPort::new)
                      .clientTicker(TileEntityMekanism::tickClient)
          .serverTicker(TileEntityMekanism::tickServer)
          .withSimple(Capabilities.CONFIGURABLE).build();
    public static final TileEntityTypeRegistryObject<TileEntitySuperchargingElement> SUPERCHARGING_ELEMENT = TILE_ENTITY_TYPES.mekBuilder(EMBlocks.SUPERCHARGING_ELEMENT, TileEntitySuperchargingElement::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .build();


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
    public static final TileEntityTypeRegistryObject<TileEntityEnergyCube> OVERCLOCKED_ENERGY_CUBE = registerEnergyCube(EMBlocks.OVERCLOCKED_ENERGY_CUBE);
    public static final TileEntityTypeRegistryObject<TileEntityEnergyCube> QUANTUM_ENERGY_CUBE = registerEnergyCube(EMBlocks.QUANTUM_ENERGY_CUBE);
    public static final TileEntityTypeRegistryObject<TileEntityEnergyCube> DENSE_ENERGY_CUBE = registerEnergyCube(EMBlocks.DENSE_ENERGY_CUBE);
    public static final TileEntityTypeRegistryObject<TileEntityEnergyCube> MULTIVERSAL_ENERGY_CUBE = registerEnergyCube(EMBlocks.MULTIVERSAL_ENERGY_CUBE);

    private static TileEntityTypeRegistryObject<TileEntityEnergyCube> registerEnergyCube(DeferredHolder<Block, BlockEnergyCube> block) {
        return TILE_ENTITY_TYPES.mekBuilder(block, (pos, state) -> new TileEntityEnergyCube(block, pos, state))
                .serverTicker(TileEntityMekanism::tickServer)
                .withSimple(Capabilities.CONFIG_CARD)
                .build();
    }
    
    //Chemical Tanks
    public static final TileEntityTypeRegistryObject<TileEntityChemicalTank> OVERCLOCKED_CHEMICAL_TANK = registerChemicalTank(EMBlocks.OVERCLOCKED_CHEMICAL_TANK);
    public static final TileEntityTypeRegistryObject<TileEntityChemicalTank> QUANTUM_CHEMICAL_TANK = registerChemicalTank(EMBlocks.QUANTUM_CHEMICAL_TANK);
    public static final TileEntityTypeRegistryObject<TileEntityChemicalTank> DENSE_CHEMICAL_TANK = registerChemicalTank(EMBlocks.DENSE_CHEMICAL_TANK);
    public static final TileEntityTypeRegistryObject<TileEntityChemicalTank> MULTIVERSAL_CHEMICAL_TANK = registerChemicalTank(EMBlocks.MULTIVERSAL_CHEMICAL_TANK);

    private static TileEntityTypeRegistryObject<TileEntityChemicalTank> registerChemicalTank(BlockRegistryObject<?, ItemBlockChemicalTank> block) {
        return TILE_ENTITY_TYPES.mekBuilder(block, (pos, state) -> new TileEntityChemicalTank(block, pos, state))
                .serverTicker(TileEntityMekanism::tickServer)
                .withSimple(Capabilities.CONFIG_CARD)
                .build();
    }

    //Fluid Tanks
    public static final TileEntityTypeRegistryObject<TileEntityFluidTank> OVERCLOCKED_FLUID_TANK = registerFluidTank(EMBlocks.OVERCLOCKED_FLUID_TANK);
    public static final TileEntityTypeRegistryObject<TileEntityFluidTank> QUANTUM_FLUID_TANK = registerFluidTank(EMBlocks.QUANTUM_FLUID_TANK);
    public static final TileEntityTypeRegistryObject<TileEntityFluidTank> DENSE_FLUID_TANK = registerFluidTank(EMBlocks.DENSE_FLUID_TANK);
    public static final TileEntityTypeRegistryObject<TileEntityFluidTank> MULTIVERSAL_FLUID_TANK = registerFluidTank(EMBlocks.MULTIVERSAL_FLUID_TANK);

    private static TileEntityTypeRegistryObject<TileEntityFluidTank> registerFluidTank(DeferredHolder<Block, BlockFluidTank> block) {
        return TILE_ENTITY_TYPES.mekBuilder(block, (pos, state) -> new TileEntityFluidTank(block, pos, state))
                .clientTicker(TileEntityMekanism::tickClient)
                .serverTicker(TileEntityMekanism::tickServer)
                .withSimple(Capabilities.CONFIG_CARD)
                .withSimple(Capabilities.CONFIGURABLE)
                .build();
    }
    
    //Bins
    public static final TileEntityTypeRegistryObject<TileEntityBin> OVERCLOCKED_BIN = registerBin(EMBlocks.OVERCLOCKED_BIN);
    public static final TileEntityTypeRegistryObject<TileEntityBin> QUANTUM_BIN = registerBin(EMBlocks.QUANTUM_BIN);
    public static final TileEntityTypeRegistryObject<TileEntityBin> DENSE_BIN = registerBin(EMBlocks.DENSE_BIN);
    public static final TileEntityTypeRegistryObject<TileEntityBin> MULTIVERSAL_BIN = registerBin(EMBlocks.MULTIVERSAL_BIN);

    private static TileEntityTypeRegistryObject<TileEntityBin> registerBin(DeferredHolder<Block, BlockBin> block) {
        return TILE_ENTITY_TYPES.mekBuilder(block, (pos, state) -> new TileEntityBin(block, pos, state))
                .serverTicker(TileEntityMekanism::tickServer)
                .withSimple(Capabilities.CONFIGURABLE)
                .build();
    }
    //Induction Cells
    public static final TileEntityTypeRegistryObject<TileEntityInductionCell> OVERCLOCKED_INDUCTION_CELL = TILE_ENTITY_TYPES.builder(EMBlocks.OVERCLOCKED_INDUCTION_CELL, (pos, state) -> new TileEntityInductionCell(EMBlocks.OVERCLOCKED_INDUCTION_CELL, pos, state)).build();
    public static final TileEntityTypeRegistryObject<TileEntityInductionCell> QUANTUM_INDUCTION_CELL = TILE_ENTITY_TYPES.builder(EMBlocks.QUANTUM_INDUCTION_CELL, (pos, state) -> new TileEntityInductionCell(EMBlocks.QUANTUM_INDUCTION_CELL, pos, state)).build();
    public static final TileEntityTypeRegistryObject<TileEntityInductionCell> DENSE_INDUCTION_CELL = TILE_ENTITY_TYPES.builder(EMBlocks.DENSE_INDUCTION_CELL, (pos, state) -> new TileEntityInductionCell(EMBlocks.DENSE_INDUCTION_CELL, pos, state)).build();
    public static final TileEntityTypeRegistryObject<TileEntityInductionCell> MULTIVERSAL_INDUCTION_CELL = TILE_ENTITY_TYPES.builder(EMBlocks.MULTIVERSAL_INDUCTION_CELL, (pos, state) -> new TileEntityInductionCell(EMBlocks.MULTIVERSAL_INDUCTION_CELL, pos, state)).build();
    public static final TileEntityTypeRegistryObject<TileEntityInductionCell> CREATIVE_INDUCTION_CELL = TILE_ENTITY_TYPES.builder(EMBlocks.CREATIVE_INDUCTION_CELL, (pos, state) -> new TileEntityInductionCell(EMBlocks.CREATIVE_INDUCTION_CELL, pos, state)).build();
    //Induction Providers
    public static final TileEntityTypeRegistryObject<TileEntityInductionProvider> OVERCLOCKED_INDUCTION_PROVIDER = TILE_ENTITY_TYPES.builder(EMBlocks.OVERCLOCKED_INDUCTION_PROVIDER, (pos, state) -> new TileEntityInductionProvider(EMBlocks.OVERCLOCKED_INDUCTION_PROVIDER, pos, state)).build();
    public static final TileEntityTypeRegistryObject<TileEntityInductionProvider> QUANTUM_INDUCTION_PROVIDER = TILE_ENTITY_TYPES.builder(EMBlocks.QUANTUM_INDUCTION_PROVIDER, (pos, state) -> new TileEntityInductionProvider(EMBlocks.QUANTUM_INDUCTION_PROVIDER, pos, state)).build();
    public static final TileEntityTypeRegistryObject<TileEntityInductionProvider> DENSE_INDUCTION_PROVIDER = TILE_ENTITY_TYPES.builder(EMBlocks.DENSE_INDUCTION_PROVIDER, (pos, state) -> new TileEntityInductionProvider(EMBlocks.DENSE_INDUCTION_PROVIDER, pos, state)).build();
    public static final TileEntityTypeRegistryObject<TileEntityInductionProvider> MULTIVERSAL_INDUCTION_PROVIDER = TILE_ENTITY_TYPES.builder(EMBlocks.MULTIVERSAL_INDUCTION_PROVIDER, (pos, state) -> new TileEntityInductionProvider(EMBlocks.MULTIVERSAL_INDUCTION_PROVIDER, pos, state)).build();
    public static final TileEntityTypeRegistryObject<TileEntityInductionProvider> CREATIVE_INDUCTION_PROVIDER = TILE_ENTITY_TYPES.builder(EMBlocks.CREATIVE_INDUCTION_PROVIDER, (pos, state) -> new TileEntityInductionProvider(EMBlocks.CREATIVE_INDUCTION_PROVIDER, pos, state)).build();

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

    public static final TileEntityTypeRegistryObject<TileEntityAlloyer> ALLOYER = TILE_ENTITY_TYPES.mekBuilder(EMBlocks.ALLOYER, TileEntityAlloyer::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .build();
    public static final TileEntityTypeRegistryObject<TileEntityChemixer> CHEMIXER = TILE_ENTITY_TYPES.mekBuilder(EMBlocks.CHEMIXER, TileEntityChemixer::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .build();
    public static final TileEntityTypeRegistryObject<TileEntityMelter> MELTER = TILE_ENTITY_TYPES.mekBuilder(EMBlocks.MELTER, TileEntityMelter::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .build();
    public static final TileEntityTypeRegistryObject<TileEntitySolidifier> SOLIDIFIER = TILE_ENTITY_TYPES.mekBuilder(EMBlocks.SOLIDIFIER, TileEntitySolidifier::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .build();


    private static <BE extends TileEntityTransmitter> TileEntityTypeRegistryObject<BE> registerTransmitter(BlockRegistryObject<?, ?> block,
                                                                                                           BlockEntityType.BlockEntitySupplier<? extends BE> factory) {
        //Note: There is no data fixer type as forge does not currently have a way exposing data fixers to mods yet
        return TILE_ENTITY_TYPES.<BE>builder(block, factory).serverTicker(TileEntityTransmitter::tickServer).build();
    }

    private static <BE extends TileEntityTieredPersonalStorage> TileEntityTypeRegistryObject<BE> registerTieredStorage(BlockRegistryObject<?, ?> block, PersonalStorageTier tier,
                                                                                                                       BlockEntitySupplier<BE> factory){
        return TILE_ENTITY_TYPES.mekBuilder(block,(pos,state)->factory.create(block,pos,state,tier)).serverTicker(TileEntityMekanism::tickServer).clientTicker(TileEntityMekanism::tickClient).build();
    }

    @FunctionalInterface
    public interface BlockEntitySupplier<T extends BlockEntity> {
        T create(Holder<Block> block, BlockPos pos, BlockState state, PersonalStorageTier tier);
    }

    @FunctionalInterface
    public interface BlockEntityFactory<T extends BlockEntity> {
        T create(Holder<Block> block, BlockPos pos, BlockState state);
    }


    public static TileEntityTypeRegistryObject<? extends TileEntityFactory<?>> getFactoryTile(FactoryTier tier, FactoryType type) {
        return FACTORIES.get(tier, type);
    }

}
