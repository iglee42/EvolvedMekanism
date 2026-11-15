package fr.iglee42.evolvedmekanism.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.curios.CuriosSlots;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EMCuriosProvider implements DataProvider {

    private static final int FIRST_SLOT_ORDER = 40;

    private final PackOutput.PathProvider slotPath;
    private final PackOutput.PathProvider entityPath;

    public EMCuriosProvider(PackOutput output) {
        this.slotPath = output.createPathProvider(PackOutput.Target.DATA_PACK, "curios/slots");
        this.entityPath = output.createPathProvider(PackOutput.Target.DATA_PACK, "curios/entities");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        int order = FIRST_SLOT_ORDER;
        for (String slot : CuriosSlots.ALL) {
            tasks.add(DataProvider.saveStable(cache, slotJson(slot, order++), slotPath.json(EvolvedMekanism.rl(slot))));
        }
        tasks.add(DataProvider.saveStable(cache, entitiesJson(), entityPath.json(EvolvedMekanism.rl(EvolvedMekanism.MODID))));
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    private static JsonObject slotJson(String slot, int order) {
        JsonObject json = new JsonObject();
        json.addProperty("order", order);
        json.addProperty("size", 1);
        json.addProperty("operation", "SET");
        json.addProperty("use_native_gui", true);
        json.addProperty("render_toggle", true);
        json.addProperty("add_cosmetic", false);
        json.addProperty("icon", EvolvedMekanism.MODID + ":slot/empty_" + slot + "_slot");
        return json;
    }

    private static JsonObject entitiesJson() {
        JsonObject json = new JsonObject();
        JsonArray entities = new JsonArray();
        entities.add("minecraft:player");
        json.add("entities", entities);
        JsonArray slots = new JsonArray();
        CuriosSlots.ALL.forEach(slots::add);
        json.add("slots", slots);
        return json;
    }

    @Override
    public String getName() {
        return "Evolved Mekanism Curios Slots";
    }
}
