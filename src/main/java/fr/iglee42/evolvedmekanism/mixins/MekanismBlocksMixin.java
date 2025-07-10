package fr.iglee42.evolvedmekanism.mixins;

import com.google.common.collect.Table;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMFactoryType;
import mekanism.api.tier.BaseTier;
import mekanism.api.tier.ITier;
import mekanism.common.block.prefab.BlockFactoryMachine;
import mekanism.common.content.blocktype.Factory;
import mekanism.common.content.blocktype.FactoryType;
import mekanism.common.item.block.machine.ItemBlockFactory;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registries.MekanismBlockTypes;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.util.EnumUtils;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(value = MekanismBlocks.class,remap = false)
public abstract class MekanismBlocksMixin {


    @Shadow @Final private static Table<FactoryTier, FactoryType, BlockRegistryObject<BlockFactoryMachine.BlockFactory<?>, ItemBlockFactory>> FACTORIES;

    @Shadow
    protected static <TILE extends TileEntityFactory<?>> BlockRegistryObject<BlockFactoryMachine.BlockFactory<?>, ItemBlockFactory> registerFactory(Factory<TILE> type) {
        return null;
    }

    @Inject(method = "registerTieredBlock(Lmekanism/api/tier/ITier;Ljava/lang/String;Ljava/util/function/Supplier;Ljava/util/function/BiFunction;)Lmekanism/common/registration/impl/BlockRegistryObject;",at = @At(value = "RETURN",shift = At.Shift.BEFORE), cancellable = true)
    private static <BLOCK extends Block, ITEM extends BlockItem> void evolveddraconic$changeModid(ITier tier, String suffix, Supplier<? extends BLOCK> blockSupplier, BiFunction<BLOCK, Item.Properties, ITEM> itemCreator, CallbackInfoReturnable<BlockRegistryObject<BLOCK, ITEM>> cir){
        if (EvolvedMekanism.isEvolvedMekanismTier(tier.getBaseTier()) &&
                (!tier.getBaseTier().equals(BaseTier.CREATIVE) || !suffix.equals("_bin") && !suffix.equals("_energy_cube") && !suffix.equals("_fluid_tank") && !suffix.equals("_chemical_tank"))){
            cir.setReturnValue(EMBlocks.BLOCKS.register(tier.getBaseTier().getLowerName() + suffix, blockSupplier, itemCreator));
        }
    }

    @Inject(method = "<clinit>", at = @At(value = "HEAD"))
    private static void em$addFactories(CallbackInfo ci){
       //for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
       //    FACTORIES.put(tier, EMFactoryType.ALLOYING, registerFactory(MekanismBlockTypes.getFactory(tier, EMFactoryType.ALLOYING)));
       //}
    }
}
