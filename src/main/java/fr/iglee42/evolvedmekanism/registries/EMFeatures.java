package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.world.EMResizableOreFeature;
import mekanism.common.Mekanism;
import mekanism.common.world.OreRetrogenFeature;
import mekanism.common.world.ResizableDiskConfig;
import mekanism.common.world.ResizableDiskReplaceFeature;
import mekanism.common.world.ResizableOreFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EMFeatures {

    private EMFeatures() {
    }

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, EvolvedMekanism.MODID);

    public static final DeferredHolder<Feature<?>, EMResizableOreFeature> ORE = FEATURES.register("ore", EMResizableOreFeature::new);
}