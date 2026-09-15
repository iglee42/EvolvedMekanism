package fr.iglee42.emgenerators.registries;

import fr.iglee42.emgenerators.tile.TileEntityLunarGenerator;
import fr.iglee42.emgenerators.tile.TileEntityTieredAdvancedLunarGenerator;
import fr.iglee42.emgenerators.tile.TileEntityTieredAdvancedSolarGenerator;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraftforge.eventbus.api.IEventBus;

public class EMGenContainerTypes {

    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(EvolvedMekanism.MODID);
    
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityTieredAdvancedSolarGenerator>> TIERED_ADVANCED_SOLAR_GENERATOR = CONTAINER_TYPES
            .register("tiered_advanced_solar_generator", TileEntityTieredAdvancedSolarGenerator.class);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityLunarGenerator>> LUNAR_GENERATOR = CONTAINER_TYPES
            .register("lunar_generator", TileEntityLunarGenerator.class);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityTieredAdvancedLunarGenerator>> TIERED_ADVANCED_LUNAR_GENERATOR = CONTAINER_TYPES
            .register("tiered_advanced_lunar_generator", TileEntityTieredAdvancedLunarGenerator.class);
    
    public static void register(IEventBus bus) {
        CONTAINER_TYPES.register(bus);
    }
}
