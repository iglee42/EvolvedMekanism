package fr.iglee42.evolvedmekanism.mixins.tiles;

import fr.iglee42.emgenerators.tile.TileEntityLunarGenerator;
import fr.iglee42.evolvedmekanism.registries.EMUpgrades;
import mekanism.api.IEvaporationSolar;
import mekanism.api.math.FloatingLong;
import mekanism.api.math.FloatingLongSupplier;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.resolver.BasicCapabilityResolver;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.generators.common.tile.TileEntitySolarGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TileEntitySolarGenerator.class,remap = false)
public abstract class TileEntitySolarGeneratorMixin extends TileEntityMekanism implements IEvaporationSolar {

    public TileEntitySolarGeneratorMixin(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Inject(method = "<init>(Lmekanism/api/providers/IBlockProvider;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lmekanism/api/math/FloatingLongSupplier;)V", at = @At("RETURN"))
    private void evolvedmekanism$basicSolarEvaporation(IBlockProvider blockProvider, BlockPos pos, BlockState state, FloatingLongSupplier max, CallbackInfo ci) {
        if (((Object) this).getClass() == TileEntitySolarGenerator.class) {
            addCapabilityResolver(BasicCapabilityResolver.constant(Capabilities.EVAPORATION_SOLAR, this));
        }
    }

    @Inject(method = "getConfiguredMax",at = @At("RETURN"),cancellable = true)
    private void evolvedmekanism$upgrade(CallbackInfoReturnable<FloatingLong> cir){
        if ((Object) this instanceof TileEntityLunarGenerator) {
            return;
        }
        int modifier = 1 + (upgradeComponent != null ? upgradeComponent.getUpgrades(EMUpgrades.SOLAR_UPGRADE) : 0);
        cir.setReturnValue(cir.getReturnValue().multiply(modifier));
    }

    @Inject(method = "onUpdateServer", at = @At(value = "INVOKE", target = "Lmekanism/common/inventory/slot/EnergyInventorySlot;drainContainer()V", shift = At.Shift.AFTER), cancellable = true)
    private void evolvedmekanism$stopIfLunar(CallbackInfo ci) {
        if ((Object) this instanceof TileEntityLunarGenerator) {
            ci.cancel();
        }
    }

}
