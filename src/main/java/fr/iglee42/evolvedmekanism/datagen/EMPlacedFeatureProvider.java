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
                    tasks.add(DataProvider.saveStable(cache, placedJson(name, type.getResource().getRegistrySuffix(), index),
                            pathProvider.json(EvolvedMekanism.rl(name))));
                }
            }
        }
        tasks.add(DataProvider.saveStable(cache, vanillaPlaced("ore_noctis", 2, absolute(-32), absolute(32)),
                pathProvider.json(EvolvedMekanism.rl("ore_noctis"))));
        tasks.add(DataProvider.saveStable(cache, vanillaPlaced("ore_noctis_buried", 4, aboveBottom(0), absolute(64)),
                pathProvider.json(EvolvedMekanism.rl("ore_noctis_buried"))));
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    private static JsonObject vanillaPlaced(String feature, int count, JsonObject min, JsonObject max) {
        JsonObject countJson = new JsonObject();
        countJson.addProperty("type", "minecraft:count");
        countJson.addProperty("count", count);

        JsonObject inSquare = new JsonObject();
        inSquare.addProperty("type", "minecraft:in_square");

        JsonObject heightType = new JsonObject();
        heightType.addProperty("type", "minecraft:trapezoid");
        heightType.add("min_inclusive", min);
        heightType.add("max_inclusive", max);
        JsonObject height = new JsonObject();
        height.addProperty("type", "minecraft:height_range");
        height.add("height", heightType);

        JsonObject biome = new JsonObject();
        biome.addProperty("type", "minecraft:biome");

        JsonArray placement = new JsonArray();
        placement.add(countJson);
        placement.add(inSquare);
        placement.add(height);
        placement.add(biome);

        JsonObject json = new JsonObject();
        json.addProperty("feature", EvolvedMekanism.MODID + ":" + feature);
        json.add("placement", placement);
        return json;
    }

    private static JsonObject absolute(int y) {
        JsonObject json = new JsonObject();
        json.addProperty("absolute", y);
        return json;
    }

    private static JsonObject aboveBottom(int y) {
        JsonObject json = new JsonObject();
        json.addProperty("above_bottom", y);
        return json;
    }

    private static JsonObject placedJson(String feature, String oreType, int index) {
        JsonObject ore = new JsonObject();
        ore.addProperty("type", oreType);
        ore.addProperty("index", index);

        JsonObject disableable = new JsonObject();
        disableable.addProperty("type", "mekanism:disableable");
        disableable.add("oreVeinType", ore);
        disableable.addProperty("retroGen", false);

        JsonObject countType = new JsonObject();
        countType.addProperty("type", "mekanism:configurable_constant");
        countType.add("oreVeinType", ore.deepCopy());
        JsonObject count = new JsonObject();
        count.addProperty("type", "minecraft:count");
        count.add("count", countType);

        JsonObject inSquare = new JsonObject();
        inSquare.addProperty("type", "minecraft:in_square");

        JsonObject heightType = new JsonObject();
        heightType.addProperty("type", "mekanism:configurable");
        heightType.add("oreVeinType", ore.deepCopy());
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
