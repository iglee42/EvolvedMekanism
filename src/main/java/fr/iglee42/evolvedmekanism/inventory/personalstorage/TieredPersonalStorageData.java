package fr.iglee42.evolvedmekanism.inventory.personalstorage;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.tiers.PersonalStorageTier;
import mekanism.api.NBTConstants;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.common.lib.MekanismSavedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NothingNullByDefault
class TieredPersonalStorageData extends MekanismSavedData {
    private final Map<UUID, TieredPersonalStorageItemInventory> inventoriesById = new HashMap<>();

    TieredPersonalStorageItemInventory getOrAddInventory(UUID id,PersonalStorageTier tier) {
        return inventoriesById.computeIfAbsent(id, unused->createInventory(tier));
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
     *              [NBTConstants.ITEMS]: TieredPersonalStorageItemInventory
     *              tier: int
     *          }
     *     ]
     * }
     */
    @Override
    public void load(@NotNull CompoundTag nbt) {
        ListTag entries = nbt.getList(NBTConstants.DATA, Tag.TAG_COMPOUND);
        for (int i = 0; i < entries.size(); i++) {
            CompoundTag entry = entries.getCompound(i);
            PersonalStorageTier tier = PersonalStorageTier.values()[entry.getInt("tier")];
            TieredPersonalStorageItemInventory inv = createInventory(tier);
            inv.deserializeNBT(entry.getList(NBTConstants.ITEMS, Tag.TAG_COMPOUND));
            inventoriesById.put(entry.getUUID(NBTConstants.PERSONAL_STORAGE_ID), inv);
        }
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        ListTag entries = new ListTag();
        inventoriesById.forEach((uuid, inv) -> {
            CompoundTag nbtEntry = new CompoundTag();
            nbtEntry.putUUID(NBTConstants.PERSONAL_STORAGE_ID, uuid);
            nbtEntry.put(NBTConstants.ITEMS, inv.serializeNBT());
            nbtEntry.putInt("tier",inv.getTier().ordinal());
            entries.add(nbtEntry);
        });
        compoundTag.put(NBTConstants.DATA, entries);
        return compoundTag;
    }

    @Override
    public void save(File file) {
        if (isDirty()) {
            File folder = file.getParentFile();
            if (!folder.exists() && !folder.mkdirs()) {
                EvolvedMekanism.logger.error("Could not create personal storage directory, saves may fail");
            }
        }
        super.save(file);
    }
}