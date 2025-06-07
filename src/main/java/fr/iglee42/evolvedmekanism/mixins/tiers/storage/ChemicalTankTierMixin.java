package fr.iglee42.evolvedmekanism.mixins.tiers.storage;


import fr.iglee42.evolvedmekanism.interfaces.InitializableEnum;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import fr.iglee42.evolvedmekanism.tiers.storage.EMBinTier;
import fr.iglee42.evolvedmekanism.tiers.storage.EMChemicalTankTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.tier.ChemicalTankTier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = ChemicalTankTier.class,remap = false)
public class ChemicalTankTierMixin implements InitializableEnum {
    @Shadow
    @Final
    @Mutable
    private static ChemicalTankTier[] $VALUES;

    @Invoker("<init>")
    public static ChemicalTankTier evolvedmekanism$initInvoker(String internalName, int internalId,BaseTier tier, long max, long out){
        throw new AssertionError();
    }

    @Unique
    private static ChemicalTankTier evolvedmekanism$addVariant(String internalName, BaseTier tier, long max,long out) {
        ArrayList<ChemicalTankTier> variants = new ArrayList<>(Arrays.asList($VALUES));
        ChemicalTankTier casing = evolvedmekanism$initInvoker(internalName,
                variants.get(variants.size() - 1).ordinal() + 1,
                tier,max,out);
        variants.add(casing);
        ChemicalTankTierMixin.$VALUES = variants.toArray(new ChemicalTankTier[0]);
        return casing;
    }

    @Override
    public void evolvedmekanism$initNewValues() {
        if (EMChemicalTankTier.OVERCLOCKED != null)return;
        EMChemicalTankTier.OVERCLOCKED = evolvedmekanism$addVariant("OVERCLOCKED", EMBaseTier.OVERCLOCKED, 65_536_000L,2_048_000);
        EMChemicalTankTier.QUANTUM = evolvedmekanism$addVariant("QUANTUM",  EMBaseTier.QUANTUM,524_288_000L,8_192_000);
        EMChemicalTankTier.DENSE = evolvedmekanism$addVariant("DENSE", EMBaseTier.DENSE,4_194_304_000L,32_768_000);
        EMChemicalTankTier.MULTIVERSAL = evolvedmekanism$addVariant("MULTIVERSAL", EMBaseTier.MULTIVERSAL,33_554_432_000L,131_072_000);
    }
}