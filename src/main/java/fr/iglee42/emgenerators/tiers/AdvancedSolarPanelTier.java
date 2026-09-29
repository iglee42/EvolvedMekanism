package fr.iglee42.emgenerators.tiers;

import fr.iglee42.evolvedmekanism.config.EMConfig;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import mekanism.api.tier.BaseTier;
import mekanism.api.tier.ITier;

public enum AdvancedSolarPanelTier implements ITier {

    ADVANCED(BaseTier.ADVANCED),
    ELITE(BaseTier.ELITE),
    ULTIMATE(BaseTier.ULTIMATE),
    OVERCLOCKED(EMBaseTier.OVERCLOCKED),
    QUANTUM(EMBaseTier.QUANTUM),
    DENSE(EMBaseTier.DENSE),
    MULTIVERSAL(EMBaseTier.MULTIVERSAL),
    CREATIVE(BaseTier.CREATIVE);

    private final BaseTier baseTier;

    private AdvancedSolarPanelTier(BaseTier baseTier) {
        this.baseTier = baseTier;
    }

    @Override
    public BaseTier getBaseTier() {
        return baseTier;
    }

    public int getMultiplier() {
        return EMConfig.generators.solarMultiplier(this);
    }
}
