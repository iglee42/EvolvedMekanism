package fr.iglee42.evolvedmekanism.jei;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.blocks.BlockTieredPersonnalBarrel;
import fr.iglee42.evolvedmekanism.blocks.BlockTieredPersonnalChest;
import fr.iglee42.evolvedmekanism.items.ItemBlockTieredPersonalStorage;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.registries.MekanismItems;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class PersonalBarrelUpgradeRecipeMaker {
	private static final String group = "jei.personal_upgrade.barrel";

	public static List<CraftingRecipe> createRecipes() {
		List<CraftingRecipe> recipes = new ArrayList<>();
		recipes.add(createRecipe(EMBlocks.ADVANCED_PERSONAL_BARREL,MekanismBlocks.PERSONAL_BARREL.asItem()));
		BlockTieredPersonnalBarrel[] chests = new BlockTieredPersonnalBarrel[]{
				EMBlocks.ADVANCED_PERSONAL_BARREL.getBlock(),
				EMBlocks.ELITE_PERSONAL_BARREL.getBlock(),
				EMBlocks.ULTIMATE_PERSONAL_BARREL.getBlock(),
				EMBlocks.OVERCLOCKED_PERSONAL_BARREL.getBlock(),
				EMBlocks.QUANTUM_PERSONAL_BARREL.getBlock(),
				EMBlocks.DENSE_PERSONAL_BARREL.getBlock(),
				EMBlocks.MULTIVERSAL_PERSONAL_BARREL.getBlock(),
				EMBlocks.CREATIVE_PERSONAL_BARREL.getBlock()
		};
		Arrays.stream(chests)
				.map(chest -> createRecipe(BlockTieredPersonnalBarrel.getUpgrade(chest.getTier()),chest.asItem()))
				.filter(Objects::nonNull)
				.forEach(recipes::add);
		return recipes;
	}

	private static CraftingRecipe createRecipe(@Nullable BlockRegistryObject<BlockTieredPersonnalBarrel, ItemBlockTieredPersonalStorage<BlockTieredPersonnalBarrel>> nextChest, Item baseChest) {
		Ingredient steel = Ingredient.of(MekanismItems.STEEL_INGOT);
		Ingredient glass = Ingredient.of(Tags.Items.GLASS_SILICA);
		if (nextChest == null) return null;
		Ingredient circuit = Ingredient.of(EvolvedMekanism.getCircuitByTier(nextChest.getBlock().getTier().getBaseTier()));
		NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, steel,glass,steel,circuit,Ingredient.of(baseChest),circuit,steel,steel,steel);
		ItemStack output = new ItemStack(nextChest.asItem());
		ResourceLocation id = EvolvedMekanism.rl(group + "." + output.getDescriptionId());
		return new ShapedRecipe(id, group, CraftingBookCategory.MISC,3,3,inputs,output);
	}

	private PersonalBarrelUpgradeRecipeMaker() {

	}
}
