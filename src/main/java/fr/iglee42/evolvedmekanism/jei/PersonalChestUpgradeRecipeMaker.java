package fr.iglee42.evolvedmekanism.jei;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.blocks.BlockTieredPersonnalChest;
import fr.iglee42.evolvedmekanism.items.ItemBlockTieredPersonalStorage;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.registries.MekanismItems;
import mezz.jei.api.constants.ModIds;
import mezz.jei.common.platform.IPlatformIngredientHelper;
import mezz.jei.common.platform.Services;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;
import java.util.*;

public final class PersonalChestUpgradeRecipeMaker {
	private static final String group = "jei.personal_upgrade.barrel";

	public static List<RecipeHolder<CraftingRecipe>> createRecipes() {
		List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();
		recipes.add(createRecipe(EMBlocks.ADVANCED_PERSONAL_CHEST,MekanismBlocks.PERSONAL_CHEST.asItem()));
		BlockTieredPersonnalChest[] chests = new BlockTieredPersonnalChest[]{
				EMBlocks.ADVANCED_PERSONAL_CHEST.get(),
				EMBlocks.ELITE_PERSONAL_CHEST.get(),
				EMBlocks.ULTIMATE_PERSONAL_CHEST.get(),
				EMBlocks.OVERCLOCKED_PERSONAL_CHEST.get(),
				EMBlocks.QUANTUM_PERSONAL_CHEST.get(),
				EMBlocks.DENSE_PERSONAL_CHEST.get(),
				EMBlocks.MULTIVERSAL_PERSONAL_CHEST.get(),
				EMBlocks.CREATIVE_PERSONAL_CHEST.get()
		};
		Arrays.stream(chests)
				.map(chest -> createRecipe(BlockTieredPersonnalChest.getUpgrade(chest.getTier()),chest.asItem()))
				.filter(Objects::nonNull)
				.forEach(recipes::add);
		return recipes;
	}

	private static RecipeHolder<CraftingRecipe> createRecipe(@Nullable BlockRegistryObject<BlockTieredPersonnalChest, ItemBlockTieredPersonalStorage<BlockTieredPersonnalChest>> nextChest, Item baseChest) {
		Ingredient steel = Ingredient.of(MekanismItems.STEEL_INGOT);
		Ingredient glass = Ingredient.of(Tags.Items.GLASS_BLOCKS_COLORLESS);
		if (nextChest == null) return null;
		Ingredient circuit = Ingredient.of(EvolvedMekanism.getCircuitByTier(nextChest.get().getTier().getBaseTier()));
		NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, steel,glass,steel,circuit,Ingredient.of(baseChest),circuit,steel,steel,steel);
		ItemStack output = new ItemStack(nextChest.asItem());
		ShapedRecipe recipe = new ShapedRecipe(group,CraftingBookCategory.MISC,new ShapedRecipePattern(3,3,inputs, Optional.empty()),output);
		ResourceLocation id = EvolvedMekanism.rl(group + "." + output.getDescriptionId());
		return new RecipeHolder<>(id,recipe);
	}

	private PersonalChestUpgradeRecipeMaker() {

	}
}
