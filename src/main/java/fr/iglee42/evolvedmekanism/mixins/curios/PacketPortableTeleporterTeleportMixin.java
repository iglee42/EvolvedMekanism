package fr.iglee42.evolvedmekanism.mixins.curios;

import fr.iglee42.evolvedmekanism.curios.CuriosHelper;
import mekanism.common.item.ItemPortableTeleporter;
import mekanism.common.network.to_server.PacketPortableTeleporterTeleport;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = PacketPortableTeleporterTeleport.class, remap = false)
public class PacketPortableTeleporterTeleportMixin {

    @ModifyVariable(method = "handle", at = @At("STORE"), ordinal = 0)
    private ItemStack evolvedmekanism$fromCurios(ItemStack stack, IPayloadContext context) {
        PacketPortableTeleporterTeleport self = (PacketPortableTeleporterTeleport) (Object) this;
        return CuriosHelper.resolveFromHandOrCurio(context.player(), self.currentHand(),
                held -> held.getItem() instanceof ItemPortableTeleporter);
    }
}
