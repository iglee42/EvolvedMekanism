package fr.iglee42.evolvedmekanism.utils;

import mekanism.common.content.blocktype.FactoryType;
import mekanism.common.resource.ore.OreType;
import mekanism.common.tier.FactoryTier;

/**
 * Vanilla Mekanism enum constants only. Other addons inject extra values into the same enums;
 * iterating {@code EnumUtils} then registers duplicate or unsupported blocks.
 */
public final class EMVanillaMekanism {

    private EMVanillaMekanism() {
    }

    public static final OreType[] ORE_TYPES = {
            OreType.TIN, OreType.OSMIUM, OreType.URANIUM, OreType.FLUORITE, OreType.LEAD
    };

    public static final FactoryType[] FACTORY_TYPES = {
            FactoryType.SMELTING, FactoryType.ENRICHING, FactoryType.CRUSHING,
            FactoryType.COMPRESSING, FactoryType.COMBINING, FactoryType.PURIFYING,
            FactoryType.INJECTING, FactoryType.INFUSING, FactoryType.SAWING
    };

    public static final FactoryTier[] FACTORY_TIERS = {
            FactoryTier.BASIC, FactoryTier.ADVANCED, FactoryTier.ELITE, FactoryTier.ULTIMATE
    };
}
