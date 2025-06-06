package fr.iglee42.evolvedmekanism.mixins.tiers;


import fr.iglee42.evolvedmekanism.interfaces.InitializableEnum;
import fr.iglee42.evolvedmekanism.tiers.EMAlloyTier;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import mekanism.api.tier.AlloyTier;
import mekanism.api.tier.BaseTier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = AlloyTier.class,remap = false)
public class AlloyTierMixin implements InitializableEnum {
    @Shadow
    @Final
    @Mutable
    private static AlloyTier[] $VALUES;

    @Invoker("<init>")
    public static AlloyTier evolvedmekanism$initInvoker(String internalName, int internalId,String name, BaseTier tier){
        throw new AssertionError();
    }

    @Unique
    private static AlloyTier evolvedmekanism$addVariant(String internalName, BaseTier baseTier) {
        ArrayList<AlloyTier> variants = new ArrayList<>(Arrays.asList($VALUES));
        AlloyTier casing = evolvedmekanism$initInvoker(internalName,
                variants.get(variants.size() - 1).ordinal() + 1,
                internalName.toLowerCase(),
                baseTier);
        variants.add(casing);
        AlloyTierMixin.$VALUES = variants.toArray(new AlloyTier[0]);
        return casing;
    }

    @Override
    public void evolvedmekanism$initNewValues() {
        if (EMAlloyTier.HYPERCHARGED != null)return;
        EMAlloyTier.HYPERCHARGED = evolvedmekanism$addVariant("HYPERCHARGED", EMBaseTier.OVERCLOCKED);
        EMAlloyTier.SUBATOMIC = evolvedmekanism$addVariant("SUBATOMIC", EMBaseTier.QUANTUM);
        EMAlloyTier.SINGULAR = evolvedmekanism$addVariant("SINGULAR", EMBaseTier.DENSE);
        EMAlloyTier.EXOVERSAL = evolvedmekanism$addVariant("EXOVERSAL", EMBaseTier.MULTIVERSAL);
        EMAlloyTier.CREATIVE = evolvedmekanism$addVariant("CREATIVE", BaseTier.CREATIVE);
    }
}