package fr.iglee42.evolvedmekanism.mixins;

import fr.iglee42.evolvedmekanism.tiers.EMFactoryTier;
import mekanism.common.inventory.container.tile.FactoryContainer;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.tile.factory.TileEntitySawingFactory;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FactoryContainer.class,remap = false)
public class FactoryContainerMixin {

    @Inject(method = "getInventoryYOffset", at = @At("HEAD"),cancellable = true)
    private void evolvedmekanism$removeOffsetOnCustomTiers(CallbackInfoReturnable<Integer> cir){
        FactoryContainer container = (FactoryContainer) ((Object)this);
        if (container.getTileEntity().tier.ordinal() >= EMFactoryTier.OVERCLOCKED.ordinal()){
            cir.setReturnValue(container.getTileEntity() instanceof TileEntitySawingFactory ? 105 : 85);
        }
    }

}
