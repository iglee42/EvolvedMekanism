package fr.iglee42.emgenerators.registries;

import fr.iglee42.emgenerators.items.ItemBlockTieredSolarGenerator;
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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class EMGenBlocks {

    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(EvolvedMekanism.MODID);

    public static final BlockRegistryObject<BlockTileModel<TileEntityTieredAdvancedSolarGenerator, Generator<TileEntityTieredAdvancedSolarGenerator>>, ItemBlockTieredSolarGenerator>
            ADVANCED_SOLAR_GENERATOR = registerTieredSolarGenerator("advanced_solar_generator", EMGenBlockTypes.ADVANCED_SOLAR_GENERATOR),
            ELITE_SOLAR_GENERATOR = registerTieredSolarGenerator("elite_solar_generator", EMGenBlockTypes.ELITE_SOLAR_GENERATOR),
            ULTIMATE_SOLAR_GENERATOR = registerTieredSolarGenerator("ultimate_solar_generator", EMGenBlockTypes.ULTIMATE_SOLAR_GENERATOR),
            OVERCLOCKED_SOLAR_GENERATOR = registerTieredSolarGenerator("overclocked_solar_generator", EMGenBlockTypes.OVERCLOCKED_SOLAR_GENERATOR),
            QUANTUM_SOLAR_GENERATOR = registerTieredSolarGenerator("quantum_solar_generator", EMGenBlockTypes.QUANTUM_SOLAR_GENERATOR),
            DENSE_SOLAR_GENERATOR = registerTieredSolarGenerator("dense_solar_generator", EMGenBlockTypes.DENSE_SOLAR_GENERATOR),
            MULTIVERSAL_SOLAR_GENERATOR = registerTieredSolarGenerator("multiversal_solar_generator", EMGenBlockTypes.MULTIVERSAL_SOLAR_GENERATOR);


    private static BlockRegistryObject<BlockTileModel<TileEntityTieredAdvancedSolarGenerator, Generator<TileEntityTieredAdvancedSolarGenerator>>, ItemBlockTieredSolarGenerator> registerTieredSolarGenerator(String name, Generator<TileEntityTieredAdvancedSolarGenerator> type) {
        return BLOCKS.register(name,
                () -> new BlockTileModel<>(type, props -> props.mapColor(MapColor.COLOR_BLUE)),
                ItemBlockTieredSolarGenerator::new
        );
    }

    
    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
