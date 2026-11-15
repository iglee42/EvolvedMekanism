package fr.iglee42.evolvedmekanism.mixins.oredictionificator;

import fr.iglee42.evolvedmekanism.config.EMConfig;
import fr.iglee42.evolvedmekanism.registries.EMTags;
import mekanism.common.tile.machine.TileEntityOredictionificator;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TileEntityOredictionificator.class,remap = false)
public class TileEntityOredictionificatorMixin {

    @Inject(method = "isPossibleFilter", at = @At("HEAD"), cancellable = true)
    private static void em$disableAlloyBlocksOnPossible(ResourceLocation tag, CallbackInfoReturnable<Boolean> cir){
        if (tag != null){
            if (tag.equals(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS.location()) && !EMConfig.general.allowAlloyBlocksInOredictionificator.get()){
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "isValidTarget", at = @At("HEAD"), cancellable = true)
    private static void em$disableAlloyBlocksAsTarget(ResourceLocation tag, CallbackInfoReturnable<Boolean> cir){
        if (tag != null){
            if (tag.equals(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS.location()) && !EMConfig.general.allowAlloyBlocksInOredictionificator.get()){
                cir.setReturnValue(false);
            }
        }
    }
}
