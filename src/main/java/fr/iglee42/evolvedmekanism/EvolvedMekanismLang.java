package fr.iglee42.evolvedmekanism;

import mekanism.api.text.ILangEntry;
import mekanism.common.MekanismLang;
import net.minecraft.Util;

public enum EvolvedMekanismLang implements ILangEntry {

    // <type>.evolvedmekanism.<path>

    MEKANISM_EVOLVED("constants","mod_name"),
    TIERED_STORAGE_CAPACITY("tiered_storage","capacity"),
    DESCRIPTION_APT_CASING("description", "apt_casing"),
    DESCRIPTION_APT_PORT("description", "apt_port"),

    DESCRIPTION_SUPERCHARGING_ELEMENT("description", "supercharging_element"),

    DESCRIPTION_MAX_TIER_INSTALLER("description", "max_tier_installer"),

    APT("apt", "apt"),
    APT_PORT_MODE("apt", "port_mode"),
    APT_INVALID_SUPERCHARGING("apt", "invalid_supercharging"),

    TOOLTIP_NO_CONSUMED("tooltip","not_consumed")
    ;
    // <type>.mekanism.<path>
    public static MekanismLang ALLOYING; // type : factory | path : alloying
    public static MekanismLang DESCRIPTION_ALLOYER; // type : description | path : alloyer

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