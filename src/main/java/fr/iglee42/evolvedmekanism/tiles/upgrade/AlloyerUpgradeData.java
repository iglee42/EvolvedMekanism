package fr.iglee42.evolvedmekanism.tiles.upgrade;

import java.util.List;

import fr.iglee42.evolvedmekanism.tiles.LimitedInputInventorySlot;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.tile.component.ITileComponent;
import mekanism.common.tile.interfaces.IRedstoneControl.RedstoneControl;
import mekanism.common.upgrade.MachineUpgradeData;

public class AlloyerUpgradeData extends MachineUpgradeData {

    public final LimitedInputInventorySlot extraSlot;
    public final LimitedInputInventorySlot secondaryExtraSlot;

    //Combiner Constructor
    public AlloyerUpgradeData(boolean redstone, RedstoneControl controlType, IEnergyContainer energyContainer, int operatingTicks, EnergyInventorySlot energySlot,
                              LimitedInputInventorySlot extraSlot, LimitedInputInventorySlot secondaryExtraSlot, InputInventorySlot inputSlot, OutputInventorySlot outputSlot, List<ITileComponent> components) {
        super(redstone, controlType, energyContainer, operatingTicks, energySlot, inputSlot, outputSlot, components);
        this.extraSlot = extraSlot;
        this.secondaryExtraSlot = secondaryExtraSlot;
    }

    //Combining Factory Constructor
    public AlloyerUpgradeData(boolean redstone, RedstoneControl controlType, IEnergyContainer energyContainer, int[] progress, EnergyInventorySlot energySlot,
                              LimitedInputInventorySlot extraSlot,LimitedInputInventorySlot secondaryExtraSlot, List<IInventorySlot> inputSlots, List<IInventorySlot> outputSlots, boolean sorting, List<ITileComponent> components) {
        super(redstone, controlType, energyContainer, progress, energySlot, inputSlots, outputSlots, sorting, components);
        this.extraSlot = extraSlot;
        this.secondaryExtraSlot = secondaryExtraSlot;

    }
}