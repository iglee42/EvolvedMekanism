package fr.iglee42.evolvedmekanism.mixins.tiers.cable;


import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import fr.iglee42.evolvedmekanism.tiers.cable.EMTubeTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.tier.TubeTier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = TubeTier.class,remap = false)
public class TubeTierMixin {
    @Shadow
    @Final
    @Mutable
    private static TubeTier[] $VALUES;

    @Invoker("<init>")
    public static TubeTier evolvedmekanism$initInvoker(String internalName, int internalId,BaseTier tier, long capacity,long pull){
        throw new AssertionError();
    }

    @Inject(method = "<clinit>",at = @At("TAIL"))
    private static void evolvedmekanism$clinit(CallbackInfo ci) {
        EMTubeTier.OVERCLOCKED = evolvedmekanism$addVariant("OVERCLOCKED", EMBaseTier.OVERCLOCKED, 8_192_000,2_048_000);
        EMTubeTier.QUANTUM = evolvedmekanism$addVariant("QUANTUM",  EMBaseTier.QUANTUM,65_536_000,16_384_000);
        EMTubeTier.DENSE = evolvedmekanism$addVariant("DENSE", EMBaseTier.DENSE,524_288_000,131_072_000);
        EMTubeTier.MULTIVERSAL = evolvedmekanism$addVariant("MULTIVERSAL", EMBaseTier.MULTIVERSAL,4_194_304_000L,1_048_576_000);
        EMTubeTier.CREATIVE = evolvedmekanism$addVariant("CREATIVE", BaseTier.CREATIVE,Long.MAX_VALUE,Long.MAX_VALUE);
    }

    @Unique
    private static TubeTier evolvedmekanism$addVariant(String internalName, BaseTier tier, long storage,long pull) {
        ArrayList<TubeTier> variants = new ArrayList<>(Arrays.asList($VALUES));
        TubeTier casing = evolvedmekanism$initInvoker(internalName,
                variants.get(variants.size() - 1).ordinal() + 1,
                tier,storage,pull);
        variants.add(casing);
        TubeTierMixin.$VALUES = variants.toArray(new TubeTier[0]);
        return casing;
    }
}