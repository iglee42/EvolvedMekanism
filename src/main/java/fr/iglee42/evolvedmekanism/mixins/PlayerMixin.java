package fr.iglee42.evolvedmekanism.mixins;

import fr.iglee42.evolvedmekanism.registries.EMModules;
import mekanism.api.gear.IModule;
import mekanism.common.content.gear.ModuleHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "getLuck",at = @At("RETURN"), cancellable = true)
    private void em$luckModule(CallbackInfoReturnable<Float> cir){
        Player entity = (Player) (Object)this;
        if (ModuleHelper.INSTANCE.isEnabled(entity.getItemBySlot(EquipmentSlot.LEGS), EMModules.LUCK)){
            IModule<?> module = ModuleHelper.INSTANCE.load(entity.getItemBySlot(EquipmentSlot.LEGS), EMModules.LUCK);
            if (module != null && module.isEnabled()){
                cir.setReturnValue(cir.getReturnValue() + module.getInstalledCount());
            }
        }
    }

}
