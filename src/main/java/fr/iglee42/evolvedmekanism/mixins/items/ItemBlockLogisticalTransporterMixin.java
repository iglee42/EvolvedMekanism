package fr.iglee42.evolvedmekanism.mixins.items;

import fr.iglee42.evolvedmekanism.tiers.cable.EMTransporterTier;
import mekanism.api.text.EnumColor;
import mekanism.client.key.MekKeyHandler;
import mekanism.client.key.MekanismKeyHandler;
import mekanism.common.MekanismLang;
import mekanism.common.item.block.transmitter.ItemBlockLogisticalTransporter;
import mekanism.common.tier.TransporterTier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = ItemBlockLogisticalTransporter.class,remap = false)
public abstract class ItemBlockLogisticalTransporterMixin {

    @Shadow public abstract @NotNull TransporterTier getTier();

    @Inject(method = "addStats",at = @At("HEAD"),cancellable = true)
    private void evolvedmekanism$changeForCreative(@NotNull ItemStack stack, Item.TooltipContext world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag, CallbackInfo ci){
        TransporterTier tier = getTier();
        if (tier.equals(EMTransporterTier.CREATIVE) && !MekKeyHandler.isKeyPressed(MekanismKeyHandler.detailsKey)) {
                tooltip.add(MekanismLang.SPEED.translateColored(EnumColor.INDIGO, EnumColor.GRAY, MekanismLang.INFINITE));//5 = 100 / 20
                tooltip.add(MekanismLang.PUMP_RATE.translateColored(EnumColor.INDIGO, EnumColor.GRAY, MekanismLang.INFINITE));
                ci.cancel();
        }
    }

}
