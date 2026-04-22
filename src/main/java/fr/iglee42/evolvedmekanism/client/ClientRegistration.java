package fr.iglee42.evolvedmekanism.client;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.client.gui.*;
import fr.iglee42.evolvedmekanism.client.renderers.RenderAPT;
import fr.iglee42.evolvedmekanism.client.renderers.datas.MultipleCustomRenderData;
import fr.iglee42.evolvedmekanism.particles.RisingBubbleParticle;
import fr.iglee42.evolvedmekanism.registries.*;
import mekanism.client.ClientRegistrationUtil;
import mekanism.client.render.transmitter.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = EvolvedMekanism.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistration {

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(EMParticleTypes.RISING_BUBBLE.get(),
                RisingBubbleParticle.Factory::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        //Register entity rendering handlers

        //Register TileEntityRenderers
        event.registerBlockEntityRenderer(EMTileEntityTypes.APT_CASING.get(), RenderAPT::new);
        event.registerBlockEntityRenderer(EMTileEntityTypes.APT_PORT.get(), RenderAPT::new);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerContainers(RegisterEvent event) {
        event.register(Registries.MENU, helper -> ClientRegistrationUtil.registerScreen(EMContainerTypes.APT, GuiAPT::new));
    }

    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Post event) {
        //noinspection deprecation
        if (!event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
            return;
        }
        CustomModelRenderer.resetCachedModels();
        MultipleCustomRenderData.clearCaches();
    }
}