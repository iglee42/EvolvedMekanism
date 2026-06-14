package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTCasing;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTPort;
import fr.iglee42.evolvedmekanism.tiles.TileEntitySuperchargingElement;
import fr.iglee42.evolvedmekanism.tiles.TileEntitySuperchargingElementMk2;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityAlloyer;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityChemixer;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.*;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.multiblock.*;
import mekanism.common.tile.transmitter.*;

public class EMTileEntityTypes {

    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(EvolvedMekanism.MODID);

    public static final TileEntityTypeRegistryObject<TileEntityAPTCasing> APT_CASING = TILE_ENTITY_TYPES.register(EMBlocks.APT_CASING, TileEntityAPTCasing::new);
    public static final TileEntityTypeRegistryObject<TileEntityAPTPort> APT_PORT = TILE_ENTITY_TYPES.register(EMBlocks.APT_PORT, TileEntityAPTPort::new);
    public static final TileEntityTypeRegistryObject<TileEntitySuperchargingElement> SUPERCHARGING_ELEMENT = TILE_ENTITY_TYPES.register(EMBlocks.SUPERCHARGING_ELEMENT, TileEntitySuperchargingElement::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    public static final TileEntityTypeRegistryObject<TileEntitySuperchargingElementMk2> SUPERCHARGING_ELEMENT_MK2 = TILE_ENTITY_TYPES.register(EMBlocks.SUPERCHARGING_ELEMENT_MK2, TileEntitySuperchargingElementMk2::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);

    public static final TileEntityTypeRegistryObject<TileEntityAlloyer> ALLOYER = TILE_ENTITY_TYPES.register(EMBlocks.ALLOYER, TileEntityAlloyer::new);
    public static final TileEntityTypeRegistryObject<TileEntityChemixer> CHEMIXER = TILE_ENTITY_TYPES.register(EMBlocks.CHEMIXER, TileEntityChemixer::new);

}