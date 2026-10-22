package fr.iglee42.evolvedmekanism.config;

import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import mekanism.api.tier.BaseTier;

/**
 * Real Java enum for the max-installer config entry.
 * Mixin-injected {@link BaseTier} constants cannot round-trip through NeoForge's
 * {@code defineEnum} ({@code Class.getEnumConstants()}/{@code Enum.valueOf}), which
 * makes general.toml fail validation and the config watcher rewrite it in a loop.
 */
public enum MaxInstallerTier {
    BASIC,
    ADVANCED,
    ELITE,
    ULTIMATE,
    OVERCLOCKED,
    QUANTUM,
    DENSE,
    MULTIVERSAL,
    CREATIVE;

    public BaseTier toBaseTier() {
        return switch (this) {
            case BASIC -> BaseTier.BASIC;
            case ADVANCED -> BaseTier.ADVANCED;
            case ELITE -> BaseTier.ELITE;
            case ULTIMATE -> BaseTier.ULTIMATE;
            case OVERCLOCKED -> EMBaseTier.OVERCLOCKED;
            case QUANTUM -> EMBaseTier.QUANTUM;
            case DENSE -> EMBaseTier.DENSE;
            case MULTIVERSAL -> EMBaseTier.MULTIVERSAL;
            case CREATIVE -> BaseTier.CREATIVE;
        };
    }
}
