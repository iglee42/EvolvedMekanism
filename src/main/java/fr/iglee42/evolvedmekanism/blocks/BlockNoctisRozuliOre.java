package fr.iglee42.evolvedmekanism.blocks;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = EvolvedMekanism.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlockNoctisRozuliOre extends EMBlockOre {

    public static final BooleanProperty UNCOVERED = BooleanProperty.create("uncovered");

    public BlockNoctisRozuliOre() {
        this(Properties.copy(Blocks.LAPIS_ORE).requiresCorrectToolForDrops());
    }

    public BlockNoctisRozuliOre(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(UNCOVERED, false));
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return cloneStack();
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return cloneStack();
    }

    private ItemStack cloneStack() {
        boolean deepslate = BuiltInRegistries.BLOCK.getKey(this).getPath().contains("deepslate");
        return deepslate ? EMBlocks.DEEPSLATE_NOCTIS_ROZULI_ORE.getItemStack() : EMBlocks.NOCTIS_ROZULI_ORE.getItemStack();
    }

    @Override
    public String getDescriptionId() {
        boolean deepslate = BuiltInRegistries.BLOCK.getKey(this).getPath().contains("deepslate");
        return Util.makeDescriptionId("block", new ResourceLocation((deepslate ? "deepslate_" : "") + "lapis_ore"));
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state.getValue(UNCOVERED) ? state : Blocks.LAPIS_ORE.defaultBlockState(), blockEntity, tool);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UNCOVERED);
        super.createBlockStateDefinition(builder);
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Level level = event.player.level();
        if (level.isClientSide) {
            return;
        }
        BlockPos origin = event.player.blockPosition();
        boolean night = level.isNight();
        BlockPos.betweenClosed(origin.offset(-15, -15, -15), origin.offset(15, 15, 15)).forEach(pos -> {
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof BlockNoctisRozuliOre && state.getValue(UNCOVERED) != night) {
                level.setBlock(pos.immutable(), state.setValue(UNCOVERED, night), Block.UPDATE_CLIENTS);
            }
        });
    }
}
