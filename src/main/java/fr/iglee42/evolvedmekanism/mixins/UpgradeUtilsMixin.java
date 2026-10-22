package fr.iglee42.evolvedmekanism.mixins;

import fr.iglee42.emgenerators.registries.EMGenBlocks;
import fr.iglee42.emgenerators.registries.EMGenItems;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import fr.iglee42.evolvedmekanism.registries.EMUpgrades;
import fr.iglee42.evolvedmekanism.utils.ModsCompats;
import mekanism.api.Upgrade;
import mekanism.common.util.UpgradeUtils;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = UpgradeUtils.class,remap = false)
public class UpgradeUtilsMixin {

    @Inject(method = "getStack(Lmekanism/api/Upgrade;I)Lnet/minecraft/world/item/ItemStack;",at =@At("HEAD"),cancellable = true)
    private static void em$newUpgrades(Upgrade upgrade, int count, CallbackInfoReturnable<ItemStack> cir){
        if (upgrade.equals(EMUpgrades.RADIOACTIVE_UPGRADE)) cir.setReturnValue(new ItemStack(EMItems.RADIOACTIVE_UPGRADE.get(),count));
        if (upgrade.equals(EMUpgrades.SOLAR_UPGRADE) && ModsCompats.MEKANISMGENERATORS.isLoaded()) cir.setReturnValue(new ItemStack(EMGenItems.SOLAR_UPGRADE.get(),count));
        else if (upgrade.equals(EMUpgrades.SOLAR_UPGRADE)) cir.setReturnValue(ItemStack.EMPTY);
        if (upgrade.equals(EMUpgrades.LUNAR_UPGRADE) && ModsCompats.MEKANISMGENERATORS.isLoaded()) cir.setReturnValue(new ItemStack(EMGenItems.LUNAR_UPGRADE.get(),count));
        else if (upgrade.equals(EMUpgrades.LUNAR_UPGRADE)) cir.setReturnValue(ItemStack.EMPTY);
    }

    @Inject(method = "getItem",at = @At("HEAD"),cancellable = true)
    private static void em$newUpgradesHolders(Upgrade upgrade, CallbackInfoReturnable<Holder<Item>> cir){
        if (upgrade.equals(EMUpgrades.RADIOACTIVE_UPGRADE)) cir.setReturnValue(Holder.direct(EMItems.RADIOACTIVE_UPGRADE.get()));
        if (upgrade.equals(EMUpgrades.SOLAR_UPGRADE) && ModsCompats.MEKANISMGENERATORS.isLoaded()) cir.setReturnValue(Holder.direct(EMGenItems.SOLAR_UPGRADE.get()));
        else if (upgrade.equals(EMUpgrades.SOLAR_UPGRADE)) cir.setReturnValue(Holder.direct(Items.AIR));
        if (upgrade.equals(EMUpgrades.LUNAR_UPGRADE) && ModsCompats.MEKANISMGENERATORS.isLoaded()) cir.setReturnValue(Holder.direct(EMGenItems.LUNAR_UPGRADE.get()));
        else if (upgrade.equals(EMUpgrades.LUNAR_UPGRADE)) cir.setReturnValue(Holder.direct(Items.AIR));
    }
}
