package fr.iglee42.evolvedmekanism.mixins.curios;

import fr.iglee42.evolvedmekanism.curios.CuriosHelper;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Player.class, remap = false)
public class PlayerFallFlyingMixin {

    @Inject(method = "tryToStartFallFlying", at = @At("RETURN"), cancellable = true)
    private void evolvedmekanism$startFromCurios(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            return;
        }
        Player player = (Player) (Object) this;
        if (player.onGround() || player.isFallFlying() || player.isInWater() || player.hasEffect(MobEffects.LEVITATION)) {
            return;
        }
        ItemStack curios = CuriosHelper.findElytra(player);
        if (!curios.isEmpty() && curios.canElytraFly(player)) {
            player.startFallFlying();
            cir.setReturnValue(true);
        }
    }
}
