package fr.iglee42.emgenerators.registries;

import fr.iglee42.emgenerators.tile.TileEntityTieredAdvancedSolarGenerator;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMContainerTypes;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.neoforged.bus.api.IEventBus;

public class EMGenContainerTypes {

    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(EvolvedMekanism.MODID);
    
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityTieredAdvancedSolarGenerator>> TIERED_ADVANCED_SOLAR_GENERATOR = CONTAINER_TYPES
            .custom("tiered_advanced_solar_generator", TileEntityTieredAdvancedSolarGenerator.class).armorSideBar(-20, 11, 0).build();
    
    public static void register(IEventBus bus) {
        CONTAINER_TYPES.register(bus);
    }
}
