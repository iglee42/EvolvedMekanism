package fr.iglee42.evolvedmekanism.mixins.client;


import fr.iglee42.emtools.client.EMShieldTextures;
import fr.iglee42.evolvedmekanism.interfaces.InitializableEnum;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import mekanism.api.tier.BaseTier;
import mekanism.tools.client.ShieldTextures;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = ShieldTextures.class,remap = false)
public class ShieldTexturesMixin implements InitializableEnum {
    @Shadow
    @Final
    @Mutable
    private static ShieldTextures[] $VALUES;

    @Invoker("<init>")
    public static ShieldTextures evolvedmekanism$initInvoker(String internalName, int internalId,String name){
        throw new AssertionError();
    }

    @Unique
    private static ShieldTextures evolvedmekanism$addVariant(String internalName) {
        ArrayList<ShieldTextures> variants = new ArrayList<>(Arrays.asList($VALUES));
        ShieldTextures casing = evolvedmekanism$initInvoker(internalName,
                variants.get(variants.size() - 1).ordinal() + 1,
                internalName.toLowerCase());
        variants.add(casing);
        ShieldTexturesMixin.$VALUES = variants.toArray(new ShieldTextures[0]);
        return casing;
    }

    @Override
    public void evolvedmekanism$initNewValues() {
        if (EMShieldTextures.BETTER_GOLD != null) return;
        EMShieldTextures.BETTER_GOLD = evolvedmekanism$addVariant("BETTER_GOLD");
        EMShieldTextures.PLASLITHERITE = evolvedmekanism$addVariant("PLASLITHERITE");
        EMShieldTextures.REFINED_REDSTONE = evolvedmekanism$addVariant("REFINED_REDSTONE");
    }
}