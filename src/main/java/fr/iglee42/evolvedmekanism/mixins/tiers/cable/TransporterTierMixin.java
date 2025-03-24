package fr.iglee42.evolvedmekanism.mixins.tiers.cable;


import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import fr.iglee42.evolvedmekanism.tiers.cable.EMTransporterTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.tier.TransporterTier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = TransporterTier.class,remap = false)
public class TransporterTierMixin {
    @Shadow
    @Final
    @Mutable
    private static TransporterTier[] $VALUES;

    @Invoker("<init>")
    public static TransporterTier evolvedmekanism$initInvoker(String internalName, int internalId,BaseTier tier, int pull,int s){
        throw new AssertionError();
    }

    @Inject(method = "<clinit>",at = @At("TAIL"))
    private static void evolvedmekanism$clinit(CallbackInfo ci) {
        EMTransporterTier.OVERCLOCKED = evolvedmekanism$addVariant("OVERCLOCKED", EMBaseTier.OVERCLOCKED, 128,75);
        EMTransporterTier.QUANTUM = evolvedmekanism$addVariant("QUANTUM",  EMBaseTier.QUANTUM,256,100);
        EMTransporterTier.DENSE = evolvedmekanism$addVariant("DENSE", EMBaseTier.DENSE,512,150);
        EMTransporterTier.MULTIVERSAL = evolvedmekanism$addVariant("MULTIVERSAL", EMBaseTier.MULTIVERSAL,1024,200);
    }

    @Unique
    private static TransporterTier evolvedmekanism$addVariant(String internalName, BaseTier tier, int pull,int s) {
        ArrayList<TransporterTier> variants = new ArrayList<>(Arrays.asList($VALUES));
        TransporterTier casing = evolvedmekanism$initInvoker(internalName,
                variants.get(variants.size() - 1).ordinal() + 1,
                tier,pull,s);
        variants.add(casing);
        TransporterTierMixin.$VALUES = variants.toArray(new TransporterTier[0]);
        return casing;
    }
}