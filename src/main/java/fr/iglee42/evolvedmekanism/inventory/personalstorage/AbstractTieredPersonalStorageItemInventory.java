package fr.iglee42.evolvedmekanism.inventory.personalstorage;

import fr.iglee42.evolvedmekanism.tiers.PersonalStorageTier;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.inventory.IMekanismInventory;
import mekanism.common.inventory.slot.BasicInventorySlot;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@NothingNullByDefault
public abstract class AbstractTieredPersonalStorageItemInventory implements IMekanismInventory {

    protected final List<IInventorySlot> slots;
    protected final PersonalStorageTier tier;

    public AbstractTieredPersonalStorageItemInventory(PersonalStorageTier tier) {
        slots = Util.make(new ArrayList<>(), lst -> TieredPersonalStorageManager.createSlots(lst::add, BasicInventorySlot.alwaysTrueBi, this,tier));
        this.tier = tier;
    }

    @Override
    public List<IInventorySlot> getInventorySlots(@Nullable Direction side) {
        return slots;
    }

    public PersonalStorageTier getTier() {
        return tier;
    }
}