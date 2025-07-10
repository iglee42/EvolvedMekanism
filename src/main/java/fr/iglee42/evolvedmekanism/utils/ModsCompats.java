package fr.iglee42.evolvedmekanism.utils;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.LoadingModList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public enum ModsCompats {

    MEKANISMGENERATORS("tiles.TileEntitySolarGeneratorMixin",
            "tiles.TileEntityAdvancedSolarGeneratorMixin","items.GenItemTierInstallerMixin"),

    MEKANISMTOOLS("client.ShieldTexturesMixin","client.ToolsRenderMekanismShieldItemMixin");
    private final String[] mixinClasses;

    ModsCompats(String... mixinClasses) {
        this.mixinClasses = Arrays.stream(mixinClasses).map(s->"fr.iglee42.evolvedmekanism.mixins."+s).toArray(String[]::new);
    }

    public List<String> getMixinClasses() {
        return Arrays.asList(mixinClasses);
    }

    public String getModId() {
        return name().toLowerCase();
    }

    public boolean isLoaded() {
        return ModList.get() != null ? ModList.get().isLoaded(getModId())
                : LoadingModList.get().getMods().stream()
                        .anyMatch(mod -> mod.getModId().equals(getModId()));
    }

    public static List<String> getAllMixinsClasses() {
        List<String> mixins = new ArrayList<>();
        Arrays.stream(values())
                .forEach(m -> m.getMixinClasses().forEach(mixins::add));
        return mixins;
    }
}
