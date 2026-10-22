package fr.iglee42.evolvedmekanism.utils;

import fr.iglee42.evolvedmekanism.mixins.slots.BasicInventorySlotAccessor;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.common.inventory.slot.FluidInventorySlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public final class FluidBucketHelper {

    private FluidBucketHelper() {
    }

    public static void fillInPlace(FluidInventorySlot slot) {
        if (slot.getStack().getCount() != 1) {
            return;
        }
        ItemStack copy = slot.getStack().copy();
        copy.setCount(1);
        IFluidHandlerItem handler = FluidUtil.getFluidHandler(copy).orElse(null);
        if (handler == null) {
            return;
        }
        FluidStack inItem = handler.getFluidInTank(0);
        if (inItem.isEmpty() || !slot.getFluidTank().isFluidValid(inItem)) {
            return;
        }
        FluidStack remainder = slot.getFluidTank().insert(inItem, Action.SIMULATE, AutomationType.INTERNAL);
        int amount = inItem.getAmount() - remainder.getAmount();
        if (amount <= 0) {
            return;
        }
        FluidStack toDrain = inItem.copy();
        toDrain.setAmount(amount);
        FluidStack drained = handler.drain(toDrain, FluidAction.EXECUTE);
        if (drained.isEmpty()) {
            return;
        }
        slot.getFluidTank().insert(drained, Action.EXECUTE, AutomationType.INTERNAL);
        ((BasicInventorySlotAccessor) (Object) slot).evolvedmekanism$setStackUnchecked(handler.getContainer());
    }

    public static void drainInPlace(FluidInventorySlot slot) {
        if (slot.getStack().getCount() != 1 || slot.getFluidTank().isEmpty()) {
            return;
        }
        ItemStack copy = slot.getStack().copy();
        copy.setCount(1);
        IFluidHandlerItem handler = FluidUtil.getFluidHandler(copy).orElse(null);
        if (handler == null) {
            return;
        }
        int filled = handler.fill(slot.getFluidTank().getFluid().copy(), FluidAction.EXECUTE);
        if (filled <= 0) {
            return;
        }
        slot.getFluidTank().extract(filled, Action.EXECUTE, AutomationType.INTERNAL);
        ((BasicInventorySlotAccessor) (Object) slot).evolvedmekanism$setStackUnchecked(handler.getContainer());
    }
}
