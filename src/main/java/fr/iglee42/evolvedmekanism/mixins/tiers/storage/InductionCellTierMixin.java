package fr.iglee42.evolvedmekanism.mixins.tiers.storage;


import fr.iglee42.evolvedmekanism.interfaces.InitializableEnum;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import fr.iglee42.evolvedmekanism.tiers.storage.EMInductionCellTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.tier.InductionCellTier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = InductionCellTier.class,remap = false)
public class InductionCellTierMixin implements InitializableEnum {
    @Shadow
    @Final
    @Mutable
    private static InductionCellTier[] $VALUES;

    @Invoker("<init>")
    public static InductionCellTier evolvedmekanism$initInvoker(String internalName, int internalId,BaseTier tier, long max){
        throw new AssertionError();
    }

    @Unique
    private static InductionCellTier evolvedmekanism$addVariant(String internalName, BaseTier tier, long storage) {
        ArrayList<InductionCellTier> variants = new ArrayList<>(Arrays.asList($VALUES));
        InductionCellTier casing = evolvedmekanism$initInvoker(internalName,
                variants.get(variants.size() - 1).ordinal() + 1,
                tier,storage);
        variants.add(casing);
        InductionCellTierMixin.$VALUES = variants.toArray(new InductionCellTier[0]);
        return casing;
    }


    @Override
    public void evolvedmekanism$initNewValues() {
        if (EMInductionCellTier.OVERCLOCKED != null) return;
        EMInductionCellTier.OVERCLOCKED = evolvedmekanism$addVariant("OVERCLOCKED", EMBaseTier.OVERCLOCKED, (long) (3.2*Math.pow(10,13)));
        EMInductionCellTier.QUANTUM = evolvedmekanism$addVariant("QUANTUM",  EMBaseTier.QUANTUM,(long) (2.56*Math.pow(10,14)));
        EMInductionCellTier.DENSE = evolvedmekanism$addVariant("DENSE", EMBaseTier.DENSE,(long)(2.048*Math.pow(10,15)));
        EMInductionCellTier.MULTIVERSAL = evolvedmekanism$addVariant("MULTIVERSAL", EMBaseTier.MULTIVERSAL,(long)(1.6384*Math.pow(10,16)));
        EMInductionCellTier.CREATIVE = evolvedmekanism$addVariant("CREATIVE", BaseTier.CREATIVE,Long.MAX_VALUE);
    }
}