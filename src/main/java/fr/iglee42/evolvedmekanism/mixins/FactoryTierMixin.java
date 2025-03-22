package fr.iglee42.evolvedmekanism.mixins;


import fr.iglee42.evolvedmekanism.registries.EMBaseTier;
import fr.iglee42.evolvedmekanism.registries.EMFactoryTier;
import fr.iglee42.igleelib.api.utils.ModsUtils;
import mekanism.api.tier.BaseTier;
import mekanism.common.tier.FactoryTier;
import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = FactoryTier.class,remap = false)
public class FactoryTierMixin {
    @Shadow
    @Final
    @Mutable
    private static FactoryTier[] $VALUES;

    @Invoker("<init>")
    public static FactoryTier evolvedmekanism$initInvoker(String internalName, int internalId,BaseTier tier, int process){
        throw new AssertionError();
    }

    @Inject(method = "<clinit>",at = @At("TAIL"))
    private static void evolvedmekanism$clinit(CallbackInfo ci) {
        EMFactoryTier.OVERCLOCKED = evolvedmekanism$addVariant("OVERCLOCKED", EMBaseTier.OVERCLOCKED,11);
        EMFactoryTier.QUANTUM = evolvedmekanism$addVariant("QUANTUM",  EMBaseTier.QUANTUM,13);
        EMFactoryTier.DENSE = evolvedmekanism$addVariant("DENSE", EMBaseTier.DENSE,15);
        EMFactoryTier.MULTIVERSAL = evolvedmekanism$addVariant("MULTIVERSAL", EMBaseTier.MULTIVERSAL,17);
    }

    @Unique
    private static FactoryTier evolvedmekanism$addVariant(String internalName, BaseTier tier, int process) {
        ArrayList<FactoryTier> variants = new ArrayList<>(Arrays.asList($VALUES));
        FactoryTier casing = evolvedmekanism$initInvoker(internalName,
                variants.get(variants.size() - 1).ordinal() + 1,
                tier,process);
        variants.add(casing);
        FactoryTierMixin.$VALUES = variants.toArray(new FactoryTier[0]);
        return casing;
    }
}