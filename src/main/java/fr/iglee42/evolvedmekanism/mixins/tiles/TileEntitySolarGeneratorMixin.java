package fr.iglee42.evolvedmekanism.mixins.tiles;

import fr.iglee42.evolvedmekanism.registries.EMUpgrades;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.generators.common.tile.TileEntitySolarGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TileEntitySolarGenerator.class,remap = false)
public class TileEntitySolarGeneratorMixin extends TileEntityMekanism {

    public TileEntitySolarGeneratorMixin(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Inject(method = "getConfiguredMax",at = @At("RETURN"),cancellable = true)
    private void evolvedmekanism$upgrade(CallbackInfoReturnable<FloatingLong> cir){
        int modifier = 1 + (upgradeComponent != null ? upgradeComponent.getUpgrades(EMUpgrades.SOLAR_UPGRADE) : 0);
        cir.setReturnValue(cir.getReturnValue().multiply(modifier));
    }

}
