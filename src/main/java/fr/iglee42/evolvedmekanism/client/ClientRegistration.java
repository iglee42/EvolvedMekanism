package fr.iglee42.evolvedmekanism.client;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMBlockEntityTypes;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import mekanism.api.text.EnumColor;
import mekanism.api.tier.BaseTier;
import mekanism.client.ClientRegistrationUtil;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.render.item.TransmitterTypeDecorator;
import mekanism.client.render.tileentity.RenderBin;
import mekanism.client.render.tileentity.RenderEnergyCube;
import mekanism.client.render.tileentity.RenderFluidTank;
import mekanism.client.render.transmitter.*;
import mekanism.common.Mekanism;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.content.gear.shared.ModuleColorModulationUnit;
import mekanism.common.item.block.machine.ItemBlockFluidTank;
import mekanism.common.tile.transmitter.TileEntityLogisticalTransporter;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = EvolvedMekanism.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistration {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public static void registerKeybindings(RegisterKeyMappingsEvent event) {
    }

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        //Register entity rendering handlers

        //Register TileEntityRenderers

        ClientRegistrationUtil.bindTileEntityRenderer(event, RenderBin::new, EMBlockEntityTypes.OVERCLOCKED_BIN, EMBlockEntityTypes.QUANTUM_BIN, EMBlockEntityTypes.DENSE_BIN,
              EMBlockEntityTypes.MULTIVERSAL_BIN);
       ClientRegistrationUtil.bindTileEntityRenderer(event, RenderEnergyCube::new, EMBlockEntityTypes.OVERCLOCKED_ENERGY_CUBE, EMBlockEntityTypes.QUANTUM_ENERGY_CUBE,
             EMBlockEntityTypes.DENSE_ENERGY_CUBE, EMBlockEntityTypes.MULTIVERSAL_ENERGY_CUBE);
       ClientRegistrationUtil.bindTileEntityRenderer(event, RenderFluidTank::new, EMBlockEntityTypes.OVERCLOCKED_FLUID_TANK, EMBlockEntityTypes.QUANTUM_FLUID_TANK, 
               EMBlockEntityTypes.DENSE_FLUID_TANK, EMBlockEntityTypes.MULTIVERSAL_FLUID_TANK);
        //Transmitters
       ClientRegistrationUtil.bindTileEntityRenderer(event, RenderLogisticalTransporter::new, EMBlockEntityTypes.OVERCLOCKED_LOGISTICAL_TRANSPORTER, EMBlockEntityTypes.QUANTUM_LOGISTICAL_TRANSPORTER,
             EMBlockEntityTypes.DENSE_LOGISTICAL_TRANSPORTER, EMBlockEntityTypes.MULTIVERSAL_LOGISTICAL_TRANSPORTER);
       ClientRegistrationUtil.bindTileEntityRenderer(event, RenderMechanicalPipe::new, EMBlockEntityTypes.OVERCLOCKED_MECHANICAL_PIPE,
             EMBlockEntityTypes.QUANTUM_MECHANICAL_PIPE, EMBlockEntityTypes.DENSE_MECHANICAL_PIPE, EMBlockEntityTypes.MULTIVERSAL_MECHANICAL_PIPE);
       ClientRegistrationUtil.bindTileEntityRenderer(event, RenderPressurizedTube::new, EMBlockEntityTypes.OVERCLOCKED_PRESSURIZED_TUBE,
             EMBlockEntityTypes.QUANTUM_PRESSURIZED_TUBE, EMBlockEntityTypes.DENSE_PRESSURIZED_TUBE, EMBlockEntityTypes.MULTIVERSAL_PRESSURIZED_TUBE);
       ClientRegistrationUtil.bindTileEntityRenderer(event, RenderUniversalCable::new, EMBlockEntityTypes.OVERCLOCKED_UNIVERSAL_CABLE,
             EMBlockEntityTypes.QUANTUM_UNIVERSAL_CABLE, EMBlockEntityTypes.DENSE_UNIVERSAL_CABLE, EMBlockEntityTypes.MULTIVERSAL_UNIVERSAL_CABLE);
       ClientRegistrationUtil.bindTileEntityRenderer(event, RenderThermodynamicConductor::new, EMBlockEntityTypes.OVERCLOCKED_THERMODYNAMIC_CONDUCTOR,
             EMBlockEntityTypes.QUANTUM_THERMODYNAMIC_CONDUCTOR, EMBlockEntityTypes.DENSE_THERMODYNAMIC_CONDUCTOR, EMBlockEntityTypes.MULTIVERSAL_THERMODYNAMIC_CONDUCTOR);
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {

    }


    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerContainers(RegisterEvent event) {
        event.register(Registries.MENU, helper -> {

        });
    }




    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        ClientRegistrationUtil.registerBlockColorHandler(event, (state, world, pos, tintIndex) -> {
                  if (tintIndex == 1) {
                      BaseTier tier = Attribute.getBaseTier(state.getBlock());
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
              EMBlocks.MULTIVERSAL_LOGISTICAL_TRANSPORTER);
    }

    @SubscribeEvent
    public static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
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
              EMBlocks.DENSE_PRESSURIZED_TUBE, EMBlocks.MULTIVERSAL_PRESSURIZED_TUBE, EMBlocks.OVERCLOCKED_THERMODYNAMIC_CONDUCTOR,
              EMBlocks.QUANTUM_THERMODYNAMIC_CONDUCTOR, EMBlocks.DENSE_THERMODYNAMIC_CONDUCTOR, EMBlocks.MULTIVERSAL_THERMODYNAMIC_CONDUCTOR,
              EMBlocks.OVERCLOCKED_UNIVERSAL_CABLE, EMBlocks.QUANTUM_UNIVERSAL_CABLE, EMBlocks.DENSE_UNIVERSAL_CABLE, EMBlocks.MULTIVERSAL_UNIVERSAL_CABLE);
    }

}