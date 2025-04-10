package fr.iglee42.evolvedmekanism.mixins.tiles;

import fr.iglee42.evolvedmekanism.interfaces.IGetEnergySlot;
import fr.iglee42.evolvedmekanism.tiers.EMFactoryTier;
import fr.iglee42.evolvedmekanism.tiles.factory.TileEntityAlloyingFactory;
import mekanism.api.IContentsListener;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.tile.factory.TileEntitySawingFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Set;

@Mixin(value = TileEntityFactory.class,remap = false)
public class TileEntityFactoryMixin implements IGetEnergySlot {

    @Shadow public FactoryTier tier;

    @Shadow EnergyInventorySlot energySlot;

    @Shadow protected MachineEnergyContainer<TileEntityFactory<?>> energyContainer;

    @Inject(method = "getInitialInventory", at= @At(value = "INVOKE", target = "Lmekanism/common/capabilities/holder/slot/InventorySlotHelper;addSlot(Lmekanism/api/inventory/IInventorySlot;)Lmekanism/api/inventory/IInventorySlot;"),locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private void evolvedmekanism$changeEnergyPos(IContentsListener listener, CallbackInfoReturnable<IInventorySlotHolder> cir, InventorySlotHelper builder){
        if (tier.ordinal() >= EMFactoryTier.OVERCLOCKED.ordinal()){
            TileEntityFactory<?> be = (TileEntityFactory<?>) ((Object)this);
            int imageWidth = 176 +(38 *( be.tier.ordinal() - EMFactoryTier.OVERCLOCKED.ordinal() + 1)) + 9;
            int inventorySize = 9 * 20;
            int startInventory = 8 + (imageWidth / 2 - inventorySize / 2);
            builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, be::getLevel, listener, startInventory - 22, be instanceof TileEntitySawingFactory ? 163 :143));
            cir.setReturnValue(builder.build());
        }
    }

    @Override
    public EnergyInventorySlot getEnergySlot() {
        return energySlot;
    }
}
