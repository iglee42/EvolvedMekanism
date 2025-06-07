package fr.iglee42.evolvedmekanism.mixins.tiers.storage;


import fr.iglee42.evolvedmekanism.interfaces.InitializableEnum;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import fr.iglee42.evolvedmekanism.tiers.storage.EMChemicalTankTier;
import fr.iglee42.evolvedmekanism.tiers.storage.EMQIODriveTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.tier.ChemicalTankTier;
import mekanism.common.tier.QIODriveTier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = QIODriveTier.class,remap = false)
public class QIODriveTierMixin implements InitializableEnum {
    @Shadow
    @Final
    @Mutable
    private static QIODriveTier[] $VALUES;

    @Invoker("<init>")
    public static QIODriveTier evolvedmekanism$initInvoker(String internalName, int internalId,BaseTier tier, long max, int out){
        throw new AssertionError();
    }


    @Unique
    private static QIODriveTier evolvedmekanism$addVariant(String internalName, BaseTier tier, long max,int out) {
        ArrayList<QIODriveTier> variants = new ArrayList<>(Arrays.asList($VALUES));
        QIODriveTier casing = evolvedmekanism$initInvoker(internalName,
                variants.get(variants.size() - 1).ordinal() + 1,
                tier,max,out);
        variants.add(casing);
        QIODriveTierMixin.$VALUES = variants.toArray(new QIODriveTier[0]);
        return casing;
    }

    @Override
    public void evolvedmekanism$initNewValues() {
        if (EMQIODriveTier.BOOSTED != null)return;
        EMQIODriveTier.BOOSTED = evolvedmekanism$addVariant("BOOSTED", EMBaseTier.OVERCLOCKED, 32_000_000_000L,16_384);
        EMQIODriveTier.SINGULARITY = evolvedmekanism$addVariant("SINGULARITY",  EMBaseTier.QUANTUM,64_000_000_000L,32_768);
        EMQIODriveTier.HYPRA_SOLIDIFIED = evolvedmekanism$addVariant("HYPRA_SOLIDIFIED", EMBaseTier.DENSE,128_000_000_000L,65_536);
        EMQIODriveTier.BLACK_HOLE = evolvedmekanism$addVariant("BLACK_HOLE", EMBaseTier.MULTIVERSAL,256_000_000_000L,131_072);
        EMQIODriveTier.CREATIVE = evolvedmekanism$addVariant("CREATIVE", BaseTier.CREATIVE,Long.MAX_VALUE,Integer.MAX_VALUE);
    }
}