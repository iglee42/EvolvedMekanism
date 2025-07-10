package fr.iglee42.evolvedmekanism.network;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.common.lib.Version;
import mekanism.common.network.BasePacketHandler;
import net.neoforged.bus.api.IEventBus;

public class EMPacketHandler extends BasePacketHandler {

    public EMPacketHandler(IEventBus modEventBus) {
        super(modEventBus, EvolvedMekanism.instance.versionNumber);
    }


    @Override
    protected void registerClientToServer(PacketRegistrar registrar) {

    }

    @Override
    protected void registerServerToClient(PacketRegistrar registrar) {

    }
}