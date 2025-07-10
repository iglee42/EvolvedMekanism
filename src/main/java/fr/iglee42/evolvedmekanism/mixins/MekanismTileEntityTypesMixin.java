package fr.iglee42.evolvedmekanism.mixins;

import com.google.common.collect.Table;
import fr.iglee42.evolvedmekanism.registries.EMFactoryType;
import fr.iglee42.evolvedmekanism.registries.EMTileEntityTypes;
import fr.iglee42.evolvedmekanism.tiles.factory.TileEntityAlloyingFactory;
import mekanism.common.block.prefab.BlockFactoryMachine;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.content.blocktype.FactoryType;
import mekanism.common.item.block.machine.ItemBlockFactory;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.registries.MekanismTileEntityTypes;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.factory.TileEntityCombiningFactory;
import mekanism.common.tile.factory.TileEntityFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = MekanismTileEntityTypes.class,remap = false)
public abstract class MekanismTileEntityTypesMixin {


    @Shadow @Final private static Table<FactoryTier, FactoryType, TileEntityTypeRegistryObject<? extends TileEntityFactory<?>>> FACTORIES;

    @Inject(method = "<clinit>",at = @At(value = "INVOKE", target = "Lmekanism/common/registries/MekanismTileEntityTypes;registerFactory(Lmekanism/common/tier/FactoryTier;Lmekanism/common/content/blocktype/FactoryType;Lmekanism/common/registries/MekanismTileEntityTypes$BlockEntityFactory;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void evolveddraconic$addNewFactories(CallbackInfo ci, FactoryTier[] var0, int var1, int var2, FactoryTier tier){
      //BlockRegistryObject<BlockFactoryMachine.BlockFactory<?>, ItemBlockFactory> block = MekanismBlocks.getFactory(tier, EMFactoryType.ALLOYING);
      //TileEntityTypeRegistryObject<? extends TileEntityFactory<?>> tileRO = MekanismTileEntityTypes.TILE_ENTITY_TYPES.mekBuilder(block, (pos, state) -> new TileEntityAlloyingFactory(block, pos, state))
      //        .clientTicker(TileEntityMekanism::tickClient)
      //        .serverTicker(TileEntityMekanism::tickServer)
      //        .withSimple(Capabilities.CONFIG_CARD)
      //        .build();
      //FACTORIES.put(tier, EMFactoryType.ALLOYING, tileRO);
    }

}
