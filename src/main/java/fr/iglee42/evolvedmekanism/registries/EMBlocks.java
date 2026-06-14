package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.blocks.EMBlockResource;
import fr.iglee42.evolvedmekanism.items.EMItemBlockResource;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTCasing;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTPort;
import fr.iglee42.evolvedmekanism.tiers.EMAlloyTier;
import fr.iglee42.evolvedmekanism.tiles.TileEntitySuperchargingElement;
import fr.iglee42.evolvedmekanism.tiles.TileEntitySuperchargingElementMk2;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityAlloyer;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityChemixer;
import mekanism.api.tier.AlloyTier;
import mekanism.common.block.BlockOre;
import mekanism.common.block.interfaces.IHasDescription;
import mekanism.common.block.prefab.BlockBasicMultiblock;
import mekanism.common.block.prefab.BlockFactoryMachine;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.block.transmitter.*;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.item.block.*;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.item.block.transmitter.*;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.resource.BlockResourceInfo;
import mekanism.common.resource.ore.OreType;
import mekanism.common.tier.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

@SuppressWarnings("ALL")
public class EMBlocks {
    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(EvolvedMekanism.MODID);

    public static final BlockRegistryObject<BlockFactoryMachine<TileEntityAlloyer, Machine.FactoryMachine<TileEntityAlloyer>>, ItemBlockMachine> ALLOYER = BLOCKS.register("alloyer", () -> new BlockFactoryMachine<>(EMBlockTypes.ALLOYER, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), ItemBlockMachine::new);

    static {
        registerOre(OreType.URANIUM);
    }
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityAPTCasing>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityAPTCasing>>> APT_CASING = registerBlock("apt_casing", () -> new BlockBasicMultiblock<>(EMBlockTypes.APT_CASING, properties -> properties.mapColor(MapColor.COLOR_MAGENTA)), Rarity.EPIC);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityAPTPort>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityAPTPort>>> APT_PORT = registerBlock("apt_port", () -> new BlockBasicMultiblock<>(EMBlockTypes.APT_PORT, properties -> properties.mapColor(MapColor.COLOR_MAGENTA)), Rarity.EPIC);
    public static final BlockRegistryObject<BlockTile<TileEntitySuperchargingElement, BlockTypeTile<TileEntitySuperchargingElement>>, ItemBlockTooltip<BlockTile<TileEntitySuperchargingElement, BlockTypeTile<TileEntitySuperchargingElement>>>> SUPERCHARGING_ELEMENT = registerBlock("supercharging_element", () -> new BlockTile<>(EMBlockTypes.SUPERCHARGING_ELEMENT, properties -> properties.mapColor(MapColor.COLOR_MAGENTA)));
    public static final BlockRegistryObject<BlockTile<TileEntitySuperchargingElementMk2, BlockTypeTile<TileEntitySuperchargingElementMk2>>, ItemBlockTooltip<BlockTile<TileEntitySuperchargingElementMk2, BlockTypeTile<TileEntitySuperchargingElementMk2>>>> SUPERCHARGING_ELEMENT_MK2 = registerBlock("supercharging_element_mk2", () -> new BlockTile<>(EMBlockTypes.SUPERCHARGING_ELEMENT_MK2, properties -> properties.mapColor(MapColor.COLOR_MAGENTA)));

    public static final BlockRegistryObject<BlockTileModel<TileEntityChemixer, Machine<TileEntityChemixer>>, ItemBlockMachine> CHEMIXER = BLOCKS.register("chemixer", () -> new BlockTileModel<>(EMBlockTypes.CHEMIXER, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), ItemBlockMachine::new);

    public static final BlockRegistryObject<EMBlockResource, EMItemBlockResource> BETTER_GOLD_BLOCK = registerResourceBlock(EMBlockResourceInfo.BETTER_GOLD);
    public static final BlockRegistryObject<EMBlockResource, EMItemBlockResource> PLASLITHERITE_BLOCK = registerResourceBlock(EMBlockResourceInfo.PLASLITHERITE);
    public static final BlockRegistryObject<EMBlockResource, EMItemBlockResource> REFINED_REDSTONE_BLOCK = registerResourceBlock(EMBlockResourceInfo.REFINED_REDSTONE);

    public static final BlockRegistryObject<Block,BlockItem> INFUSED_ALLOY_BLOCK = registerAlloyBlock(AlloyTier.INFUSED);
    public static final BlockRegistryObject<Block,BlockItem> REINFORCED_ALLOY_BLOCK = registerAlloyBlock(AlloyTier.REINFORCED);
    public static final BlockRegistryObject<Block,BlockItem> ATOMIC_ALLOY_BLOCK = registerAlloyBlock(AlloyTier.ATOMIC);
    public static final BlockRegistryObject<Block,BlockItem> HYPERCHARGED_ALLOY_BLOCK = registerAlloyBlock(EMAlloyTier.HYPERCHARGED);
    public static final BlockRegistryObject<Block,BlockItem> SUBATOMIC_ALLOY_BLOCK = registerAlloyBlock(EMAlloyTier.SUBATOMIC);
    public static final BlockRegistryObject<Block,BlockItem> SINGULAR_ALLOY_BLOCK = registerAlloyBlock(EMAlloyTier.SINGULAR);
    public static final BlockRegistryObject<Block,BlockItem> EXOVERSAL_ALLOY_BLOCK = registerAlloyBlock(EMAlloyTier.EXOVERSAL);
    public static final BlockRegistryObject<Block,BlockItem> CREATIVE_ALLOY_BLOCK = registerAlloyBlock(EMAlloyTier.CREATIVE);

    private static void registerOre(OreType ore) {
        String name = ore.getResource().getRegistrySuffix() + "_ore";
        BlockRegistryObject<BlockOre, ItemBlockTooltip<BlockOre>> nether = registerBlock("netherrack_"+name, () -> new BlockOre(ore,BlockBehaviour.Properties.copy(Blocks.NETHERRACK)));
        BlockRegistryObject<BlockOre, ItemBlockTooltip<BlockOre>> endStone = registerBlock("end_stone_" + name,
                () -> new BlockOre(ore, BlockBehaviour.Properties.copy(Blocks.END_STONE)));
    }

    private static <BLOCK extends Block & IHasDescription> BlockRegistryObject<BLOCK, ItemBlockTooltip<BLOCK>> registerBlock(String name,
                                                                                                                             Supplier<? extends BLOCK> blockSupplier) {
        return BLOCKS.registerDefaultProperties(name, blockSupplier, ItemBlockTooltip::new);
    }

    private static <BLOCK extends Block & IHasDescription> BlockRegistryObject<BLOCK, ItemBlockTooltip<BLOCK>> registerBlock(String name,
                                                                                                                             Supplier<? extends BLOCK> blockSupplier, @SuppressWarnings("SameParameterValue") Rarity rarity) {
        return BLOCKS.registerDefaultProperties(name, blockSupplier, (block, props) -> new ItemBlockTooltip<>(block, props.rarity(rarity)));
    }


    private static BlockRegistryObject<Block, BlockItem> registerAlloyBlock(AlloyTier tier) {
        return BLOCKS.register("block_alloy_"+tier.getName(), ()->new Block(BlockBehaviour.Properties.of().strength(5,9)), b->new BlockItem(b,new Item.Properties()));
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