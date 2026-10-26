package fr.iglee42.evolvedmekanism.mixins;

import fr.iglee42.evolvedmekanism.registries.MoltenFluidType;
import net.minecraft.world.level.LevelReader;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mekanism builds flowing fluids with water's tick rate. Lava waits longer and spreads less far.
 */
@Mixin(value = ForgeFlowingFluid.class, remap = false)
public class ForgeFlowingFluidMixin {

    @Inject(method = "getTickDelay", at = @At("RETURN"), cancellable = true)
    private void evolvedmekanism$lavaTickDelay(LevelReader level, CallbackInfoReturnable<Integer> cir) {
        if (this.evolvedmekanism$isMolten()) {
            cir.setReturnValue(level.dimensionType().ultraWarm() ? 10 : 30);
        }
    }

    @Inject(method = "getSlopeFindDistance", at = @At("RETURN"), cancellable = true)
    private void evolvedmekanism$lavaSlope(LevelReader level, CallbackInfoReturnable<Integer> cir) {
        if (this.evolvedmekanism$isMolten()) {
            cir.setReturnValue(level.dimensionType().ultraWarm() ? 4 : 2);
        }
    }

    @Inject(method = "getDropOff", at = @At("RETURN"), cancellable = true)
    private void evolvedmekanism$lavaDropOff(LevelReader level, CallbackInfoReturnable<Integer> cir) {
        if (this.evolvedmekanism$isMolten()) {
            cir.setReturnValue(level.dimensionType().ultraWarm() ? 1 : 2);
        }
    }

    private boolean evolvedmekanism$isMolten() {
        return ((ForgeFlowingFluid) (Object) this).getFluidType() instanceof MoltenFluidType;
    }
}
