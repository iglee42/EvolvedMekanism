package fr.iglee42.evolvedmekanism.crafting;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.blocks.BlockTieredPersonnalBarrel;
import fr.iglee42.evolvedmekanism.inventory.personalstorage.TieredPersonalStorageManager;
import fr.iglee42.evolvedmekanism.items.ItemBlockTieredPersonalStorage;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMRecipeSerializers;
import fr.iglee42.evolvedmekanism.tiers.PersonalStorageTier;
import mekanism.common.block.BlockPersonalBarrel;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.item.block.ItemBlockPersonalStorage;
import mekanism.common.lib.inventory.personalstorage.AbstractPersonalStorageItemInventory;
import mekanism.common.lib.inventory.personalstorage.PersonalStorageManager;
import mekanism.common.lib.security.ItemSecurityUtils;
import mekanism.common.lib.security.SecurityUtils;
import mekanism.common.registries.MekanismItems;
import mekanism.common.tags.MekanismTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class PersonalBarrelUpgrading extends CustomRecipe {
    public PersonalBarrelUpgrading(CraftingBookCategory p_250756_) {
        super(p_250756_);
    }

    public boolean matches(CraftingInput container, Level level) {
        Boolean[] isValid = new Boolean[9];

        if (container.size() > 9) return false;
        if (container.getItem(4).getItem() instanceof ItemBlockPersonalStorage<?> it){
            if (!(it.getBlock() instanceof BlockPersonalBarrel)) return false;
            isValid[4] = true;
            for (int i = 0; i < container.size(); i++){
                if (i == 4) continue;
                if (i == 0 || i == 2 || i >= 6) isValid[i] = container.getItem(i).is(MekanismTags.Items.INGOTS_STEEL);
                if (i == 1) isValid[i] = container.getItem(i).is(Tags.Items.GLASS_BLOCKS_COLORLESS);
                if (i == 3 || i == 5) isValid[i] = container.getItem(i).is(MekanismTags.Items.CIRCUITS_ADVANCED);
            }
            return Arrays.stream(isValid).allMatch(Boolean::booleanValue);
        }
        if (!(container.getItem(4).getItem() instanceof ItemBlockTieredPersonalStorage<?> it)) return false;
        if (!(it.getBlock() instanceof BlockTieredPersonnalBarrel chest)) return false;
        PersonalStorageTier tier = chest.getTier();
        if (BlockTieredPersonnalBarrel.getUpgrade(tier) == null) return false;
        if (tier.ordinal() >= PersonalStorageTier.values().length - 1) return false;
        PersonalStorageTier nextTier = PersonalStorageTier.values()[tier.ordinal()+1];
        if (EvolvedMekanism.getCircuitByTier(nextTier.getBaseTier()) == null) return false;
        isValid[4] = true;
        for (int i = 0; i < container.size(); i++){
            if (i == 4) continue;
            if (i == 0 || i == 2 || i >= 6) isValid[i] = container.getItem(i).is(MekanismTags.Items.INGOTS_STEEL);
            if (i == 1) isValid[i] = container.getItem(i).is(Tags.Items.GLASS_BLOCKS_COLORLESS);
            if (i == 3 || i == 5) isValid[i] = container.getItem(i).is(EvolvedMekanism.getCircuitByTier(nextTier.getBaseTier()).asItem());
        }
        return Arrays.stream(isValid).allMatch(Boolean::booleanValue);
    }

    public @NotNull ItemStack assemble(CraftingInput container, HolderLookup.Provider access) {
        ItemStack oldBarrel = container.getItem(4);
        if (container.getItem(4).getItem() instanceof ItemBlockPersonalStorage<?> it){
            UUID owner = ItemSecurityUtils.get().getOwnerUUID(oldBarrel);
            if (owner == null) return new ItemStack(EMBlocks.ADVANCED_PERSONAL_BARREL.asItem());
            AbstractPersonalStorageItemInventory inventory = PersonalStorageManager.getInventoryFor(oldBarrel).orElse(null);
            if (inventory == null)return new ItemStack(EMBlocks.ADVANCED_PERSONAL_BARREL.asItem());
            ItemStack stack = new ItemStack(EMBlocks.ADVANCED_PERSONAL_BARREL.asItem());
            Optional.ofNullable(ItemSecurityUtils.get().securityCapability(stack)).ifPresent(i->i.setOwnerUUID(owner));
            TieredPersonalStorageManager.transferFromBasic(inventory,stack);
            return stack;
        }
        if (!(container.getItem(4).getItem() instanceof ItemBlockTieredPersonalStorage<?> it)) return ItemStack.EMPTY;
        if (!(it.getBlock() instanceof BlockTieredPersonnalBarrel chest)) return ItemStack.EMPTY;
        PersonalStorageTier tier = chest.getTier();
        if (BlockTieredPersonnalBarrel.getUpgrade(tier) == null) return ItemStack.EMPTY;
        if (tier.ordinal() >= PersonalStorageTier.values().length - 1) return ItemStack.EMPTY;
        UUID owner = ItemSecurityUtils.get().getOwnerUUID(oldBarrel);
        if (owner == null) return new ItemStack(BlockTieredPersonnalBarrel.getUpgrade(tier).asItem());
        ItemStack stack = new ItemStack(BlockTieredPersonnalBarrel.getUpgrade(tier).asItem());
        Optional.ofNullable(ItemSecurityUtils.get().securityCapability(stack)).ifPresent(i->i.setOwnerUUID(owner));
        TieredPersonalStorageManager.transferToNew(oldBarrel,stack);
        return stack;
    }

    public boolean canCraftInDimensions(int p_44314_, int p_44315_) {
        return p_44314_ * p_44315_ >= 9;
    }

    public RecipeSerializer<?> getSerializer() {
        return EMRecipeSerializers.PERSONAL_BARREL_UPGRADING.get();
    }
}
