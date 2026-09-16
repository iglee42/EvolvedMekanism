package fr.iglee42.evolvedmekanism.curios;

import mekanism.common.item.gear.ItemCanteen;
import mekanism.common.item.interfaces.IGuiItem;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class EMCurio implements ICurioItem {

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        CuriosGameplay.tick(slotContext, stack);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return !(stack.getItem() instanceof ItemCanteen) && !(stack.getItem() instanceof IGuiItem);
    }
}
