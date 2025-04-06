package fr.iglee42.evolvedmekanism.mixins.tiers.cable;


import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import fr.iglee42.evolvedmekanism.tiers.cable.EMPipeTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.tier.PipeTier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = PipeTier.class,remap = false)
public class PipeTierMixin {
    @Shadow
    @Final
    @Mutable
    private static PipeTier[] $VALUES;

    @Invoker("<init>")
    public static PipeTier evolvedmekanism$initInvoker(String internalName, int internalId,BaseTier tier, int capacity,int pull){
        throw new AssertionError();
    }

    @Inject(method = "<clinit>",at = @At("TAIL"))
    private static void evolvedmekanism$clinit(CallbackInfo ci) {
        EMPipeTier.OVERCLOCKED = evolvedmekanism$addVariant("OVERCLOCKED", EMBaseTier.OVERCLOCKED, 1_024_000,256_000);
        EMPipeTier.QUANTUM = evolvedmekanism$addVariant("QUANTUM",  EMBaseTier.QUANTUM,8_192_000,2_048_000);
        EMPipeTier.DENSE = evolvedmekanism$addVariant("DENSE", EMBaseTier.DENSE,65_536_000,16_384_000);
        EMPipeTier.MULTIVERSAL = evolvedmekanism$addVariant("MULTIVERSAL", EMBaseTier.MULTIVERSAL,524_288_000,131_072_000);
        EMPipeTier.CREATIVE = evolvedmekanism$addVariant("CREATIVE", BaseTier.CREATIVE,Integer.MAX_VALUE,Integer.MAX_VALUE);
    }

    @Unique
    private static PipeTier evolvedmekanism$addVariant(String internalName, BaseTier tier, int storage,int pull) {
        ArrayList<PipeTier> variants = new ArrayList<>(Arrays.asList($VALUES));
        PipeTier casing = evolvedmekanism$initInvoker(internalName,
                variants.get(variants.size() - 1).ordinal() + 1,
                tier,storage,pull);
        variants.add(casing);
        PipeTierMixin.$VALUES = variants.toArray(new PipeTier[0]);
        return casing;
    }
}