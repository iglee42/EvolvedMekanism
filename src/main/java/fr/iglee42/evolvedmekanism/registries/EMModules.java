package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.modules.ModuleAirAffinity;
import fr.iglee42.evolvedmekanism.modules.ModuleCapturing;
import mekanism.common.registration.impl.ModuleDeferredRegister;
import mekanism.common.registration.impl.ModuleRegistryObject;
import net.minecraft.world.item.Rarity;

public class EMModules {

    public static final ModuleDeferredRegister MODULES = new ModuleDeferredRegister(EvolvedMekanism.MODID);

    public static final ModuleRegistryObject<?> AIR_AFFINITY = MODULES.register("air_affinity_unit",
            ModuleAirAffinity::new, ()->EMItems.AIR_AFFINITY.asItem(), builder -> builder.rarity(Rarity.RARE));

    public static final ModuleRegistryObject<?> AQUA_AFFINITY = MODULES.registerMarker("aqua_affinity_unit",
            ()->EMItems.AQUA_AFFINITY.asItem(), builder -> builder.rarity(Rarity.RARE));

    public static final ModuleRegistryObject<?> CAPTURING = MODULES.register("capturing_unit",
            ModuleCapturing::new,()->EMItems.CAPTURING.asItem(), builder -> builder.rarity(Rarity.RARE).maxStackSize(3));

    public static final ModuleRegistryObject<?> LUCK = MODULES.registerMarker("luck_unit",
            ()->EMItems.LUCK.asItem(), builder -> builder.rarity(Rarity.RARE).maxStackSize(4));

}
