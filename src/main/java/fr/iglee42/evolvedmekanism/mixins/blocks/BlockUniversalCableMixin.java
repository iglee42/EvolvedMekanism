package fr.iglee42.evolvedmekanism.mixins.blocks;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMTileEntityTypes;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.block.transmitter.BlockUniversalCable;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.transmitter.TileEntityUniversalCable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockUniversalCable.class,remap = false)
public abstract class BlockUniversalCableMixin {

    @Shadow protected abstract BaseTier getBaseTier();

    @Inject(method = "getTileType",at = @At("HEAD"),cancellable = true)
    private void evolvedmekanism$upgradeToNewTier(CallbackInfoReturnable<TileEntityTypeRegistryObject<TileEntityUniversalCable>> cir){
        BaseTier tier = getBaseTier();
        if (EvolvedMekanism.isEvolvedMekanismTier(tier)){
            TileEntityTypeRegistryObject<TileEntityUniversalCable> block = null;
            if (tier.equals(EMBaseTier.OVERCLOCKED)) block = EMTileEntityTypes.OVERCLOCKED_UNIVERSAL_CABLE;
            else if (tier.equals(EMBaseTier.QUANTUM)) block = EMTileEntityTypes.QUANTUM_UNIVERSAL_CABLE;
            else if (tier.equals(EMBaseTier.DENSE)) block = EMTileEntityTypes.DENSE_UNIVERSAL_CABLE;
            else if (tier.equals(EMBaseTier.MULTIVERSAL)) block = EMTileEntityTypes.MULTIVERSAL_UNIVERSAL_CABLE;
            cir.setReturnValue(block);
        }
    }
}
