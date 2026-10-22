package fr.iglee42.evolvedmekanism.datagen;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EMFluidTags extends FluidTagsProvider {

    public EMFluidTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper existing) {
        super(output, lookup, EvolvedMekanism.MODID, existing);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        EMFluids.FLUIDS.getFluidEntries().forEach(holder -> {
            String path = holder.getId().getPath();
            String tagPath = path.startsWith("flowing_") ? path.substring("flowing_".length()) : path;
            tag(EMDatagenTags.cFluid(tagPath)).add(holder.get());
        });
        tag(FluidTags.create(ResourceLocation.fromNamespaceAndPath("create", "no_infinite_draining")))
                .add(EMFluids.NITROGEN.get(), EMFluids.NITROGEN.getFlowingFluid().get(),
                        EMFluids.CRYONOCTIS.get(), EMFluids.CRYONOCTIS.getFlowingFluid().get());
    }
}
