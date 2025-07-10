package fr.iglee42.evolvedmekanism.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.blocks.BlockTieredPersonnalBarrel;
import fr.iglee42.evolvedmekanism.blocks.BlockTieredPersonnalChest;
import fr.iglee42.evolvedmekanism.blocks.EMBlockResource;
import fr.iglee42.evolvedmekanism.interfaces.EMInputRecipeCache;
import fr.iglee42.evolvedmekanism.interfaces.InitializableEnum;
import fr.iglee42.evolvedmekanism.items.EMItemBlockResource;
import fr.iglee42.evolvedmekanism.items.ItemBlockTieredPersonalStorage;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTCasing;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTPort;
import fr.iglee42.evolvedmekanism.tiers.EMAlloyTier;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import fr.iglee42.evolvedmekanism.tiers.EMFactoryTier;
import fr.iglee42.evolvedmekanism.tiers.PersonalStorageTier;
import fr.iglee42.evolvedmekanism.tiers.cable.*;
import fr.iglee42.evolvedmekanism.tiles.TileEntitySuperchargingElement;
import fr.iglee42.evolvedmekanism.tiles.TileEntityTieredPersonalBarrel;
import fr.iglee42.evolvedmekanism.tiles.TileEntityTieredPersonalChest;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityAlloyer;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityChemixer;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityMelter;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntitySolidifier;
import fr.iglee42.evolvedmekanism.utils.EMAttachmedSideConfig;
import mekanism.api.tier.AlloyTier;
import mekanism.api.tier.ITier;
import mekanism.common.attachments.component.AttachedEjector;
import mekanism.common.attachments.component.AttachedSideConfig;
import mekanism.common.attachments.containers.ContainerType;
import mekanism.common.attachments.containers.chemical.ChemicalTanksBuilder;
import mekanism.common.attachments.containers.chemical.ComponentBackedChemicalTankTank;
import mekanism.common.attachments.containers.fluid.ComponentBackedFluidTankFluidTank;
import mekanism.common.attachments.containers.fluid.FluidTanksBuilder;
import mekanism.common.attachments.containers.heat.HeatCapacitorsBuilder;
import mekanism.common.attachments.containers.item.ComponentBackedBinInventorySlot;
import mekanism.common.attachments.containers.item.ItemSlotsBuilder;
import mekanism.common.block.BlockEnergyCube;
import mekanism.common.block.BlockOre;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.block.basic.BlockBin;
import mekanism.common.block.basic.BlockFluidTank;
import mekanism.common.block.basic.BlockResource;
import mekanism.common.block.interfaces.IHasDescription;
import mekanism.common.block.prefab.BlockBasicMultiblock;
import mekanism.common.block.prefab.BlockFactoryMachine;
import mekanism.common.block.prefab.BlockFactoryMachine.BlockFactory;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.block.transmitter.*;
import mekanism.common.content.blocktype.*;
import mekanism.common.item.block.*;
import mekanism.common.item.block.machine.ItemBlockFactory;
import mekanism.common.item.block.machine.ItemBlockFluidTank;
import mekanism.common.item.block.transmitter.*;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.recipe.lookup.cache.type.ItemInputCache;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registries.MekanismBlockTypes;
import mekanism.common.registries.MekanismDataComponents;
import mekanism.common.registries.MekanismTileEntityTypes;
import mekanism.common.resource.BlockResourceInfo;
import mekanism.common.resource.ore.OreBlockType;
import mekanism.common.resource.ore.OreType;
import mekanism.common.tier.*;
import mekanism.common.tile.TileEntityBin;
import mekanism.common.tile.TileEntityChemicalTank;
import mekanism.common.tile.TileEntityEnergyCube;
import mekanism.common.tile.TileEntityFluidTank;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.tile.machine.TileEntityCombiner;
import mekanism.common.tile.machine.TileEntityMetallurgicInfuser;
import mekanism.common.tile.machine.TileEntityPressurizedReactionChamber;
import mekanism.common.tile.multiblock.TileEntityInductionCell;
import mekanism.common.tile.multiblock.TileEntityInductionProvider;
import mekanism.common.tile.multiblock.TileEntitySuperheatingElement;
import mekanism.common.tile.prefab.TileEntityAdvancedElectricMachine;
import mekanism.common.tile.transmitter.*;
import mekanism.common.util.EnumUtils;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class EMBlocks {
    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(EvolvedMekanism.MODID);

    private static final Table<FactoryTier, FactoryType, BlockRegistryObject<BlockFactory<?>, ItemBlockFactory>> FACTORIES = HashBasedTable.create();

    static {
        // factories
        for (FactoryTier tier :  Arrays.asList(EMFactoryTier.OVERCLOCKED,EMFactoryTier.QUANTUM,EMFactoryTier.DENSE,EMFactoryTier.MULTIVERSAL,EMFactoryTier.CREATIVE)) {
            for (FactoryType type : EnumUtils.FACTORY_TYPES) {
                FACTORIES.put(tier, type, registerFactory(EMBlockTypes.getFactory(tier, type)));
            }
        }
        List<FactoryTier> allTiers = new ArrayList<>();
        allTiers.addAll(Arrays.stream(EnumUtils.FACTORY_TIERS).toList());
        allTiers.addAll( Arrays.asList(EMFactoryTier.OVERCLOCKED,EMFactoryTier.QUANTUM,EMFactoryTier.DENSE,EMFactoryTier.MULTIVERSAL,EMFactoryTier.CREATIVE));
        for (FactoryTier tier :  allTiers) {
            FACTORIES.put(tier, EMFactoryType.ALLOYING, registerFactory(EMBlockTypes.getFactory(tier, EMFactoryType.ALLOYING)));
        }
        for (OreType ore : EnumUtils.ORE_TYPES) {
            registerOre(ore);
        }
    }
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityAPTCasing>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityAPTCasing>>> APT_CASING = registerBlock("apt_casing", () -> new BlockBasicMultiblock<>(EMBlockTypes.APT_CASING, properties -> properties.mapColor(MapColor.COLOR_MAGENTA)), Rarity.EPIC);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityAPTPort>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityAPTPort>>> APT_PORT = registerBlock("apt_port", () -> new BlockBasicMultiblock<>(EMBlockTypes.APT_PORT, properties -> properties.mapColor(MapColor.COLOR_MAGENTA)), Rarity.EPIC);
    public static final BlockRegistryObject<BlockTile<TileEntitySuperchargingElement, BlockTypeTile<TileEntitySuperchargingElement>>, ItemBlockTooltip<BlockTile<TileEntitySuperchargingElement, BlockTypeTile<TileEntitySuperchargingElement>>>> SUPERCHARGING_ELEMENT = registerBlock("supercharging_element", () -> new BlockTile<>(EMBlockTypes.SUPERCHARGING_ELEMENT, properties -> properties.mapColor(MapColor.COLOR_MAGENTA)));


    public static final BlockRegistryObject<BlockFactoryMachine<TileEntityAlloyer, Machine.FactoryMachine<TileEntityAlloyer>>, ItemBlockTooltip<BlockFactoryMachine<TileEntityAlloyer, Machine.FactoryMachine<TileEntityAlloyer>>>> ALLOYER = BLOCKS.register("alloyer", () -> new BlockFactoryMachine<>(EMBlockTypes.ALLOYER, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())),
            (block, properties) -> new ItemBlockTooltip<>(block, true, properties
                    .component(MekanismDataComponents.EJECTOR, AttachedEjector.DEFAULT)
                    .component(MekanismDataComponents.SIDE_CONFIG, AttachedSideConfig.EXTRA_MACHINE)
            )
    ).forItemHolder(holder -> holder.addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
            .addInput(EMRecipeType.ALLOYING, EMInputRecipeCache.TripleItem::containsInputA)
            .addInput(EMRecipeType.ALLOYING, EMInputRecipeCache.TripleItem::containsInputB)
            .addInput(EMRecipeType.ALLOYING, EMInputRecipeCache.TripleItem::containsInputC)
            .addOutput()
            .addEnergy()
            .build()
    ));

    public static final BlockRegistryObject<BlockTileModel<TileEntityChemixer, Machine<TileEntityChemixer>>, ItemBlockTooltip<BlockTileModel<TileEntityChemixer, Machine<TileEntityChemixer>>>> CHEMIXER =
            BLOCKS.register("chemixer", () -> new BlockTileModel<>(EMBlockTypes.CHEMIXER, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())),
                    (block, properties) -> new ItemBlockTooltip<>(block, true, properties
                            .component(MekanismDataComponents.EJECTOR, AttachedEjector.DEFAULT)
                            .component(MekanismDataComponents.SIDE_CONFIG, EMAttachmedSideConfig.CHEMIXER_MACHINE)
                    )).forItemHolder(holder -> holder
                    .addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                            .addInput(EMRecipeType.CHEMIXING, EMInputRecipeCache.ItemItemChemical::containsInputA)
                            .addInput(EMRecipeType.CHEMIXING, EMInputRecipeCache.ItemItemChemical::containsInputB)
                            .addOutput()
                            .addEnergy()
                            .build()
                    ).addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> ChemicalTanksBuilder.builder()
                            .addBasic(TileEntityChemixer.MAX_GAS, EMRecipeType.CHEMIXING, EMInputRecipeCache.ItemItemChemical::containsInputC)
                            .build()
                    )
            );
    public static final BlockRegistryObject<BlockTileModel<TileEntityMelter, Machine<TileEntityMelter>>,  ItemBlockTooltip<BlockTileModel<TileEntityMelter, Machine<TileEntityMelter>>>> MELTER =
            BLOCKS.register("thermalizer", () -> new BlockTileModel<>(EMBlockTypes.MELTER, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())),
                    (block, properties) -> new ItemBlockTooltip<>(block, true, properties
                            .component(MekanismDataComponents.EJECTOR, AttachedEjector.DEFAULT)
                            .component(MekanismDataComponents.SIDE_CONFIG, EMAttachmedSideConfig.MELTER_MACHINE)
                    )).forItemHolder(holder -> holder
                    .addAttachmentOnlyContainers(ContainerType.FLUID, () -> FluidTanksBuilder.builder()
                            .addBasic(TileEntityMelter.MAX_FLUID)
                            .build()
                    ).addAttachmentOnlyContainers(ContainerType.HEAT, () -> HeatCapacitorsBuilder.builder()
                            .addBasic(10,5,100)
                            .build()
                    ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                            .addInput(EMRecipeType.MELTING, InputRecipeCache.SingleItem::containsInput)
                            .addFluidDrainSlot(0)
                            .addEnergy()
                            .build()
                    )
            );
    public static final BlockRegistryObject<BlockTileModel<TileEntitySolidifier, Machine<TileEntitySolidifier>>,   ItemBlockTooltip<BlockTileModel<TileEntitySolidifier, Machine<TileEntitySolidifier>>>> SOLIDIFIER =
            BLOCKS.register("solidification_chamber", () -> new BlockTileModel<>(EMBlockTypes.SOLIDIFIER, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())),
                    (block, properties) -> new ItemBlockTooltip<>(block, true, properties
                            .component(MekanismDataComponents.EJECTOR, AttachedEjector.DEFAULT)
                            .component(MekanismDataComponents.SIDE_CONFIG, EMAttachmedSideConfig.MELTER_MACHINE)
                    )).forItemHolder(holder -> holder
                    .addAttachmentOnlyContainers(ContainerType.FLUID, () -> FluidTanksBuilder.builder()
                            .addBasic(10_000,EMRecipeType.SOLIDIFICATION, EMInputRecipeCache.ItemFluidFluid::containsInputC)
                            .addBasic(10_000,EMRecipeType.SOLIDIFICATION, EMInputRecipeCache.ItemFluidFluid::containsInputB)
                            .build()
                    ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                            .addInput(EMRecipeType.SOLIDIFICATION, EMInputRecipeCache.ItemFluidFluid::containsInputA)
                            .addEnergy()
                            .build()
                    ));


    public static final BlockRegistryObject<EMBlockResource, EMItemBlockResource> BETTER_GOLD_BLOCK = registerResourceBlock(EMBlockResourceInfo.BETTER_GOLD);
    public static final BlockRegistryObject<EMBlockResource, EMItemBlockResource> PLASLITHERITE_BLOCK = registerResourceBlock(EMBlockResourceInfo.PLASLITHERITE);

    public static final BlockRegistryObject<Block,BlockItem> INFUSED_ALLOY_BLOCK = registerAlloyBlock(AlloyTier.INFUSED);
    public static final BlockRegistryObject<Block,BlockItem> REINFORCED_ALLOY_BLOCK = registerAlloyBlock(AlloyTier.REINFORCED);
    public static final BlockRegistryObject<Block,BlockItem> ATOMIC_ALLOY_BLOCK = registerAlloyBlock(AlloyTier.ATOMIC);
    public static final BlockRegistryObject<Block,BlockItem> HYPERCHARGED_ALLOY_BLOCK = registerAlloyBlock(EMAlloyTier.HYPERCHARGED);
    public static final BlockRegistryObject<Block,BlockItem> SUBATOMIC_ALLOY_BLOCK = registerAlloyBlock(EMAlloyTier.SUBATOMIC);
    public static final BlockRegistryObject<Block,BlockItem> SINGULAR_ALLOY_BLOCK = registerAlloyBlock(EMAlloyTier.SINGULAR);
    public static final BlockRegistryObject<Block,BlockItem> EXOVERSAL_ALLOY_BLOCK = registerAlloyBlock(EMAlloyTier.EXOVERSAL);
    public static final BlockRegistryObject<Block,BlockItem> CREATIVE_ALLOY_BLOCK = registerAlloyBlock(EMAlloyTier.CREATIVE);

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

    public static final BlockRegistryObject<BlockSmallTransmitter<TileEntityUniversalCable>, ItemBlockUniversalCable> OVERCLOCKED_UNIVERSAL_CABLE = registerUniversalCable(EMBlockTypes.OVERCLOCKED_UNIVERSAL_CABLE);
    public static final BlockRegistryObject<BlockSmallTransmitter<TileEntityUniversalCable>, ItemBlockUniversalCable> QUANTUM_UNIVERSAL_CABLE = registerUniversalCable(EMBlockTypes.QUANTUM_UNIVERSAL_CABLE);
    public static final BlockRegistryObject<BlockSmallTransmitter<TileEntityUniversalCable>, ItemBlockUniversalCable> DENSE_UNIVERSAL_CABLE = registerUniversalCable(EMBlockTypes.DENSE_UNIVERSAL_CABLE);
    public static final BlockRegistryObject<BlockSmallTransmitter<TileEntityUniversalCable>, ItemBlockUniversalCable> MULTIVERSAL_UNIVERSAL_CABLE = registerUniversalCable(EMBlockTypes.MULTIVERSAL_UNIVERSAL_CABLE);
    public static final BlockRegistryObject<BlockSmallTransmitter<TileEntityUniversalCable>, ItemBlockUniversalCable> CREATIVE_UNIVERSAL_CABLE = registerUniversalCable(EMBlockTypes.CREATIVE_UNIVERSAL_CABLE);

    public static final BlockRegistryObject<BlockLargeTransmitter<TileEntityMechanicalPipe>, ItemBlockMechanicalPipe> OVERCLOCKED_MECHANICAL_PIPE = registerMechanicalPipe(EMBlockTypes.OVERCLOCKED_MECHANICAL_PIPE);
    public static final BlockRegistryObject<BlockLargeTransmitter<TileEntityMechanicalPipe>, ItemBlockMechanicalPipe> QUANTUM_MECHANICAL_PIPE = registerMechanicalPipe(EMBlockTypes.QUANTUM_MECHANICAL_PIPE);
    public static final BlockRegistryObject<BlockLargeTransmitter<TileEntityMechanicalPipe>, ItemBlockMechanicalPipe> DENSE_MECHANICAL_PIPE = registerMechanicalPipe(EMBlockTypes.DENSE_MECHANICAL_PIPE);
    public static final BlockRegistryObject<BlockLargeTransmitter<TileEntityMechanicalPipe>, ItemBlockMechanicalPipe> MULTIVERSAL_MECHANICAL_PIPE = registerMechanicalPipe(EMBlockTypes.MULTIVERSAL_MECHANICAL_PIPE);
    public static final BlockRegistryObject<BlockLargeTransmitter<TileEntityMechanicalPipe>, ItemBlockMechanicalPipe> CREATIVE_MECHANICAL_PIPE = registerMechanicalPipe(EMBlockTypes.CREATIVE_MECHANICAL_PIPE);

    public static final BlockRegistryObject<BlockSmallTransmitter<TileEntityPressurizedTube>, ItemBlockPressurizedTube> OVERCLOCKED_PRESSURIZED_TUBE = registerPressurizedTube(EMBlockTypes.OVERCLOCKED_PRESSURIZED_TUBE);
    public static final BlockRegistryObject<BlockSmallTransmitter<TileEntityPressurizedTube>, ItemBlockPressurizedTube> QUANTUM_PRESSURIZED_TUBE = registerPressurizedTube(EMBlockTypes.QUANTUM_PRESSURIZED_TUBE);
    public static final BlockRegistryObject<BlockSmallTransmitter<TileEntityPressurizedTube>, ItemBlockPressurizedTube> DENSE_PRESSURIZED_TUBE = registerPressurizedTube(EMBlockTypes.DENSE_PRESSURIZED_TUBE);
    public static final BlockRegistryObject<BlockSmallTransmitter<TileEntityPressurizedTube>, ItemBlockPressurizedTube> MULTIVERSAL_PRESSURIZED_TUBE = registerPressurizedTube(EMBlockTypes.MULTIVERSAL_PRESSURIZED_TUBE);
    public static final BlockRegistryObject<BlockSmallTransmitter<TileEntityPressurizedTube>, ItemBlockPressurizedTube> CREATIVE_PRESSURIZED_TUBE = registerPressurizedTube(EMBlockTypes.CREATIVE_PRESSURIZED_TUBE);

    public static final BlockRegistryObject<BlockLargeTransmitter<TileEntityLogisticalTransporter>, ItemBlockLogisticalTransporter> OVERCLOCKED_LOGISTICAL_TRANSPORTER = registerLogisticalTransporter(EMBlockTypes.OVERCLOCKED_LOGISTICAL_TRANSPORTER);
    public static final BlockRegistryObject<BlockLargeTransmitter<TileEntityLogisticalTransporter>, ItemBlockLogisticalTransporter> QUANTUM_LOGISTICAL_TRANSPORTER = registerLogisticalTransporter(EMBlockTypes.QUANTUM_LOGISTICAL_TRANSPORTER);
    public static final BlockRegistryObject<BlockLargeTransmitter<TileEntityLogisticalTransporter>, ItemBlockLogisticalTransporter> DENSE_LOGISTICAL_TRANSPORTER = registerLogisticalTransporter(EMBlockTypes.DENSE_LOGISTICAL_TRANSPORTER);
    public static final BlockRegistryObject<BlockLargeTransmitter<TileEntityLogisticalTransporter>, ItemBlockLogisticalTransporter> MULTIVERSAL_LOGISTICAL_TRANSPORTER = registerLogisticalTransporter(EMBlockTypes.MULTIVERSAL_LOGISTICAL_TRANSPORTER);
    public static final BlockRegistryObject<BlockLargeTransmitter<TileEntityLogisticalTransporter>, ItemBlockLogisticalTransporter> CREATIVE_LOGISTICAL_TRANSPORTER = registerLogisticalTransporter(EMBlockTypes.CREATIVE_LOGISTICAL_TRANSPORTER);

    public static final BlockRegistryObject<BlockSmallTransmitter<TileEntityThermodynamicConductor>, ItemBlockThermodynamicConductor> OVERCLOCKED_THERMODYNAMIC_CONDUCTOR = registerThermodynamicConductor(EMBlockTypes.OVERCLOCKED_THERMODYNAMIC_CONDUCTOR);
    public static final BlockRegistryObject<BlockSmallTransmitter<TileEntityThermodynamicConductor>, ItemBlockThermodynamicConductor> QUANTUM_THERMODYNAMIC_CONDUCTOR = registerThermodynamicConductor(EMBlockTypes.QUANTUM_THERMODYNAMIC_CONDUCTOR);
    public static final BlockRegistryObject<BlockSmallTransmitter<TileEntityThermodynamicConductor>, ItemBlockThermodynamicConductor> DENSE_THERMODYNAMIC_CONDUCTOR = registerThermodynamicConductor(EMBlockTypes.DENSE_THERMODYNAMIC_CONDUCTOR);
    public static final BlockRegistryObject<BlockSmallTransmitter<TileEntityThermodynamicConductor>, ItemBlockThermodynamicConductor> MULTIVERSAL_THERMODYNAMIC_CONDUCTOR = registerThermodynamicConductor(EMBlockTypes.MULTIVERSAL_THERMODYNAMIC_CONDUCTOR);
    public static final BlockRegistryObject<BlockSmallTransmitter<TileEntityThermodynamicConductor>, ItemBlockThermodynamicConductor> CREATIVE_THERMODYNAMIC_CONDUCTOR = registerThermodynamicConductor(EMBlockTypes.CREATIVE_THERMODYNAMIC_CONDUCTOR);

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


    private static void registerOre(OreType ore) {
        String name = ore.getResource().getRegistrySuffix() + "_ore";
        BlockRegistryObject<BlockOre, ItemBlockTooltip<BlockOre>> nether = registerBlock("netherrack_"+name, () -> new BlockOre(ore,BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERRACK)));
        BlockRegistryObject<BlockOre, ItemBlockTooltip<BlockOre>> endStone = registerBlock("end_stone_" + name,
                () -> new BlockOre(ore, BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE)));
        BlockRegistryObject<BlockOre, ItemBlockTooltip<BlockOre>> aether = registerBlock("holystone_" + name,
                () -> new BlockOre(ore, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).mapColor(MapColor.WOOL).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F).requiresCorrectToolForDrops()));
        BlockRegistryObject<BlockOre, ItemBlockTooltip<BlockOre>> undergarden = registerBlock("depthrock_" + name,
                () -> new BlockOre(ore, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.TERRACOTTA_LIGHT_GREEN).strength(1.5F, 6.0F).sound(SoundType.BASALT).requiresCorrectToolForDrops()));
        BlockRegistryObject<BlockOre, ItemBlockTooltip<BlockOre>> undergarden2 = registerBlock("shiverstone_" + name,
                () -> new BlockOre(ore, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.GLOW_LICHEN).strength(3.5F, 12F).sound(SoundType.NETHER_BRICKS).requiresCorrectToolForDrops().friction(0.98F)));
    }

    private static <BLOCK extends Block & IHasDescription> BlockRegistryObject<BLOCK, ItemBlockTooltip<BLOCK>> registerBlock(String name,
                                                                                                                             Supplier<? extends BLOCK> blockSupplier) {
        return BLOCKS.register(name, blockSupplier, ItemBlockTooltip::new);
    }

    private static <BLOCK extends Block & IHasDescription> BlockRegistryObject<BLOCK, ItemBlockTooltip<BLOCK>> registerBlock(String name,
                                                                                                                             Supplier<? extends BLOCK> blockSupplier, Rarity rarity) {
        return BLOCKS.register(name, blockSupplier, (block, props) -> new ItemBlockTooltip<>(block, props.rarity(rarity)));
    }


    private static BlockRegistryObject<BlockTieredPersonnalChest, ItemBlockTieredPersonalStorage<BlockTieredPersonnalChest>> registerPersonalChest(BlockTypeTile<TileEntityTieredPersonalChest> type,PersonalStorageTier tier) {
        return registerTieredBlock(tier, "_personal_chest", () -> new BlockTieredPersonnalChest(type,tier), (block,props) -> new ItemBlockTieredPersonalStorage<>(block, Stats.OPEN_CHEST,block.getTier()));
    }
    private static BlockRegistryObject<BlockTieredPersonnalBarrel, ItemBlockTieredPersonalStorage<BlockTieredPersonnalBarrel>> registerPersonalBarrel(BlockTypeTile<TileEntityTieredPersonalBarrel> type, PersonalStorageTier tier) {
        return registerTieredBlock(tier, "_personal_barrel", () -> new BlockTieredPersonnalBarrel(type,tier), (block,props) -> new ItemBlockTieredPersonalStorage<>(block, Stats.OPEN_CHEST,block.getTier()));
    }

    private static BlockRegistryObject<BlockBin, ItemBlockBin> registerBin(BlockTypeTile<TileEntityBin> type) {
        return registerTieredBlock(type, "_bin", color -> new BlockBin(type, properties -> properties.mapColor(color)), ItemBlockBin::new)
                .forItemHolder(holder -> holder.addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                        .addSlot(ComponentBackedBinInventorySlot::create)
                        .build()
                ));
    }

    private static BlockRegistryObject<BlockTile<TileEntityInductionCell, BlockTypeTile<TileEntityInductionCell>>, ItemBlockInductionCell> registerInductionCell(BlockTypeTile<TileEntityInductionCell> type) {
        return registerTieredBlock(type, "_induction_cell", color -> new BlockTile<>(type, properties -> properties.mapColor(color)), ItemBlockInductionCell::new);
    }

    private static BlockRegistryObject<BlockTile<TileEntityInductionProvider, BlockTypeTile<TileEntityInductionProvider>>, ItemBlockInductionProvider> registerInductionProvider(BlockTypeTile<TileEntityInductionProvider> type) {
        return registerTieredBlock(type, "_induction_provider", color -> new BlockTile<>(type, properties -> properties.mapColor(color)), ItemBlockInductionProvider::new);
    }

    private static BlockRegistryObject<BlockFluidTank, ItemBlockFluidTank> registerFluidTank(Machine<TileEntityFluidTank> type) {
        return registerTieredBlock(type, "_fluid_tank", () -> new BlockFluidTank(type), ItemBlockFluidTank::new)
                .forItemHolder(holder -> holder
                        .addAttachedContainerCapabilities(ContainerType.FLUID, () -> FluidTanksBuilder.builder()
                                .addTank(ComponentBackedFluidTankFluidTank::create)
                                .build()
                        ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                                .addFluidInputSlot(0)
                                .addOutput()
                                .build()
                        )
                );
    }

    private static BlockRegistryObject<BlockEnergyCube, ItemBlockEnergyCube> registerEnergyCube(Machine<TileEntityEnergyCube> type) {
        return registerTieredBlock(type, "_energy_cube", () -> new BlockEnergyCube(type), ItemBlockEnergyCube::new)
                .forItemHolder(holder -> holder.addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                        .addEnergy()
                        .addDrainEnergy()
                        .build()
                ));
    }

    private static BlockRegistryObject<BlockSmallTransmitter<TileEntityUniversalCable>, ItemBlockUniversalCable> registerUniversalCable(
            BlockTypeTile<TileEntityUniversalCable> type) {
        return registerTieredBlock(type, "_universal_cable", () -> new BlockSmallTransmitter<>(type), ItemBlockUniversalCable::new);
    }

    private static BlockRegistryObject<BlockLargeTransmitter<TileEntityMechanicalPipe>, ItemBlockMechanicalPipe> registerMechanicalPipe(
            BlockTypeTile<TileEntityMechanicalPipe> type) {
        return registerTieredBlock(type, "_mechanical_pipe", () -> new BlockLargeTransmitter<>(type), ItemBlockMechanicalPipe::new);
    }

    private static BlockRegistryObject<BlockSmallTransmitter<TileEntityPressurizedTube>, ItemBlockPressurizedTube> registerPressurizedTube(
            BlockTypeTile<TileEntityPressurizedTube> type) {
        return registerTieredBlock(type, "_pressurized_tube", () -> new BlockSmallTransmitter<>(type), ItemBlockPressurizedTube::new);
    }

    private static BlockRegistryObject<BlockLargeTransmitter<TileEntityLogisticalTransporter>, ItemBlockLogisticalTransporter> registerLogisticalTransporter(
            BlockTypeTile<TileEntityLogisticalTransporter> type) {
        return registerTieredBlock(type, "_logistical_transporter", () -> new BlockLargeTransmitter<>(type), ItemBlockLogisticalTransporter::new);
    }

    private static BlockRegistryObject<BlockSmallTransmitter<TileEntityThermodynamicConductor>, ItemBlockThermodynamicConductor> registerThermodynamicConductor(
            BlockTypeTile<TileEntityThermodynamicConductor> type) {
        return registerTieredBlock(type, "_thermodynamic_conductor", () -> new BlockSmallTransmitter<>(type), ItemBlockThermodynamicConductor::new);
    }

    private static BlockRegistryObject<BlockTileModel<TileEntityChemicalTank, Machine<TileEntityChemicalTank>>, ItemBlockChemicalTank> registerChemicalTank(
            Machine<TileEntityChemicalTank> type) {
        return registerTieredBlock(type, "_chemical_tank", color -> new BlockTileModel<>(type, properties -> properties.mapColor(color)), ItemBlockChemicalTank::new)
                .forItemHolder(holder -> holder
                        .addAttachedContainerCapabilities(ContainerType.CHEMICAL, () -> ChemicalTanksBuilder.builder()
                                .addTank(ComponentBackedChemicalTankTank::create).build()
                        ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                                .addChemicalDrainSlot(0)
                                .addChemicalFillSlot(0)
                                .build()
                        )
                );
    }

    private static <TILE extends TileEntityFactory<?>> BlockRegistryObject<BlockFactory<?>, ItemBlockFactory> registerFactory(Factory<TILE> type) {
        FactoryTier tier = (FactoryTier) type.get(AttributeTier.class).tier();
        BlockRegistryObject<BlockFactory<?>, ItemBlockFactory> factory = registerTieredBlock(tier, "_" + type.getFactoryType().getRegistryNameComponent() + "_factory", () -> new BlockFactory<>(type), ItemBlockFactory::new);
        factory.forItemHolder(holder -> {
            int processes = tier.processes;
            Predicate<ItemStack> finalPredicate = getItemStackPredicate(type);
            if (type.getFactoryType().equals(EMFactoryType.ALLOYING)){
                holder.addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                        .addBasicFactorySlots(processes, finalPredicate)
                        .addInput(EMRecipeType.ALLOYING, EMInputRecipeCache.TripleItem::containsInputB)
                        .addInput(EMRecipeType.ALLOYING, EMInputRecipeCache.TripleItem::containsInputC)
                        .addEnergy()
                        .build()
                );
            }
            switch (type.getFactoryType()) {
                case SMELTING, ENRICHING, CRUSHING -> holder.addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                        .addBasicFactorySlots(processes, finalPredicate)
                        .addEnergy()
                        .build()
                );
                case COMPRESSING, INJECTING, PURIFYING -> holder
                        .addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> ChemicalTanksBuilder.builder()
                                .addBasic(TileEntityAdvancedElectricMachine.MAX_GAS * processes, switch (type.getFactoryType()) {
                                    case COMPRESSING -> MekanismRecipeType.COMPRESSING;
                                    case INJECTING -> MekanismRecipeType.INJECTING;
                                    case PURIFYING -> MekanismRecipeType.PURIFYING;
                                    default -> throw new IllegalStateException("Factory type doesn't have a known gas recipe");
                                }, InputRecipeCache.ItemChemical::containsInputB)
                                .build()
                        ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                                .addBasicFactorySlots(processes, finalPredicate)
                                .addChemicalFillOrConvertSlot(0)
                                .addEnergy()
                                .build()
                        );
                case COMBINING -> holder.addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                        .addBasicFactorySlots(processes, finalPredicate)
                        .addInput(MekanismRecipeType.COMBINING, InputRecipeCache.DoubleItem::containsInputB)
                        .addEnergy()
                        .build()
                );
                case INFUSING -> holder
                        .addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> ChemicalTanksBuilder.builder()
                                .addBasic(TileEntityMetallurgicInfuser.MAX_INFUSE * processes, MekanismRecipeType.METALLURGIC_INFUSING, InputRecipeCache.ItemChemical::containsInputB)
                                .build()
                        ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                                .addBasicFactorySlots(processes, finalPredicate)
                                .addInfusionFillOrConvertSlot(0)
                                .addEnergy()
                                .build()
                        );
                case SAWING -> holder.addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                        .addBasicFactorySlots(processes, finalPredicate, true)
                        .addEnergy()
                        .build()
                );
            }

        });
        return factory;
    }

    private static <TILE extends TileEntityFactory<?>> @Nullable Predicate<ItemStack> getItemStackPredicate(Factory<TILE> type) {
        Predicate<ItemStack> recipeInputPredicate = null;
        if (type.getFactoryType().equals(EMFactoryType.ALLOYING)) {
            recipeInputPredicate = s -> EMRecipeType.ALLOYING.getInputCache().containsInputA(null, s);
        } else {
            switch (type.getFactoryType()) {
                case SMELTING -> recipeInputPredicate = s -> MekanismRecipeType.SMELTING.getInputCache().containsInput(null, s);
                case ENRICHING -> recipeInputPredicate = s -> MekanismRecipeType.ENRICHING.getInputCache().containsInput(null, s);
                case CRUSHING -> recipeInputPredicate = s -> MekanismRecipeType.CRUSHING.getInputCache().containsInput(null, s);
                case COMPRESSING -> recipeInputPredicate = s -> MekanismRecipeType.COMPRESSING.getInputCache().containsInputA(null, s);
                case COMBINING -> recipeInputPredicate = s -> MekanismRecipeType.COMBINING.getInputCache().containsInputA(null, s);
                case PURIFYING -> recipeInputPredicate = s -> MekanismRecipeType.PURIFYING.getInputCache().containsInputA(null, s);
                case INJECTING -> recipeInputPredicate = s -> MekanismRecipeType.INJECTING.getInputCache().containsInputA(null, s);
                case INFUSING -> recipeInputPredicate = s -> MekanismRecipeType.METALLURGIC_INFUSING.getInputCache().containsInputA(null, s);
                case SAWING -> recipeInputPredicate = s -> MekanismRecipeType.SAWING.getInputCache().containsInput(null, s);
            }
        }
        return recipeInputPredicate;
    }

    private static <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerTieredBlock(BlockType type, String suffix,
                                                                                                                      Function<MapColor, ? extends BLOCK> blockSupplier, BiFunction<BLOCK, Item.Properties, ITEM> itemCreator) {
        ITier tier = type.get(AttributeTier.class).tier();
        return registerTieredBlock(tier, suffix, () -> blockSupplier.apply(tier.getBaseTier().getMapColor()), itemCreator);
    }

    private static <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerTieredBlock(BlockType type, String suffix,
                                                                                                                      Supplier<? extends BLOCK> blockSupplier, BiFunction<BLOCK, Item.Properties, ITEM> itemCreator) {
        return registerTieredBlock(type.get(AttributeTier.class).tier(), suffix, blockSupplier, itemCreator);
    }

    private static <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerTieredBlock(ITier tier, String suffix,
                                                                                                                      Supplier<? extends BLOCK> blockSupplier, BiFunction<BLOCK, Item.Properties, ITEM> itemCreator) {
        return BLOCKS.register(tier.getBaseTier().getLowerName() + suffix, blockSupplier, itemCreator);
    }
    private static BlockRegistryObject<Block, BlockItem> registerAlloyBlock(AlloyTier tier) {
        ((InitializableEnum) (Object)tier).evolvedmekanism$initNewValues();
        return BLOCKS.register("block_alloy_"+tier.getName(), ()->new Block(BlockBehaviour.Properties.of().strength(5,9)), BlockItem::new);
    }


    private static BlockRegistryObject<EMBlockResource, EMItemBlockResource> registerResourceBlock(EMBlockResourceInfo resource) {
        return BLOCKS.register("block_" + resource.getRegistrySuffix(), () -> new EMBlockResource(resource), (block, properties) -> {
            if (!block.getResourceInfo().burnsInFire()) {
                properties = properties.fireResistant();
            }
            return new EMItemBlockResource(block, properties);
        });
    }
    public static BlockRegistryObject<BlockFactory<?>, ItemBlockFactory> getFactory(@NotNull FactoryTier tier, @NotNull FactoryType type) {
        return FACTORIES.get(tier, type);
    }

}
