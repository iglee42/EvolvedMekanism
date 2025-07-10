package fr.iglee42.evolvedmekanism.mixins.tiles;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.tiers.EMFactoryTier;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.IChemicalTank;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.chemical.ChemicalInventorySlot;
import mekanism.common.tile.factory.TileEntityItemStackChemicalToItemStackFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileEntityItemStackChemicalToItemStackFactory.class,remap = false)
public class TileEntityItemStackChemicalToItemStackFactoryMixin {

    @Shadow private IChemicalTank chemicalTank;

    @Shadow private ChemicalInventorySlot extraSlot;

    @Inject(method = "addSlots",at = @At(value = "INVOKE", target = "Lmekanism/common/tile/factory/TileEntityItemToItemFactory;addSlots(Lmekanism/common/capabilities/holder/slot/InventorySlotHelper;Lmekanism/api/IContentsListener;Lmekanism/api/IContentsListener;)V",shift = At.Shift.AFTER), cancellable = true)
    private void evolvedmekanism$moveSlot(InventorySlotHelper builder, IContentsListener listener, IContentsListener updateSortingListener, CallbackInfo ci){
        TileEntityItemStackChemicalToItemStackFactory be = (TileEntityItemStackChemicalToItemStackFactory) ((Object)this);
        if (EvolvedMekanism.isEvolvedMekanismTier(be.tier.getBaseTier())){
            int imageWidth = 176 +(38 *( be.tier.ordinal() - EMFactoryTier.OVERCLOCKED.ordinal() + 1)) + 9;
            int inventorySize = 9 * 20;
            int endInventory = (imageWidth / 2 + inventorySize / 2) - 10;
            builder.addSlot(extraSlot = ChemicalInventorySlot.fillOrConvert(chemicalTank, be::getLevel, listener, endInventory + 4, 143));

            ci.cancel();
        }
    }

}
