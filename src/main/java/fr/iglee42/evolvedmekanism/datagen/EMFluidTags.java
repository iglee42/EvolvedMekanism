package fr.iglee42.evolvedmekanism.datagen;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EMFluidTags extends FluidTagsProvider {

    public EMFluidTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper existing) {
        super(output, lookup, EvolvedMekanism.MODID, existing);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (Fluid fluid : ForgeRegistries.FLUIDS) {
            ResourceLocation id = ForgeRegistries.FLUIDS.getKey(fluid);
            if (id == null || !EvolvedMekanism.MODID.equals(id.getNamespace())) {
                continue;
            }
            String path = id.getPath();
            String tagPath = path.startsWith("flowing_") ? path.substring("flowing_".length()) : path;
            tag(EMDatagenTags.forgeFluid(tagPath)).add(fluid);
        }
    }
}
