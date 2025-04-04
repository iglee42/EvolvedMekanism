package fr.iglee42.evolvedmekanism.mixins.containers;

import fr.iglee42.evolvedmekanism.tiers.EMFactoryTier;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.tile.FactoryContainer;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.factory.TileEntityFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FactoryContainer.class,remap = false)
public class FactoryContainerMixin {

    @Inject(method = "getInventoryXOffset",at =@At("HEAD"),cancellable = true)
    private void evolvedmekanism$moveInventorySlots(CallbackInfoReturnable<Integer> cir){
        FactoryContainer container = (FactoryContainer) (Object)this;
        TileEntityFactory<?> be = container.getTileEntity();
        if (be.tier.ordinal() >= EMFactoryTier.OVERCLOCKED.ordinal()){
            int imageWidth = 176 +(38 *( be.tier.ordinal() - EMFactoryTier.OVERCLOCKED.ordinal() + 1)) + 9;
            int inventorySize = 9 * 20;
            cir.setReturnValue( 8 + (imageWidth / 2 - inventorySize / 2));
        }
    }
}
