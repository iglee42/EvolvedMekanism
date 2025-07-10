package fr.iglee42.evolvedmekanism.mixins.items;

import fr.iglee42.evolvedmekanism.tiers.cable.EMCableTier;
import fr.iglee42.evolvedmekanism.tiers.cable.EMTubeTier;
import mekanism.api.text.EnumColor;
import mekanism.client.key.MekKeyHandler;
import mekanism.client.key.MekanismKeyHandler;
import mekanism.common.MekanismLang;
import mekanism.common.item.block.transmitter.ItemBlockPressurizedTube;
import mekanism.common.item.block.transmitter.ItemBlockUniversalCable;
import mekanism.common.tier.CableTier;
import mekanism.common.tier.TubeTier;
import mekanism.common.util.text.EnergyDisplay;
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

@Mixin(value = ItemBlockUniversalCable.class,remap = false)
public abstract class ItemBlockUniversalCableMixin {

    @Shadow public abstract @NotNull CableTier getTier();

    @Inject(method = "addStats",at = @At("HEAD"),cancellable = true)
    private void evolvedmekanism$changeForCreative(@NotNull ItemStack stack, Item.TooltipContext world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag, CallbackInfo ci){
        CableTier tier = getTier();
        if (tier.equals(EMCableTier.CREATIVE) && !MekKeyHandler.isKeyPressed(MekanismKeyHandler.detailsKey)) {
            tooltip.add(MekanismLang.CAPACITY_PER_TICK.translateColored(EnumColor.INDIGO, EnumColor.GRAY, MekanismLang.INFINITE));
            ci.cancel();
        }
    }

}
