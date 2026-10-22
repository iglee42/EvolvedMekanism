package fr.iglee42.evolvedmekanism.mixins.curios;

import fr.iglee42.evolvedmekanism.curios.CuriosHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class LivingEntityElytraMixin {

    @Redirect(method = "updateFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;canElytraFly(Lnet/minecraft/world/entity/LivingEntity;)Z", remap = false))
    private boolean evolvedmekanism$keepFromCurios(ItemStack chest, LivingEntity entity) {
        if (chest.canElytraFly(entity)) {
            return true;
        }
        ItemStack curios = CuriosHelper.findElytra(entity);
        return !curios.isEmpty() && curios.canElytraFly(entity);
    }

    @Redirect(method = "updateFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;elytraFlightTick(Lnet/minecraft/world/entity/LivingEntity;I)Z", remap = false))
    private boolean evolvedmekanism$tickFromCurios(ItemStack chest, LivingEntity entity, int flightTicks) {
        if (chest.elytraFlightTick(entity, flightTicks)) {
            return true;
        }
        ItemStack curios = CuriosHelper.findElytra(entity);
        return !curios.isEmpty() && curios.elytraFlightTick(entity, flightTicks);
    }
}
