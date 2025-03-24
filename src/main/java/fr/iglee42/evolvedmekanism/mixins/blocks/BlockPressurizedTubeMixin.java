package fr.iglee42.evolvedmekanism.mixins.blocks;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMBlockEntityTypes;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.tier.BaseTier;
import mekanism.common.block.states.BlockStateHelper;
import mekanism.common.block.transmitter.BlockPressurizedTube;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.transmitter.TileEntityPressurizedTube;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockPressurizedTube.class,remap = false)
public abstract class BlockPressurizedTubeMixin {

    @Shadow protected abstract BaseTier getBaseTier();

    @Inject(method = "getTileType",at = @At("HEAD"),cancellable = true)
    private void evolvedmekanism$upgradeToNewTier(CallbackInfoReturnable<TileEntityTypeRegistryObject<TileEntityPressurizedTube>> cir){
        BaseTier tier = getBaseTier();
        if (EvolvedMekanism.isEvolvedMekanismTier(tier)){
            TileEntityTypeRegistryObject<TileEntityPressurizedTube> block = null;
            if (tier.equals(EMBaseTier.OVERCLOCKED)) block = EMBlockEntityTypes.OVERCLOCKED_PRESSURIZED_TUBE;
            else if (tier.equals(EMBaseTier.QUANTUM)) block = EMBlockEntityTypes.QUANTUM_PRESSURIZED_TUBE;
            else if (tier.equals(EMBaseTier.DENSE)) block = EMBlockEntityTypes.DENSE_PRESSURIZED_TUBE;
            else if (tier.equals(EMBaseTier.MULTIVERSAL)) block = EMBlockEntityTypes.MULTIVERSAL_PRESSURIZED_TUBE;
            cir.setReturnValue(block);
        }
    }
}
