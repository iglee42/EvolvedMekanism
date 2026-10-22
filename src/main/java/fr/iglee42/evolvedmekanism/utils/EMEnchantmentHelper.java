package fr.iglee42.evolvedmekanism.utils;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class EMEnchantmentHelper {

    private EMEnchantmentHelper() {
    }

    public static boolean isCurse(Enchantment enchantment) {
        return enchantment.isCurse();
    }

    public static boolean areCompatible(Enchantment first, Enchantment second) {
        return first.isCompatibleWith(second) && second.isCompatibleWith(first);
    }

    public static int getRarityMultiplier(Enchantment enchantment) {
        int weight = enchantment.getRarity().getWeight();
        if (weight >= 10) {
            return 1;
        } else if (weight >= 5) {
            return 2;
        } else if (weight >= 2) {
            return 4;
        }
        return 8;
    }

    public static Map<Enchantment, Integer> getEnchantments(ItemStack stack) {
        if (stack.is(Items.ENCHANTED_BOOK)) {
            ListTag stored = EnchantedBookItem.getEnchantments(stack);
            return new HashMap<>(EnchantmentHelper.deserializeEnchantments(stored));
        }
        return new HashMap<>(EnchantmentHelper.getEnchantments(stack));
    }

    public static Set<Enchantment> getPossibleEnchantments(ItemStack stack) {
        Set<Enchantment> enchantments = new HashSet<>();
        for (Enchantment enchantment : ForgeRegistries.ENCHANTMENTS) {
            if (enchantment.canEnchant(stack) || stack.is(Items.BOOK) || stack.is(Items.ENCHANTED_BOOK)) {
                enchantments.add(enchantment);
            }
        }
        enchantments.addAll(getEnchantments(stack).keySet());
        return enchantments;
    }

    public static void setEnchantments(ItemStack stack, Map<Enchantment, Integer> enchantments) {
        if (stack.is(Items.ENCHANTED_BOOK)) {
            stack.removeTagKey("StoredEnchantments");
            enchantments.forEach((enchantment, level) -> {
                if (level > 0) {
                    EnchantedBookItem.addEnchantment(stack, new EnchantmentInstance(enchantment, level));
                }
            });
            return;
        }
        Map<Enchantment, Integer> filtered = new HashMap<>();
        enchantments.forEach((enchantment, level) -> {
            if (level > 0) {
                filtered.put(enchantment, level);
            }
        });
        EnchantmentHelper.setEnchantments(filtered, stack);
    }
}
