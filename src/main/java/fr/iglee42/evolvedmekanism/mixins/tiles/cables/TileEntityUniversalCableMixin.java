package fr.iglee42.evolvedmekanism.mixins.tiles.cables;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.tier.BaseTier;
import mekanism.common.block.states.BlockStateHelper;
import mekanism.common.tile.transmitter.TileEntityUniversalCable;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TileEntityUniversalCable.class,remap = false)
public class TileEntityUniversalCableMixin {

    @Inject(method = "upgradeResult",at = @At("HEAD"),cancellable = true)
    private void evolvedmekanism$upgradeToNewTier(BlockState current, BaseTier tier, CallbackInfoReturnable<BlockState> cir){
        if (EvolvedMekanism.isEvolvedMekanismTier(tier)){
            IBlockProvider block = null;
            if (tier.equals(EMBaseTier.OVERCLOCKED)) block = EMBlocks.OVERCLOCKED_UNIVERSAL_CABLE;
            else if (tier.equals(EMBaseTier.QUANTUM)) block = EMBlocks.QUANTUM_UNIVERSAL_CABLE;
            else if (tier.equals(EMBaseTier.DENSE)) block = EMBlocks.DENSE_UNIVERSAL_CABLE;
            else if (tier.equals(EMBaseTier.MULTIVERSAL)) block = EMBlocks.MULTIVERSAL_UNIVERSAL_CABLE;
            else if (tier.equals(BaseTier.CREATIVE)) block = EMBlocks.CREATIVE_UNIVERSAL_CABLE;
            cir.setReturnValue(BlockStateHelper.copyStateData(current, block));
        }
    }

}
