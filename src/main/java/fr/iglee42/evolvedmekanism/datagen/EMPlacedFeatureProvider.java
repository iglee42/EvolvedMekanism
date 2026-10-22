package fr.iglee42.evolvedmekanism.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.common.resource.ore.OreType;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EMPlacedFeatureProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;

    public EMPlacedFeatureProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "worldgen/placed_feature");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        for (OreType type : OreType.values()) {
            for (int index = 0; index < type.getBaseConfigs().size(); index++) {
                OreType.OreVeinType vein = new OreType.OreVeinType(type, index);
                for (String dim : List.of("nether", "end", "aether", "undergarden")) {
                    String name = vein.name() + "_" + dim;
                    tasks.add(DataProvider.saveStable(cache, placedJson(name, "mekanism", type.getResource().getRegistrySuffix(), index),
                            pathProvider.json(EvolvedMekanism.rl(name))));
                }
            }
        }
        tasks.add(DataProvider.saveStable(cache, placedJson("ore_noctis", "evolvedmekanism", "noctis_rozuli", 0),
                pathProvider.json(EvolvedMekanism.rl("ore_noctis"))));
        tasks.add(DataProvider.saveStable(cache, placedJson("ore_noctis_buried", "evolvedmekanism", "noctis_rozuli", 1),
                pathProvider.json(EvolvedMekanism.rl("ore_noctis_buried"))));
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    private static JsonObject placedJson(String feature, String namespace, String oreType, int index) {
        JsonObject ore = new JsonObject();
        ore.addProperty("type", oreType);
        ore.addProperty("index", index);

        JsonObject disableable = new JsonObject();
        disableable.addProperty("type", namespace + ":disableable");
        disableable.add("ore_type", ore);
        disableable.addProperty("retro_gen", false);

        JsonObject countType = new JsonObject();
        countType.addProperty("type", namespace + ":configurable_constant");
        countType.add("ore_type", ore.deepCopy());
        JsonObject count = new JsonObject();
        count.addProperty("type", "minecraft:count");
        count.add("count", countType);

        JsonObject inSquare = new JsonObject();
        inSquare.addProperty("type", "minecraft:in_square");

        JsonObject heightType = new JsonObject();
        heightType.addProperty("type", namespace + ":configurable");
        heightType.add("ore_type", ore.deepCopy());
        JsonObject height = new JsonObject();
        height.addProperty("type", "minecraft:height_range");
        height.add("height", heightType);

        JsonObject biome = new JsonObject();
        biome.addProperty("type", "minecraft:biome");

        JsonArray placement = new JsonArray();
        placement.add(disableable);
        placement.add(count);
        placement.add(inSquare);
        placement.add(height);
        placement.add(biome);

        JsonObject json = new JsonObject();
        json.addProperty("feature", EvolvedMekanism.MODID + ":" + feature);
        json.add("placement", placement);
        return json;
    }

    @Override
    public String getName() {
        return "Evolved Mekanism Placed Features";
    }
}
