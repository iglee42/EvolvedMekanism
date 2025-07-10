package fr.iglee42.evolvedmekanism.client;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.client.gui.*;
import fr.iglee42.evolvedmekanism.client.renderers.RenderAPT;
import fr.iglee42.evolvedmekanism.client.renderers.RenderTieredPersonalChest;
import fr.iglee42.evolvedmekanism.client.renderers.datas.MultipleCustomRenderData;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMContainerTypes;
import fr.iglee42.evolvedmekanism.registries.EMFluids;
import fr.iglee42.evolvedmekanism.registries.EMTileEntityTypes;
import mekanism.api.text.EnumColor;
import mekanism.api.tier.BaseTier;
import mekanism.client.ClientRegistrationUtil;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.render.RenderPropertiesProvider;
import mekanism.client.render.armor.*;
import mekanism.client.render.item.TransmitterTypeDecorator;
import mekanism.client.render.item.block.RenderEnergyCubeItem;
import mekanism.client.render.item.block.RenderFluidTankItem;
import mekanism.client.render.item.gear.*;
import mekanism.client.render.tileentity.RenderBin;
import mekanism.client.render.tileentity.RenderEnergyCube;
import mekanism.client.render.tileentity.RenderFluidTank;
import mekanism.client.render.transmitter.*;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.item.block.machine.ItemBlockFluidTank;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.registries.MekanismFluids;
import mekanism.common.registries.MekanismItems;
import mekanism.common.tile.transmitter.TileEntityLogisticalTransporter;
import mekanism.common.util.WorldUtils;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = EvolvedMekanism.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientRegistration {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            //Set fluids to a translucent render layer
            for (Holder<Fluid> fluid : MekanismFluids.FLUIDS.getFluidEntries()) {
                ItemBlockRenderTypes.setRenderLayer(fluid.value(), RenderType.translucent());
            }
        });
    }

    @SubscribeEvent
    public static void registerKeybindings(RegisterKeyMappingsEvent event) {
    }

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiLayersEvent event) {
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        //Register entity rendering handlers

        //Register TileEntityRenderers

        ClientRegistrationUtil.bindTileEntityRenderer(event, RenderBin::new, EMTileEntityTypes.OVERCLOCKED_BIN, EMTileEntityTypes.QUANTUM_BIN, EMTileEntityTypes.DENSE_BIN,
              EMTileEntityTypes.MULTIVERSAL_BIN);
       ClientRegistrationUtil.bindTileEntityRenderer(event, RenderEnergyCube::new, EMTileEntityTypes.OVERCLOCKED_ENERGY_CUBE, EMTileEntityTypes.QUANTUM_ENERGY_CUBE,
             EMTileEntityTypes.DENSE_ENERGY_CUBE, EMTileEntityTypes.MULTIVERSAL_ENERGY_CUBE);
       ClientRegistrationUtil.bindTileEntityRenderer(event, RenderFluidTank::new, EMTileEntityTypes.OVERCLOCKED_FLUID_TANK, EMTileEntityTypes.QUANTUM_FLUID_TANK,
               EMTileEntityTypes.DENSE_FLUID_TANK, EMTileEntityTypes.MULTIVERSAL_FLUID_TANK);
        //Transmitters
       ClientRegistrationUtil.bindTileEntityRenderer(event, RenderLogisticalTransporter::new, EMTileEntityTypes.OVERCLOCKED_LOGISTICAL_TRANSPORTER, EMTileEntityTypes.QUANTUM_LOGISTICAL_TRANSPORTER,
             EMTileEntityTypes.DENSE_LOGISTICAL_TRANSPORTER, EMTileEntityTypes.MULTIVERSAL_LOGISTICAL_TRANSPORTER,EMTileEntityTypes.CREATIVE_LOGISTICAL_TRANSPORTER);
       ClientRegistrationUtil.bindTileEntityRenderer(event, RenderMechanicalPipe::new, EMTileEntityTypes.OVERCLOCKED_MECHANICAL_PIPE,
             EMTileEntityTypes.QUANTUM_MECHANICAL_PIPE, EMTileEntityTypes.DENSE_MECHANICAL_PIPE, EMTileEntityTypes.MULTIVERSAL_MECHANICAL_PIPE,EMTileEntityTypes.CREATIVE_MECHANICAL_PIPE);
       ClientRegistrationUtil.bindTileEntityRenderer(event, RenderPressurizedTube::new, EMTileEntityTypes.OVERCLOCKED_PRESSURIZED_TUBE,
             EMTileEntityTypes.QUANTUM_PRESSURIZED_TUBE, EMTileEntityTypes.DENSE_PRESSURIZED_TUBE, EMTileEntityTypes.MULTIVERSAL_PRESSURIZED_TUBE,EMTileEntityTypes.CREATIVE_PRESSURIZED_TUBE);
       ClientRegistrationUtil.bindTileEntityRenderer(event, RenderUniversalCable::new, EMTileEntityTypes.OVERCLOCKED_UNIVERSAL_CABLE,
             EMTileEntityTypes.QUANTUM_UNIVERSAL_CABLE, EMTileEntityTypes.DENSE_UNIVERSAL_CABLE, EMTileEntityTypes.MULTIVERSAL_UNIVERSAL_CABLE,EMTileEntityTypes.CREATIVE_UNIVERSAL_CABLE);
       ClientRegistrationUtil.bindTileEntityRenderer(event, RenderThermodynamicConductor::new, EMTileEntityTypes.OVERCLOCKED_THERMODYNAMIC_CONDUCTOR,
             EMTileEntityTypes.QUANTUM_THERMODYNAMIC_CONDUCTOR, EMTileEntityTypes.DENSE_THERMODYNAMIC_CONDUCTOR, EMTileEntityTypes.MULTIVERSAL_THERMODYNAMIC_CONDUCTOR, EMTileEntityTypes.CREATIVE_THERMODYNAMIC_CONDUCTOR);
        ClientRegistrationUtil.bindTileEntityRenderer(event,RenderTieredPersonalChest::new, EMTileEntityTypes.ADVANCED_PERSONAL_CHEST,
                EMTileEntityTypes.ELITE_PERSONAL_CHEST, EMTileEntityTypes.ULTIMATE_PERSONAL_CHEST, EMTileEntityTypes.OVERCLOCKED_PERSONAL_CHEST,
                EMTileEntityTypes.QUANTUM_PERSONAL_CHEST, EMTileEntityTypes.DENSE_PERSONAL_CHEST, EMTileEntityTypes.MULTIVERSAL_PERSONAL_CHEST,EMTileEntityTypes.CREATIVE_PERSONAL_CHEST);

        event.registerBlockEntityRenderer(EMTileEntityTypes.APT_CASING.get(), RenderAPT::new);
        event.registerBlockEntityRenderer(EMTileEntityTypes.APT_PORT.get(), RenderAPT::new);
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {

    }


    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerContainers(RegisterMenuScreensEvent event) {
            ClientRegistrationUtil.registerScreen(event,EMContainerTypes.TIERED_PERSONAL_STORAGE_ITEM, GuiTieredPersonalStorageItem::new);
            ClientRegistrationUtil.registerScreen(event,EMContainerTypes.TIERED_PERSONAL_STORAGE_BLOCK, GuiTieredPersonalStorageTile::new);
            ClientRegistrationUtil.registerScreen(event,EMContainerTypes.ALLOYER, GuiAlloyer::new);
            ClientRegistrationUtil.registerScreen(event,EMContainerTypes.CHEMIXER, GuiChemixer::new);
            ClientRegistrationUtil.registerScreen(event,EMContainerTypes.APT, GuiAPT::new);
            ClientRegistrationUtil.registerScreen(event,EMContainerTypes.MELTER, GuiMelter::new);
            ClientRegistrationUtil.registerScreen(event,EMContainerTypes.SOLIDIFIER, GuiSolidifier::new);

    }




    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        ClientRegistrationUtil.registerBlockColorHandler(event, (state, world, pos, tintIndex) -> {
                  if (tintIndex == 1) {
                      BaseTier tier = Attribute.getBaseTier(state.getBlockHolder());
                      if (tier != null) {
                          return MekanismRenderer.getColorARGB(tier, 1);
                      }
                  }
                  return -1;
              }, EMBlocks.OVERCLOCKED_FLUID_TANK, EMBlocks.QUANTUM_FLUID_TANK, EMBlocks.DENSE_FLUID_TANK, EMBlocks.MULTIVERSAL_FLUID_TANK);
        ClientRegistrationUtil.registerBlockColorHandler(event, (state, world, pos, tintIndex) -> {
                  if (tintIndex == 1 && pos != null) {
                      TileEntityLogisticalTransporter transporter = WorldUtils.getTileEntity(TileEntityLogisticalTransporter.class, world, pos);
                      if (transporter != null) {
                          EnumColor renderColor = transporter.getTransmitter().getColor();
                          if (renderColor != null) {
                              return MekanismRenderer.getColorARGB(renderColor, 1);
                          }
                      }
                  }
                  return -1;
              }, EMBlocks.OVERCLOCKED_LOGISTICAL_TRANSPORTER, EMBlocks.QUANTUM_LOGISTICAL_TRANSPORTER, EMBlocks.DENSE_LOGISTICAL_TRANSPORTER,
              EMBlocks.MULTIVERSAL_LOGISTICAL_TRANSPORTER,EMBlocks.CREATIVE_LOGISTICAL_TRANSPORTER);
    }

    @SubscribeEvent
    public static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        ClientRegistrationUtil.registerBucketColorHandler(event, EMFluids.FLUIDS);
        ClientRegistrationUtil.registerItemColorHandler(event, (stack, tintIndex) -> {
                  Item item = stack.getItem();
                  if (tintIndex == 1 && item instanceof ItemBlockFluidTank tank) {
                      return MekanismRenderer.getColorARGB(tank.getTier().getBaseTier(), 1);
                  }
                  return -1;
              }, EMBlocks.OVERCLOCKED_FLUID_TANK, EMBlocks.QUANTUM_FLUID_TANK, EMBlocks.DENSE_FLUID_TANK, EMBlocks.MULTIVERSAL_FLUID_TANK);

    }

    @SubscribeEvent
    public static void registerItemDecorations(RegisterItemDecorationsEvent event) {
        TransmitterTypeDecorator.registerDecorators(event, EMBlocks.OVERCLOCKED_PRESSURIZED_TUBE, EMBlocks.QUANTUM_PRESSURIZED_TUBE,
              EMBlocks.DENSE_PRESSURIZED_TUBE, EMBlocks.MULTIVERSAL_PRESSURIZED_TUBE, EMBlocks.CREATIVE_PRESSURIZED_TUBE, EMBlocks.OVERCLOCKED_THERMODYNAMIC_CONDUCTOR,
              EMBlocks.QUANTUM_THERMODYNAMIC_CONDUCTOR, EMBlocks.DENSE_THERMODYNAMIC_CONDUCTOR, EMBlocks.MULTIVERSAL_THERMODYNAMIC_CONDUCTOR, EMBlocks.CREATIVE_THERMODYNAMIC_CONDUCTOR,
              EMBlocks.OVERCLOCKED_UNIVERSAL_CABLE, EMBlocks.QUANTUM_UNIVERSAL_CABLE, EMBlocks.DENSE_UNIVERSAL_CABLE, EMBlocks.MULTIVERSAL_UNIVERSAL_CABLE, EMBlocks.CREATIVE_UNIVERSAL_CABLE);
    }

    @SubscribeEvent
    public static void onStitch(TextureAtlasStitchedEvent event) {
        if (!event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
            return;
        }
        CustomModelRenderer.resetCachedModels();
        MultipleCustomRenderData.clearCaches();
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {

        ClientRegistrationUtil.registerItemExtensions(event, new RenderPropertiesProvider.MekRenderProperties(RenderEnergyCubeItem.RENDERER), EMBlocks.OVERCLOCKED_ENERGY_CUBE,
                EMBlocks.QUANTUM_ENERGY_CUBE, EMBlocks.DENSE_ENERGY_CUBE, EMBlocks.MULTIVERSAL_ENERGY_CUBE);
        ClientRegistrationUtil.registerItemExtensions(event, new RenderPropertiesProvider.MekRenderProperties(RenderFluidTankItem.RENDERER), EMBlocks.OVERCLOCKED_FLUID_TANK,
                EMBlocks.QUANTUM_FLUID_TANK, EMBlocks.DENSE_FLUID_TANK, EMBlocks.MULTIVERSAL_FLUID_TANK);

        ClientRegistrationUtil.registerBlockExtensions(event, EMBlocks.BLOCKS);
        ClientRegistrationUtil.registerFluidExtensions(event, EMFluids.FLUIDS);
    }

}