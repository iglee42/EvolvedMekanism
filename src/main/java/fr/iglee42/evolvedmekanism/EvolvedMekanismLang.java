package fr.iglee42.evolvedmekanism;

import mekanism.api.text.ILangEntry;
import mekanism.common.Mekanism;
import net.minecraft.Util;

public enum EvolvedMekanismLang implements ILangEntry {

    MEKANISM_EVOLVED("constants","mod_name"),
    TIERED_STORAGE_CAPACITY("tiered_storage","capacity")
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
