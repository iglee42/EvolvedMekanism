package fr.iglee42.evolvedmekanism.mixins.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import mekanism.common.item.ItemTierInstaller;
import mekanism.api.tier.BaseTier;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.AttributeUpgradeable;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.ITierUpgradable;
import mekanism.common.tile.interfaces.ITileDirectional;
import mekanism.common.upgrade.IUpgradeData;
import mekanism.common.util.WorldUtils;
import mekanism.common.Mekanism;

import java.lang.reflect.Method;

/**
 * Mixin template for `mekanism.common.item.ItemTierInstaller`.
 * <p>
 * Purpose: Allow a higher-tier installer (e.g. Advanced) to upgrade a lower-tier
 * alloyer (e.g. Basic Alloyer) to the higher tier.
 * <p>
 * Note: Mekanism source may use different method names/signatures across versions.
 * This template uses reflection and attempts common method names. If your Mekanism
 * version uses different names, adjust the method name strings accordingly.
 * <p>
 * Approach:
 * - Inject into `useOn(UseOnContext)` and try to locate the BlockEntity server-side,
 *   then read the current tier via reflection.
 * - If the installer's tier is higher than the block's tier, attempt to call
 *   setter/apply methods via reflection to apply the upgrade.
 * <p>
 * Warning: This template is purposely defensive (try/catch) and does not log by default.
 * Remove or adapt debug output and method calls for your Mekanism version as needed.
 */
@SuppressWarnings("unused")
@Mixin(value = ItemTierInstaller.class, remap = false)
public class ItemTierInstallerMixin {

    // We don't need to shadow item constructor/fields here; we access the original class via reflection
    // when necessary.

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void em$onUseOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        // Only act on the server side
        Level level = context.getLevel();
        if (level.isClientSide()) return;

        BlockPos pos = context.getClickedPos();
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile == null) return;

        try {
            @SuppressWarnings("DataFlowIssue") ItemTierInstaller installer = (ItemTierInstaller) (Object) this;
            BaseTier fromTier = installer.getFromTier();
            BaseTier toTier = installer.getToTier();

            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            AttributeUpgradeable upgradeableBlock = Attribute.get(block, AttributeUpgradeable.class);
            if (upgradeableBlock != null) {
                BaseTier baseTier = Attribute.getBaseTier(block);
                boolean matchesFrom = (fromTier == null ? baseTier == null : (baseTier != null && baseTier.name().equals(fromTier.name())));
                boolean differentTo = !(baseTier != null && toTier != null && baseTier.name().equals(toTier.name()));
                if (matchesFrom && differentTo) {
                    BlockState upgradeState = upgradeableBlock.upgradeResult(state, toTier);
                    if (state == upgradeState) {
                        return;
                    }
                    BlockEntity tileEntity = WorldUtils.getTileEntity(level, pos);
                    if (tileEntity instanceof ITierUpgradable tierUpgradable) {
                        if (tileEntity instanceof TileEntityMekanism tileMek && !tileMek.playersUsing.isEmpty()) {
                            cir.setReturnValue(InteractionResult.FAIL);
                            return;
                        }
                        IUpgradeData upgradeData = tierUpgradable.getUpgradeData();
                        if (upgradeData == null) {
                            if (tierUpgradable.canBeUpgraded()) {
                                Mekanism.logger.warn("Got no upgrade data for block {} at position: {} in {} but it said it would be able to provide some.", block, pos, level);
                                cir.setReturnValue(InteractionResult.FAIL);
                            }
                        } else {
                            level.setBlockAndUpdate(pos, upgradeState);
                            TileEntityMekanism upgradedTile = WorldUtils.getTileEntity(TileEntityMekanism.class, level, pos);
                            if (upgradedTile == null) {
                                Mekanism.logger.warn("Error upgrading block at position: {} in {}.", pos, level);
                                cir.setReturnValue(InteractionResult.FAIL);
                            } else {
                                if (tileEntity instanceof ITileDirectional directional && directional.isDirectional()) {
                                    upgradedTile.setFacing(directional.getDirection());
                                }
                                upgradedTile.parseUpgradeData(upgradeData);
                                upgradedTile.sendUpdatePacket();
                                upgradedTile.setChanged();
                                if (context.getPlayer() != null && !context.getPlayer().isCreative()) {
                                    context.getItemInHand().shrink(1);
                                }
                                cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide()));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Log the exception via Mekanism's logger so output is consistent with the rest of the mod
            Mekanism.logger.warn("Error in ItemTierInstallerMixin while handling installer use at position {}.", pos, e);
        }
    }

    /**
     * Try several possible getter method names via reflection and return the first successful result.
     */
    private Object invokeFirstAvailable(Class<?> clazz, Object instance, String... methodNames) {
        for (String name : methodNames) {
            try {
                Method m = clazz.getMethod(name);
                m.setAccessible(true);
                return m.invoke(instance);
                } catch (NoSuchMethodException ignored) {
                } catch (Throwable t) {
                    // other errors -> continue trying other names
                }
        }
        return null;
    }

    /**
     * Try to call several possible single-parameter methods via reflection (e.g. setTier(installerTier)).
     * If the method returns boolean, that value will be used; otherwise a successful invocation
     * is treated as success.
     */
    private boolean invokeBooleanFirstAvailable(Class<?> clazz, Object instance, String[] methodNames, Object param) {
        for (String name : methodNames) {
            try {
                Method[] methods = clazz.getMethods();
                for (Method m : methods) {
                    if (!m.getName().equals(name)) continue;
                    Class<?>[] params = m.getParameterTypes();
                    if (params.length == 1) {
                        try {
                            m.setAccessible(true);
                            Object ret = m.invoke(instance, param);
                            if (ret instanceof Boolean) return (Boolean) ret;
                            return true; // if no boolean but invocation succeeded -> assume success
                        } catch (Throwable ignored) {
                            // invocation failed, try next method
                        }
                    }
                }
            } catch (Throwable ignored) {
                // ignore and continue to next method name
            }
        }
        return false;
    }


    /**
     * Try to call the `ordinal()` method on enum-like objects via reflection.
     */
    private Integer getOrdinal(Object enumLike) {
        if (enumLike == null) return null;
        try {
            Method ord = enumLike.getClass().getMethod("ordinal");
            ord.setAccessible(true);
            Object o = ord.invoke(enumLike);
            if (o instanceof Integer) return (Integer) o;
        } catch (NoSuchMethodException ignored) {
        } catch (Throwable t) {
            // ignore and return null
        }
        return null;
    }
}





