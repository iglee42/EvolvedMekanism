package fr.iglee42.evolvedmekanism.network;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.curios.CuriosGameplay;
import fr.iglee42.evolvedmekanism.utils.ModsCompats;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record PacketCurioAction(Action action, String slot) implements CustomPacketPayload {

    public static final Type<PacketCurioAction> TYPE = new Type<>(EvolvedMekanism.rl("curio_action"));

    public static final StreamCodec<ByteBuf, PacketCurioAction> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, packet -> packet.action.ordinal(),
            ByteBufCodecs.STRING_UTF8, PacketCurioAction::slot,
            (ordinal, slot) -> new PacketCurioAction(Action.values()[ordinal], slot)
    );

    @Override
    public @NotNull Type<PacketCurioAction> type() {
        return TYPE;
    }

    public static void handle(PacketCurioAction packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!ModsCompats.CURIOS.isLoaded()) {
                return;
            }
            Player player = context.player();
            if (player instanceof ServerPlayer serverPlayer) {
                CuriosGameplay.handleAction(serverPlayer, packet.action(), packet.slot());
            }
        });
    }

    public enum Action {
        OPEN_QIO,
        OPEN_TELEPORTER,
        DRINK_CANTEEN,
        SLOT_USE
    }
}
