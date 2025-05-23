package fr.iglee42.evolvedmekanism.tiles;

import fr.iglee42.evolvedmekanism.tiers.PersonalStorageTier;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tile.TileEntityPersonalStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TileEntityTieredPersonalChest extends TileEntityTieredPersonalStorage implements LidBlockEntity {

    private final ChestLidController chestLidController = new ChestLidController();

    public TileEntityTieredPersonalChest(IBlockProvider blockProvider, BlockPos pos, BlockState state, PersonalStorageTier tier) {
        super(blockProvider, pos, state, tier);
    }

    @Override
    protected void onOpen(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state) {
        level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 0.5F,
              level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    protected void onClose(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state) {
        level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 0.5F,
              level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    protected ResourceLocation getStat() {
        return Stats.OPEN_CHEST;
    }

    @Override
    protected void onUpdateClient() {
        super.onUpdateClient();
        chestLidController.tickLid();
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 1) {
            this.chestLidController.shouldBeOpen(type > 0);
            return true;
        }
        return super.triggerEvent(id, type);
    }

    @Override
    public float getOpenNess(float partialTicks) {
        return chestLidController.getOpenness(partialTicks);
    }

    @Override
    public InteractionResult openGui(Player player) {
        BlockPos above = getBlockPos().above();
        if (level.getBlockState(above).isRedstoneConductor(level, above)) {
            //If the block above is solid consume the action
            return InteractionResult.CONSUME;
        }
        return super.openGui(player);
    }
}