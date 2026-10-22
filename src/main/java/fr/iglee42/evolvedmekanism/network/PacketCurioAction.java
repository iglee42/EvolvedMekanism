package fr.iglee42.evolvedmekanism.network;

import fr.iglee42.evolvedmekanism.curios.CuriosGameplay;
import fr.iglee42.evolvedmekanism.utils.ModsCompats;
import mekanism.common.network.BasePacketHandler;
import mekanism.common.network.IMekanismPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class PacketCurioAction implements IMekanismPacket {

    private final Action action;
    private final String slot;

    public PacketCurioAction(Action action, String slot) {
        this.action = action;
        this.slot = slot;
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        ServerPlayer player = context.getSender();
        if (player != null && ModsCompats.CURIOS.isLoaded()) {
            CuriosGameplay.handleAction(player, action, slot);
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeEnum(action);
        buffer.writeUtf(slot);
    }

    public static PacketCurioAction decode(FriendlyByteBuf buffer) {
        return new PacketCurioAction(buffer.readEnum(Action.class), BasePacketHandler.readString(buffer));
    }

    public enum Action {
        OPEN_QIO,
        OPEN_TELEPORTER,
        DRINK_CANTEEN,
        SLOT_USE
    }
}
