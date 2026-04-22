package fr.iglee42.evolvedmekanism.mixins;

import mekanism.api.text.APILang;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = APILang.class,remap = false)
public class APILangMixin {
    @Shadow
    @Final
    @Mutable
    private static APILang[] $VALUES;

    @Invoker("<init>")
    public static APILang evolvedmekanism$initInvoker(String internalName, int internalId, String type, String path){
        throw new AssertionError();
    }

    @Unique
    private static APILang evolvedmekanism$addVariant(String internalName, String type, String path) {
        ArrayList<APILang> variants = new ArrayList<>(Arrays.asList($VALUES));
        APILang casing = evolvedmekanism$initInvoker(internalName,
                variants.get(variants.size() - 1).ordinal() + 1,
                type,path);
        variants.add(casing);
        APILangMixin.$VALUES = variants.toArray(new APILang[0]);
        return casing;
    }
}