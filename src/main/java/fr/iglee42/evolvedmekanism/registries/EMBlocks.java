package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.blocks.BlockTieredPersonnalBarrel;
import fr.iglee42.evolvedmekanism.blocks.BlockTieredPersonnalChest;
import fr.iglee42.evolvedmekanism.blocks.EMBlockResource;
import fr.iglee42.evolvedmekanism.items.EMItemBlockResource;
import fr.iglee42.evolvedmekanism.items.ItemBlockTieredPersonalStorage;
import fr.iglee42.evolvedmekanism.tiers.PersonalStorageTier;
import fr.iglee42.evolvedmekanism.tiers.cable.*;
import fr.iglee42.evolvedmekanism.tiles.TileEntityTieredPersonalBarrel;
import fr.iglee42.evolvedmekanism.tiles.TileEntityTieredPersonalChest;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityAlloyer;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityChemixer;
import mekanism.api.tier.ITier;
import mekanism.common.block.BlockEnergyCube;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.block.basic.BlockBin;
import mekanism.common.block.basic.BlockFluidTank;
import mekanism.common.block.prefab.BlockFactoryMachine;
import mekanism.common.block.prefab.BlockFactoryMachine.BlockFactory;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.block.transmitter.*;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.Factory;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.item.block.*;
import mekanism.common.item.block.machine.ItemBlockFactory;
import mekanism.common.item.block.machine.ItemBlockFluidTank;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.item.block.transmitter.*;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registries.MekanismBlockTypes;
import mekanism.common.resource.BlockResourceInfo;
import mekanism.common.tier.*;
import mekanism.common.tile.TileEntityBin;
import mekanism.common.tile.TileEntityChemicalTank;
import mekanism.common.tile.TileEntityEnergyCube;
import mekanism.common.tile.TileEntityFluidTank;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.tile.machine.TileEntityCombiner;
import mekanism.common.tile.machine.TileEntityPressurizedReactionChamber;
import mekanism.common.tile.multiblock.TileEntityInductionCell;
import mekanism.common.tile.multiblock.TileEntityInductionProvider;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;
import java.util.function.Supplier;

public class EMBlocks {
    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(EvolvedMekanism.MODID);

    public static final BlockRegistryObject<BlockFactoryMachine<TileEntityAlloyer, Machine.FactoryMachine<TileEntityAlloyer>>, ItemBlockMachine> ALLOYER = BLOCKS.register("alloyer", () -> new BlockFactoryMachine<>(EMBlockTypes.ALLOYER, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), ItemBlockMachine::new);

    public static final BlockRegistryObject<BlockTileModel<TileEntityChemixer, Machine<TileEntityChemixer>>, ItemBlockMachine> CHEMIXER = BLOCKS.register("chemixer", () -> new BlockTileModel<>(EMBlockTypes.CHEMIXER, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), ItemBlockMachine::new);


    public static final BlockRegistryObject<EMBlockResource, EMItemBlockResource> BETTER_GOLD_BLOCK = registerResourceBlock(EMBlockResourceInfo.BETTER_GOLD);
    public static final BlockRegistryObject<EMBlockResource, EMItemBlockResource> PLASLITHERITE_BLOCK = registerResourceBlock(EMBlockResourceInfo.PLASLITHERITE);

    public static final BlockRegistryObject<BlockBin, ItemBlockBin> OVERCLOCKED_BIN = registerBin(EMBlockTypes.OVERCLOCKED_BIN);
    public static final BlockRegistryObject<BlockBin, ItemBlockBin> QUANTUM_BIN = registerBin(EMBlockTypes.QUANTUM_BIN);
    public static final BlockRegistryObject<BlockBin, ItemBlockBin> DENSE_BIN = registerBin(EMBlockTypes.DENSE_BIN);
    public static final BlockRegistryObject<BlockBin, ItemBlockBin> MULTIVERSAL_BIN = registerBin(EMBlockTypes.MULTIVERSAL_BIN);

    public static final BlockRegistryObject<BlockTile<TileEntityInductionCell, BlockTypeTile<TileEntityInductionCell>>, ItemBlockInductionCell> OVERCLOCKED_INDUCTION_CELL = registerInductionCell(EMBlockTypes.OVERCLOCKED_INDUCTION_CELL);
    public static final BlockRegistryObject<BlockTile<TileEntityInductionCell, BlockTypeTile<TileEntityInductionCell>>, ItemBlockInductionCell> QUANTUM_INDUCTION_CELL = registerInductionCell(EMBlockTypes.QUANTUM_INDUCTION_CELL);
    public static final BlockRegistryObject<BlockTile<TileEntityInductionCell, BlockTypeTile<TileEntityInductionCell>>, ItemBlockInductionCell> DENSE_INDUCTION_CELL = registerInductionCell(EMBlockTypes.DENSE_INDUCTION_CELL);
    public static final BlockRegistryObject<BlockTile<TileEntityInductionCell, BlockTypeTile<TileEntityInductionCell>>, ItemBlockInductionCell> MULTIVERSAL_INDUCTION_CELL = registerInductionCell(EMBlockTypes.MULTIVERSAL_INDUCTION_CELL);
    public static final BlockRegistryObject<BlockTile<TileEntityInductionCell, BlockTypeTile<TileEntityInductionCell>>, ItemBlockInductionCell> CREATIVE_INDUCTION_CELL = registerInductionCell(EMBlockTypes.CREATIVE_INDUCTION_CELL);

    public static final BlockRegistryObject<BlockTile<TileEntityInductionProvider, BlockTypeTile<TileEntityInductionProvider>>, ItemBlockInductionProvider> OVERCLOCKED_INDUCTION_PROVIDER = registerInductionProvider(EMBlockTypes.OVERCLOCKED_INDUCTION_PROVIDER);
    public static final BlockRegistryObject<BlockTile<TileEntityInductionProvider, BlockTypeTile<TileEntityInductionProvider>>, ItemBlockInductionProvider> QUANTUM_INDUCTION_PROVIDER = registerInductionProvider(EMBlockTypes.QUANTUM_INDUCTION_PROVIDER);
    public static final BlockRegistryObject<BlockTile<TileEntityInductionProvider, BlockTypeTile<TileEntityInductionProvider>>, ItemBlockInductionProvider> DENSE_INDUCTION_PROVIDER = registerInductionProvider(EMBlockTypes.DENSE_INDUCTION_PROVIDER);
    public static final BlockRegistryObject<BlockTile<TileEntityInductionProvider, BlockTypeTile<TileEntityInductionProvider>>, ItemBlockInductionProvider> MULTIVERSAL_INDUCTION_PROVIDER = registerInductionProvider(EMBlockTypes.MULTIVERSAL_INDUCTION_PROVIDER);
    public static final BlockRegistryObject<BlockTile<TileEntityInductionProvider, BlockTypeTile<TileEntityInductionProvider>>, ItemBlockInductionProvider> CREATIVE_INDUCTION_PROVIDER = registerInductionProvider(EMBlockTypes.CREATIVE_INDUCTION_PROVIDER);

    public static final BlockRegistryObject<BlockFluidTank, ItemBlockFluidTank> OVERCLOCKED_FLUID_TANK = registerFluidTank(EMBlockTypes.OVERCLOCKED_FLUID_TANK);
    public static final BlockRegistryObject<BlockFluidTank, ItemBlockFluidTank> QUANTUM_FLUID_TANK = registerFluidTank(EMBlockTypes.QUANTUM_FLUID_TANK);
    public static final BlockRegistryObject<BlockFluidTank, ItemBlockFluidTank> DENSE_FLUID_TANK = registerFluidTank(EMBlockTypes.DENSE_FLUID_TANK);
    public static final BlockRegistryObject<BlockFluidTank, ItemBlockFluidTank> MULTIVERSAL_FLUID_TANK = registerFluidTank(EMBlockTypes.MULTIVERSAL_FLUID_TANK);

    public static final BlockRegistryObject<BlockEnergyCube, ItemBlockEnergyCube> OVERCLOCKED_ENERGY_CUBE = registerEnergyCube(EMBlockTypes.OVERCLOCKED_ENERGY_CUBE);
    public static final BlockRegistryObject<BlockEnergyCube, ItemBlockEnergyCube> QUANTUM_ENERGY_CUBE = registerEnergyCube(EMBlockTypes.QUANTUM_ENERGY_CUBE);
    public static final BlockRegistryObject<BlockEnergyCube, ItemBlockEnergyCube> DENSE_ENERGY_CUBE = registerEnergyCube(EMBlockTypes.DENSE_ENERGY_CUBE);
    public static final BlockRegistryObject<BlockEnergyCube, ItemBlockEnergyCube> MULTIVERSAL_ENERGY_CUBE = registerEnergyCube(EMBlockTypes.MULTIVERSAL_ENERGY_CUBE);

    public static final BlockRegistryObject<BlockUniversalCable, ItemBlockUniversalCable> OVERCLOCKED_UNIVERSAL_CABLE = registerUniversalCable(EMCableTier.OVERCLOCKED);
    public static final BlockRegistryObject<BlockUniversalCable, ItemBlockUniversalCable> QUANTUM_UNIVERSAL_CABLE = registerUniversalCable(EMCableTier.QUANTUM);
    public static final BlockRegistryObject<BlockUniversalCable, ItemBlockUniversalCable> DENSE_UNIVERSAL_CABLE = registerUniversalCable(EMCableTier.DENSE);
    public static final BlockRegistryObject<BlockUniversalCable, ItemBlockUniversalCable> MULTIVERSAL_UNIVERSAL_CABLE = registerUniversalCable(EMCableTier.MULTIVERSAL);
    public static final BlockRegistryObject<BlockUniversalCable, ItemBlockUniversalCable> CREATIVE_UNIVERSAL_CABLE = registerUniversalCable(EMCableTier.CREATIVE);

    public static final BlockRegistryObject<BlockMechanicalPipe, ItemBlockMechanicalPipe> OVERCLOCKED_MECHANICAL_PIPE = registerMechanicalPipe(EMPipeTier.OVERCLOCKED);
    public static final BlockRegistryObject<BlockMechanicalPipe, ItemBlockMechanicalPipe> QUANTUM_MECHANICAL_PIPE = registerMechanicalPipe(EMPipeTier.QUANTUM);
    public static final BlockRegistryObject<BlockMechanicalPipe, ItemBlockMechanicalPipe> DENSE_MECHANICAL_PIPE = registerMechanicalPipe(EMPipeTier.DENSE);
    public static final BlockRegistryObject<BlockMechanicalPipe, ItemBlockMechanicalPipe> MULTIVERSAL_MECHANICAL_PIPE = registerMechanicalPipe(EMPipeTier.MULTIVERSAL);
    public static final BlockRegistryObject<BlockMechanicalPipe, ItemBlockMechanicalPipe> CREATIVE_MECHANICAL_PIPE = registerMechanicalPipe(EMPipeTier.CREATIVE);

    public static final BlockRegistryObject<BlockPressurizedTube, ItemBlockPressurizedTube> OVERCLOCKED_PRESSURIZED_TUBE = registerPressurizedTube(EMTubeTier.OVERCLOCKED);
    public static final BlockRegistryObject<BlockPressurizedTube, ItemBlockPressurizedTube> QUANTUM_PRESSURIZED_TUBE = registerPressurizedTube(EMTubeTier.QUANTUM);
    public static final BlockRegistryObject<BlockPressurizedTube, ItemBlockPressurizedTube> DENSE_PRESSURIZED_TUBE = registerPressurizedTube(EMTubeTier.DENSE);
    public static final BlockRegistryObject<BlockPressurizedTube, ItemBlockPressurizedTube> MULTIVERSAL_PRESSURIZED_TUBE = registerPressurizedTube(EMTubeTier.MULTIVERSAL);
    public static final BlockRegistryObject<BlockPressurizedTube, ItemBlockPressurizedTube> CREATIVE_PRESSURIZED_TUBE = registerPressurizedTube(EMTubeTier.CREATIVE);

    public static final BlockRegistryObject<BlockLogisticalTransporter, ItemBlockLogisticalTransporter> OVERCLOCKED_LOGISTICAL_TRANSPORTER = registerLogisticalTransporter(EMTransporterTier.OVERCLOCKED);
    public static final BlockRegistryObject<BlockLogisticalTransporter, ItemBlockLogisticalTransporter> QUANTUM_LOGISTICAL_TRANSPORTER = registerLogisticalTransporter(EMTransporterTier.QUANTUM);
    public static final BlockRegistryObject<BlockLogisticalTransporter, ItemBlockLogisticalTransporter> DENSE_LOGISTICAL_TRANSPORTER = registerLogisticalTransporter(EMTransporterTier.DENSE);
    public static final BlockRegistryObject<BlockLogisticalTransporter, ItemBlockLogisticalTransporter> MULTIVERSAL_LOGISTICAL_TRANSPORTER = registerLogisticalTransporter(EMTransporterTier.MULTIVERSAL);
    public static final BlockRegistryObject<BlockLogisticalTransporter, ItemBlockLogisticalTransporter> CREATIVE_LOGISTICAL_TRANSPORTER = registerLogisticalTransporter(EMTransporterTier.CREATIVE);

    public static final BlockRegistryObject<BlockThermodynamicConductor, ItemBlockThermodynamicConductor> OVERCLOCKED_THERMODYNAMIC_CONDUCTOR = registerThermodynamicConductor(EMConductorTier.OVERCLOCKED);
    public static final BlockRegistryObject<BlockThermodynamicConductor, ItemBlockThermodynamicConductor> QUANTUM_THERMODYNAMIC_CONDUCTOR = registerThermodynamicConductor(EMConductorTier.QUANTUM);
    public static final BlockRegistryObject<BlockThermodynamicConductor, ItemBlockThermodynamicConductor> DENSE_THERMODYNAMIC_CONDUCTOR = registerThermodynamicConductor(EMConductorTier.DENSE);
    public static final BlockRegistryObject<BlockThermodynamicConductor, ItemBlockThermodynamicConductor> MULTIVERSAL_THERMODYNAMIC_CONDUCTOR = registerThermodynamicConductor(EMConductorTier.MULTIVERSAL);
    public static final BlockRegistryObject<BlockThermodynamicConductor, ItemBlockThermodynamicConductor> CREATIVE_THERMODYNAMIC_CONDUCTOR = registerThermodynamicConductor(EMConductorTier.CREATIVE);

    public static final BlockRegistryObject<BlockTileModel<TileEntityChemicalTank, Machine<TileEntityChemicalTank>>, ItemBlockChemicalTank> OVERCLOCKED_CHEMICAL_TANK = registerChemicalTank(EMBlockTypes.OVERCLOCKED_CHEMICAL_TANK);
    public static final BlockRegistryObject<BlockTileModel<TileEntityChemicalTank, Machine<TileEntityChemicalTank>>, ItemBlockChemicalTank> QUANTUM_CHEMICAL_TANK = registerChemicalTank(EMBlockTypes.QUANTUM_CHEMICAL_TANK);
    public static final BlockRegistryObject<BlockTileModel<TileEntityChemicalTank, Machine<TileEntityChemicalTank>>, ItemBlockChemicalTank> DENSE_CHEMICAL_TANK = registerChemicalTank(EMBlockTypes.DENSE_CHEMICAL_TANK);
    public static final BlockRegistryObject<BlockTileModel<TileEntityChemicalTank, Machine<TileEntityChemicalTank>>, ItemBlockChemicalTank> MULTIVERSAL_CHEMICAL_TANK = registerChemicalTank(EMBlockTypes.MULTIVERSAL_CHEMICAL_TANK);

    public static final BlockRegistryObject<BlockTieredPersonnalBarrel, ItemBlockTieredPersonalStorage<BlockTieredPersonnalBarrel>> ADVANCED_PERSONAL_BARREL =registerPersonalBarrel(EMBlockTypes.ADVANCED_PERSONAL_BARREL, PersonalStorageTier.ADVANCED);
    public static final BlockRegistryObject<BlockTieredPersonnalBarrel, ItemBlockTieredPersonalStorage<BlockTieredPersonnalBarrel>> ELITE_PERSONAL_BARREL =registerPersonalBarrel(EMBlockTypes.ELITE_PERSONAL_BARREL, PersonalStorageTier.ELITE);
    public static final BlockRegistryObject<BlockTieredPersonnalBarrel, ItemBlockTieredPersonalStorage<BlockTieredPersonnalBarrel>> ULTIMATE_PERSONAL_BARREL =registerPersonalBarrel(EMBlockTypes.ULTIMATE_PERSONAL_BARREL, PersonalStorageTier.ULTIMATE);
    public static final BlockRegistryObject<BlockTieredPersonnalBarrel, ItemBlockTieredPersonalStorage<BlockTieredPersonnalBarrel>> OVERCLOCKED_PERSONAL_BARREL =registerPersonalBarrel(EMBlockTypes.OVERCLOCKED_PERSONAL_BARREL, PersonalStorageTier.OVERCLOCKED);
    public static final BlockRegistryObject<BlockTieredPersonnalBarrel, ItemBlockTieredPersonalStorage<BlockTieredPersonnalBarrel>> QUANTUM_PERSONAL_BARREL =registerPersonalBarrel(EMBlockTypes.QUANTUM_PERSONAL_BARREL, PersonalStorageTier.QUANTUM);
    public static final BlockRegistryObject<BlockTieredPersonnalBarrel, ItemBlockTieredPersonalStorage<BlockTieredPersonnalBarrel>> DENSE_PERSONAL_BARREL =registerPersonalBarrel(EMBlockTypes.DENSE_PERSONAL_BARREL, PersonalStorageTier.DENSE);
    public static final BlockRegistryObject<BlockTieredPersonnalBarrel, ItemBlockTieredPersonalStorage<BlockTieredPersonnalBarrel>> MULTIVERSAL_PERSONAL_BARREL =registerPersonalBarrel(EMBlockTypes.MULTIVERSAL_PERSONAL_BARREL, PersonalStorageTier.MULTIVERSAL);
    public static final BlockRegistryObject<BlockTieredPersonnalBarrel, ItemBlockTieredPersonalStorage<BlockTieredPersonnalBarrel>> CREATIVE_PERSONAL_BARREL =registerPersonalBarrel(EMBlockTypes.CREATIVE_PERSONAL_BARREL, PersonalStorageTier.CREATIVE);

    public static final BlockRegistryObject<BlockTieredPersonnalChest, ItemBlockTieredPersonalStorage<BlockTieredPersonnalChest>> ADVANCED_PERSONAL_CHEST = registerPersonalChest(EMBlockTypes.ADVANCED_PERSONAL_CHEST,PersonalStorageTier.ADVANCED);
    public static final BlockRegistryObject<BlockTieredPersonnalChest, ItemBlockTieredPersonalStorage<BlockTieredPersonnalChest>> ELITE_PERSONAL_CHEST = registerPersonalChest(EMBlockTypes.ELITE_PERSONAL_CHEST,PersonalStorageTier.ELITE);
    public static final BlockRegistryObject<BlockTieredPersonnalChest, ItemBlockTieredPersonalStorage<BlockTieredPersonnalChest>> ULTIMATE_PERSONAL_CHEST = registerPersonalChest(EMBlockTypes.ULTIMATE_PERSONAL_CHEST,PersonalStorageTier.ULTIMATE);
    public static final BlockRegistryObject<BlockTieredPersonnalChest, ItemBlockTieredPersonalStorage<BlockTieredPersonnalChest>> OVERCLOCKED_PERSONAL_CHEST = registerPersonalChest(EMBlockTypes.OVERCLOCKED_PERSONAL_CHEST,PersonalStorageTier.OVERCLOCKED);
    public static final BlockRegistryObject<BlockTieredPersonnalChest, ItemBlockTieredPersonalStorage<BlockTieredPersonnalChest>> QUANTUM_PERSONAL_CHEST = registerPersonalChest(EMBlockTypes.QUANTUM_PERSONAL_CHEST,PersonalStorageTier.QUANTUM);
    public static final BlockRegistryObject<BlockTieredPersonnalChest, ItemBlockTieredPersonalStorage<BlockTieredPersonnalChest>> DENSE_PERSONAL_CHEST = registerPersonalChest(EMBlockTypes.DENSE_PERSONAL_CHEST,PersonalStorageTier.DENSE);
    public static final BlockRegistryObject<BlockTieredPersonnalChest, ItemBlockTieredPersonalStorage<BlockTieredPersonnalChest>> MULTIVERSAL_PERSONAL_CHEST = registerPersonalChest(EMBlockTypes.MULTIVERSAL_PERSONAL_CHEST,PersonalStorageTier.MULTIVERSAL);
    public static final BlockRegistryObject<BlockTieredPersonnalChest, ItemBlockTieredPersonalStorage<BlockTieredPersonnalChest>> CREATIVE_PERSONAL_CHEST = registerPersonalChest(EMBlockTypes.CREATIVE_PERSONAL_CHEST,PersonalStorageTier.CREATIVE);

    private static BlockRegistryObject<BlockTieredPersonnalChest, ItemBlockTieredPersonalStorage<BlockTieredPersonnalChest>> registerPersonalChest(BlockTypeTile<TileEntityTieredPersonalChest> type,PersonalStorageTier tier) {
        return registerTieredBlock(tier, "_personal_chest", () -> new BlockTieredPersonnalChest(type,tier), block -> new ItemBlockTieredPersonalStorage<>(block, Stats.OPEN_CHEST,block.getTier()));
    }
    private static BlockRegistryObject<BlockTieredPersonnalBarrel, ItemBlockTieredPersonalStorage<BlockTieredPersonnalBarrel>> registerPersonalBarrel(BlockTypeTile<TileEntityTieredPersonalBarrel> type, PersonalStorageTier tier) {
        return registerTieredBlock(tier, "_personal_barrel", () -> new BlockTieredPersonnalBarrel(type,tier), block -> new ItemBlockTieredPersonalStorage<>(block, Stats.OPEN_CHEST,block.getTier()));
    }

    private static BlockRegistryObject<BlockBin, ItemBlockBin> registerBin(BlockTypeTile<TileEntityBin> type) {
        return registerTieredBlock(type, "_bin", color -> new BlockBin(type, properties -> properties.mapColor(color)), ItemBlockBin::new);
    }

    private static BlockRegistryObject<BlockTile<TileEntityInductionCell, BlockTypeTile<TileEntityInductionCell>>, ItemBlockInductionCell> registerInductionCell(BlockTypeTile<TileEntityInductionCell> type) {
        return registerTieredBlock(type, "_induction_cell", color -> new BlockTile<>(type, properties -> properties.mapColor(color)), ItemBlockInductionCell::new);
    }

    private static BlockRegistryObject<BlockTile<TileEntityInductionProvider, BlockTypeTile<TileEntityInductionProvider>>, ItemBlockInductionProvider> registerInductionProvider(BlockTypeTile<TileEntityInductionProvider> type) {
        return registerTieredBlock(type, "_induction_provider", color -> new BlockTile<>(type, properties -> properties.mapColor(color)), ItemBlockInductionProvider::new);
    }

    private static BlockRegistryObject<BlockFluidTank, ItemBlockFluidTank> registerFluidTank(Machine<TileEntityFluidTank> type) {
        return registerTieredBlock(type, "_fluid_tank", () -> new BlockFluidTank(type), ItemBlockFluidTank::new);
    }

    private static BlockRegistryObject<BlockEnergyCube, ItemBlockEnergyCube> registerEnergyCube(Machine<TileEntityEnergyCube> type) {
        return registerTieredBlock(type, "_energy_cube", () -> new BlockEnergyCube(type), ItemBlockEnergyCube::new);
    }

    private static BlockRegistryObject<BlockUniversalCable, ItemBlockUniversalCable> registerUniversalCable(CableTier tier) {
        return registerTieredBlock(tier, "_universal_cable", () -> new BlockUniversalCable(tier), ItemBlockUniversalCable::new);
    }

    private static BlockRegistryObject<BlockMechanicalPipe, ItemBlockMechanicalPipe> registerMechanicalPipe(PipeTier tier) {
        return registerTieredBlock(tier, "_mechanical_pipe", () -> new BlockMechanicalPipe(tier), ItemBlockMechanicalPipe::new);
    }

    private static BlockRegistryObject<BlockPressurizedTube, ItemBlockPressurizedTube> registerPressurizedTube(TubeTier tier) {
        return registerTieredBlock(tier, "_pressurized_tube", () -> new BlockPressurizedTube(tier), ItemBlockPressurizedTube::new);
    }

    private static BlockRegistryObject<BlockLogisticalTransporter, ItemBlockLogisticalTransporter> registerLogisticalTransporter(TransporterTier tier) {
        return registerTieredBlock(tier, "_logistical_transporter", () -> new BlockLogisticalTransporter(tier), ItemBlockLogisticalTransporter::new);
    }

    private static BlockRegistryObject<BlockThermodynamicConductor, ItemBlockThermodynamicConductor> registerThermodynamicConductor(ConductorTier tier) {
        return registerTieredBlock(tier, "_thermodynamic_conductor", () -> new BlockThermodynamicConductor(tier), ItemBlockThermodynamicConductor::new);
    }

    private static BlockRegistryObject<BlockTileModel<TileEntityChemicalTank, Machine<TileEntityChemicalTank>>, ItemBlockChemicalTank> registerChemicalTank(
            Machine<TileEntityChemicalTank> type) {
        return registerTieredBlock(type, "_chemical_tank", color -> new BlockTileModel<>(type, properties -> properties.mapColor(color)), ItemBlockChemicalTank::new);
    }

    private static <TILE extends TileEntityFactory<?>> BlockRegistryObject<BlockFactory<?>, ItemBlockFactory> registerFactory(Factory<TILE> type) {
        return registerTieredBlock(type, "_" + type.getFactoryType().getRegistryNameComponent() + "_factory", () -> new BlockFactory<>(type), ItemBlockFactory::new);
    }

    private static <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerTieredBlock(BlockType type, String suffix,
                                                                                                                      Function<MapColor, ? extends BLOCK> blockSupplier, Function<BLOCK, ITEM> itemCreator) {
        ITier tier = type.get(AttributeTier.class).tier();
        return registerTieredBlock(tier, suffix, () -> blockSupplier.apply(tier.getBaseTier().getMapColor()), itemCreator);
    }

    private static <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerTieredBlock(BlockType type, String suffix,
                                                                                                                      Supplier<? extends BLOCK> blockSupplier, Function<BLOCK, ITEM> itemCreator) {
        return registerTieredBlock(type.get(AttributeTier.class).tier(), suffix, blockSupplier, itemCreator);
    }

    private static <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerTieredBlock(ITier tier, String suffix,
                                                                                                                      Supplier<? extends BLOCK> blockSupplier, Function<BLOCK, ITEM> itemCreator) {
        return BLOCKS.register(tier.getBaseTier().getLowerName() + suffix, blockSupplier, itemCreator);
    }

    private static BlockRegistryObject<EMBlockResource, EMItemBlockResource> registerResourceBlock(EMBlockResourceInfo resource) {
        return BLOCKS.registerDefaultProperties("block_" + resource.getRegistrySuffix(), () -> new EMBlockResource(resource), (block, properties) -> {
            if (!block.getResourceInfo().burnsInFire()) {
                properties = properties.fireResistant();
            }
            return new EMItemBlockResource(block, properties);
        });
    }
}
