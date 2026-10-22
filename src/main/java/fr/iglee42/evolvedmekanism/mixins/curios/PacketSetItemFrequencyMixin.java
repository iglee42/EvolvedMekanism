package fr.iglee42.evolvedmekanism.mixins.curios;

import fr.iglee42.evolvedmekanism.curios.CuriosHelper;
import mekanism.common.lib.frequency.IFrequencyItem;
import mekanism.common.network.to_server.frequency.PacketSetItemFrequency;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = PacketSetItemFrequency.class, remap = false)
public class PacketSetItemFrequencyMixin {

    @ModifyVariable(method = "handle", at = @At("STORE"), ordinal = 0)
    private ItemStack evolvedmekanism$fromCurios(ItemStack stack, IPayloadContext context) {
        PacketSetItemFrequency self = (PacketSetItemFrequency) (Object) this;
        return CuriosHelper.resolveFromHandOrCurio(context.player(), self.currentHand(),
                held -> held.getItem() instanceof IFrequencyItem);
    }
}
