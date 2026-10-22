package fr.iglee42.evolvedmekanism.client;

import fr.iglee42.evolvedmekanism.modules.ModuleAdvancedVisionEnhancementUnit;
import fr.iglee42.evolvedmekanism.registries.EMModules;
import mekanism.api.gear.IModule;
import mekanism.api.gear.IModuleContainer;
import mekanism.api.gear.IModuleHelper;
import mekanism.client.MekanismClient;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.config.MekanismConfig;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.FogType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

public class EMClientTickHandler {

    private static final Minecraft minecraft = Minecraft.getInstance();
    private static final RandomSource rand = RandomSource.create();

    private static boolean advancedVisionEnhancement = false;

    public static void reset() {
        advancedVisionEnhancement = false;
    }


    public static boolean isAdvancedVisionEnhancementOn(Player player) {
        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
        if (!player.getCooldowns().isOnCooldown(head.getItem())) {
            IModuleContainer container = IModuleHelper.INSTANCE.getModuleContainer(head);
            if (container != null) {
                IModule<ModuleAdvancedVisionEnhancementUnit> module = container.getIfEnabled(EMModules.ADVANCED_VISION_ENHANCEMENT);
                return module != null && module.hasEnoughEnergy(head, MekanismConfig.gear.mekaSuitEnergyUsageVisionEnhancement);
            }
        }
        return false;
    }

    private boolean isConnected = false;

    @SubscribeEvent
    public void onJoinServer(ClientPlayerNetworkEvent.LoggingIn event) {
        if (!isConnected) {
            isConnected = true;
        }
    }

    @SubscribeEvent
    public void onLeaveServer(ClientPlayerNetworkEvent.LoggingOut event) {
        if (isConnected) {
            isConnected = false;
            EMClient.reset();
        }
    }


    @SubscribeEvent
    public void onTick(ClientTickEvent.Pre event) {
        if (minecraft.level == null || minecraft.player == null) {
            return;
        }
        boolean tickingNormally = MekanismRenderer.isRunningNormally();

        if (isAdvancedVisionEnhancementOn(minecraft.player)) {
            advancedVisionEnhancement = true;
            // adds if it doesn't exist, otherwise tops off duration to 220. equal or less than 200 will make vision flickers
            minecraft.player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 11 * SharedConstants.TICKS_PER_SECOND, 0, false, false, false));
        } else if (advancedVisionEnhancement) {
            advancedVisionEnhancement = false;
            MobEffectInstance effect = minecraft.player.getEffect(MobEffects.NIGHT_VISION);
            if (effect != null && effect.getDuration() <= 11 * SharedConstants.TICKS_PER_SECOND) {
                //Only remove it if it is our effect and not one that has a longer remaining duration
                minecraft.player.removeEffect(MobEffects.NIGHT_VISION);
            }
        }
    }

    @SubscribeEvent
    public void onFogLighting(ViewportEvent.ComputeFogColor event) {
        if (advancedVisionEnhancement && event.getCamera().getEntity() instanceof Player player) {
            IModule<ModuleAdvancedVisionEnhancementUnit> module = IModuleHelper.INSTANCE.getIfEnabled(player, EquipmentSlot.HEAD, EMModules.ADVANCED_VISION_ENHANCEMENT);
            if (module == null) return;
            if (!module.getCustomInstance().fogEnabled()) return;
            float oldRatio = 0.1F;
            float newRatio = 1 - oldRatio;
            float red = oldRatio * event.getRed();
            float green = oldRatio * event.getGreen();
            float blue = oldRatio * event.getBlue();

            float rRatio = module.getCustomInstance().fogColor().rf();
            float gRatio = module.getCustomInstance().fogColor().gf();
            float bRatio = module.getCustomInstance().fogColor().bf();

            event.setRed(red + newRatio * rRatio);
            event.setGreen(green + newRatio * gRatio);
            event.setBlue(blue + newRatio * bRatio);
        }
    }

    @SubscribeEvent
    public void onFog(ViewportEvent.RenderFog event) {
        if (advancedVisionEnhancement && event.getCamera().getEntity() instanceof Player player) {
            IModule<ModuleAdvancedVisionEnhancementUnit> module = IModuleHelper.INSTANCE.getIfEnabled(player, EquipmentSlot.HEAD, EMModules.ADVANCED_VISION_ENHANCEMENT);
            if (module == null) return;
            if (!module.getCustomInstance().fogEnabled()) return;
            //This near plane is the same as spectators have set for lava and powdered snow
            event.setNearPlaneDistance(-8.0F);
            if (event.getFarPlaneDistance() < 20) {
                float scalar;
                if (event.getType() == FogType.LAVA) {
                    //Special handling for lava which is usually either at 1 or 3
                    scalar = 24 * event.getFarPlaneDistance();
                } else {
                    //Shortly before 27 this ends up being 192, but we want to get it beforehand, so we just allow numbers below 20
                    scalar = 5 + 2.5F * (float) Math.pow(Math.E, 0.16F * event.getFarPlaneDistance());
                }
                //192 is roughly equivalent to what spectators have lava, powdered snow, and a couple other bounds for fog are,
                // so we want to make sure we don't go above that
                event.setFarPlaneDistance(Math.min(192, scalar));
            }
            //Scale the distance based on the number of installed modules
            event.scaleFarPlaneDistance(((float) Math.pow(module.getInstalledCount(), 1.25)) / module.getUntypedData().getMaxStackSize());
            //Cancel the event to ensure our changes are applied
            event.setCanceled(true);
        }

    }

}
