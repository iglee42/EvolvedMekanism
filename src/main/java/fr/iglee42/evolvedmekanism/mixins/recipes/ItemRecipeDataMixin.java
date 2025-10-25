package fr.iglee42.evolvedmekanism.mixins.recipes;

import fr.iglee42.evolvedmekanism.inventory.personalstorage.TieredPersonalStorageManager;
import fr.iglee42.evolvedmekanism.items.ItemBlockTieredPersonalStorage;
import mekanism.api.functions.ConstantPredicates;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.inventory.IMekanismInventory;
import mekanism.common.item.block.ItemBlockPersonalStorage;
import mekanism.common.lib.inventory.personalstorage.PersonalStorageManager;
import mekanism.common.recipe.upgrade.ItemRecipeData;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
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

@Mixin(value = ItemRecipeData.class,remap = false)
public abstract class ItemRecipeDataMixin {

    @Shadow
    @Final
    private List<IInventorySlot> slots;

    @Shadow
    static boolean applyToStack(IMekanismInventory outputHandler, List<IInventorySlot> dataSlots) {
        return false;
    }

    @Inject(method = "applyToStack(Lnet/minecraft/core/HolderLookup$Provider;Lnet/minecraft/world/item/ItemStack;)Z",at = @At(value = "INVOKE", target = "Lmekanism/common/attachments/containers/ContainerType;createHandler(Lnet/minecraft/world/item/ItemStack;)Lmekanism/common/attachments/containers/ComponentBackedHandler;",ordinal = 0,shift = At.Shift.BEFORE),locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private void em$applyToTieredStorages(HolderLookup.Provider provider, ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        if (stack.getItem() instanceof ItemBlockTieredPersonalStorage<?> it) {
            List<IInventorySlot> stackSlots = new ArrayList<>();
            TieredPersonalStorageManager.createSlots(stackSlots::add, ConstantPredicates.alwaysTrueBi(), null,it.getTier());
            IMekanismInventory outputHandler = new IMekanismInventory() {
                @NotNull
                @Override
                public List<IInventorySlot> getInventorySlots(@Nullable Direction side) {
                    return stackSlots;
                }

                @Override
                public void onContentsChanged() {
                }
            };
            if (applyToStack(outputHandler, slots)) {
                //We managed to transfer it all into valid slots, so save it as a new inventory
                cir.setReturnValue(TieredPersonalStorageManager.createInventoryFor(it.getTier(),provider, stack, stackSlots));
                return;
            }
            cir.setReturnValue(false);
            return;
        }
    }
}
