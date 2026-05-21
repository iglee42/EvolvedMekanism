package fr.iglee42.evolvedmekanism;

import mekanism.api.text.APILang;
import mekanism.api.text.ILangEntry;
import mekanism.common.MekanismLang;
import net.minecraft.Util;

public enum EvolvedMekanismLang implements ILangEntry {

    // <type>.evolvedmekanism.<path>

    MEKANISM_EVOLVED("constants","mod_name"),
    TIERED_STORAGE_CAPACITY("tiered_storage","capacity"),
    DESCRIPTION_APT_CASING("description", "apt_casing"),
    DESCRIPTION_APT_PORT("description", "apt_port"),

    DESCRIPTION_ADVANCED_SOLAR_GENERATOR("description", "advanced_solar_generator"),
    DESCRIPTION_ELITE_SOLAR_GENERATOR("description", "elite_solar_generator"),
    DESCRIPTION_ULTIMATE_SOLAR_GENERATOR("description", "ultimate_solar_generator"),
    DESCRIPTION_OVERCLOCKED_SOLAR_GENERATOR("description", "overclocked_solar_generator"),
    DESCRIPTION_QUANTUM_SOLAR_GENERATOR("description", "quantum_solar_generator"),
    DESCRIPTION_DENSE_SOLAR_GENERATOR("description", "dense_solar_generator"),
    DESCRIPTION_MULTIVERSAL_SOLAR_GENERATOR("description", "multiversal_solar_generator"),
    DESCRIPTION_CREATIVE_SOLAR_GENERATOR("description", "creative_solar_generator"),
    DESCRIPTION_SUPERCHARGING_ELEMENT("description", "supercharging_element"),
    DESCRIPTION_LUNAR_NEUTRON_ACTIVATOR("description", "lunar_neutron_activator"),
    DESCRIPTION_BASIC_ADVANCED_LUNAR_GENERATOR("description", "basic_advanced_lunar_generator"),
    DESCRIPTION_ADVANCED_LUNAR_GENERATOR("description", "advanced_lunar_generator"),
    DESCRIPTION_ELITE_LUNAR_GENERATOR("description", "elite_lunar_generator"),
    DESCRIPTION_ULTIMATE_LUNAR_GENERATOR("description", "ultimate_lunar_generator"),
    DESCRIPTION_OVERCLOCKED_LUNAR_GENERATOR("description", "overclocked_lunar_generator"),
    DESCRIPTION_QUANTUM_LUNAR_GENERATOR("description", "quantum_lunar_generator"),
    DESCRIPTION_DENSE_LUNAR_GENERATOR("description", "dense_lunar_generator"),
    DESCRIPTION_MULTIVERSAL_LUNAR_GENERATOR("description", "multiversal_lunar_generator"),
    DESCRIPTION_CREATIVE_LUNAR_GENERATOR("description", "creative_lunar_generator"),

    DESCRIPTION_LUNAR_GENERATOR("description", "lunar_generator"),
    DESCRIPTION_LASER_DISENCHANTER("description", "laser_disenchanter"),

    DESCRIPTION_MAX_TIER_INSTALLER("description", "max_tier_installer"),

    APT("apt", "apt"),
    APT_PORT_MODE("apt", "port_mode"),
    APT_INVALID_SUPERCHARGING("apt", "invalid_supercharging"),

    TOOLTIP_NO_CONSUMED("tooltip","not_consumed"),

    ALLOYING("factory","alloying"),

    DESCRIPTION_ALLOYER("description","alloyer"),
    DESCRIPTION_MELTER("description","thermalizer"),
    DESCRIPTION_SOLIDIFIER("description","solidification_chamber"),

    UPGRADE_RADIOACTIVE("upgrade","radioactive"),
    UPGRADE_SOLAR("upgrade","solar"),
    UPGRADE_LUNAR("upgrade","lunar"),
    UPGRADE_RADIOACTIVE_DESCRIPTION("upgrade","radioactive.description"),
    UPGRADE_SOLAR_DESCRIPTION("upgrade","solar.description"),
    UPGRADE_LUNAR_DESCRIPTION("upgrade","lunar.description"),

    ;



    private final String key;

    private EvolvedMekanismLang(String type, String path) {
        this(Util.makeDescriptionId(type, EvolvedMekanism.rl(path)));
    }

    private EvolvedMekanismLang(String key) {
        this.key = key;
    }

    public String getTranslationKey() {
        return this.key;
    }
}
