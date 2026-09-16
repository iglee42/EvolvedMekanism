package fr.iglee42.evolvedmekanism.mixins;

import mekanism.api.math.MathUtils;
import mekanism.common.capabilities.energy.VariableCapacityEnergyContainer;
import mekanism.common.content.network.EnergyNetwork;
import mekanism.common.content.network.transmitter.UniversalCable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EnergyNetwork.class, remap = false)
public class EnergyNetworkMixin {

    @Shadow
    public VariableCapacityEnergyContainer energyContainer;

    /**
     * Creative cables can each hold {@code Long.MAX_VALUE}. Absorbing two of those shares
     * overflows the long add and {@code setEnergy} throws, crashing the server every tick.
     */
    @Inject(method = "absorbBuffer", at = @At("HEAD"), cancellable = true)
    private void evolvedmekanism$absorbWithoutOverflow(UniversalCable transmitter, CallbackInfo ci) {
        long energy = transmitter.releaseShare();
        if (energy > 0L) {
            energyContainer.setEnergy(MathUtils.addClamped(energyContainer.getEnergy(), energy));
        }
        ci.cancel();
    }
}
