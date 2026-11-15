package fr.iglee42.evolvedmekanism.items;

import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.client.ClientNameAnimation;
import fr.iglee42.evolvedmekanism.config.EMConfig;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import mekanism.api.text.EnumColor;
import mekanism.api.tier.BaseTier;
import mekanism.client.key.MekKeyHandler;
import mekanism.client.key.MekanismKeyHandler;
import mekanism.common.Mekanism;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.AttributeUpgradeable;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.ITierUpgradable;
import mekanism.common.tile.interfaces.ITileDirectional;
import mekanism.common.upgrade.IUpgradeData;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ItemMaxTierInstaller extends Item {


    public ItemMaxTierInstaller(Properties properties) {
        super(properties);
    }

    private static BlockState nextUpgradeState(AttributeUpgradeable upgradeable, BlockState current, BaseTier targetTier) {
        try {
            return upgradeable.upgradeResult(current, targetTier);
        } catch (NullPointerException e) {
            return current;
        }
    }

    private static int compareInstallerTiers(BaseTier current, BaseTier target) {
        if (Objects.equals(current, target)) {
            return 0;
        }
        int currentIndex = installerTierIndex(current);
        int targetIndex = installerTierIndex(target);
        if (currentIndex < 0 || targetIndex < 0) {
            return 1;
        }
        return Integer.compare(currentIndex, targetIndex);
    }

    private static int installerTierIndex(BaseTier tier) {
        if (tier == null) {
            return -1;
        }
        if (tier.equals(BaseTier.BASIC)) {
            return 0;
        }
        if (tier.equals(BaseTier.ADVANCED)) {
            return 1;
        }
        if (tier.equals(BaseTier.ELITE)) {
            return 2;
        }
        if (tier.equals(BaseTier.ULTIMATE)) {
            return 3;
        }
        if (tier.equals(EMBaseTier.OVERCLOCKED)) {
            return 4;
        }
        if (tier.equals(EMBaseTier.QUANTUM)) {
            return 5;
        }
        if (tier.equals(EMBaseTier.DENSE)) {
            return 6;
        }
        if (tier.equals(EMBaseTier.MULTIVERSAL)) {
            return 7;
        }
        if (tier.equals(BaseTier.CREATIVE)) {
            return 8;
        }
        return -1;
    }

    @NotNull
    @Override
    public Component getName(@NotNull ItemStack stack) {
        return FMLEnvironment.dist.isClient() ? ClientNameAnimation.animateTierInstaller(stack, super.getName(stack)) : super.getName(stack);
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        if (world.isClientSide || player == null) {
            return InteractionResult.PASS;
        }
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        AttributeUpgradeable upgradeableBlock = Attribute.get(block, AttributeUpgradeable.class);
        if (upgradeableBlock != null) {
            BaseTier baseTier = Attribute.getBaseTier(block);
            BaseTier maxTier = EMConfig.general.getMaxInstallerTier();
            if (baseTier == null) {
                baseTier = BaseTier.BASIC;
            }
            if (maxTier == null) {
                maxTier = BaseTier.CREATIVE;
            }
            if (compareInstallerTiers(baseTier, maxTier) >= 0) {
                return InteractionResult.PASS;
            }

            BlockState upgradeState = state;
            BaseTier toTier = baseTier;
            while (compareInstallerTiers(toTier, maxTier) < 0) {
                AttributeUpgradeable nextUpgradeable = Attribute.get(upgradeState.getBlock(), AttributeUpgradeable.class);
                if (nextUpgradeable == null) {
                    break;
                }
                BlockState candidate = nextUpgradeState(nextUpgradeable, upgradeState, maxTier);
                if (candidate == null || candidate == upgradeState) {
                    break;
                }
                BaseTier candidateTier = Attribute.getBaseTier(candidate.getBlock());
                if (candidateTier == null || compareInstallerTiers(candidateTier, maxTier) > 0) {
                    break;
                }
                upgradeState = candidate;
                toTier = candidateTier;
            }

            if (state == upgradeState) {
                return InteractionResult.PASS;
            }
            BlockEntity tile = WorldUtils.getTileEntity(world, pos);
            if (tile instanceof ITierUpgradable tierUpgradable) {
                if (tile instanceof TileEntityMekanism tileMek && !tileMek.playersUsing.isEmpty()) {
                    return InteractionResult.FAIL;
                }
                IUpgradeData upgradeData = tierUpgradable.getUpgradeData();
                if (upgradeData == null) {
                    if (tierUpgradable.canBeUpgraded()) {
                        Mekanism.logger.warn("Got no upgrade data for block {} at position: {} in {} but it said it would be able to provide some.", block, pos, world);
                        return InteractionResult.FAIL;
                    }
                } else {
                    world.setBlockAndUpdate(pos, upgradeState);
                    //TODO: Make it so it doesn't have to be a TileEntityMekanism?
                    TileEntityMekanism upgradedTile = WorldUtils.getTileEntity(TileEntityMekanism.class, world, pos);
                    if (upgradedTile == null) {
                        Mekanism.logger.warn("Error upgrading block at position: {} in {}.", pos, world);
                        return InteractionResult.FAIL;
                    } else {
                        if (tile instanceof ITileDirectional directional && directional.isDirectional()) {
                            upgradedTile.setFacing(directional.getDirection());
                        }
                        upgradedTile.parseUpgradeData(upgradeData);
                        upgradedTile.sendUpdatePacket();
                        upgradedTile.setChanged();
                        if (!player.isCreative()) {
                            context.getItemInHand().shrink(1);
                        }
                        return InteractionResult.sidedSuccess(world.isClientSide);
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> tooltip, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, tooltip, p_41424_);
        if (!FMLEnvironment.dist.isClient()) return;
        if(MekKeyHandler.isKeyPressed(MekanismKeyHandler.descriptionKey)) tooltip.add(EvolvedMekanismLang.DESCRIPTION_MAX_TIER_INSTALLER.translate( EMConfig.general.getMaxInstallerTier().getSerializedName()));
        else tooltip.add(MekanismLang.HOLD_FOR_DESCRIPTION.translateColored(EnumColor.GRAY, EnumColor.AQUA, MekanismKeyHandler.descriptionKey.getTranslatedKeyMessage()));

    }
}