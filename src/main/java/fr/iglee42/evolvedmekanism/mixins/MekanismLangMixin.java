package fr.iglee42.evolvedmekanism.mixins;

import mekanism.common.MekanismLang;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = MekanismLang.class,remap = false)
public class MekanismLangMixin {
    @Shadow
    @Final
    @Mutable
    private static MekanismLang[] $VALUES;

    @Invoker("<init>")
    public static MekanismLang evolvedmekanism$initInvoker(String internalName, int internalId, String type, String path){
        throw new AssertionError();
    }

    @Unique
    private static MekanismLang evolvedmekanism$addVariant(String internalName, String type, String path) {
        ArrayList<MekanismLang> variants = new ArrayList<>(Arrays.asList($VALUES));
        MekanismLang casing = evolvedmekanism$initInvoker(internalName,
                variants.get(variants.size() - 1).ordinal() + 1,
                type,path);
        variants.add(casing);
        MekanismLangMixin.$VALUES = variants.toArray(new MekanismLang[0]);
        return casing;
    }
}