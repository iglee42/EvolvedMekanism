package fr.iglee42.evolvedmekanism.mixins.curios;

import fr.iglee42.evolvedmekanism.curios.CuriosHelper;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = LocalPlayer.class, remap = false)
public class LocalPlayerFallFlyingMixin {

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;canElytraFly(Lnet/minecraft/world/entity/LivingEntity;)Z"))
    private boolean evolvedmekanism$clientCanFlyFromCurios(ItemStack chest, LivingEntity entity) {
        if (chest.canElytraFly(entity)) {
            return true;
        }
        ItemStack curios = CuriosHelper.findElytra(entity);
        return !curios.isEmpty() && curios.canElytraFly(entity);
    }
}
