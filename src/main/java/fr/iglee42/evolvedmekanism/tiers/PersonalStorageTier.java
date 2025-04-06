package fr.iglee42.evolvedmekanism.tiers;

import mekanism.api.tier.BaseTier;
import mekanism.api.tier.ITier;

public enum PersonalStorageTier implements ITier {
    BASIC(BaseTier.BASIC, 6),
    ADVANCED(BaseTier.ADVANCED, 7),
    ELITE(BaseTier.ELITE, 8),
    ULTIMATE(BaseTier.ULTIMATE, 9),
    OVERCLOCKED(EMBaseTier.OVERCLOCKED,9,11),
    QUANTUM(EMBaseTier.QUANTUM,9,13),
    DENSE(EMBaseTier.DENSE,9,15),
    MULTIVERSAL(EMBaseTier.MULTIVERSAL,9,17),
    CREATIVE(BaseTier.CREATIVE,11,19);

    public final int columns, rows;
    private final BaseTier baseTier;

    PersonalStorageTier(BaseTier baseTier, int rows, int columns) {
        this.baseTier = baseTier;
        this.rows = rows;
        this.columns = columns;
    }
    PersonalStorageTier(BaseTier baseTier, int rows) {
        this.baseTier = baseTier;
        this.rows = rows;
        this.columns = 9;
    }

    @Override
    public BaseTier getBaseTier() {
        return baseTier;
    }
}
