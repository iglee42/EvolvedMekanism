package fr.iglee42.evolvedmekanism.client;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.modules.ModuleAdvancedVisionEnhancementUnit;
import fr.iglee42.evolvedmekanism.registries.EMModules;
import mekanism.api.gear.IModule;
import mekanism.api.gear.IModuleHelper;
import mekanism.common.config.MekanismConfig;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EvolvedMekanism.MODID, value = Dist.CLIENT)
public class EMClientTickHandler {

    private static final Minecraft minecraft = Minecraft.getInstance();
    private static boolean advancedVisionEnhancement;

    public static void reset() {
        advancedVisionEnhancement = false;
    }

    public static boolean isAdvancedVisionEnhancementOn(Player player) {
        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
        if (!player.getCooldowns().isOnCooldown(head.getItem())) {
            IModule<ModuleAdvancedVisionEnhancementUnit> module = IModuleHelper.INSTANCE.load(head, EMModules.ADVANCED_VISION_ENHANCEMENT);
            return module != null && module.isEnabled() && module.hasEnoughEnergy(MekanismConfig.gear.mekaSuitEnergyUsageVisionEnhancement);
        }
        return false;
    }

    @SubscribeEvent
    public static void onLeaveServer(ClientPlayerNetworkEvent.LoggingOut event) {
        reset();
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START || minecraft.level == null || minecraft.player == null || minecraft.isPaused()) {
            return;
        }
        if (isAdvancedVisionEnhancementOn(minecraft.player)) {
            advancedVisionEnhancement = true;
            minecraft.player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 11 * SharedConstants.TICKS_PER_SECOND, 0, false, false, false));
        } else if (advancedVisionEnhancement) {
            advancedVisionEnhancement = false;
            MobEffectInstance effect = minecraft.player.getEffect(MobEffects.NIGHT_VISION);
            if (effect != null && effect.getDuration() <= 11 * SharedConstants.TICKS_PER_SECOND) {
                minecraft.player.removeEffect(MobEffects.NIGHT_VISION);
            }
        }
    }

    @SubscribeEvent
    public static void onFogLighting(ViewportEvent.ComputeFogColor event) {
        if (!advancedVisionEnhancement || !(event.getCamera().getEntity() instanceof Player player)) {
            return;
        }
        IModule<ModuleAdvancedVisionEnhancementUnit> module = IModuleHelper.INSTANCE.load(player.getItemBySlot(EquipmentSlot.HEAD), EMModules.ADVANCED_VISION_ENHANCEMENT);
        if (module == null || !module.isEnabled() || !module.getCustomInstance().fogEnabled()) {
            return;
        }
        float oldRatio = 0.1F;
        float newRatio = 1 - oldRatio;
        float red = oldRatio * event.getRed();
        float green = oldRatio * event.getGreen();
        float blue = oldRatio * event.getBlue();
        event.setRed(red + newRatio * module.getCustomInstance().fogColor().rf());
        event.setGreen(green + newRatio * module.getCustomInstance().fogColor().gf());
        event.setBlue(blue + newRatio * module.getCustomInstance().fogColor().bf());
    }

    @SubscribeEvent
    public static void onFog(ViewportEvent.RenderFog event) {
        if (!advancedVisionEnhancement || !(event.getCamera().getEntity() instanceof Player player)) {
            return;
        }
        IModule<ModuleAdvancedVisionEnhancementUnit> module = IModuleHelper.INSTANCE.load(player.getItemBySlot(EquipmentSlot.HEAD), EMModules.ADVANCED_VISION_ENHANCEMENT);
        if (module == null || !module.isEnabled() || !module.getCustomInstance().fogEnabled()) {
            return;
        }
        event.setNearPlaneDistance(-8.0F);
        if (event.getFarPlaneDistance() < 20) {
            float scalar;
            if (event.getType() == FogType.LAVA) {
                scalar = 24 * event.getFarPlaneDistance();
            } else {
                scalar = 5 + 2.5F * (float) Math.pow(Math.E, 0.16F * event.getFarPlaneDistance());
            }
            event.setFarPlaneDistance(Math.min(192, scalar));
        }
        event.scaleFarPlaneDistance(((float) Math.pow(module.getInstalledCount(), 1.25)) / module.getData().getMaxStackSize());
        event.setCanceled(true);
    }
}
