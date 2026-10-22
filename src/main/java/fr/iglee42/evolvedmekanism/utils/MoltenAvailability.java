package fr.iglee42.evolvedmekanism.utils;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.datagen.recipe.MoltenRecipes;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.util.StorageUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class MoltenAvailability {

    private MoltenAvailability() {
    }

    public static void hideFromCreativeTab(BuildCreativeModeTabContentsEvent event) {
        HolderLookup.Provider registries = event.getParameters().holders();
        List<ItemStack> hidden = new ArrayList<>();
        for (Map.Entry<ItemStack, CreativeModeTab.TabVisibility> entry : event.getEntries()) {
            if (shouldHide(registries, entry.getKey())) {
                hidden.add(entry.getKey());
            }
        }
        hidden.forEach(event.getEntries()::remove);
    }

    private static boolean shouldHide(HolderLookup.Provider registries, ItemStack stack) {
        Fluid fluid = fluidOf(stack);
        return fluid != null && !isObtainable(registries, fluid);
    }

    private static Fluid fluidOf(ItemStack stack) {
        if (stack.getItem() == MekanismBlocks.CREATIVE_FLUID_TANK.asItem()) {
            FluidStack stored = StorageUtils.getStoredFluidFromNBT(stack);
            return stored.isEmpty() ? null : stored.getFluid();
        }
        if (stack.getItem() instanceof BucketItem bucket) {
            return bucket.getFluid();
        }
        return null;
    }

    public static boolean isObtainable(HolderLookup.Provider registries, Fluid fluid) {
        ResourceLocation id = ForgeRegistries.FLUIDS.getKey(fluid);
        if (id == null || !EvolvedMekanism.MODID.equals(id.getNamespace()) || !id.getPath().startsWith("molten_")) {
            return true;
        }
        var items = registries.lookup(Registries.ITEM);
        if (items.isEmpty()) {
            return true;
        }
        String material = id.getPath().substring("molten_".length());
        HolderLookup.RegistryLookup<Item> lookup = items.get();
        for (MoltenRecipes.MaterialForm form : MoltenRecipes.FORMS) {
            if (tagHasItems(lookup, form.itemTag(material))) {
                return true;
            }
        }
        return extraSourcePresent(lookup, material);
    }

    private static boolean extraSourcePresent(HolderLookup.RegistryLookup<Item> items, String material) {
        return switch (material) {
            case "clay" -> itemPresent(items, new ResourceLocation("minecraft", "clay_ball"));
            case "nether_star" -> itemPresent(items, new ResourceLocation("minecraft", "nether_star"));
            case "ender" -> tagHasItems(items, TagKey.create(Registries.ITEM, new ResourceLocation("forge", "ender_pearls")));
            case "soul" -> tagHasItems(items, TagKey.create(Registries.ITEM, new ResourceLocation("minecraft", "soul_fire_base_blocks")));
            case "pink_slime" -> itemPresent(items, new ResourceLocation("industrialforegoing", "pink_slime"));
            default -> false;
        };
    }

    private static boolean tagHasItems(HolderLookup.RegistryLookup<Item> items, TagKey<Item> tag) {
        return items.get(tag).map(set -> set.size() > 0).orElse(false);
    }

    private static boolean itemPresent(HolderLookup.RegistryLookup<Item> items, ResourceLocation id) {
        return items.get(ResourceKey.create(Registries.ITEM, id)).isPresent();
    }
}
