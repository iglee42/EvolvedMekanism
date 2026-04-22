package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTCasing;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;

public class EMContainerTypes {

    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(EvolvedMekanism.MODID);

    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityAPTCasing>> APT = CONTAINER_TYPES.custom(EMBlocks.APT_CASING, TileEntityAPTCasing.class).offset(0, 16).build();

}
