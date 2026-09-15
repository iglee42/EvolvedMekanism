package fr.iglee42.evolvedmekanism.mixins.blocks;

import fr.iglee42.evolvedmekanism.blocks.BlockTieredPersonalStorage;
import mekanism.common.block.BlockPersonalStorage;
import mekanism.common.lib.inventory.personalstorage.PersonalStorageManager;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(BlockPersonalStorage.class)
public class BlockPersonalStorageMixin {

    @Redirect(method = "setPlacedBy", at = @At(value = "INVOKE", target = "Lmekanism/common/lib/inventory/personalstorage/PersonalStorageManager;getInventoryIfPresent(Lnet/minecraft/world/item/ItemStack;)Ljava/util/Optional;", remap = false))
    private Optional<?> em$skipVanillaInventoryForTiered(ItemStack stack) {
        if ((Object) this instanceof BlockTieredPersonalStorage<?, ?>) {
            return Optional.empty();
        }
        return PersonalStorageManager.getInventoryIfPresent(stack);
    }
}
