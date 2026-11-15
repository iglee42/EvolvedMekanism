package fr.iglee42.evolvedmekanism.mixins;

import mekanism.common.content.network.transmitter.LogisticalTransporterBase;
import mekanism.common.content.transporter.TransporterManager;
import mekanism.common.content.transporter.TransporterStack;
import mekanism.common.util.TransporterUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TransporterUtils.class, remap = false)
public class TransporterUtilsMixin {

    /**
     * Mekanism splits oversized stacks into max-size item entities. Creative (and other EM)
     * transporters can hold millions of items in one stack, which freezes the world on break.
     * Keep the oversized stack as a single entity instead.
     */
    @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
    private static void evolvedmekanism$dropOversizedAsOne(LogisticalTransporterBase transporter, TransporterStack stack, CallbackInfo ci) {
        ItemStack item = stack.itemStack;
        if (item.isEmpty() || item.getCount() <= item.getMaxStackSize()) {
            return;
        }
        BlockPos blockPos;
        if (stack.hasPath()) {
            float[] pos = TransporterUtils.getStackPosition(transporter, stack, 0);
            blockPos = transporter.getBlockPos().offset(Mth.floor(pos[0]), Mth.floor(pos[1]), Mth.floor(pos[2]));
        } else {
            blockPos = transporter.getBlockPos();
        }
        TransporterManager.remove(transporter.getLevel(), stack);
        Block.popResource(transporter.getLevel(), blockPos, item.copy());
        ci.cancel();
    }
}
