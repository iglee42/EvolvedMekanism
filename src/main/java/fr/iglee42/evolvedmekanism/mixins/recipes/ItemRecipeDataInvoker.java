package fr.iglee42.evolvedmekanism.mixins.recipes;

import mekanism.api.inventory.IInventorySlot;
import mekanism.common.recipe.upgrade.ItemRecipeData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(ItemRecipeData.class)
public interface ItemRecipeDataInvoker {

    @Invoker("<init>")
    static ItemRecipeData em$invokeInit(List<IInventorySlot> slots) {
        throw new AssertionError();
    }
}
