package fr.iglee42.evolvedmekanism.multiblock.apt;

import mekanism.api.NBTConstants;
import mekanism.api.math.FloatingLong;
import mekanism.common.lib.multiblock.MultiblockCache;
import mekanism.common.util.NBTUtils;
import net.minecraft.nbt.CompoundTag;

public class APTCache extends MultiblockCache<APTMultiblockData> {

    private int progress;

    @Override
    public void merge(MultiblockCache<APTMultiblockData> mergeCache, RejectContents rejectContents) {
        super.merge(mergeCache, rejectContents);
        progress += ((APTCache) mergeCache).progress;
    }

    @Override
    public void apply(APTMultiblockData data) {
        super.apply(data);
        data.progress = progress;
    }

    @Override
    public void sync(APTMultiblockData data) {
        super.sync(data);
        progress = data.progress;
    }

    @Override
    public void load(CompoundTag nbtTags) {
        super.load(nbtTags);
        NBTUtils.setIntIfPresent(nbtTags, NBTConstants.PROGRESS, val -> progress = val);
    }

    @Override
    public void save(CompoundTag nbtTags) {
        super.save(nbtTags);
        nbtTags.putDouble(NBTConstants.PROGRESS, progress);
    }
}