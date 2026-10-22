package fr.iglee42.evolvedmekanism.datagen;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMChemicals;
import fr.iglee42.evolvedmekanism.registries.EMTags;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EMChemicalTags extends TagsProvider<Chemical> {

    public EMChemicalTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper existing) {
        super(output, MekanismAPI.CHEMICAL_REGISTRY_NAME, lookup, EvolvedMekanism.MODID, existing);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(EMTags.Gases.URANIUM).add(EMChemicals.URANIUM.getKey());
        tag(EMTags.Gases.BETTER_GOLD).add(EMChemicals.BETTER_GOLD.getKey());
        tag(EMTags.Gases.PLASLITHERITE).add(EMChemicals.PLASLITHERITE.getKey());
        tag(EMDatagenTags.chemical("mekanism", "uranium")).add(EMChemicals.URANIUM.getKey());
        tag(EMDatagenTags.chemical("mekanism", "better_gold")).add(EMChemicals.BETTER_GOLD.getKey());
        tag(EMDatagenTags.chemical("mekanism", "plaslitherite")).add(EMChemicals.PLASLITHERITE.getKey());
        tag(EMDatagenTags.chemical(EvolvedMekanism.MODID, "gaseous")).add(EMChemicals.NITROGEN.getKey(), EMChemicals.CRYONOCTIS.getKey());
    }
}
