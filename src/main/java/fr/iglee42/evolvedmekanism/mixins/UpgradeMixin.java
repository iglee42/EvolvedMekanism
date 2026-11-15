package fr.iglee42.evolvedmekanism.mixins;


import com.mojang.serialization.Codec;
import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.registries.EMUpgrades;
import io.netty.buffer.ByteBuf;
import mekanism.api.Upgrade;
import mekanism.api.text.EnumColor;
import mekanism.api.text.ILangEntry;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntFunction;

@Mixin(value = Upgrade.class,remap = false)
public class UpgradeMixin {
    @Shadow
    @Final
    @Mutable
    private static Upgrade[] $VALUES;

    @Shadow
    @Final
    @Mutable
    private static Codec<Upgrade> CODEC;

    @Shadow
    @Final
    @Mutable
    private static IntFunction<Upgrade> BY_ID;

    @Shadow
    @Final
    @Mutable
    private static StreamCodec<ByteBuf, Upgrade> STREAM_CODEC;

    @Invoker("<init>")
    public static Upgrade evolvedmekanism$initInvoker(String internalName, int internalId, String name, ILangEntry langKey, ILangEntry descLangKey, int maxStack, EnumColor color){
        throw new AssertionError();
    }

    @Inject(method = "<clinit>",at = @At("TAIL"))
    private static void evolvedmekanism$clinit(CallbackInfo ci) {
        EMUpgrades.RADIOACTIVE_UPGRADE = evolvedmekanism$addVariant("RADIOACTIVE", "radioactive", EvolvedMekanismLang.UPGRADE_RADIOACTIVE,EvolvedMekanismLang.UPGRADE_RADIOACTIVE_DESCRIPTION,1, EnumColor.DARK_GREEN);
        EMUpgrades.SOLAR_UPGRADE = evolvedmekanism$addVariant("SOLAR", "solar", EvolvedMekanismLang.UPGRADE_SOLAR,EvolvedMekanismLang.UPGRADE_SOLAR_DESCRIPTION,4, EnumColor.YELLOW);
        EMUpgrades.LUNAR_UPGRADE = evolvedmekanism$addVariant("LUNAR", "lunar", EvolvedMekanismLang.UPGRADE_LUNAR,EvolvedMekanismLang.UPGRADE_LUNAR_DESCRIPTION,4, EnumColor.PINK);
        // CODEC, BY_ID and STREAM_CODEC were built from the original constants, before these existed.
        // Removal packets and synced upgrade components resolve through them, so an extra ordinal wrapped onto another upgrade.
        evolvedmekanism$refreshLookups();
    }

    @Unique
    private static void evolvedmekanism$refreshLookups() {
        Function<String, Upgrade> lookup = StringRepresentable.createNameLookup($VALUES, Function.identity());
        Function<String, Upgrade> remapper = name -> "gas".equals(name) ? Upgrade.CHEMICAL : lookup.apply(name);
        CODEC = new StringRepresentable.EnumCodec<>($VALUES, remapper);
        BY_ID = ByIdMap.continuous(Upgrade::ordinal, $VALUES, ByIdMap.OutOfBoundsStrategy.WRAP);
        STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Upgrade::ordinal);
        evolvedmekanism$refreshUpgradeList();
    }

    @Unique
    private static void evolvedmekanism$refreshUpgradeList() {
        try {
            Class<?> utils = Class.forName("mekanism.common.util.EnumUtils");
            Field upgrades = utils.getDeclaredField("UPGRADES");
            upgrades.setAccessible(true);
            upgrades.set(null, Arrays.copyOf($VALUES, $VALUES.length));
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
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