package fr.iglee42.evolvedmekanism.mixins.tiers.storage;


import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import fr.iglee42.evolvedmekanism.tiers.storage.EMEnergyCubeTier;
import mekanism.api.math.FloatingLong;
import mekanism.api.tier.BaseTier;
import mekanism.common.tier.EnergyCubeTier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = EnergyCubeTier.class,remap = false)
public class EnergyCubeTierMixin {
    @Shadow
    @Final
    @Mutable
    private static EnergyCubeTier[] $VALUES;

    @Invoker("<init>")
    public static EnergyCubeTier evolvedmekanism$initInvoker(String internalName, int internalId,BaseTier tier, FloatingLong max, FloatingLong out){
        throw new AssertionError();
    }

    @Inject(method = "<clinit>",at = @At("TAIL"))
    private static void evolvedmekanism$clinit(CallbackInfo ci) {
        EMEnergyCubeTier.OVERCLOCKED = evolvedmekanism$addVariant("OVERCLOCKED", EMBaseTier.OVERCLOCKED, 1_024_000_000L,1_024_000);
        EMEnergyCubeTier.QUANTUM = evolvedmekanism$addVariant("QUANTUM",  EMBaseTier.QUANTUM,4_096_000_000L,4_096_000);
        EMEnergyCubeTier.DENSE = evolvedmekanism$addVariant("DENSE", EMBaseTier.DENSE,16_384_000_000L,16_384_000);
        EMEnergyCubeTier.MULTIVERSAL = evolvedmekanism$addVariant("MULTIVERSAL", EMBaseTier.MULTIVERSAL,65_536_000_000L,65_536_000);
    }

    @Unique
    private static EnergyCubeTier evolvedmekanism$addVariant(String internalName, BaseTier tier, long max,long out) {
        ArrayList<EnergyCubeTier> variants = new ArrayList<>(Arrays.asList($VALUES));
        EnergyCubeTier casing = evolvedmekanism$initInvoker(internalName,
                variants.get(variants.size() - 1).ordinal() + 1,
                tier,FloatingLong.create(max),FloatingLong.create(out));
        variants.add(casing);
        EnergyCubeTierMixin.$VALUES = variants.toArray(new EnergyCubeTier[0]);
        return casing;
    }
}