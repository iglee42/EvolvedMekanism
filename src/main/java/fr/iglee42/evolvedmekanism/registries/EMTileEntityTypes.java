package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTCasing;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTPort;
import fr.iglee42.evolvedmekanism.tiles.TileEntitySuperchargingElement;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.*;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.multiblock.*;
import mekanism.common.tile.transmitter.*;

@SuppressWarnings("deprecation")
public class EMTileEntityTypes {

    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(EvolvedMekanism.MODID);

    public static final TileEntityTypeRegistryObject<TileEntityAPTCasing> APT_CASING = TILE_ENTITY_TYPES.register(EMBlocks.APT_CASING, TileEntityAPTCasing::new);
    public static final TileEntityTypeRegistryObject<TileEntityAPTPort> APT_PORT = TILE_ENTITY_TYPES.register(EMBlocks.APT_PORT, TileEntityAPTPort::new);
    public static final TileEntityTypeRegistryObject<TileEntitySuperchargingElement> SUPERCHARGING_ELEMENT = TILE_ENTITY_TYPES.register(EMBlocks.SUPERCHARGING_ELEMENT, TileEntitySuperchargingElement::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);

}
