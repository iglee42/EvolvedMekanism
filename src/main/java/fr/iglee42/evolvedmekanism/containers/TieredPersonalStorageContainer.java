package fr.iglee42.evolvedmekanism.containers;

import fr.iglee42.evolvedmekanism.registries.EMContainerTypes;
import fr.iglee42.evolvedmekanism.tiles.TileEntityTieredPersonalStorage;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class TieredPersonalStorageContainer extends MekanismTileContainer<TileEntityTieredPersonalStorage> {
    public TieredPersonalStorageContainer(int id, Inventory inv, @NotNull TileEntityTieredPersonalStorage tileEntityTieredPersonalStorage) {
        super(EMContainerTypes.TIERED_PERSONAL_STORAGE_BLOCK, id, inv, tileEntityTieredPersonalStorage);
    }

    @Override
    protected int getInventoryYOffset() {
        return super.getInventoryYOffset() + 56 + (getTileEntity().getTier().rows - 6) * 18;
    }

    @Override
    protected int getInventoryXOffset() {
        return super.getInventoryXOffset() + (getTileEntity().getTier().columns - 9) / 2 * 18;
    }
}
