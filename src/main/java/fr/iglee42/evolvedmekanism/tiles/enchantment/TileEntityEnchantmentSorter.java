package fr.iglee42.evolvedmekanism.tiles.enchantment;

import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import mekanism.api.Action;
import mekanism.api.IContentsListener;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableBoolean;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TileEntityEnchantmentSorter extends TileEntityConfigurableMachine {

    public boolean didProcess;

    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getInputItem", docPlaceholder = "input slot")
    InputInventorySlot inputSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getOutputItem1", docPlaceholder = "output slot 1")
    OutputInventorySlot outputSlot1;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getOutputItem2", docPlaceholder = "output slot 2")
    OutputInventorySlot outputSlot2;

    public TileEntityEnchantmentSorter(BlockPos pos, BlockState state) {
        super(EMBlocks.SORTIFICHANTOR, pos, state);
        ConfigInfo itemConfig = configComponent.getConfig(TransmissionType.ITEM);
        if (itemConfig != null) {
            itemConfig.addSlotInfo(DataType.INPUT, new InventorySlotInfo(true, false,inputSlot));
            itemConfig.addSlotInfo(DataType.OUTPUT_1, new InventorySlotInfo(false, true,outputSlot1));
            itemConfig.addSlotInfo(DataType.OUTPUT_2, new InventorySlotInfo(false, true,outputSlot2));
        }

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this);
        //Only allow inserting items with tags that match filters, but mark all items that have any filterable tags as valid
        builder.addSlot(inputSlot = InputInventorySlot.at(listener, 40, 35));
        builder.addSlot(outputSlot1 = OutputInventorySlot.at(listener, 100, 35));
        builder.addSlot(outputSlot2 = OutputInventorySlot.at(listener, 120, 35));
        return builder.build();
    }

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdatePacket = super.onUpdateServer();
        didProcess = false;
        if (canFunction() && !inputSlot.isEmpty()) {
                ItemStack result = inputSlot.getStack().copyWithCount(1);
                OutputInventorySlot outputSlot = result.isEnchanted() || !result.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty() ? outputSlot1 : outputSlot2;
                ItemStack outputStack = outputSlot.getStack();
                if (outputStack.isEmpty()) {
                    inputSlot.shrinkStack(1, Action.EXECUTE);
                    outputSlot.setStack(result);
                    didProcess = true;
                } else if (ItemStack.isSameItemSameComponents(outputStack, result) && outputStack.getCount() < outputSlot.getLimit(outputStack)) {
                    inputSlot.shrinkStack(1, Action.EXECUTE);
                    outputSlot.growStack(1, Action.EXECUTE);
                    didProcess = true;
                }
        }
        return sendUpdatePacket;
    }


    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableBoolean.create(() -> didProcess, value -> didProcess = value));
    }

}
