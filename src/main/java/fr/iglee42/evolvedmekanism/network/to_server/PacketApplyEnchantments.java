package fr.iglee42.evolvedmekanism.network.to_server;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.tiles.enchantment.TileEntityEnchantingStation;
import java.util.HashMap;
import java.util.Map;
import mekanism.common.network.IMekanismPacket;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record PacketApplyEnchantments(BlockPos pos, Map<Holder<Enchantment>, Integer> levels) implements IMekanismPacket {

    public static final CustomPacketPayload.Type<PacketApplyEnchantments> TYPE = new CustomPacketPayload.Type<>(EvolvedMekanism.rl("apply_enchantments"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketApplyEnchantments> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, PacketApplyEnchantments::pos,
            ByteBufCodecs.<RegistryFriendlyByteBuf, Holder<Enchantment>, Integer, Map<Holder<Enchantment>, Integer>>map(HashMap::new,
                    ByteBufCodecs.holderRegistry(Registries.ENCHANTMENT), ByteBufCodecs.VAR_INT, 256), PacketApplyEnchantments::levels,
            PacketApplyEnchantments::new
    );

    @NotNull
    @Override
    public CustomPacketPayload.Type<PacketApplyEnchantments> type() {
        return TYPE;
    }

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        TileEntityEnchantingStation tile = WorldUtils.getTileEntity(TileEntityEnchantingStation.class, player.level(), pos);
        if (tile != null) {
            tile.applyEnchantments(levels);
        }
    }
}
