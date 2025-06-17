package fr.iglee42.evolvedmekanism.tiles.machine;

import java.util.function.Predicate;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.math.FloatingLong;
import mekanism.common.block.attribute.AttributeEnergy;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public class SolidifierEnergyContainer extends MachineEnergyContainer<TileEntitySolidifier> {

    public static SolidifierEnergyContainer input(TileEntitySolidifier tile, @Nullable IContentsListener listener) {
        AttributeEnergy electricBlock = validateBlock(tile);
        return new SolidifierEnergyContainer(electricBlock.getStorage(), electricBlock.getUsage(), notExternal, alwaysTrue, tile, listener);
    }

    private SolidifierEnergyContainer(FloatingLong maxEnergy, FloatingLong energyPerTick, Predicate<@NotNull AutomationType> canExtract,
                                      Predicate<@NotNull AutomationType> canInsert, TileEntitySolidifier tile, @Nullable IContentsListener listener) {
        super(maxEnergy, energyPerTick, canExtract, canInsert, tile, listener);
    }

    @Override
    public FloatingLong getBaseEnergyPerTick() {
        return super.getBaseEnergyPerTick().add(tile.getRecipeEnergyRequired());
    }
}