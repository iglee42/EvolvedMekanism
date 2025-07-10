package fr.iglee42.emtools.client;

import fr.iglee42.emgenerators.tile.TileEntityTieredAdvancedSolarGenerator;
import fr.iglee42.emtools.registries.EMToolsItems;
import mekanism.api.providers.IItemProvider;
import mekanism.client.ClientRegistration;
import mekanism.client.ClientRegistrationUtil;
import mekanism.client.model.baked.ExtensionBakedModel;
import mekanism.client.render.lib.QuadTransformation;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.generators.client.gui.GuiSolarGenerator;
import mekanism.tools.common.MekanismTools;
import mekanism.tools.common.registries.ToolsItems;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegisterEvent;

public class EMToolsClientRegistration {

    @SubscribeEvent
    public void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> addShieldPropertyOverrides(MekanismTools.rl("blocking"),
                (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F,
                EMToolsItems.BETTER_GOLD_SHIELD,EMToolsItems.REFINED_REDSTONE_SHIELD, EMToolsItems.PLASLITHERITE_SHIELD));

    }

    private static void addShieldPropertyOverrides(ResourceLocation override, ItemPropertyFunction propertyGetter, IItemProvider... shields) {
        for (IItemProvider shield : shields) {
            ClientRegistrationUtil.setPropertyOverride(shield, override, propertyGetter);
        }
    }


    @SubscribeEvent
    public void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    }

    @SubscribeEvent
    public void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }

    @SubscribeEvent
    public void registerClientReloadListeners(RegisterClientReloadListenersEvent event) {
    }

    @SuppressWarnings("Convert2MethodRef")
    @SubscribeEvent(priority = EventPriority.LOW)
    public void registerContainers(RegisterEvent event) {

    }

    @SubscribeEvent
    public void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
    }

    @SubscribeEvent
    public void onStitch(TextureStitchEvent.Post event) {
        if (!event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
            return;
        }
    }
}
