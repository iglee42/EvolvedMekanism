package fr.iglee42.evolvedmekanism.curios;

import mekanism.common.item.gear.ItemCanteen;
import mekanism.common.item.interfaces.IGuiItem;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class EMCurio implements ICurio {

    private final ItemStack stack;

    public EMCurio(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public ItemStack getStack() {
        return stack;
    }

    @Override
    public void curioTick(SlotContext slotContext) {
        CuriosGameplay.tick(slotContext, stack);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext) {
        return !(stack.getItem() instanceof ItemCanteen) && !(stack.getItem() instanceof IGuiItem);
    }
}
