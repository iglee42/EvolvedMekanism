package fr.iglee42.evolvedmekanism.blocks;

import fr.iglee42.evolvedmekanism.registries.EMBlockResourceInfo;
import mekanism.common.block.BlockMekanism;
import mekanism.common.resource.BlockResourceInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EMBlockResource extends BlockMekanism {

    @NotNull
    private final EMBlockResourceInfo resource;

    public EMBlockResource(@NotNull EMBlockResourceInfo resource) {
        super(resource.modifyProperties(BlockBehaviour.Properties.of().requiresCorrectToolForDrops()));
        this.resource = resource;
    }

    @NotNull
    public EMBlockResourceInfo getResourceInfo() {
        return resource;
    }

    @Override
    public boolean isPortalFrame(BlockState state, BlockGetter world, BlockPos pos) {
        return resource.isPortalFrame();
    }
}