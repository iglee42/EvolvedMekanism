package fr.iglee42.evolvedmekanism.mixins.oredictionificator;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.config.EMConfig;
import fr.iglee42.evolvedmekanism.registries.EMTags;
import mekanism.common.content.oredictionificator.OredictionificatorFilter;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = OredictionificatorFilter.class,remap = false)
public class OredictionificatorFilterMixin<TYPE> {

    @Shadow
    @Nullable TagKey<TYPE> filterLocation;

    @Shadow
    boolean isValid;

    @Inject(method = "checkValidity", at = @At("HEAD"), cancellable = true)
    private void em$disableAlloyBlocks(CallbackInfo ci){
        if (filterLocation != null){
            if (filterLocation.location().equals(EMTags.Blocks.STORAGE_BLOCKS_ALLOYS.location()) && !EMConfig.general.allowAlloyBlocksInOredictionificator.get()){
                isValid = false;
                ci.cancel();
            }
        }
    }
}
