package fr.iglee42.evolvedmekanism.items;

import fr.iglee42.evolvedmekanism.blocks.EMBlockResource;
import mekanism.common.block.basic.BlockResource;
import mekanism.common.item.block.ItemBlockMekanism;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

public class EMItemBlockResource extends ItemBlockMekanism<EMBlockResource> {

    public EMItemBlockResource(EMBlockResource block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return getBlock().getResourceInfo().getBurnTime();
    }
}