package fr.iglee42.evolvedmekanism.mixins.curios;

import fr.iglee42.evolvedmekanism.curios.CuriosHelper;
import mekanism.common.item.interfaces.IGuiItem;
import mekanism.common.network.to_server.PacketItemGuiInteract;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = PacketItemGuiInteract.class, remap = false)
public class PacketItemGuiInteractMixin {

    @ModifyVariable(method = "handle", at = @At("STORE"), ordinal = 0)
    private ItemStack evolvedmekanism$fromCurios(ItemStack stack, IPayloadContext context) {
        PacketItemGuiInteract self = (PacketItemGuiInteract) (Object) this;
        return CuriosHelper.resolveFromHandOrCurio(context.player(), self.hand(),
                held -> held.getItem() instanceof IGuiItem);
    }
}
