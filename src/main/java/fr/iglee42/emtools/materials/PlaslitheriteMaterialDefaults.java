package fr.iglee42.emtools.materials;

import fr.iglee42.emtools.registries.EMToolsTags;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMTags;
import mekanism.tools.common.material.BaseMekanismMaterial;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaslitheriteMaterialDefaults extends BaseMekanismMaterial {

    @Override
    public int getShieldDurability() {
        return 448;
    }

    @Override
    public float getAxeDamage() {
        return 7;
    }

    @Override
    public float getAxeAtkSpeed() {
        return -3.0F;
    }

    @Override
    public int getUses() {
        return 500;
    }

    @Override
    public float getSpeed() {
        return 8;
    }

    @Override
    public float getAttackDamageBonus() {
        return 3;
    }

    @Override
    public int getLevel() {
        return 3;
    }

    @Override
    public int getCommonEnchantability() {
        return 16;
    }

    @Override
    public float getToughness() {
        return 2;
    }

    @Override
    public int getDurabilityForType(@NotNull ArmorItem.Type armorType) {
        return switch (armorType) {
            case BOOTS -> 260;
            case LEGGINGS -> 300;
            case CHESTPLATE -> 320;
            case HELMET -> 220;
        };
    }

    @Override
    public int getDefenseForType(@NotNull ArmorItem.Type armorType) {
        return switch (armorType) {
            case BOOTS -> 3;
            case LEGGINGS -> 6;
            case CHESTPLATE -> 8;
            case HELMET -> 3;
        };
    }

    @NotNull
    @Override
    public String getConfigCommentName() {
        return "Plaslitherite";
    }

    @NotNull
    @Override
    public String getRegistryPrefix() {
        return "plaslitherite";
    }

    @Nullable
    @Override
    public TagKey<Block> getTag() {
        return EMToolsTags.Blocks.NEEDS_PLASLITHERITE_TOOL;
    }

    @NotNull
    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_NETHERITE;
    }

    @NotNull
    @Override
    public Ingredient getCommonRepairMaterial() {
        return Ingredient.of(EMTags.Items.INGOTS_PLASLITHERITE);
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }

    @Override
    public String getName() {
        return EvolvedMekanism.MODID + ":" + getRegistryPrefix();
    }
}