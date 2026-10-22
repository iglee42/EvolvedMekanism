package fr.iglee42.evolvedmekanism.mixins.curios;

import mekanism.common.inventory.container.item.MekanismItemContainer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = MekanismItemContainer.class, remap = false)
public interface MekanismItemContainerAccessor {

    @Accessor("stack")
    ItemStack evolvedmekanism$getStack();
}
