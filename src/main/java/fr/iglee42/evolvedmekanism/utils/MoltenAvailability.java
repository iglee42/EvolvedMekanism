package fr.iglee42.evolvedmekanism.utils;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.datagen.EMDatagenTags;
import fr.iglee42.evolvedmekanism.datagen.recipe.MoltenRecipes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

public final class MoltenAvailability {

    private MoltenAvailability() {
    }

    public static boolean isObtainable(HolderLookup.Provider registries, Fluid fluid) {
        ResourceLocation id = BuiltInRegistries.FLUID.getKey(fluid);
        if (id == null) {
            return true;
        }
        if (!EvolvedMekanism.MODID.equals(id.getNamespace()) || !id.getPath().startsWith("molten_")) {
            return true;
        }
        String material = id.getPath().substring("molten_".length());
        HolderLookup.RegistryLookup<Item> items = registries.lookupOrThrow(Registries.ITEM);
        for (MoltenRecipes.MaterialForm form : MoltenRecipes.FORMS) {
            if (tagHasItems(items, form.itemTag(material))) {
                return true;
            }
        }
        return extraSourcePresent(items, material);
    }

    public static boolean bucketObtainable(Holder<Item> bucket, HolderLookup.Provider registries) {
        ResourceLocation id = bucket.unwrapKey().map(ResourceKey::location).orElse(null);
        if (id == null || !id.getPath().endsWith("_bucket")) {
            return true;
        }
        String fluidPath = id.getPath().substring(0, id.getPath().length() - "_bucket".length());
        if (!fluidPath.startsWith("molten_")) {
            return true;
        }
        ResourceKey<Fluid> fluidKey = ResourceKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath(id.getNamespace(), fluidPath));
        return registries.lookupOrThrow(Registries.FLUID).get(fluidKey)
                .map(holder -> isObtainable(registries, holder.value()))
                .orElse(true);
    }

    private static boolean extraSourcePresent(HolderLookup.RegistryLookup<Item> items, String material) {
        return switch (material) {
            case "clay", "slime", "nether_star" -> true;
            case "ender" -> tagHasItems(items, EMDatagenTags.cItem("ender_pearls"));
            case "pink_slime" -> itemExists(items, ResourceLocation.fromNamespaceAndPath("industrialforegoing", "pink_slime"));
            default -> false;
        };
    }

    private static boolean tagHasItems(HolderLookup.RegistryLookup<Item> items, TagKey<Item> tag) {
        return items.get(tag).filter(set -> set.size() > 0).isPresent();
    }

    private static boolean itemExists(HolderLookup.RegistryLookup<Item> items, ResourceLocation id) {
        return items.get(ResourceKey.create(Registries.ITEM, id)).isPresent();
    }
}
