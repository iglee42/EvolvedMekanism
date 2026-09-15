package fr.iglee42.evolvedmekanism.datagen;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class EMSpriteSourceProvider extends SpriteSourceProvider {

    private static final ResourceLocation ARMOR_TRIMS_ATLAS = ResourceLocation.withDefaultNamespace("armor_trims");
    private static final List<String> TRIM_PATTERNS = List.of(
            "coast", "sentry", "dune", "wild", "ward", "eye", "vex", "tide", "snout", "rib", "spire",
            "wayfinder", "shaper", "silence", "raiser", "host", "flow", "bolt"
    );

    public EMSpriteSourceProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper existing) {
        super(output, lookup, EvolvedMekanism.MODID, existing);
    }

    @Override
    protected void gather() {
        atlas(BLOCKS_ATLAS)
                .addSource(single("infuse_type/better_gold"))
                .addSource(single("infuse_type/plaslitherite"));
        atlas(SHIELD_PATTERNS_ATLAS)
                .addSource(single("entity/shield/better_gold"))
                .addSource(single("entity/shield/plaslitherite"))
                .addSource(single("entity/shield/refined_redstone"))
                .addSource(single("entity/shield/noctis_rozuli"));
        atlas(ARMOR_TRIMS_ATLAS).addSource(new PalettedPermutations(
                TRIM_PATTERNS.stream()
                        .flatMap(pattern -> List.of(
                                ResourceLocation.withDefaultNamespace("trims/models/armor/" + pattern),
                                ResourceLocation.withDefaultNamespace("trims/models/armor/" + pattern + "_leggings")
                        ).stream())
                        .toList(),
                ResourceLocation.withDefaultNamespace("trims/color_palettes/trim_palette"),
                Map.of(
                        "plaslitherite", ResourceLocation.withDefaultNamespace("trims/color_palettes/plaslitherite"),
                        "better_gold", ResourceLocation.withDefaultNamespace("trims/color_palettes/better_gold"),
                        "refined_redstone", ResourceLocation.withDefaultNamespace("trims/color_palettes/refined_redstone"),
                        "noctis_rozuli", ResourceLocation.withDefaultNamespace("trims/color_palettes/noctis_rozuli")
                )
        ));
    }

    private SingleFile single(String path) {
        return new SingleFile(EvolvedMekanism.rl(path), Optional.empty());
    }
}
