package fr.iglee42.evolvedmekanism.network;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.common.network.BasePacketHandler;
import net.minecraftforge.network.simple.SimpleChannel;

public class EMPacketHandler extends BasePacketHandler {

    private final SimpleChannel netHandler = createChannel(EvolvedMekanism.rl(EvolvedMekanism.MODID), EvolvedMekanism.instance.versionNumber);

    @Override
    protected SimpleChannel getChannel() {
        return netHandler;
    }

    @Override
    public void initialize() {
        registerClientToServer(PacketCurioAction.class, PacketCurioAction::decode);
    }
}