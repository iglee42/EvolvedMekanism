package fr.iglee42.evolvedmekanism.datagen;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMGases;
import fr.iglee42.evolvedmekanism.registries.EMTags;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.gas.Gas;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EMGasTags extends TagsProvider<Gas> {

    public EMGasTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper existing) {
        super(output, MekanismAPI.GAS_REGISTRY_NAME, lookup, EvolvedMekanism.MODID, existing);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(EMTags.Gases.NITROGEN).add(EMGases.NITROGEN.key());
        tag(EMTags.Gases.CRYONOCTIS).add(EMGases.CRYONOCTIS.key());
    }
}
