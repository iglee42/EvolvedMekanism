package fr.iglee42.evolvedmekanism.mixins.tiers.storage;


import fr.iglee42.evolvedmekanism.interfaces.InitializableEnum;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import fr.iglee42.evolvedmekanism.tiers.storage.EMInductionProviderTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.tier.InductionProviderTier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = InductionProviderTier.class,remap = false)
public class InductionProviderTierMixin implements InitializableEnum {
    @Shadow
    @Final
    @Mutable
    private static InductionProviderTier[] $VALUES;

    @Invoker("<init>")
    public static InductionProviderTier evolvedmekanism$initInvoker(String internalName, int internalId,BaseTier tier, long out){
        throw new AssertionError();
    }

    @Unique
    private static InductionProviderTier evolvedmekanism$addVariant(String internalName, BaseTier tier, long storage) {
        ArrayList<InductionProviderTier> variants = new ArrayList<>(Arrays.asList($VALUES));
        InductionProviderTier casing = evolvedmekanism$initInvoker(internalName,
                variants.get(variants.size() - 1).ordinal() + 1,
                tier,storage);
        variants.add(casing);
        InductionProviderTierMixin.$VALUES = variants.toArray(new InductionProviderTier[0]);
        return casing;
    }


    @Override
    public void evolvedmekanism$initNewValues() {
        if (EMInductionProviderTier.OVERCLOCKED != null) return;
        EMInductionProviderTier.OVERCLOCKED = evolvedmekanism$addVariant("OVERCLOCKED", EMBaseTier.OVERCLOCKED, 1_048_576_000L);
        EMInductionProviderTier.QUANTUM = evolvedmekanism$addVariant("QUANTUM",  EMBaseTier.QUANTUM,8_388_608_000L);
        EMInductionProviderTier.DENSE = evolvedmekanism$addVariant("DENSE", EMBaseTier.DENSE,67_108_864_000L);
        EMInductionProviderTier.MULTIVERSAL = evolvedmekanism$addVariant("MULTIVERSAL", EMBaseTier.MULTIVERSAL,536_870_912_000L);
        EMInductionProviderTier.CREATIVE = evolvedmekanism$addVariant("CREATIVE", BaseTier.CREATIVE,Long.MAX_VALUE);
    }
}