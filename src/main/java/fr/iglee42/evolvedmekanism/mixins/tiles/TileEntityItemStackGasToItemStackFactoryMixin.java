package fr.iglee42.evolvedmekanism.mixins.tiles;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.MetallurgicInfuserRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.inventory.slot.chemical.InfusionInventorySlot;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.IDoubleRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.tile.factory.TileEntityItemStackGasToItemStackFactory;
import mekanism.common.tile.factory.TileEntityItemToItemFactory;
import mekanism.common.tile.factory.TileEntityMetallurgicInfuserFactory;
import mekanism.common.tile.interfaces.IHasDumpButton;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Set;

@Mixin(value = TileEntityItemStackGasToItemStackFactory.class,remap = false)
public class TileEntityItemStackGasToItemStackFactoryMixin  {

    @Shadow private IGasTank gasTank;

    @Shadow private GasInventorySlot extraSlot;

    @Inject(method = "addSlots",at = @At(value = "INVOKE", target = "Lmekanism/common/tile/factory/TileEntityItemToItemFactory;addSlots(Lmekanism/common/capabilities/holder/slot/InventorySlotHelper;Lmekanism/api/IContentsListener;Lmekanism/api/IContentsListener;)V",shift = At.Shift.AFTER), cancellable = true)
    private void evolvedmekanism$moveSlot(InventorySlotHelper builder, IContentsListener listener, IContentsListener updateSortingListener, CallbackInfo ci){
        TileEntityItemStackGasToItemStackFactory be = (TileEntityItemStackGasToItemStackFactory) ((Object)this);
        if (EvolvedMekanism.isEvolvedMekanismTier(be.tier.getBaseTier())){
            builder.addSlot(extraSlot = GasInventorySlot.fillOrConvert(gasTank, be::getLevel, listener, 192, 143));

            ci.cancel();
        }
    }

}
