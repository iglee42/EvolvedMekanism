package fr.iglee42.evolvedmekanism.utils;

import net.minecraft.world.level.Level;

public final class EMNight {

    private EMNight() {
    }

    /**
     * Vanilla night, plus the displayed clock so a fixed-time dimension locked at night still counts.
     * {@link Level#isNight()} is false whenever the dimension has a fixed time.
     */
    public static boolean isNight(Level level) {
        if (level.isNight()) {
            return true;
        }
        long time = Math.floorMod(level.dimensionType().fixedTime().orElse(level.getDayTime()), 24000L);
        return time >= 13000L && time < 23000L;
    }
}
