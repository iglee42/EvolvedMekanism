package fr.iglee42.evolvedmekanism.mixins.tiers.cable;


import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import fr.iglee42.evolvedmekanism.tiers.cable.EMConductorTier;
import mekanism.api.heat.HeatAPI;
import mekanism.api.tier.BaseTier;
import mekanism.common.lib.Color;
import mekanism.common.tier.ConductorTier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = ConductorTier.class,remap = false)
public class ConductorTierMixin {
    @Shadow
    @Final
    @Mutable
    private static ConductorTier[] $VALUES;

    @Invoker("<init>")
    public static ConductorTier evolvedmekanism$initInvoker(String internalName, int internalId,BaseTier tier, double conduction, double heatCapacity, double conductionInsulation, Color color){
        throw new AssertionError();
    }

    @Inject(method = "<clinit>",at = @At("TAIL"))
    private static void evolvedmekanism$clinit(CallbackInfo ci) {
        EMConductorTier.OVERCLOCKED = evolvedmekanism$addVariant("OVERCLOCKED", EMBaseTier.OVERCLOCKED, 8_192_000);
        EMConductorTier.QUANTUM = evolvedmekanism$addVariant("QUANTUM",  EMBaseTier.QUANTUM,65_536_000);
        EMConductorTier.DENSE = evolvedmekanism$addVariant("DENSE", EMBaseTier.DENSE,524_288_000);
        EMConductorTier.MULTIVERSAL = evolvedmekanism$addVariant("MULTIVERSAL", EMBaseTier.MULTIVERSAL,4_194_304_000L);
    }

    @Unique
    private static ConductorTier evolvedmekanism$addVariant(String internalName, BaseTier tier, double conduction) {
        ArrayList<ConductorTier> variants = new ArrayList<>(Arrays.asList($VALUES));
        ConductorTier casing = evolvedmekanism$initInvoker(internalName,
                variants.get(variants.size() - 1).ordinal() + 1,
                tier,5, HeatAPI.DEFAULT_HEAT_CAPACITY,conduction,Color.rgbad(0.2, 0.2, 0.2, 1));
        variants.add(casing);
        ConductorTierMixin.$VALUES = variants.toArray(new ConductorTier[0]);
        return casing;
    }
}