package fr.iglee42.emtools.registries;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

import fr.iglee42.emtools.config.EMToolsMaterialConfig;
import fr.iglee42.emtools.config.EvolvedMekanismToolsConfig;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.common.util.EnumUtils;
import mekanism.tools.common.material.BaseMekanismMaterial;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EMToolsArmorMaterials {

    private EMToolsArmorMaterials() {
    }

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, EvolvedMekanism.MODID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> BETTER_GOLD = ARMOR_MATERIALS.register("better_gold", name -> createMaterial(name, EvolvedMekanismToolsConfig.materials.betterGold));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> PLASLITHERITE = ARMOR_MATERIALS.register("plaslitherite", name -> createMaterial(name, EvolvedMekanismToolsConfig.materials.plaslitherite));

    private static ArmorMaterial createMaterial(ResourceLocation name, BaseMekanismMaterial material) {
        EnumMap<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
        for (ArmorItem.Type type : EnumUtils.ARMOR_TYPES) {
            int providedDefense = material.getDefense(type);
            if (providedDefense > 0) {
                defense.put(type, providedDefense);
            }
        }
        return new ArmorMaterial(defense.isEmpty() ? Collections.emptyMap() : defense, material.getEnchantmentValue(), material.equipSound(), material::getRepairIngredient,
              List.of(new ArmorMaterial.Layer(name)), material.toughness(), material.knockbackResistance()
        );
    }
}