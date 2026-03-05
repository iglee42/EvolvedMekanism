package fr.iglee42.evolvedmekanism.mixins;


import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.registries.EMUpgrades;
import mekanism.api.Upgrade;
import mekanism.api.text.EnumColor;
import mekanism.api.text.ILangEntry;
import net.minecraft.util.ByIdMap;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.IntFunction;

@Mixin(value = Upgrade.class,remap = false)
public class UpgradeMixin {
    @Shadow
    @Final
    @Mutable
    private static Upgrade[] $VALUES;

    @Invoker("<init>")
    public static Upgrade evolvedmekanism$initInvoker(String internalName, int internalId, String name, ILangEntry langKey, ILangEntry descLangKey, int maxStack, EnumColor color){
        throw new AssertionError();
    }

    @Inject(method = "<clinit>",at = @At("TAIL"))
    private static void evolvedmekanism$clinit(CallbackInfo ci) {
        EMUpgrades.RADIOACTIVE_UPGRADE = evolvedmekanism$addVariant("RADIOACTIVE", "radioactive", EvolvedMekanismLang.UPGRADE_RADIOACTIVE,EvolvedMekanismLang.UPGRADE_RADIOACTIVE_DESCRIPTION,1, EnumColor.DARK_GREEN);
        EMUpgrades.SOLAR_UPGRADE = evolvedmekanism$addVariant("SOLAR", "solar", EvolvedMekanismLang.UPGRADE_SOLAR,EvolvedMekanismLang.UPGRADE_SOLAR_DESCRIPTION,4, EnumColor.YELLOW);
        EMUpgrades.LUNAR_UPGRADE = evolvedmekanism$addVariant("LUNAR", "lunar", EvolvedMekanismLang.UPGRADE_LUNAR,EvolvedMekanismLang.UPGRADE_LUNAR_DESCRIPTION,4, EnumColor.PINK);

    }

    @Unique
    private static Upgrade evolvedmekanism$addVariant(String internalName, String name, ILangEntry langKey, ILangEntry descLangKey, int maxStack, EnumColor color) {
        ArrayList<Upgrade> variants = new ArrayList<>(Arrays.asList($VALUES));
        Upgrade casing = evolvedmekanism$initInvoker(internalName,
                variants.get(variants.size() - 1).ordinal() + 1,
                internalName.toLowerCase(),
                langKey,descLangKey,maxStack,color);
        variants.add(casing);
        UpgradeMixin.$VALUES = variants.toArray(new Upgrade[0]);
        return casing;
    }

    @Redirect(method = "buildMap",at = @At(value = "INVOKE", target = "Ljava/util/function/IntFunction;apply(I)Ljava/lang/Object;"))
    private static <R> R evolvedmekanism$loadCustomUpgrades(IntFunction instance, int i){
        return (R) ByIdMap.continuous(Enum::ordinal,$VALUES, ByIdMap.OutOfBoundsStrategy.WRAP).apply(i);
    }

}