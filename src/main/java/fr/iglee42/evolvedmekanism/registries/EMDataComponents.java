package fr.iglee42.evolvedmekanism.registries;

import mekanism.common.util.ItemDataUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public final class EMDataComponents {

    public static final String CURIO_ACTIVE = "curio_active";

    private EMDataComponents() {
    }

    public static boolean isActive(ItemStack stack) {
        if (!ItemDataUtils.hasData(stack, CURIO_ACTIVE, Tag.TAG_BYTE)) {
            return true;
        }
        return ItemDataUtils.getBoolean(stack, CURIO_ACTIVE);
    }

    public static boolean hasToggleState(ItemStack stack) {
        return ItemDataUtils.hasData(stack, CURIO_ACTIVE, Tag.TAG_BYTE);
    }

    public static boolean toggle(ItemStack stack) {
        boolean next = !isActive(stack);
        ItemDataUtils.setBoolean(stack, CURIO_ACTIVE, next);
        return next;
    }
}
