package fr.iglee42.evolvedmekanism.network;

import fr.iglee42.evolvedmekanism.tiles.enchantment.TileEntityEnchantingStation;
import mekanism.common.network.IMekanismPacket;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class PacketApplyEnchantments implements IMekanismPacket {

    private final BlockPos pos;
    private final Map<Enchantment, Integer> levels;

    public PacketApplyEnchantments(BlockPos pos, Map<Enchantment, Integer> levels) {
        this.pos = pos;
        this.levels = levels;
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        Player player = context.getSender();
        if (player == null) {
            return;
        }
        TileEntityEnchantingStation tile = WorldUtils.getTileEntity(TileEntityEnchantingStation.class, player.level(), pos);
        if (tile != null) {
            tile.applyEnchantments(levels);
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeVarInt(levels.size());
        levels.forEach((enchantment, level) -> {
            buffer.writeResourceLocation(ForgeRegistries.ENCHANTMENTS.getKey(enchantment));
            buffer.writeVarInt(level);
        });
    }

    public static PacketApplyEnchantments decode(FriendlyByteBuf buffer) {
        BlockPos pos = buffer.readBlockPos();
        int size = buffer.readVarInt();
        Map<Enchantment, Integer> levels = new HashMap<>();
        for (int i = 0; i < size && i < 256; i++) {
            ResourceLocation id = buffer.readResourceLocation();
            int level = buffer.readVarInt();
            Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(id);
            if (enchantment != null) {
                levels.put(enchantment, level);
            }
        }
        return new PacketApplyEnchantments(pos, levels);
    }
}
