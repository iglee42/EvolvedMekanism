package fr.iglee42.evolvedmekanism;

import mekanism.api.text.ILangEntry;
import net.minecraft.Util;
import org.jetbrains.annotations.NotNull;

public enum EvolvedMekanismLang implements ILangEntry {

    // <type>.evolvedmekanism.<path>

    MEKANISM_EVOLVED("constants","mod_name"),
    DESCRIPTION_APT_CASING("description", "apt_casing"),
    DESCRIPTION_APT_PORT("description", "apt_port"),

    DESCRIPTION_SUPERCHARGING_ELEMENT("description", "supercharging_element"),

    DESCRIPTION_MAX_TIER_INSTALLER("description", "max_tier_installer"),

    APT("apt", "apt"),
    APT_PORT_MODE("apt", "port_mode"),
    APT_INVALID_SUPERCHARGING("apt", "invalid_supercharging"),
    ;

    private final String key;

    EvolvedMekanismLang(String type, String path) {
        this(Util.makeDescriptionId(type, EvolvedMekanism.rl(path)));
    }

    EvolvedMekanismLang(String key) {
        this.key = key;
    }

    public @NotNull String getTranslationKey() {
        return this.key;
    }
}
