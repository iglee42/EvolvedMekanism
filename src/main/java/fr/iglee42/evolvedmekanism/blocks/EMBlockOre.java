package fr.iglee42.evolvedmekanism.blocks;

import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import mekanism.api.text.ILangEntry;
import mekanism.common.block.interfaces.IHasDescription;
import mekanism.common.block.states.BlockStateHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class EMBlockOre extends Block implements IHasDescription {

    public EMBlockOre() {
        this(BlockStateHelper.applyLightLevelAdjustments(BlockBehaviour.Properties.of()
                .strength(3, 3)
                .requiresCorrectToolForDrops()
                .mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)));
    }

    public EMBlockOre(BlockBehaviour.Properties properties) {
        super(BlockStateHelper.applyLightLevelAdjustments(properties));
    }

    @NotNull
    @Override
    public ILangEntry getDescription() {
        return EvolvedMekanismLang.DESCRIPTION_NOCTIS_ROZULI_ORE;
    }

    @Override
    public int getExpDrop(@NotNull BlockState state, @NotNull LevelReader level, @NotNull RandomSource randomSource,
                          @NotNull BlockPos pos, int fortuneLevel, int silkTouchLevel) {
        return silkTouchLevel == 0 ? Mth.nextInt(randomSource, 2, 8) : 0;
    }
}
