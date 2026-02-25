package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.world.EMDisableableFeaturePlacement;
import mekanism.common.Mekanism;
import mekanism.common.world.DisableableFeaturePlacement;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EMPlacementModifiers {

    private EMPlacementModifiers() {
    }

    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, EvolvedMekanism.MODID);

    public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<EMDisableableFeaturePlacement>> DISABLEABLE = PLACEMENT_MODIFIERS.register("disableable", () -> () -> EMDisableableFeaturePlacement.CODEC);
}