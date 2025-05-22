package fr.iglee42.evolvedmekanism.mixins;

import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import mekanism.api.text.APILang;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Inject(method = "<clinit>",at = @At("TAIL"))
    private static void evolvedmekanism$clinit(CallbackInfo ci) {
        EvolvedMekanismLang.UPGRADE_RADIOACTIVE = evolvedmekanism$addVariant("UPGRADE_RADIOACTIVE", "upgrade","radioactive");
        EvolvedMekanismLang.UPGRADE_RADIOACTIVE_DESCRIPTION = evolvedmekanism$addVariant("UPGRADE_RADIOACTIVE_DESCRIPTION", "upgrade","radioactive.description");
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