package fr.iglee42.evolvedmekanism.datagen;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMInfuseTypes;
import fr.iglee42.evolvedmekanism.registries.EMTags;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.infuse.InfuseType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EMInfuseTags extends TagsProvider<InfuseType> {

    public EMInfuseTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper existing) {
        super(output, MekanismAPI.INFUSE_TYPE_REGISTRY_NAME, lookup, EvolvedMekanism.MODID, existing);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(EMTags.InfuseTypes.URANIUM).add(EMInfuseTypes.URANIUM.key());
        tag(EMTags.InfuseTypes.BETTER_GOLD).add(EMInfuseTypes.BETTER_GOLD.key());
        tag(EMTags.InfuseTypes.PLASLITHERITE).add(EMInfuseTypes.PLASLITHERITE.key());
    }
}
