package fr.iglee42.emgenerators.registries;

import fr.iglee42.emgenerators.items.ItemBlockTieredLunarGenerator;
import fr.iglee42.emgenerators.items.ItemBlockTieredSolarGenerator;
import fr.iglee42.emgenerators.tile.TileEntityLunarGenerator;
import fr.iglee42.emgenerators.tile.TileEntityTieredAdvancedLunarGenerator;
import fr.iglee42.emgenerators.tile.TileEntityTieredAdvancedSolarGenerator;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMContainerTypes;
import mekanism.common.attachments.containers.ContainerType;
import mekanism.common.attachments.containers.item.ItemSlotsBuilder;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.generators.common.content.blocktype.Generator;
import mekanism.generators.common.registries.GeneratorsBlockTypes;
import mekanism.generators.common.tile.TileEntityAdvancedSolarGenerator;
import mekanism.generators.common.tile.TileEntitySolarGenerator;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;

public class EMGenBlocks {

    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(EvolvedMekanism.MODID);

    public static final BlockRegistryObject<BlockTileModel<TileEntityTieredAdvancedSolarGenerator, Generator<TileEntityTieredAdvancedSolarGenerator>>, ItemBlockTieredSolarGenerator>
            ADVANCED_SOLAR_GENERATOR = registerTieredSolarGenerator("advanced_solar_generator", EMGenBlockTypes.ADVANCED_SOLAR_GENERATOR),
            ELITE_SOLAR_GENERATOR = registerTieredSolarGenerator("elite_solar_generator", EMGenBlockTypes.ELITE_SOLAR_GENERATOR),
            ULTIMATE_SOLAR_GENERATOR = registerTieredSolarGenerator("ultimate_solar_generator", EMGenBlockTypes.ULTIMATE_SOLAR_GENERATOR),
            OVERCLOCKED_SOLAR_GENERATOR = registerTieredSolarGenerator("overclocked_solar_generator", EMGenBlockTypes.OVERCLOCKED_SOLAR_GENERATOR),
            QUANTUM_SOLAR_GENERATOR = registerTieredSolarGenerator("quantum_solar_generator", EMGenBlockTypes.QUANTUM_SOLAR_GENERATOR),
            DENSE_SOLAR_GENERATOR = registerTieredSolarGenerator("dense_solar_generator", EMGenBlockTypes.DENSE_SOLAR_GENERATOR),
            MULTIVERSAL_SOLAR_GENERATOR = registerTieredSolarGenerator("multiversal_solar_generator", EMGenBlockTypes.MULTIVERSAL_SOLAR_GENERATOR),
            CREATIVE_SOLAR_GENERATOR = registerTieredSolarGenerator("creative_solar_generator", EMGenBlockTypes.CREATIVE_SOLAR_GENERATOR);


    public static final BlockRegistryObject<BlockTileModel<TileEntityLunarGenerator, Generator<TileEntityLunarGenerator>>, ItemBlockTooltip<BlockTileModel<TileEntityLunarGenerator, Generator<TileEntityLunarGenerator>>>> LUNAR_GENERATOR =
            BLOCKS.registerDetails("lunar_generator", () -> new BlockTileModel<>(EMGenBlockTypes.LUNAR_GENERATOR, properties -> properties.mapColor(MapColor.COLOR_PINK)))
                    .forItemHolder(holder -> holder.addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder().addEnergy().build()));

    public static final BlockRegistryObject<BlockTileModel<TileEntityTieredAdvancedLunarGenerator, Generator<TileEntityTieredAdvancedLunarGenerator>>, ItemBlockTieredLunarGenerator>
            BASIC_ADVANCED_LUNAR_GENERATOR = registerTieredLunarGenerator("basic_advanced_lunar_generator", EMGenBlockTypes.BASIC_ADVANCED_LUNAR_GENERATOR),
            ADVANCED_LUNAR_GENERATOR = registerTieredLunarGenerator("advanced_lunar_generator", EMGenBlockTypes.ADVANCED_LUNAR_GENERATOR),
            ELITE_LUNAR_GENERATOR = registerTieredLunarGenerator("elite_lunar_generator", EMGenBlockTypes.ELITE_LUNAR_GENERATOR),
            ULTIMATE_LUNAR_GENERATOR = registerTieredLunarGenerator("ultimate_lunar_generator", EMGenBlockTypes.ULTIMATE_LUNAR_GENERATOR),
            OVERCLOCKED_LUNAR_GENERATOR = registerTieredLunarGenerator("overclocked_lunar_generator", EMGenBlockTypes.OVERCLOCKED_LUNAR_GENERATOR),
            QUANTUM_LUNAR_GENERATOR = registerTieredLunarGenerator("quantum_lunar_generator", EMGenBlockTypes.QUANTUM_LUNAR_GENERATOR),
            DENSE_LUNAR_GENERATOR = registerTieredLunarGenerator("dense_lunar_generator", EMGenBlockTypes.DENSE_LUNAR_GENERATOR),
            MULTIVERSAL_LUNAR_GENERATOR = registerTieredLunarGenerator("multiversal_lunar_generator", EMGenBlockTypes.MULTIVERSAL_LUNAR_GENERATOR),
            CREATIVE_LUNAR_GENERATOR = registerTieredLunarGenerator("creative_lunar_generator", EMGenBlockTypes.CREATIVE_LUNAR_GENERATOR);    
    
    private static BlockRegistryObject<BlockTileModel<TileEntityTieredAdvancedSolarGenerator, Generator<TileEntityTieredAdvancedSolarGenerator>>, ItemBlockTieredSolarGenerator> registerTieredSolarGenerator(String name, Generator<TileEntityTieredAdvancedSolarGenerator> type) {
        return BLOCKS.register(name,
                () -> new BlockTileModel<>(type, props -> props.mapColor(MapColor.COLOR_BLUE)),
                ItemBlockTieredSolarGenerator::new
        );
    }

    private static BlockRegistryObject<BlockTileModel<TileEntityTieredAdvancedLunarGenerator, Generator<TileEntityTieredAdvancedLunarGenerator>>, ItemBlockTieredLunarGenerator> registerTieredLunarGenerator(String name, Generator<TileEntityTieredAdvancedLunarGenerator> type) {
        return BLOCKS.register(name,
                () -> new BlockTileModel<>(type, props -> props.mapColor(MapColor.COLOR_PINK)),
                ItemBlockTieredLunarGenerator::new
        );
    }

    
    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
