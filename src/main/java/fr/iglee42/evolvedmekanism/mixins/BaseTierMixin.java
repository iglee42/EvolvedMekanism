package fr.iglee42.evolvedmekanism.mixins;


import fr.iglee42.evolvedmekanism.registries.EMBaseTier;
import fr.iglee42.igleelib.api.utils.ModsUtils;
import mekanism.api.tier.BaseTier;
import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = BaseTier.class,remap = false)
public class BaseTierMixin {
    @Shadow
    @Final
    @Mutable
    private static BaseTier[] $VALUES;

    @Invoker("<init>")
    public static BaseTier evolvedmekanism$initInvoker(String internalName, int internalId,String name, int[] rgbCode, MapColor mapColor){
        throw new AssertionError();
    }

    @Inject(method = "<clinit>",at = @At("TAIL"))
    private static void evolvedmekanism$clinit(CallbackInfo ci) {
        EMBaseTier.OVERCLOCKED = evolvedmekanism$addVariant("OVERCLOCKED", new int[]{0, 144, 0},MapColor.COLOR_LIGHT_GREEN);
        EMBaseTier.QUANTUM = evolvedmekanism$addVariant("QUANTUM", new int[]{154, 83, 183},MapColor.COLOR_PURPLE);
        EMBaseTier.DENSE = evolvedmekanism$addVariant("DENSE", new int[]{233, 177, 21},MapColor.GOLD);
        EMBaseTier.MULTIVERSAL = evolvedmekanism$addVariant("MULTIVERSAL", new int[]{66, 66, 67},MapColor.COLOR_BLACK);
    }

    @Unique
    private static BaseTier evolvedmekanism$addVariant(String internalName, int[] rgb,MapColor mapColor) {
        ArrayList<BaseTier> variants = new ArrayList<>(Arrays.asList($VALUES));
        BaseTier casing = evolvedmekanism$initInvoker(internalName,
                variants.get(variants.size() - 1).ordinal() + 1,
                ModsUtils.getUpperName(internalName.toLowerCase(),"_"),
                rgb,
                mapColor);
        variants.add(casing);
        BaseTierMixin.$VALUES = variants.toArray(new BaseTier[0]);
        return casing;
    }
}