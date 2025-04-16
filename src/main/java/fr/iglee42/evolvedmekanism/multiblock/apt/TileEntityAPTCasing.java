package fr.iglee42.evolvedmekanism.multiblock.apt;

import java.util.LinkedList;
import java.util.Queue;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import mekanism.api.NBTConstants;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.Mekanism;
import mekanism.common.content.sps.SPSMultiblockData;
import mekanism.common.lib.multiblock.MultiblockManager;
import mekanism.common.particle.SPSOrbitEffect;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tile.prefab.TileEntityMultiblock;
import mekanism.common.util.NBTUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TileEntityAPTCasing extends TileEntityMultiblock<APTMultiblockData> {


    private boolean handleSound;
    private boolean prevActive;

    public TileEntityAPTCasing(BlockPos pos, BlockState state) {
        this(EMBlocks.APT_CASING, pos, state);
    }

    public TileEntityAPTCasing(IBlockProvider provider, BlockPos pos, BlockState state) {
        super(provider, pos, state);
    }


    @Override
    protected boolean onUpdateServer(APTMultiblockData multiblock) {
        boolean needsPacket = super.onUpdateServer(multiblock);
        boolean active = multiblock.isFormed() && multiblock.handlesSound(this) && multiblock.progress > 0;
        if (active != prevActive) {
            prevActive = active;
            needsPacket = true;
        }
        return needsPacket;
    }


    @Override
    public APTMultiblockData createMultiblock() {
        return new APTMultiblockData(this);
    }

    @Override
    public MultiblockManager<APTMultiblockData> getManager() {
        return EvolvedMekanism.aptManager;
    }

    @Override
    protected boolean canPlaySound() {
        APTMultiblockData multiblock = getMultiblock();
        return multiblock.isFormed() && handleSound && multiblock.progress > 0;
    }

    @NotNull
    @Override
    public CompoundTag getReducedUpdateTag() {
        CompoundTag updateTag = super.getReducedUpdateTag();
        APTMultiblockData multiblock = getMultiblock();
        updateTag.putBoolean(NBTConstants.HANDLE_SOUND, multiblock.isFormed() && multiblock.handlesSound(this));
        return updateTag;
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag) {
        super.handleUpdateTag(tag);
        NBTUtils.setBooleanIfPresent(tag, NBTConstants.HANDLE_SOUND, value -> handleSound = value);
    }
}