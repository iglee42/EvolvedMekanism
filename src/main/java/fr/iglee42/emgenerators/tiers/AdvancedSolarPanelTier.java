package fr.iglee42.emgenerators.tiers;

import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import mekanism.api.tier.BaseTier;
import mekanism.api.tier.ITier;

public enum AdvancedSolarPanelTier implements ITier {

    ADVANCED (BaseTier.ADVANCED, 2),
    ELITE (BaseTier.ELITE, 3),
    ULTIMATE(BaseTier.ULTIMATE, 4),
    OVERCLOCKED(EMBaseTier.OVERCLOCKED, 5),
    QUANTUM(EMBaseTier.QUANTUM, 6),
    DENSE(EMBaseTier.DENSE, 7),
    MULTIVERSAL(EMBaseTier.MULTIVERSAL, 8),
    CREATIVE(BaseTier.CREATIVE, 16),;

    private final BaseTier baseTier;
    private final int multiplier;


    private AdvancedSolarPanelTier(BaseTier baseTier, int multiplier) {
        this.baseTier = baseTier;
        this.multiplier = multiplier;
    }


    @Override
    public BaseTier getBaseTier() {
        return baseTier;
    }

    public int getMultiplier() {
        return multiplier;
    }
    
}
