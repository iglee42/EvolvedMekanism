package fr.iglee42.evolvedmekanism.mixins.tiles;

import fr.iglee42.evolvedmekanism.tiers.EMFactoryTier;
import mekanism.api.IContentsListener;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.tile.factory.TileEntitySawingFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = TileEntityFactory.class,remap = false)
public class TileEntityFactoryMixin {

    @Shadow public FactoryTier tier;

    @Shadow EnergyInventorySlot energySlot;

    @Shadow protected MachineEnergyContainer<TileEntityFactory<?>> energyContainer;

    @Inject(method = "getInitialInventory", at= @At(value = "INVOKE", target = "Lmekanism/common/capabilities/holder/slot/InventorySlotHelper;addSlot(Lmekanism/api/inventory/IInventorySlot;)Lmekanism/api/inventory/IInventorySlot;"),locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private void evolvedmekanism$changeEnergyPos(IContentsListener listener, CallbackInfoReturnable<IInventorySlotHolder> cir, InventorySlotHelper builder){
        if (tier.ordinal() >= EMFactoryTier.OVERCLOCKED.ordinal()){
            TileEntityFactory<?> be = (TileEntityFactory<?>) ((Object)this);
            builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, be::getLevel, listener, 171, be instanceof TileEntitySawingFactory ? 163 :143));
            cir.setReturnValue(builder.build());
        }
    }

}
