package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTCasing;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityAlloyer;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityChemixer;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;

public class EMContainerTypes {

    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(EvolvedMekanism.MODID);

    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityAlloyer>> ALLOYER = CONTAINER_TYPES.register(EMBlocks.ALLOYER, TileEntityAlloyer.class);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityChemixer>> CHEMIXER = CONTAINER_TYPES.register(EMBlocks.CHEMIXER, TileEntityChemixer.class);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityAPTCasing>> APT = CONTAINER_TYPES.custom(EMBlocks.APT_CASING, TileEntityAPTCasing.class).offset(0, 16).build();

}