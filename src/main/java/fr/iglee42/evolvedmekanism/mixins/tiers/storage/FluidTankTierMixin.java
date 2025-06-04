package fr.iglee42.evolvedmekanism.mixins.tiers.storage;


import fr.iglee42.evolvedmekanism.interfaces.InitializableEnum;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import fr.iglee42.evolvedmekanism.tiers.storage.EMFluidTankTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.tier.FluidTankTier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = FluidTankTier.class,remap = false)
public class FluidTankTierMixin implements InitializableEnum {
    @Shadow
    @Final
    @Mutable
    private static FluidTankTier[] $VALUES;

    @Invoker("<init>")
    public static FluidTankTier evolvedmekanism$initInvoker(String internalName, int internalId,BaseTier tier, int max, int out){
        throw new AssertionError();
    }

    @Unique
    private static FluidTankTier evolvedmekanism$addVariant(String internalName, BaseTier tier, int max,int out) {
        ArrayList<FluidTankTier> variants = new ArrayList<>(Arrays.asList($VALUES));
        FluidTankTier casing = evolvedmekanism$initInvoker(internalName,
                variants.get(variants.size() - 1).ordinal() + 1,
                tier,max,out);
        variants.add(casing);
        FluidTankTierMixin.$VALUES = variants.toArray(new FluidTankTier[0]);
        return casing;
    }

    @Override
    public void evolvedmekanism$initNewValues() {
        if (EMFluidTankTier.OVERCLOCKED != null) return;
        EMFluidTankTier.OVERCLOCKED = evolvedmekanism$addVariant("OVERCLOCKED", EMBaseTier.OVERCLOCKED, 512_000,256_000);
        EMFluidTankTier.QUANTUM = evolvedmekanism$addVariant("QUANTUM",  EMBaseTier.QUANTUM,1_024_000,1_024_000);
        EMFluidTankTier.DENSE = evolvedmekanism$addVariant("DENSE", EMBaseTier.DENSE,2_048_000,4_096_000);
        EMFluidTankTier.MULTIVERSAL = evolvedmekanism$addVariant("MULTIVERSAL", EMBaseTier.MULTIVERSAL,4_096_000,16_384_000);
    }
}