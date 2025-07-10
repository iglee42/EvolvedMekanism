package fr.iglee42.evolvedmekanism.utils;

import java.util.List;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.heat.IHeatCapacitor;
import mekanism.api.inventory.IInventorySlot;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.IFluidTank;

@NothingNullByDefault
public class EMDataHandlerUtils {

    private EMDataHandlerUtils() {
    }

    /**
     * Helper to read and load a list of handler contents from a {@link ListTag}
     */
    public static void readContents(HolderLookup.Provider provider,List<? extends INBTSerializable<CompoundTag>> contents, ListTag storedContents, String key) {
        int size = contents.size();
        for (int tagCount = 0; tagCount < storedContents.size(); tagCount++) {
            CompoundTag tagCompound = storedContents.getCompound(tagCount);
            int id = tagCompound.getInt(key);
            if (id >= 0 && id < size) {
                contents.get(id).deserializeNBT(provider,tagCompound);
            }
        }
    }

    /**
     * Helper to read and load a list of handler contents to a {@link ListTag}
     */
    public static ListTag writeContents(HolderLookup.Provider provider,List<? extends INBTSerializable<CompoundTag>> contents, String key) {
        ListTag storedContents = new ListTag();
        for (int tank = 0; tank < contents.size(); tank++) {
            CompoundTag tagCompound = contents.get(tank).serializeNBT(provider);
            if (!tagCompound.isEmpty()) {
                tagCompound.putInt(key, tank);
                storedContents.add(tagCompound);
            }
        }
        return storedContents;
    }

}