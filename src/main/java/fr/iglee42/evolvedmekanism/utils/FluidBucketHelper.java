package fr.iglee42.evolvedmekanism.utils;

import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.inventory.slot.FluidInventorySlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public final class FluidBucketHelper {

    private FluidBucketHelper() {
    }

    public static void fillInPlace(FluidInventorySlot slot) {
        if (slot.getCount() != 1) {
            return;
        }
        ItemStack copy = slot.getStack().copyWithCount(1);
        IFluidHandlerItem handler = Capabilities.FLUID.getCapability(copy);
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
        FluidStack drained = handler.drain(inItem.copyWithAmount(amount), FluidAction.EXECUTE);
        if (drained.isEmpty()) {
            return;
        }
        slot.getFluidTank().insert(drained, Action.EXECUTE, AutomationType.INTERNAL);
        slot.setStackUnchecked(handler.getContainer());
    }

    public static void drainInPlace(FluidInventorySlot slot) {
        if (slot.getCount() != 1 || slot.getFluidTank().isEmpty()) {
            return;
        }
        ItemStack copy = slot.getStack().copyWithCount(1);
        IFluidHandlerItem handler = Capabilities.FLUID.getCapability(copy);
        if (handler == null) {
            return;
        }
        FluidStack stored = slot.getFluidTank().getFluid();
        int filled = handler.fill(stored.copy(), FluidAction.EXECUTE);
        if (filled <= 0) {
            return;
        }
        slot.getFluidTank().extract(filled, Action.EXECUTE, AutomationType.INTERNAL);
        slot.setStackUnchecked(handler.getContainer());
    }
}
