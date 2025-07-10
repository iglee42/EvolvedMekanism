package fr.iglee42.evolvedmekanism.inventory.personalstorage;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.tiers.PersonalStorageTier;
import mekanism.api.SerializationConstants;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.attachments.containers.ContainerType;
import mekanism.common.lib.MekanismSavedData;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@NothingNullByDefault
class TieredPersonalStorageData extends MekanismSavedData {
    private final Map<UUID, TieredPersonalStorageItemInventory> inventoriesById = new HashMap<>();

    TieredPersonalStorageItemInventory getOrAddInventory(UUID id,PersonalStorageTier tier) {
        return inventoriesById.computeIfAbsent(id, unused -> createInventory(tier));
    }

    TieredPersonalStorageItemInventory addInventory(HolderLookup.Provider provider, UUID id, List<IInventorySlot> contents,PersonalStorageTier tier) {
        TieredPersonalStorageItemInventory inventory = inventoriesById.get(id);
        if (inventory == null) {
            inventory = createInventory(tier);
            inventoriesById.put(id, inventory);
            List<IInventorySlot> inventorySlots = inventory.getInventorySlots(null);
            for (int i = 0, slots = contents.size(); i < slots; i++) {
                inventorySlots.get(i).deserializeNBT(provider, contents.get(i).serializeNBT(provider));
            }
            setDirty();
        }
        return inventory;
    }

    void removeInventory(UUID id) {
        if (this.inventoriesById.remove(id) != null) {
            setDirty();
        }
    }

    @NotNull
    private TieredPersonalStorageItemInventory createInventory(PersonalStorageTier tier) {
        return new TieredPersonalStorageItemInventory(tier,this::setDirty);
    }

    /**
     * {
     *     [NBTConstants.DATA]: [
     *          {
     *              [NBTConstants.PERSONAL_STORAGE_ID]: UUID,
     *              [NBTConstants.ITEMS]: PersonalStorageItemInventory
     *          }
     *     ]
     * }
     */
    @Override
    public void load(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider provider) {
        ListTag entries = nbt.getList(SerializationConstants.DATA, Tag.TAG_COMPOUND);
        for (int i = 0; i < entries.size(); i++) {
            CompoundTag entry = entries.getCompound(i);
            PersonalStorageTier tier = PersonalStorageTier.values()[entry.getInt("tier")];
            TieredPersonalStorageItemInventory inv = createInventory(tier);
            ContainerType.ITEM.readFrom(provider, entry, inv.getInventorySlots(null));
            inventoriesById.put(entry.getUUID(SerializationConstants.PERSONAL_STORAGE_ID), inv);
        }
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, @NotNull HolderLookup.Provider provider) {
        ListTag entries = new ListTag();
        for (Map.Entry<UUID, TieredPersonalStorageItemInventory> entry : inventoriesById.entrySet()) {
            CompoundTag nbtEntry = new CompoundTag();
            nbtEntry.putUUID(SerializationConstants.PERSONAL_STORAGE_ID, entry.getKey());
            ContainerType.ITEM.saveTo(provider, nbtEntry, entry.getValue().getInventorySlots(null));
            nbtEntry.putInt("tier",entry.getValue().getTier().ordinal());
            entries.add(nbtEntry);
        }
        compoundTag.put(SerializationConstants.DATA, entries);
        return compoundTag;
    }

    @Override
    public void save(File file, @NotNull HolderLookup.Provider provider) {
        if (isDirty()) {
            File folder = file.getParentFile();
            if (!folder.exists() && !folder.mkdirs()) {
                EvolvedMekanism.logger.error("Could not create tiered personal storage directory, saves may fail");
            }
        }
        super.save(file, provider);
    }
}