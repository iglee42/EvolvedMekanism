package fr.iglee42.evolvedmekanism.mixins.tiers.cable;


import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import fr.iglee42.evolvedmekanism.tiers.cable.EMCableTier;
import mekanism.api.math.FloatingLong;
import mekanism.api.tier.BaseTier;
import mekanism.common.tier.CableTier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = CableTier.class,remap = false)
public class CableTierMixin {
    @Shadow
    @Final
    @Mutable
    private static CableTier[] $VALUES;

    @Invoker("<init>")
    public static CableTier evolvedmekanism$initInvoker(String internalName, int internalId,BaseTier tier, FloatingLong out){
        throw new AssertionError();
    }

    @Inject(method = "<clinit>",at = @At("TAIL"))
    private static void evolvedmekanism$clinit(CallbackInfo ci) {
        EMCableTier.OVERCLOCKED = evolvedmekanism$addVariant("OVERCLOCKED", EMBaseTier.OVERCLOCKED, 65_536_000L);
        EMCableTier.QUANTUM = evolvedmekanism$addVariant("QUANTUM",  EMBaseTier.QUANTUM,524_288_000L);
        EMCableTier.DENSE = evolvedmekanism$addVariant("DENSE", EMBaseTier.DENSE,4_194_304_000L);
        EMCableTier.MULTIVERSAL = evolvedmekanism$addVariant("MULTIVERSAL", EMBaseTier.MULTIVERSAL,33_554_432_000L);
    }

    @Unique
    private static CableTier evolvedmekanism$addVariant(String internalName, BaseTier tier, long storage) {
        ArrayList<CableTier> variants = new ArrayList<>(Arrays.asList($VALUES));
        CableTier casing = evolvedmekanism$initInvoker(internalName,
                variants.get(variants.size() - 1).ordinal() + 1,
                tier,FloatingLong.create(storage));
        variants.add(casing);
        CableTierMixin.$VALUES = variants.toArray(new CableTier[0]);
        return casing;
    }
}