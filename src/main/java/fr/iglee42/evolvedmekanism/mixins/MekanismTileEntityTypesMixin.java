package fr.iglee42.evolvedmekanism.mixins;

import com.google.common.collect.Table;
import fr.iglee42.evolvedmekanism.registries.EMFactoryType;
import fr.iglee42.evolvedmekanism.registries.EMTileEntityTypes;
import fr.iglee42.evolvedmekanism.tiles.factory.TileEntityAlloyingFactory;
import mekanism.common.content.blocktype.FactoryType;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.registries.MekanismTileEntityTypes;
import mekanism.common.tier.FactoryTier;
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
public class MekanismTileEntityTypesMixin {


    @Shadow @Final private static Table<FactoryTier, FactoryType, TileEntityTypeRegistryObject<? extends TileEntityFactory<?>>> FACTORIES;

    @Inject(method = "<clinit>",at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lcom/google/common/collect/Table;put(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",ordinal = 0), locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void evolveddraconic$addNewFactories(CallbackInfo ci, FactoryTier[] var0, int var1, int var2, FactoryTier tier){
        FACTORIES.put(tier, EMFactoryType.ALLOYING, EMTileEntityTypes.TILE_ENTITY_TYPES.register(MekanismBlocks.getFactory(tier, EMFactoryType.ALLOYING), (pos, state) -> new TileEntityAlloyingFactory(MekanismBlocks.getFactory(tier, EMFactoryType.ALLOYING), pos, state)));
    }

}
