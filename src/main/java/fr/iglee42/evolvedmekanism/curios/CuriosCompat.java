package fr.iglee42.evolvedmekanism.curios;

import fr.iglee42.evolvedmekanism.curios.client.CuriosClient;
import fr.iglee42.evolvedmekanism.network.PacketCurioAction;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import mekanism.common.registries.MekanismItems;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import top.theillusivec4.curios.api.CuriosCapability;

public final class CuriosCompat {

    private CuriosCompat() {
    }

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(CuriosCompat::registerCapabilities);
        modEventBus.addListener(CuriosCompat::registerPackets);
        NeoForge.EVENT_BUS.register(CuriosGameplay.class);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            CuriosClient.register(modEventBus);
        }
    }

    private static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(PacketCurioAction.TYPE, PacketCurioAction.STREAM_CODEC, PacketCurioAction::handle);
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        Item[] items = {
                MekanismItems.CANTEEN.asItem(),
                MekanismItems.PORTABLE_QIO_DASHBOARD.asItem(),
                MekanismItems.DOSIMETER.asItem(),
                MekanismItems.GEIGER_COUNTER.asItem(),
                MekanismItems.HDPE_REINFORCED_ELYTRA.asItem(),
                MekanismItems.ENERGY_TABLET.asItem(),
                MekanismItems.PORTABLE_TELEPORTER.asItem(),
                MekanismItems.SCUBA_MASK.asItem(),
                MekanismItems.SCUBA_TANK.asItem(),
                MekanismItems.FREE_RUNNERS.asItem(),
                MekanismItems.ARMORED_FREE_RUNNERS.asItem(),
                MekanismItems.JETPACK.asItem(),
                MekanismItems.ARMORED_JETPACK.asItem(),
                EMItems.PORTABLE_HAZMAT_SUIT.asItem()
        };
        event.registerItem(CuriosCapability.ITEM, (stack, ctx) -> new EMCurio(stack), items);
    }
}
