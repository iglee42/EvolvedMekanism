package fr.iglee42.evolvedmekanism.mixins;

import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import mekanism.api.tier.AlloyTier;
import mekanism.api.tier.ITier;
import mekanism.common.content.network.transmitter.IUpgradeableTransmitter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = IUpgradeableTransmitter.class,remap = false)
public interface IUpgradeableTransmitterMixin {

    @Shadow ITier getTier();

     /**
      * @author iglee42
      * @reason allow upgrade to overclocked
      */
     @Overwrite
     default boolean canUpgrade(AlloyTier alloyTier) {
         if (alloyTier.getBaseTier().equals(EMBaseTier.OVERCLOCKED)) return alloyTier.getBaseTier().ordinal() == getTier().getBaseTier().ordinal() + 2;
         return alloyTier.getBaseTier().ordinal() == getTier().getBaseTier().ordinal() + 1;
     }

}
