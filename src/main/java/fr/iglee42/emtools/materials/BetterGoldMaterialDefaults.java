package fr.iglee42.emtools.materials;

import fr.iglee42.emtools.registries.EMToolsTags;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMTags;
import mekanism.tools.common.material.BaseMekanismMaterial;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class BetterGoldMaterialDefaults implements BaseMekanismMaterial {

    @Override
    public int getShieldDurability() {
        return 3360;
    }

    @Override
    public float getAxeDamage() {
        return 16;
    }

    @Override
    public float getAxeAtkSpeed() {
        return -3.0F;
    }

    @Override
    public float getSwordDamage() {
        return 14;
    }

    @Override
    public float getPickaxeDamage() {
        return 10;
    }

    @Override
    public float getShovelDamage() {
        return 10.5f;
    }

    @Override
    public float getHoeDamage() {
        return 0;
    }

    @Override
    public int getUses() {
        return 8192;
    }

    @Override
    public float getSpeed() {
        return 8;
    }

    @Override
    public float getAttackDamageBonus() {
        return 2;
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return EMToolsTags.Blocks.INCORRECT_FOR_NEEDS_BETTER_GOLD_TOOL;
    }

    @Override
    public int getEnchantmentValue() {
        return 16;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(EMTags.Items.INGOTS_BETTER_GOLD);
    }

    @Override
    public float toughness() {
        return 5;
        }

    @Override
    public int getDurabilityForType(@NotNull ArmorItem.Type armorType) {
        return switch (armorType) {
            default -> 0;
            case BOOTS -> 1650;
            case LEGGINGS -> 2250;
            case CHESTPLATE -> 2400;
            case HELMET -> 1950;
        };
    }

    @Override
    public int getDefense(@NotNull ArmorItem.Type armorType) {
        return switch (armorType) {
            default -> 0;
            case BOOTS -> 7;
            case LEGGINGS -> 10;
            case CHESTPLATE -> 12;
            case HELMET -> 8;
        };
    }

    @NotNull
    @Override
    public String getRegistryPrefix() {
        return "better_gold";
    }

    @Override
    public float knockbackResistance() {
        return 0.2f;
    }

    @Override
    public Holder<SoundEvent> equipSound() {
        return SoundEvents.ARMOR_EQUIP_GOLD;
    }

}