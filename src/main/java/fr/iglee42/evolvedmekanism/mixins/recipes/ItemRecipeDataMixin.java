package fr.iglee42.evolvedmekanism.mixins.recipes;

import fr.iglee42.evolvedmekanism.inventory.personalstorage.TieredPersonalStorageManager;
import fr.iglee42.evolvedmekanism.items.ItemBlockTieredPersonalStorage;
import mekanism.api.functions.ConstantPredicates;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.inventory.IMekanismInventory;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.item.block.ItemBlockPersonalStorage;
import mekanism.common.lib.inventory.personalstorage.PersonalStorageManager;
import mekanism.common.recipe.upgrade.ItemRecipeData;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Mixin(value = ItemRecipeData.class,remap = false)
public abstract class ItemRecipeDataMixin {

    @Shadow
    @Final
    private List<IInventorySlot> slots;


    @Shadow
    private static boolean applyToStack(List<IInventorySlot> dataSlots, List<IInventorySlot> stackSlots, Predicate<ListTag> stackWriter) {
        return false;
    }

    @Inject(method = "applyToStack(Lnet/minecraft/world/item/ItemStack;)Z",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getCapability(Lnet/minecraftforge/common/capabilities/Capability;)Lnet/minecraftforge/common/util/LazyOptional;",ordinal = 0,shift = At.Shift.BEFORE),locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private void em$applyToTieredStorages(ItemStack stack, CallbackInfoReturnable<Boolean> cir, Item item, List<IInventorySlot> stackSlots, boolean isBin){
        if (stack.getItem() instanceof ItemBlockTieredPersonalStorage<?> it) {
            TieredPersonalStorageManager.createSlots(stackSlots::add, BasicInventorySlot.alwaysTrueBi, null,it.getTier());
            cir.setReturnValue(applyToStack(slots, stackSlots, (ListTag toWrite) -> TieredPersonalStorageManager.createInventoryFor(it.getTier(),stack, stackSlots)));
            return;
        }
    }
}
