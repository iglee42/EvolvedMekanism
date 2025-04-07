package fr.iglee42.evolvedmekanism.mixins;

import fr.iglee42.evolvedmekanism.crafting.PersonalBarrelUpgrading;
import fr.iglee42.evolvedmekanism.crafting.PersonalChestUpgrading;
import fr.iglee42.evolvedmekanism.jei.PersonalBarrelUpgradeRecipeMaker;
import fr.iglee42.evolvedmekanism.jei.PersonalChestUpgradeRecipeMaker;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.library.plugins.vanilla.VanillaPlugin;
import net.minecraft.world.item.crafting.CraftingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mixin(value = VanillaPlugin.class,remap = false)
public class VanillaPluginMixin {


    @Inject(method = "replaceSpecialCraftingRecipes", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",shift = At.Shift.AFTER),locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void evolvedmekansim$registerUpgrades(List<CraftingRecipe> unhandledCraftingRecipes, IStackHelper stackHelper, CallbackInfoReturnable<List<CraftingRecipe>> cir, Map<Class<? extends CraftingRecipe>, Supplier<List<CraftingRecipe>>> replacers){
        replacers.put(PersonalChestUpgrading.class, PersonalChestUpgradeRecipeMaker::createRecipes);
        replacers.put(PersonalBarrelUpgrading.class, PersonalBarrelUpgradeRecipeMaker::createRecipes);
    }

}
