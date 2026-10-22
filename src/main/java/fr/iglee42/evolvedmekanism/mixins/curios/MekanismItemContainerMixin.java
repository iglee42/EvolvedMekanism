package fr.iglee42.evolvedmekanism.mixins.curios;

import fr.iglee42.evolvedmekanism.curios.CuriosHelper;
import fr.iglee42.evolvedmekanism.utils.ModsCompats;
import mekanism.common.inventory.container.item.MekanismItemContainer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = MekanismItemContainer.class, remap = false)
public class MekanismItemContainerMixin {

    @Shadow
    protected ItemStack stack;

    @Redirect(method = "stillValid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack evolvedmekanism$heldOrCurio(Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (!held.isEmpty() && (stack.isEmpty() || held.is(stack.getItem()))) {
            return held;
        }
        if (ModsCompats.CURIOS.isLoaded() && !stack.isEmpty() && CuriosHelper.isAvailableOutsideHand(player, stack)) {
            return stack;
        }
        return held;
    }
}
