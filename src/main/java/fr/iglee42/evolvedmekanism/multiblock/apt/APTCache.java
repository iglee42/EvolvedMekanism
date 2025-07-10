package fr.iglee42.evolvedmekanism.multiblock.apt;

import mekanism.api.SerializationConstants;
import mekanism.common.lib.multiblock.MultiblockCache;
import mekanism.common.util.NBTUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class APTCache extends MultiblockCache<APTMultiblockData> {

    private int progress;

    @Override
    public void merge(MultiblockCache<APTMultiblockData> mergeCache, RejectContents rejectContents) {
        super.merge(mergeCache, rejectContents);
        progress += ((APTCache) mergeCache).progress;
    }

    @Override
    public void apply(HolderLookup.Provider provider,APTMultiblockData data) {
        super.apply(provider,data);
        data.progress = progress;
    }

    @Override
    public void sync(APTMultiblockData data) {
        super.sync(data);
        progress = data.progress;
    }

    @Override
    public void load(HolderLookup.Provider provider,CompoundTag nbtTags) {
        super.load(provider,nbtTags);
        NBTUtils.setIntIfPresent(nbtTags, SerializationConstants.PROGRESS, val -> progress = val);
    }

    @Override
    public void save(HolderLookup.Provider provider,CompoundTag nbtTags) {
        super.save(provider,nbtTags);
        nbtTags.putDouble(SerializationConstants.PROGRESS, progress);
    }
}