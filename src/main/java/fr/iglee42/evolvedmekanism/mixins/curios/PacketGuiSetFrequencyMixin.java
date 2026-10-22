package fr.iglee42.evolvedmekanism.mixins.curios;

import fr.iglee42.evolvedmekanism.curios.CuriosHelper;
import mekanism.common.lib.frequency.IFrequencyItem;
import mekanism.common.network.to_server.PacketGuiSetFrequency;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PacketGuiSetFrequency.class)
public class PacketGuiSetFrequencyMixin {

    @Redirect(method = "handle", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack evolvedmekanism$fromCurios(ServerPlayer player, InteractionHand hand) {
        return CuriosHelper.resolveFromHandOrCurio(player, hand, stack -> stack.getItem() instanceof IFrequencyItem);
    }
}
