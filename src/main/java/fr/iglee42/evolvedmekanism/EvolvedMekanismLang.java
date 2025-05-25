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

    APT("apt", "apt"),
    APT_PORT_MODE("apt", "port_mode"),

    ;

    // <type>.mekanism.<path>
    public static MekanismLang ALLOYING; // type : factory | path : alloying
    public static MekanismLang DESCRIPTION_ALLOYER; // type : description | path : alloyer
    public static APILang UPGRADE_RADIOACTIVE; // type : upgrade | path : radioactive
    public static APILang UPGRADE_RADIOACTIVE_DESCRIPTION; // type : upgrade | path : radioactive.description


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
