package fr.iglee42.evolvedmekanism.mixins.slots;

import mekanism.common.inventory.slot.BasicInventorySlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = BasicInventorySlot.class, remap = false)
public interface BasicInventorySlotAccessor {

    @Invoker("setStackUnchecked")
    void evolvedmekanism$setStackUnchecked(ItemStack stack);
}
