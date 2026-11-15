package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.modules.ModuleAdvancedVisionEnhancementUnit;
import fr.iglee42.evolvedmekanism.modules.ModuleAirAffinity;
import fr.iglee42.evolvedmekanism.modules.ModuleCapturing;
import mekanism.api.gear.config.ModuleBooleanConfig;
import mekanism.api.gear.config.ModuleColorConfig;
import mekanism.common.content.gear.shared.ModuleColorModulationUnit;
import mekanism.common.registration.impl.ModuleDeferredRegister;
import mekanism.common.registration.impl.ModuleRegistryObject;
import mekanism.common.registries.MekanismItems;
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

    public static final ModuleRegistryObject<ModuleAdvancedVisionEnhancementUnit> ADVANCED_VISION_ENHANCEMENT = MODULES.register("advanced_vision_enhancement_unit",
            ModuleAdvancedVisionEnhancementUnit::new, () -> EMItems.ADVANCED_VISION_ENHANCEMENT, builder -> builder
                    .maxStackSize(4)
                    .handlesModeChange()
                    .rendersHUD()
                    .addConfig(ModuleColorConfig.rgb(ModuleAdvancedVisionEnhancementUnit.FOG_COLOR, 0x66CC66), ModuleColorConfig.RGB_CODEC, ModuleColorConfig.RGB_STREAM_CODEC)
                    .addConfig(ModuleBooleanConfig.create(ModuleAdvancedVisionEnhancementUnit.FOG, false))
    );


}
