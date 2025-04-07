package fr.iglee42.evolvedmekanism.mixins.blocks;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMTileEntityTypes;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.block.transmitter.BlockLogisticalTransporter;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.transmitter.TileEntityLogisticalTransporter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockLogisticalTransporter.class,remap = false)
public abstract class BlockLogisticalTransporterMixin {

    @Shadow protected abstract BaseTier getBaseTier();

    @Inject(method = "getTileType",at = @At("HEAD"),cancellable = true)
    private void evolvedmekanism$upgradeToNewTier(CallbackInfoReturnable<TileEntityTypeRegistryObject<TileEntityLogisticalTransporter>> cir){
        BaseTier tier = getBaseTier();
        if (EvolvedMekanism.isEvolvedMekanismTier(tier)){
            TileEntityTypeRegistryObject<TileEntityLogisticalTransporter> block = null;
            if (tier.equals(EMBaseTier.OVERCLOCKED)) block = EMTileEntityTypes.OVERCLOCKED_LOGISTICAL_TRANSPORTER;
            else if (tier.equals(EMBaseTier.QUANTUM)) block = EMTileEntityTypes.QUANTUM_LOGISTICAL_TRANSPORTER;
            else if (tier.equals(EMBaseTier.DENSE)) block = EMTileEntityTypes.DENSE_LOGISTICAL_TRANSPORTER;
            else if (tier.equals(EMBaseTier.MULTIVERSAL)) block = EMTileEntityTypes.MULTIVERSAL_LOGISTICAL_TRANSPORTER;
            else if (tier.equals(BaseTier.CREATIVE)) block = EMTileEntityTypes.CREATIVE_LOGISTICAL_TRANSPORTER;
            cir.setReturnValue(block);
        }
    }
}
