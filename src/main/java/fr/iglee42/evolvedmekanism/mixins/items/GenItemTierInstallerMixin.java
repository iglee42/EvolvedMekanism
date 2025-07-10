package fr.iglee42.evolvedmekanism.mixins.items;

import fr.iglee42.emgenerators.tile.TileEntityTieredAdvancedSolarGenerator;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import mekanism.api.text.EnumColor;
import mekanism.api.tier.BaseTier;
import mekanism.common.block.attribute.AttributeUpgradeable;
import mekanism.common.item.ItemTierInstaller;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.ITierUpgradable;
import mekanism.generators.common.tile.TileEntityAdvancedSolarGenerator;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = ItemTierInstaller.class,remap = false)
public class GenItemTierInstallerMixin {

    @Inject(method = "useOn",at = @At(value = "INVOKE", target = "Ljava/util/Set;isEmpty()Z",shift = At.Shift.BEFORE),cancellable = true,locals = LocalCapture.CAPTURE_FAILSOFT)
    private void evolvedmekanism$disableOnSolarGenerator(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir, Player player, Level world, BlockPos pos, BlockState state, Holder block, AttributeUpgradeable upgradeableBlock, BaseTier baseTier, BlockState upgradeState, BlockEntity tile, ITierUpgradable tierUpgradable){
        if (tile instanceof TileEntityAdvancedSolarGenerator || tile instanceof TileEntityTieredAdvancedSolarGenerator){
            player.displayClientMessage( EvolvedMekanism.logFormat(EnumColor.RED, "Sorry, tier installers are disabled on these blocks, we're waiting Mekanism to fix the issue."),false);
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

}
