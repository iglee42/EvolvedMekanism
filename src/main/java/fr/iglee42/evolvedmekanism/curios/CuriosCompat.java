package fr.iglee42.evolvedmekanism.curios;

import fr.iglee42.evolvedmekanism.curios.client.CuriosClient;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import mekanism.common.registries.MekanismItems;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import top.theillusivec4.curios.api.CuriosApi;

public final class CuriosCompat {

    private CuriosCompat() {
    }

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(CuriosCompat::onCommonSetup);
        MinecraftForge.EVENT_BUS.register(CuriosGameplay.class);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            CuriosClient.register(modEventBus);
        }
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            EMCurio curio = new EMCurio();
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
            for (Item item : items) {
                CuriosApi.registerCurio(item, curio);
            }
        });
    }
}
