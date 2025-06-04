package fr.iglee42.evolvedmekanism.mixins.tiers.storage;


import fr.iglee42.evolvedmekanism.interfaces.InitializableEnum;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import fr.iglee42.evolvedmekanism.tiers.storage.EMBinTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.tier.BinTier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = BinTier.class,remap = false)
public class BinTierMixin implements InitializableEnum {
    @Shadow
    @Final
    @Mutable
    private static BinTier[] $VALUES;

    @Invoker("<init>")
    public static BinTier evolvedmekanism$initInvoker(String internalName, int internalId,BaseTier tier, int s){
        throw new AssertionError();
    }

    @Unique
    private static BinTier evolvedmekanism$addVariant(String internalName, BaseTier tier, int storage) {
        ArrayList<BinTier> variants = new ArrayList<>(Arrays.asList($VALUES));
        BinTier casing = evolvedmekanism$initInvoker(internalName,
                variants.get(variants.size() - 1).ordinal() + 1,
                tier,storage);
        variants.add(casing);
        BinTierMixin.$VALUES = variants.toArray(new BinTier[0]);
        return casing;
    }

    @Override
    public void evolvedmekanism$initNewValues() {
        if (EMBinTier.OVERCLOCKED != null)return;
        EMBinTier.OVERCLOCKED = evolvedmekanism$addVariant("OVERCLOCKED", EMBaseTier.OVERCLOCKED,2_097_152);
        EMBinTier.QUANTUM = evolvedmekanism$addVariant("QUANTUM",  EMBaseTier.QUANTUM,16_777_216);
        EMBinTier.DENSE = evolvedmekanism$addVariant("DENSE", EMBaseTier.DENSE,134_217_728);
        EMBinTier.MULTIVERSAL = evolvedmekanism$addVariant("MULTIVERSAL", EMBaseTier.MULTIVERSAL,1_073_741_824);
    }
}