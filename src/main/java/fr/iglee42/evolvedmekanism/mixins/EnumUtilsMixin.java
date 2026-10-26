package fr.iglee42.evolvedmekanism.mixins;

import mekanism.api.Upgrade;
import mekanism.common.util.EnumUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EnumUtils.class, remap = false)
public class EnumUtilsMixin {
    @Shadow
    @Final
    @Mutable
    private static Upgrade[] UPGRADES;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void evolvedmekanism$includeAddedUpgrades(CallbackInfo ci) {
        UPGRADES = Upgrade.values();
    }
}
