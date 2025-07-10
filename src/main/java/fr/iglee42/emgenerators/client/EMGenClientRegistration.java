package fr.iglee42.emgenerators.client;

import fr.iglee42.emgenerators.registries.EMGenBlocks;
import fr.iglee42.emgenerators.registries.EMGenContainerTypes;
import fr.iglee42.emgenerators.tile.TileEntityTieredAdvancedSolarGenerator;
import mekanism.api.gear.IModuleHelper;
import mekanism.client.ClientRegistration;
import mekanism.client.ClientRegistrationUtil;
import mekanism.client.model.baked.ExtensionBakedModel;
import mekanism.client.render.lib.QuadTransformation;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.FluidRegistryObject;
import mekanism.generators.client.GeneratorsSpecialColors;
import mekanism.generators.client.gui.*;
import mekanism.generators.client.model.ModelTurbine;
import mekanism.generators.client.model.ModelWindGenerator;
import mekanism.generators.client.render.*;
import mekanism.generators.client.render.item.RenderWindGeneratorItem;
import mekanism.generators.common.MekanismGenerators;
import mekanism.generators.common.registries.*;
import mekanism.generators.common.tile.TileEntityAdvancedSolarGenerator;
import mekanism.generators.common.tile.TileEntitySolarGenerator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.registries.RegisterEvent;

public class EMGenClientRegistration {

    @SubscribeEvent
    public void init(FMLClientSetupEvent event) {
        ClientRegistration.addCustomModel(EMGenBlocks.ADVANCED_SOLAR_GENERATOR, this::translatedSolarModels);
        ClientRegistration.addCustomModel(EMGenBlocks.ELITE_SOLAR_GENERATOR, this::translatedSolarModels);
        ClientRegistration.addCustomModel(EMGenBlocks.ULTIMATE_SOLAR_GENERATOR, this::translatedSolarModels);
        ClientRegistration.addCustomModel(EMGenBlocks.OVERCLOCKED_SOLAR_GENERATOR, this::translatedSolarModels);
        ClientRegistration.addCustomModel(EMGenBlocks.QUANTUM_SOLAR_GENERATOR, this::translatedSolarModels);
        ClientRegistration.addCustomModel(EMGenBlocks.DENSE_SOLAR_GENERATOR, this::translatedSolarModels);
        ClientRegistration.addCustomModel(EMGenBlocks.MULTIVERSAL_SOLAR_GENERATOR, this::translatedSolarModels);
        ClientRegistration.addCustomModel(EMGenBlocks.CREATIVE_SOLAR_GENERATOR, this::translatedSolarModels);
    }

    private BakedModel translatedSolarModels(BakedModel original, ModelEvent.ModifyBakingResult event){
        return new ExtensionBakedModel.TransformedBakedModel<Void>(original,
                QuadTransformation.translate(0, 1, 0));
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
    public void registerContainers(RegisterMenuScreensEvent event) {
            ClientRegistrationUtil.registerScreen(event,EMGenContainerTypes.TIERED_ADVANCED_SOLAR_GENERATOR, (MekanismTileContainer<TileEntityTieredAdvancedSolarGenerator> container, Inventory inv, Component title) -> new GuiSolarGenerator<>(container, inv, title));
    }

    @SubscribeEvent
    public void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
    }

    @SubscribeEvent
    public void onStitch(TextureAtlasStitchedEvent event) {
        if (!event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
            return;
        }
    }
}
