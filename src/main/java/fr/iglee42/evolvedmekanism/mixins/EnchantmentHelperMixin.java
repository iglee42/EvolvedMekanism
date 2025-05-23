package fr.iglee42.evolvedmekanism.mixins;

import fr.iglee42.evolvedmekanism.registries.EMModules;
import mekanism.api.gear.IModule;
import mekanism.common.content.gear.ModuleHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "hasAquaAffinity",at = @At("HEAD"), cancellable = true)
    private static void em$aquaAffinityModule(LivingEntity entity, CallbackInfoReturnable<Boolean> cir){
        if (entity instanceof Player p && ModuleHelper.INSTANCE.isEnabled(entity.getItemBySlot(EquipmentSlot.HEAD), EMModules.AQUA_AFFINITY))cir.setReturnValue(true);
    }
}
