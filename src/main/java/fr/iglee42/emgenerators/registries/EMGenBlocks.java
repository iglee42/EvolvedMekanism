package fr.iglee42.emgenerators.registries;

import fr.iglee42.emgenerators.tile.TileEntityTieredAdvancedSolarGenerator;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMContainerTypes;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.generators.common.content.blocktype.Generator;
import mekanism.generators.common.registries.GeneratorsBlockTypes;
import mekanism.generators.common.tile.TileEntityAdvancedSolarGenerator;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;

public class EMGenBlocks {

    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(EvolvedMekanism.MODID);


    public static final BlockRegistryObject<BlockTileModel<TileEntityTieredAdvancedSolarGenerator, Generator<TileEntityTieredAdvancedSolarGenerator>>, ItemBlockMachine> ADVANCED_SOLAR_GENERATOR = BLOCKS.register("advanced_solar_generator", () -> new BlockTileModel<>(EMGenBlockTypes.ADVANCED_SOLAR_GENERATOR, properties -> properties.mapColor(MapColor.COLOR_BLUE)), ItemBlockMachine::new);
    public static final BlockRegistryObject<BlockTileModel<TileEntityTieredAdvancedSolarGenerator, Generator<TileEntityTieredAdvancedSolarGenerator>>, ItemBlockMachine> ELITE_SOLAR_GENERATOR = BLOCKS.register("elite_solar_generator", () -> new BlockTileModel<>(EMGenBlockTypes.ELITE_SOLAR_GENERATOR, properties -> properties.mapColor(MapColor.COLOR_BLUE)), ItemBlockMachine::new);
    public static final BlockRegistryObject<BlockTileModel<TileEntityTieredAdvancedSolarGenerator, Generator<TileEntityTieredAdvancedSolarGenerator>>, ItemBlockMachine> ULTIMATE_SOLAR_GENERATOR = BLOCKS.register("ultimate_solar_generator", () -> new BlockTileModel<>(EMGenBlockTypes.ULTIMATE_SOLAR_GENERATOR, properties -> properties.mapColor(MapColor.COLOR_BLUE)), ItemBlockMachine::new);
    public static final BlockRegistryObject<BlockTileModel<TileEntityTieredAdvancedSolarGenerator, Generator<TileEntityTieredAdvancedSolarGenerator>>, ItemBlockMachine> OVERCLOCKED_SOLAR_GENERATOR = BLOCKS.register("overclocked_solar_generator", () -> new BlockTileModel<>(EMGenBlockTypes.OVERCLOCKED_SOLAR_GENERATOR, properties -> properties.mapColor(MapColor.COLOR_BLUE)), ItemBlockMachine::new);
    public static final BlockRegistryObject<BlockTileModel<TileEntityTieredAdvancedSolarGenerator, Generator<TileEntityTieredAdvancedSolarGenerator>>, ItemBlockMachine> QUANTUM_SOLAR_GENERATOR = BLOCKS.register("quantum_solar_generator", () -> new BlockTileModel<>(EMGenBlockTypes.QUANTUM_SOLAR_GENERATOR, properties -> properties.mapColor(MapColor.COLOR_BLUE)), ItemBlockMachine::new);
    public static final BlockRegistryObject<BlockTileModel<TileEntityTieredAdvancedSolarGenerator, Generator<TileEntityTieredAdvancedSolarGenerator>>, ItemBlockMachine> DENSE_SOLAR_GENERATOR = BLOCKS.register("dense_solar_generator", () -> new BlockTileModel<>(EMGenBlockTypes.DENSE_SOLAR_GENERATOR, properties -> properties.mapColor(MapColor.COLOR_BLUE)), ItemBlockMachine::new);
    public static final BlockRegistryObject<BlockTileModel<TileEntityTieredAdvancedSolarGenerator, Generator<TileEntityTieredAdvancedSolarGenerator>>, ItemBlockMachine> MULTIVERSAL_SOLAR_GENERATOR = BLOCKS.register("multiversal_solar_generator", () -> new BlockTileModel<>(EMGenBlockTypes.MULTIVERSAL_SOLAR_GENERATOR, properties -> properties.mapColor(MapColor.COLOR_BLUE)), ItemBlockMachine::new);

    
    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
