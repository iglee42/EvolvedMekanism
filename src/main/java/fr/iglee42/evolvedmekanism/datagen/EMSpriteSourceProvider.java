package fr.iglee42.evolvedmekanism.datagen;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;

import java.util.Optional;

public class EMSpriteSourceProvider extends SpriteSourceProvider {

    public EMSpriteSourceProvider(PackOutput output, ExistingFileHelper existing) {
        super(output, existing, EvolvedMekanism.MODID);
    }

    @Override
    protected void addSources() {
        atlas(BLOCKS_ATLAS)
                .addSource(single("infuse_type/better_gold"))
                .addSource(single("infuse_type/plaslitherite"));
        atlas(SHIELD_PATTERNS_ATLAS)
                .addSource(single("entity/shield/better_gold"))
                .addSource(single("entity/shield/plaslitherite"))
                .addSource(single("entity/shield/refined_redstone"))
                .addSource(single("entity/shield/noctis_rozuli"));
    }

    private SingleFile single(String path) {
        return new SingleFile(modLoc(path), Optional.empty());
    }

    private ResourceLocation modLoc(String path) {
        return EvolvedMekanism.rl(path);
    }
}
