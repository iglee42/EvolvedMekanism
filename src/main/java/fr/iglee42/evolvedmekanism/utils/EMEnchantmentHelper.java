package fr.iglee42.evolvedmekanism.utils;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class EMEnchantmentHelper {

    private EMEnchantmentHelper() {
    }

    public static boolean isCurse(Holder<Enchantment> enchantment) {
        return enchantment.is(EnchantmentTags.CURSE);
    }

    public static Map<Holder<Enchantment>, Integer> getEnchantmentsForItem(RegistryAccess access, ItemStack stack) {
        ItemEnchantments enchantments = stack.getTagEnchantments();
        Set<Holder<Enchantment>> possibleEnchantments = getPossibleEnchantmentsForItem(access, stack);
        Map<Holder<Enchantment>, Integer> enchantmentMap = new HashMap<>();
        possibleEnchantments.forEach(enchantment -> enchantmentMap.put(enchantment, enchantments.getLevel(enchantment)));
        return enchantmentMap;
    }

    public static Set<Holder<Enchantment>> getPossibleEnchantmentsForItem(RegistryAccess access, ItemStack stack) {
        Set<Holder<Enchantment>> availableEnchantments = getAvailableEnchantmentsForItem(access, stack);
        availableEnchantments.addAll(stack.getTagEnchantments().keySet());
        return availableEnchantments;
    }

    public static Set<Holder<Enchantment>> getAvailableEnchantmentsForItem(RegistryAccess access,ItemStack stack){
        var registry = access.registry(Registries.ENCHANTMENT).orElse(null);
        if (registry == null) {
            return Set.of();
        }
        return registry.holders().filter(stack::supportsEnchantment).collect(Collectors.toSet());
    }
}
