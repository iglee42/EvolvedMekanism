package fr.iglee42.evolvedmekanism.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.common.resource.ore.OreType;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EMBiomeModifierProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;

    public EMBiomeModifierProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "forge/biome_modifier");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        tasks.add(write(cache, "add_nether_features", "#minecraft:is_nether", "nether"));
        tasks.add(write(cache, "add_end_features", "#minecraft:is_end", "end"));
        tasks.add(write(cache, "add_aether_features", "#aether:is_aether", "aether"));
        tasks.add(write(cache, "add_undergarden_features", "#undergarden:is_undergarden", "undergarden"));
        tasks.add(writeNoctis(cache));
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> writeNoctis(CachedOutput cache) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "forge:add_features");
        json.addProperty("biomes", "#mekanism:spawn_ores");
        JsonArray features = new JsonArray();
        features.add("evolvedmekanism:ore_noctis");
        features.add("evolvedmekanism:ore_noctis_buried");
        json.add("features", features);
        json.addProperty("step", "underground_ores");
        return DataProvider.saveStable(cache, json, pathProvider.json(EvolvedMekanism.rl("noctis")));
    }

    private CompletableFuture<?> write(CachedOutput cache, String name, String biomes, String dimension) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "forge:add_features");
        json.addProperty("biomes", biomes);
        JsonArray features = new JsonArray();
        for (OreType type : OreType.values()) {
            for (int index = 0; index < type.getBaseConfigs().size(); index++) {
                features.add("evolvedmekanism:" + new OreType.OreVeinType(type, index).name() + "_" + dimension);
            }
        }
        json.add("features", features);
        json.addProperty("step", "underground_ores");
        Path path = pathProvider.json(EvolvedMekanism.rl(name));
        return DataProvider.saveStable(cache, json, path);
    }

    @Override
    public String getName() {
        return "Evolved Mekanism Biome Modifiers";
    }
}
