package fr.iglee42.evolvedmekanism.items;

import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.config.EMConfig;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import mekanism.api.MekanismAPITags;
import mekanism.api.security.IBlockSecurityUtils;
import mekanism.api.text.EnumColor;
import mekanism.api.tier.BaseTier;
import mekanism.client.key.MekKeyHandler;
import mekanism.client.key.MekanismKeyHandler;
import mekanism.common.Mekanism;
import mekanism.common.MekanismLang;
import mekanism.common.advancements.MekanismCriteriaTriggers;
import mekanism.common.block.BlockBounding;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.AttributeHasBounding;
import mekanism.common.block.attribute.AttributeUpgradeable;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.ITierUpgradable;
import mekanism.common.tile.interfaces.ITileDirectional;
import mekanism.common.upgrade.IUpgradeData;
import mekanism.common.util.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
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
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ItemMaxTierInstaller extends Item {


    public ItemMaxTierInstaller(Properties properties) {
        super(properties);
    }

    /** Interpolation linéaire entre deux couleurs RGB */
    private static int lerpRGB(int a, int b, float t) {
        int rA = (a >> 16) & 0xFF;
        int gA = (a >> 8) & 0xFF;
        int bA = a & 0xFF;
        int rB = (b >> 16) & 0xFF;
        int gB = (b >> 8) & 0xFF;
        int bB = b & 0xFF;

        int r = (int) (rA + (rB - rA) * t);
        int g = (int) (gA + (gB - gA) * t);
        int bC = (int) (bA + (bB - bA) * t);

        return (r << 16) | (g << 8) | bC;
    }

    @NotNull
    @Override
    public Component getName(@NotNull ItemStack stack) {
        String baseName = super.getName(stack).getString();

        // Exemple : tes couleurs de tiers (à adapter)
        int[] tierColors = new int[]{BaseTier.BASIC.getColor().getValue(), BaseTier.ADVANCED.getColor().getValue(), BaseTier.ELITE.getColor().getValue(), BaseTier.ULTIMATE.getColor().getValue(), EMBaseTier.OVERCLOCKED.getColor().getValue(), EMBaseTier.QUANTUM.getColor().getValue(), EMBaseTier.DENSE.getColor().getValue(), EMBaseTier.MULTIVERSAL.getColor().getValue()};

        // Temps du jeu (pour animation)
        long time = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.getGameTime() : System.currentTimeMillis() / 50;

        MutableComponent animatedName = Component.literal("");

        // Vitesse et échelle de l’effet
        float speed = 6.0F;
        float shift = (time / speed) % tierColors.length;

        // Boucle sur chaque caractère du nom
        for (int i = 0; i < baseName.length(); i++) {
            // Décalage pour que chaque lettre soit légèrement déphasée
            float colorIndex = (shift + i * 0.3F) % tierColors.length;
            int indexA = (int) Math.floor(colorIndex);
            int indexB = (indexA + 1) % tierColors.length;

            float blend = colorIndex - indexA;

            // Interpolation douce entre deux couleurs (fade)
            int rgbA = tierColors[indexA];
            int rgbB = tierColors[indexB];
            int blended = lerpRGB(rgbA, rgbB, blend);

            // Ajoute la lettre colorée
            animatedName.append(Component.literal(String.valueOf(baseName.charAt(i))).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(blended))));
        }

        return animatedName;
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        if (player == null) {
            return InteractionResult.PASS;
        }
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        if (state.is(MekanismBlocks.BOUNDING_BLOCK)) {
            BlockPos mainPos = BlockBounding.getMainBlockPos(world, pos);
            if (mainPos != null) {
                //If we are a bounding block with a main pos, update to pretend we interacted with the logic block
                pos = mainPos;
                state = world.getBlockState(mainPos);
            }
        }
        if (!IBlockSecurityUtils.INSTANCE.canAccessOrDisplayError(player, world, pos) || state.is(MekanismAPITags.Blocks.BLACKLIST_INSTALLER_UPGRADEABLE)) {
            return InteractionResult.FAIL;
        } else if (world.isClientSide()) {
            return InteractionResult.PASS;
        }
        Holder<Block> block = state.getBlockHolder();
        AttributeUpgradeable upgradeableBlock = Attribute.get(block, AttributeUpgradeable.class);
        if (upgradeableBlock != null) {
            BaseTier baseTier = Attribute.getBaseTier(block);
            BaseTier maxTier = EMConfig.general.maxInstallerTier.getOrDefault();

            if (Objects.equals(baseTier, maxTier)) {
                return InteractionResult.PASS;
            }

            if (baseTier == null) {
                baseTier = BaseTier.BASIC;
            }

            BaseTier toTier = baseTier;
            BlockState upgradeState = upgradeableBlock.upgradeResult(state, toTier);
            while (toTier != maxTier) {
                AttributeUpgradeable nextUpgradeable = Attribute.get(upgradeState.getBlockHolder(), AttributeUpgradeable.class);
                if (nextUpgradeable == null) {
                    break;
                }
                upgradeableBlock = nextUpgradeable;
                upgradeState = upgradeableBlock.upgradeResult(upgradeState, toTier);
                toTier = Attribute.getBaseTier(upgradeState.getBlockHolder());
                if (toTier == null) {
                    break;
                }
            }

            if (state == upgradeState) {
                return InteractionResult.PASS;
            }
            BlockEntity tile = WorldUtils.getTileEntity(world, pos);
            if (tile instanceof ITierUpgradable tierUpgradable) {
                if (tile instanceof TileEntityMekanism tileMek && !tileMek.playersUsing.isEmpty()) {
                    return InteractionResult.FAIL;
                }
                IUpgradeData upgradeData = tierUpgradable.getUpgradeData(world.registryAccess());
                if (upgradeData == null) {
                    if (tierUpgradable.canBeUpgraded()) {
                        Mekanism.logger.warn("Got no upgrade data for block {} at position: {} in {} but it said it would be able to provide some.", block, pos, world.dimension().location());
                        return InteractionResult.FAIL;
                    }
                } else {
                    AttributeHasBounding upgradeBounding = Attribute.get(upgradeState, AttributeHasBounding.class);
                    //If the resulting block has bounding blocks, validate that all of them will be able to be placed
                    if (upgradeBounding != null && !upgradeBounding.handle(world, pos, upgradeState, pos, (level, boundingPos, mainPos) -> {
                        Optional<BlockState> blockState = WorldUtils.getBlockState(level, boundingPos);
                        if (blockState.isPresent()) {
                            BlockState boundingCurrentState = blockState.get();
                            if (boundingCurrentState.canBeReplaced()) {
                                return true;
                            } else if (boundingCurrentState.is(MekanismBlocks.BOUNDING_BLOCK)) {
                                //Treat bounding blocks that will be removed because they are actually part of the unupgraded multiblock as valid
                                // for us to put a new bounding block in
                                return mainPos.equals(BlockBounding.getMainBlockPos(level, boundingPos));
                            }
                        }
                        return false;
                    })) {
                        //At least one bounding block we would be adding can't be placed. Error out instead of upgrading the block
                        return InteractionResult.FAIL;
                    }
                    //Update the block
                    if (!world.setBlockAndUpdate(pos, upgradeState)) {
                        //Something went wrong, bail rather than trying to
                        Mekanism.logger.warn("Error upgrading block at position: {} in {}.", pos, world.dimension().location());
                        return InteractionResult.FAIL;
                    }
                    //Place any bounding blocks the new state may have
                    if (upgradeBounding != null) {
                        upgradeBounding.placeBoundingBlocks(world, pos, upgradeState);
                    }
                    TileEntityMekanism upgradedTile = WorldUtils.getTileEntity(TileEntityMekanism.class, world, pos);
                    if (upgradedTile == null) {
                        Mekanism.logger.warn("Error upgrading block at position: {} in {}. Expected a mekanism block as the result.", pos, world.dimension().location());
                        return InteractionResult.FAIL;
                    }
                    //TODO: Make it so it doesn't have to be a TileEntityMekanism in order to do these things?
                    if (tile instanceof ITileDirectional directional && directional.isDirectional()) {
                        upgradedTile.setFacing(directional.getDirection(), false);
                    }
                    upgradedTile.parseUpgradeData(world.registryAccess(), upgradeData);
                    upgradedTile.resyncMasterToBounding();
                    upgradedTile.sendUpdatePacket();
                    upgradedTile.setChanged();
                    //Notify the level that the caps at the position are no longer valid
                    // In general replacing the tile likely will have caused this to be invalidated
                    // but mark it just to be safe, and in case there are any bounding blocks so that they notify the level their caps might have changed
                    upgradedTile.invalidateCapabilitiesFull();
                    if (!player.isCreative()) {
                        context.getItemInHand().shrink(1);
                    }
                    if (player instanceof ServerPlayer serverPlayer) {
                        MekanismCriteriaTriggers.USE_TIER_INSTALLER.value().trigger(serverPlayer, toTier);
                    }
                    return InteractionResult.CONSUME;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, TooltipContext p_339594_, List<Component> tooltip, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_339594_, tooltip, p_41424_);
        if (MekKeyHandler.isKeyPressed(MekanismKeyHandler.descriptionKey))
            tooltip.add(EvolvedMekanismLang.DESCRIPTION_MAX_TIER_INSTALLER.translate(EMConfig.general.maxInstallerTier.getOrDefault().getSerializedName()));
        else
            tooltip.add(MekanismLang.HOLD_FOR_DESCRIPTION.translateColored(EnumColor.GRAY, EnumColor.AQUA, MekanismKeyHandler.descriptionKey.getTranslatedKeyMessage()));

    }
}