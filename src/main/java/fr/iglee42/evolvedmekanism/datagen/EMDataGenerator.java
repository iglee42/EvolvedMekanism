package fr.iglee42.evolvedmekanism.datagen;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.datagen.loot.EMBlockLoot;
import fr.iglee42.evolvedmekanism.datagen.recipe.EMRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class EMDataGenerator {

    private EMDataGenerator() {
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existing = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();
        boolean server = event.includeServer();
        boolean client = event.includeClient();

        EMBlockTags blockTags = new EMBlockTags(output, lookup, existing);
        generator.addProvider(server, blockTags);
        generator.addProvider(server, new EMItemTags(output, lookup, blockTags.contentsGetter(), existing));
        generator.addProvider(server, new EMFluidTags(output, lookup, existing));
        generator.addProvider(server, new EMInfuseTags(output, lookup, existing));
        generator.addProvider(server, new EMGasTags(output, lookup, existing));
        generator.addProvider(server, new EMRecipeProvider(output));
        generator.addProvider(server, new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(EMBlockLoot::new, LootContextParamSets.BLOCK)
        )));
        generator.addProvider(server, new EMWorldGenProvider(output, lookup));
        generator.addProvider(server, new EMPlacedFeatureProvider(output));
        generator.addProvider(server, new EMBiomeModifierProvider(output));

        generator.addProvider(client, new EMBlockStateProvider(output, existing));
        generator.addProvider(client, new EMMekanismAssetProvider(output, existing));
        generator.addProvider(client, new EMItemModelProvider(output, existing));
        generator.addProvider(client, new EMSpriteSourceProvider(output, existing));
        generator.addProvider(client, new EMLangProvider(output));
    }
}
