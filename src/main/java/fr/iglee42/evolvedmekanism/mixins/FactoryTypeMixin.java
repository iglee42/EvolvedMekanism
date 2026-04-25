package fr.iglee42.evolvedmekanism.mixins;


import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.registries.EMBlockTypes;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMFactoryType;
import mekanism.common.MekanismLang;
import mekanism.common.content.blocktype.FactoryType;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.registration.impl.BlockRegistryObject;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;
//New Factory type ALLOYING
@SuppressWarnings("unused")
@Mixin(value = FactoryType.class,remap = false)
public class FactoryTypeMixin {
    @Shadow
    @Final
    @Mutable
    private static FactoryType[] $VALUES;

    @Invoker("<init>")
    public static FactoryType evolvedmekanism$initInvoker(String internalName, int internalId, String registryNameComponent, MekanismLang langEntry, Supplier<Machine.FactoryMachine<?>> baseMachine, Supplier<BlockRegistryObject<?, ?>> baseBlock){
        throw new AssertionError();
    }

    @Inject(method = "<clinit>",at = @At("TAIL"))
    private static void evolvedmekanism$clinit(CallbackInfo ci) {
        EMFactoryType.ALLOYING = evolvedmekanism$addVariant(EvolvedMekanismLang.ALLOYING,()-> EMBlockTypes.ALLOYER,()-> EMBlocks.ALLOYER);
    }

    @Unique
    private static FactoryType evolvedmekanism$addVariant(MekanismLang langEntry, Supplier<Machine.FactoryMachine<?>> baseMachine, Supplier<BlockRegistryObject<?, ?>> baseBlock) {
        ArrayList<FactoryType> variants = new ArrayList<>(Arrays.asList($VALUES));
        FactoryType casing = evolvedmekanism$initInvoker("ALLOYING",
                variants.get(variants.size() - 1).ordinal() + 1,
                "alloying",langEntry,baseMachine,baseBlock);
        variants.add(casing);
        FactoryTypeMixin.$VALUES = variants.toArray(new FactoryType[0]);
        return casing;
    }
}