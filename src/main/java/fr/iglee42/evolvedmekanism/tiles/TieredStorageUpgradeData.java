package fr.iglee42.evolvedmekanism.tiles;

import fr.iglee42.evolvedmekanism.inventory.personalstorage.TieredPersonalStorageItemInventory;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.upgrade.IUpgradeData;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public record TieredStorageUpgradeData(@Nullable List<IInventorySlot> inventory,@Nullable UUID owner) implements IUpgradeData {
}