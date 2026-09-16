package fr.iglee42.evolvedmekanism.mixins.curios;

import fr.iglee42.evolvedmekanism.curios.CuriosHelper;
import mekanism.common.item.ItemPortableTeleporter;
import mekanism.common.network.to_server.PacketPortableTeleporterTeleport;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = PacketPortableTeleporterTeleport.class, remap = false)
public class PacketPortableTeleporterTeleportMixin {

    @Redirect(method = "handle", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack evolvedmekanism$fromCurios(Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (!held.isEmpty() && held.getItem() instanceof ItemPortableTeleporter) {
            return held;
        }
        return CuriosHelper.resolveFromHandOrCurio(player, hand, stack -> stack.getItem() instanceof ItemPortableTeleporter);
    }
}
