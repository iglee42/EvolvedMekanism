package fr.iglee42.evolvedmekanism.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.LoadingModList;

public enum ModsCompats {

    MEKANISMGENERATORS("tiles.TileEntitySolarGeneratorMixin",
            "tiles.TileEntityAdvancedSolarGeneratorMixin");

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
