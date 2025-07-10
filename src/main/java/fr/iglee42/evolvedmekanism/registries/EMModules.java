package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.modules.ModuleAirAffinity;
import fr.iglee42.evolvedmekanism.modules.ModuleCapturing;
import mekanism.common.registration.impl.ModuleDeferredRegister;
import mekanism.common.registration.impl.ModuleRegistryObject;
import net.minecraft.world.item.enchantment.Enchantments;

public class EMModules {

    public static final ModuleDeferredRegister MODULES = new ModuleDeferredRegister(EvolvedMekanism.MODID);

    public static final ModuleRegistryObject<ModuleAirAffinity> AIR_AFFINITY = MODULES.registerInstanced("air_affinity_unit",
            ModuleAirAffinity::new, ()->EMItems.AIR_AFFINITY, builder -> builder);

    public static final ModuleRegistryObject<?> AQUA_AFFINITY = MODULES.registerEnchantBased("aqua_affinity_unit",
            Enchantments.AQUA_AFFINITY,()->EMItems.AQUA_AFFINITY, builder -> builder);

    public static final ModuleRegistryObject<?> CAPTURING = MODULES.registerInstanced("capturing_unit",
            ModuleCapturing::new,()->EMItems.CAPTURING, builder -> builder.maxStackSize(3));

    public static final ModuleRegistryObject<?> LUCK = MODULES.registerMarker("luck_unit",
            ()->EMItems.LUCK, builder -> builder.maxStackSize(4));

}
