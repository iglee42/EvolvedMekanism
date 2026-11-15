package fr.iglee42.evolvedmekanism.network;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.network.to_server.PacketApplyEnchantments;
import mekanism.common.network.BasePacketHandler;
import net.neoforged.bus.api.IEventBus;

public class EMPacketHandler extends BasePacketHandler {

    public EMPacketHandler(IEventBus modEventBus) {
        super(modEventBus, EvolvedMekanism.instance.versionNumber);
    }


    @Override
    protected void registerClientToServer(PacketRegistrar registrar) {
        registrar.play(PacketApplyEnchantments.TYPE, PacketApplyEnchantments.STREAM_CODEC);

    }

    @Override
    protected void registerServerToClient(PacketRegistrar registrar) {

    }
}