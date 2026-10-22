package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.world.EMDisableableFeaturePlacement;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class EMPlacementModifiers {

    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, EvolvedMekanism.MODID);

    public static final RegistryObject<PlacementModifierType<EMDisableableFeaturePlacement>> DISABLEABLE = PLACEMENT_MODIFIERS.register("disableable",
          () -> () -> EMDisableableFeaturePlacement.CODEC);
}
