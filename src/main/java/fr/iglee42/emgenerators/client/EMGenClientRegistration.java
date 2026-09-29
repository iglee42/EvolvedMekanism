package fr.iglee42.emgenerators.client;

import fr.iglee42.emgenerators.registries.EMGenBlocks;
import fr.iglee42.emgenerators.registries.EMGenContainerTypes;
import fr.iglee42.emgenerators.registries.EMGenTileEntityTypes;
import fr.iglee42.emgenerators.tile.TileEntityLunarGenerator;
import fr.iglee42.emgenerators.tile.TileEntityTieredAdvancedLunarGenerator;
import fr.iglee42.emgenerators.tile.TileEntityTieredAdvancedSolarGenerator;
import fr.iglee42.emgenerators.tile.TileEntityTieredWindGenerator;
import mekanism.api.gear.IModuleHelper;
import mekanism.client.ClientRegistration;
import mekanism.client.ClientRegistrationUtil;
import mekanism.client.render.RenderPropertiesProvider.MekRenderProperties;
import mekanism.client.model.baked.ExtensionBakedModel;
import mekanism.client.render.lib.QuadTransformation;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.FluidRegistryObject;
import mekanism.generators.client.GeneratorsSpecialColors;
import mekanism.generators.client.gui.*;
import mekanism.generators.client.model.ModelTurbine;
import mekanism.generators.client.model.ModelWindGenerator;
import mekanism.generators.client.render.*;
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
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
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

        ClientRegistration.addCustomModel(EMGenBlocks.BASIC_ADVANCED_LUNAR_GENERATOR, this::translatedSolarModels);
        ClientRegistration.addCustomModel(EMGenBlocks.ADVANCED_LUNAR_GENERATOR, this::translatedSolarModels);
        ClientRegistration.addCustomModel(EMGenBlocks.ELITE_LUNAR_GENERATOR, this::translatedSolarModels);
        ClientRegistration.addCustomModel(EMGenBlocks.ULTIMATE_LUNAR_GENERATOR, this::translatedSolarModels);
        ClientRegistration.addCustomModel(EMGenBlocks.OVERCLOCKED_LUNAR_GENERATOR, this::translatedSolarModels);
        ClientRegistration.addCustomModel(EMGenBlocks.QUANTUM_LUNAR_GENERATOR, this::translatedSolarModels);
        ClientRegistration.addCustomModel(EMGenBlocks.DENSE_LUNAR_GENERATOR, this::translatedSolarModels);
        ClientRegistration.addCustomModel(EMGenBlocks.MULTIVERSAL_LUNAR_GENERATOR, this::translatedSolarModels);
        ClientRegistration.addCustomModel(EMGenBlocks.CREATIVE_LUNAR_GENERATOR, this::translatedSolarModels);
    }

    private BakedModel translatedSolarModels(BakedModel original, ModelEvent.ModifyBakingResult event){
        return new ExtensionBakedModel.TransformedBakedModel<Void>(original,
                QuadTransformation.translate(0, 1, 0));
    }

    @SubscribeEvent
    public void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ClientRegistrationUtil.bindTileEntityRenderer(event, RenderTieredWindGenerator::new,
                EMGenTileEntityTypes.ADVANCED_WIND_GENERATOR,
                EMGenTileEntityTypes.ELITE_WIND_GENERATOR,
                EMGenTileEntityTypes.ULTIMATE_WIND_GENERATOR,
                EMGenTileEntityTypes.OVERCLOCKED_WIND_GENERATOR,
                EMGenTileEntityTypes.QUANTUM_WIND_GENERATOR,
                EMGenTileEntityTypes.DENSE_WIND_GENERATOR,
                EMGenTileEntityTypes.MULTIVERSAL_WIND_GENERATOR,
                EMGenTileEntityTypes.CREATIVE_WIND_GENERATOR);
    }

    @SubscribeEvent
    public void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }

    @SubscribeEvent
    public void registerClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(RenderTieredWindGeneratorItem.RENDERER);
    }

    @SuppressWarnings("Convert2MethodRef")
    @SubscribeEvent(priority = EventPriority.LOW)
    public void registerContainers(RegisterMenuScreensEvent event) {
            ClientRegistrationUtil.registerScreen(event,EMGenContainerTypes.TIERED_ADVANCED_SOLAR_GENERATOR, (MekanismTileContainer<TileEntityTieredAdvancedSolarGenerator> container, Inventory inv, Component title) -> new GuiSolarGenerator<>(container, inv, title));
            ClientRegistrationUtil.registerScreen(event,EMGenContainerTypes.LUNAR_GENERATOR, (MekanismTileContainer<TileEntityLunarGenerator> container, Inventory inv, Component title) -> new GuiLunarGenerator<>(container, inv, title));
            ClientRegistrationUtil.registerScreen(event,EMGenContainerTypes.TIERED_ADVANCED_LUNAR_GENERATOR, (MekanismTileContainer<TileEntityTieredAdvancedLunarGenerator> container, Inventory inv, Component title) -> new GuiLunarGenerator<>(container, inv, title));
            ClientRegistrationUtil.registerScreen(event, EMGenContainerTypes.TIERED_WIND_GENERATOR, (MekanismTileContainer<TileEntityTieredWindGenerator> container, Inventory inv, Component title) -> new GuiTieredWindGenerator(container, inv, title));
    }

    @SubscribeEvent
    public void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new MekRenderProperties(RenderTieredWindGeneratorItem.RENDERER),
                EMGenBlocks.ADVANCED_WIND_GENERATOR.getItemHolder(),
                EMGenBlocks.ELITE_WIND_GENERATOR.getItemHolder(),
                EMGenBlocks.ULTIMATE_WIND_GENERATOR.getItemHolder(),
                EMGenBlocks.OVERCLOCKED_WIND_GENERATOR.getItemHolder(),
                EMGenBlocks.QUANTUM_WIND_GENERATOR.getItemHolder(),
                EMGenBlocks.DENSE_WIND_GENERATOR.getItemHolder(),
                EMGenBlocks.MULTIVERSAL_WIND_GENERATOR.getItemHolder(),
                EMGenBlocks.CREATIVE_WIND_GENERATOR.getItemHolder());
        ClientRegistrationUtil.registerBlockExtensions(event, EMGenBlocks.BLOCKS);
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
