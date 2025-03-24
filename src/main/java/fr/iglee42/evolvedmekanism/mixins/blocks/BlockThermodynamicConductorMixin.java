package fr.iglee42.evolvedmekanism.mixins.blocks;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMBlockEntityTypes;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.block.transmitter.BlockPressurizedTube;
import mekanism.common.block.transmitter.BlockThermodynamicConductor;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.transmitter.TileEntityPressurizedTube;
import mekanism.common.tile.transmitter.TileEntityThermodynamicConductor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockThermodynamicConductor.class,remap = false)
public abstract class BlockThermodynamicConductorMixin {

    @Shadow protected abstract BaseTier getBaseTier();

    @Inject(method = "getTileType",at = @At("HEAD"),cancellable = true)
    private void evolvedmekanism$upgradeToNewTier(CallbackInfoReturnable<TileEntityTypeRegistryObject<TileEntityThermodynamicConductor>> cir){
        BaseTier tier = getBaseTier();
        if (EvolvedMekanism.isEvolvedMekanismTier(tier)){
            TileEntityTypeRegistryObject<TileEntityThermodynamicConductor> block = null;
            if (tier.equals(EMBaseTier.OVERCLOCKED)) block = EMBlockEntityTypes.OVERCLOCKED_THERMODYNAMIC_CONDUCTOR;
            else if (tier.equals(EMBaseTier.QUANTUM)) block = EMBlockEntityTypes.QUANTUM_THERMODYNAMIC_CONDUCTOR;
            else if (tier.equals(EMBaseTier.DENSE)) block = EMBlockEntityTypes.DENSE_THERMODYNAMIC_CONDUCTOR;
            else if (tier.equals(EMBaseTier.MULTIVERSAL)) block = EMBlockEntityTypes.MULTIVERSAL_THERMODYNAMIC_CONDUCTOR;
            cir.setReturnValue(block);
        }
    }
}
