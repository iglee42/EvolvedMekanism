package fr.iglee42.emtools.materials;

import fr.iglee42.emtools.registries.EMToolsTags;
import fr.iglee42.evolvedmekanism.registries.EMTags;
import mekanism.tools.common.material.BaseMekanismMaterial;
import mekanism.tools.common.material.impl.LapisLazuliMaterialDefaults;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NoctisRozuliMaterialDefaults extends LapisLazuliMaterialDefaults {

    @NotNull
    @Override
    public String getRegistryPrefix() {
        return "noctis_rozuli";
    }

    @Nullable
    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return EMToolsTags.Blocks.INCORRECT_FOR_NOCTIS_ROZULI_TOOL;
    }


    @NotNull
    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(EMTags.Items.GEMS_NOCTIS_ROZULI);
    }

}