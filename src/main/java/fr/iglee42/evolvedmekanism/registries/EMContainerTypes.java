package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.containers.TieredPersonalStorageContainer;
import fr.iglee42.evolvedmekanism.inventory.personalstorage.TieredPersonalStorageItemContainer;
import fr.iglee42.evolvedmekanism.items.ItemBlockTieredPersonalStorage;
import fr.iglee42.evolvedmekanism.tiles.TileEntityTieredPersonalStorage;
import mekanism.common.Mekanism;
import mekanism.common.inventory.container.item.PersonalStorageItemContainer;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.container.type.MekanismItemContainerType;
import mekanism.common.item.block.ItemBlockPersonalStorage;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.tile.TileEntityPersonalStorage;

public class EMContainerTypes {

    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(EvolvedMekanism.MODID);

    public static final ContainerTypeRegistryObject<TieredPersonalStorageItemContainer> TIERED_PERSONAL_STORAGE_ITEM = CONTAINER_TYPES.register("tiered_personal_storage_item", () -> MekanismItemContainerType.item(ItemBlockTieredPersonalStorage.class, TieredPersonalStorageItemContainer::new));
    public static final ContainerTypeRegistryObject<TieredPersonalStorageContainer> TIERED_PERSONAL_STORAGE_BLOCK = CONTAINER_TYPES.register("tiered_personal_storage_block", TileEntityTieredPersonalStorage.class,TieredPersonalStorageContainer::new);


}
