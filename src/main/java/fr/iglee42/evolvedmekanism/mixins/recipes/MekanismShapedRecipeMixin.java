package fr.iglee42.evolvedmekanism.mixins.recipes;

import fr.iglee42.evolvedmekanism.inventory.personalstorage.TieredPersonalStorageManager;
import fr.iglee42.evolvedmekanism.items.ItemBlockTieredPersonalStorage;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.recipe.upgrade.MekanismShapedRecipe;
import mekanism.common.recipe.upgrade.RecipeUpgradeData;
import mekanism.common.recipe.upgrade.RecipeUpgradeType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Mixin(value = MekanismShapedRecipe.class,remap = false)
public class MekanismShapedRecipeMixin {

    @Redirect(method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "INVOKE", target = "Lmekanism/common/recipe/upgrade/RecipeUpgradeData;getSupportedTypes(Lnet/minecraft/world/item/ItemStack;)Ljava/util/Set;"))
    private @NotNull Set<RecipeUpgradeType> em$addTieredStoragesTypes(ItemStack stack){
        Set<RecipeUpgradeType> types = RecipeUpgradeData.getSupportedTypes(stack);
        if (stack.getItem() instanceof ItemBlockTieredPersonalStorage){
            types.add(RecipeUpgradeType.ITEM);
        }
        return types;
    }

    @Redirect(method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;", at= @At(value = "INVOKE", target = "Lmekanism/common/recipe/upgrade/RecipeUpgradeData;getUpgradeData(Lmekanism/common/recipe/upgrade/RecipeUpgradeType;Lnet/minecraft/world/item/ItemStack;)Lmekanism/common/recipe/upgrade/RecipeUpgradeData;"))
    private @Nullable RecipeUpgradeData<?> em$getDataFromTieredStorages(@NotNull RecipeUpgradeType type, @NotNull ItemStack stack){
        RecipeUpgradeData<?> data = RecipeUpgradeData.getUpgradeData(type,stack);
        if (stack.getItem() instanceof ItemBlockTieredPersonalStorage && type.equals(RecipeUpgradeType.ITEM)){
            List<IInventorySlot> slots = TieredPersonalStorageManager.getInventoryIfPresent(stack).map(inv-> inv.getInventorySlots(null)).orElse(Collections.emptyList());
            data = slots.isEmpty() ? null : ItemRecipeDataInvoker.em$invokeInit(slots);
        }
        return data;
    }

}
