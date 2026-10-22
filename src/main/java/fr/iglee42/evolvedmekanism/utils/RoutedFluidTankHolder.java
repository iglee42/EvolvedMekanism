package fr.iglee42.evolvedmekanism.utils;

import java.util.ArrayList;
import java.util.List;

import mekanism.api.RelativeSide;
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.slot.ISlotInfo;
import mekanism.common.tile.interfaces.ISideConfiguration;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RoutedFluidTankHolder implements IFluidTankHolder {

    private final ISideConfiguration tile;
    private final List<IExtendedFluidTank> tanks = new ArrayList<>();
    private final List<IExtendedFluidTank> inputTanks = new ArrayList<>();
    private final List<IExtendedFluidTank> outputTanks = new ArrayList<>();

    public RoutedFluidTankHolder(ISideConfiguration tile) {
        this.tile = tile;
    }

    public void addInput(IExtendedFluidTank tank) {
        tanks.add(tank);
        inputTanks.add(tank);
    }

    public void addOutput(IExtendedFluidTank tank) {
        tanks.add(tank);
        outputTanks.add(tank);
    }

    @NotNull
    @Override
    public List<IExtendedFluidTank> getTanks(@Nullable Direction side) {
        if (side == null || !isOutputOnly(side)) {
            return side == null ? tanks : inputTanks;
        }
        return outputTanks;
    }

    @Override
    public boolean canInsert(@Nullable Direction side) {
        return side != null && !isOutputOnly(side);
    }

    @Override
    public boolean canExtract(@Nullable Direction side) {
        return side != null && isOutputOnly(side);
    }

    private boolean isOutputOnly(Direction side) {
        ConfigInfo info = tile.getConfig().getConfig(TransmissionType.FLUID);
        if (info == null) {
            return false;
        }
        ISlotInfo slot = info.getSlotInfo(RelativeSide.fromDirections(tile.getDirection(), side));
        return slot != null && slot.canOutput() && !slot.canInput();
    }
}
