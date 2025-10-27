package fr.iglee42.evolvedmekanism.items;

import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
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
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemMaxTierInstaller extends Item {


    public ItemMaxTierInstaller(Properties properties) {
        super(properties);
    }
    @NotNull
    @Override
    public Component getName(@NotNull ItemStack stack) {
        String baseName = super.getName(stack).getString();

        // Exemple : tes couleurs de tiers (à adapter)
        int[] tierColors = new int[]{
                BaseTier.BASIC.getColor().getValue(),BaseTier.ADVANCED.getColor().getValue(),
                BaseTier.ELITE.getColor().getValue(),BaseTier.ULTIMATE.getColor().getValue(),
                EMBaseTier.OVERCLOCKED.getColor().getValue(),EMBaseTier.QUANTUM.getColor().getValue(),
                EMBaseTier.DENSE.getColor().getValue(),EMBaseTier.MULTIVERSAL.getColor().getValue()
        };

        // Temps du jeu (pour animation)
        long time = Minecraft.getInstance().level != null
                ? Minecraft.getInstance().level.getGameTime()
                : System.currentTimeMillis() / 50;

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
            animatedName.append(Component.literal(String.valueOf(baseName.charAt(i)))
                    .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(blended))));
        }

        return animatedName;
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
            BaseTier toTier = baseTier;
            BlockState upgradeState = upgradeableBlock.upgradeResult(state, toTier);
            ;
            while (toTier != EMConfig.general.maxInstallerTier.getOrDefault()) {
                if (Attribute.get(upgradeState.getBlock(), AttributeUpgradeable.class) == null) {
                    break;
                }
                upgradeableBlock = Attribute.get(upgradeState.getBlock(), AttributeUpgradeable.class);
                upgradeState = upgradeableBlock.upgradeResult(upgradeState, toTier);
                toTier = Attribute.getBaseTier(upgradeState.getBlock());
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
        if(MekKeyHandler.isKeyPressed(MekanismKeyHandler.descriptionKey)) tooltip.add(EvolvedMekanismLang.DESCRIPTION_MAX_TIER_INSTALLER.translate( EMConfig.general.maxInstallerTier.getOrDefault().getSerializedName()));
        else tooltip.add(MekanismLang.HOLD_FOR_DESCRIPTION.translateColored(EnumColor.GRAY, EnumColor.AQUA, MekanismKeyHandler.descriptionKey.getTranslatedKeyMessage()));

    }
}