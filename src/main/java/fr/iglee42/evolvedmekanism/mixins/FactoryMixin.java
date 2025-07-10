package fr.iglee42.evolvedmekanism.mixins;

import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMFactoryType;
import fr.iglee42.evolvedmekanism.tiers.EMFactoryTier;
import mekanism.api.text.ILangEntry;
import mekanism.common.block.attribute.AttributeUpgradeable;
import mekanism.common.content.blocktype.Factory;
import mekanism.common.content.blocktype.FactoryType;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.util.EnumUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(Factory.class)
public class FactoryMixin<TILE extends TileEntityFactory<?>>  extends Machine.FactoryMachine<TILE> {

    public FactoryMixin(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntitySupplier, ILangEntry description, FactoryType factoryType) {
        super(tileEntitySupplier, description, factoryType);
    }

    @Inject(method = "<init>",at = @At("TAIL"))
    private void em$newTiers(Supplier tileEntityRegistrar, Supplier containerRegistrar, Machine.FactoryMachine origMachine, FactoryTier tier, CallbackInfo ci){
        if (tier.ordinal() == FactoryTier.ULTIMATE.ordinal()) {
            add(new AttributeUpgradeable(() -> EMBlocks.getFactory(EMFactoryTier.OVERCLOCKED, origMachine.getFactoryType())));
        }
        if (tier.ordinal() > FactoryTier.ULTIMATE.ordinal()) {
            if (tier.ordinal() == EMFactoryTier.OVERCLOCKED.ordinal()) {
                add(new AttributeUpgradeable(() -> EMBlocks.getFactory(EMFactoryTier.QUANTUM, origMachine.getFactoryType())));
            }
            if (tier.ordinal() == EMFactoryTier.QUANTUM.ordinal()) {
                add(new AttributeUpgradeable(() -> EMBlocks.getFactory(EMFactoryTier.DENSE, origMachine.getFactoryType())));
            }
            if (tier.ordinal() == EMFactoryTier.DENSE.ordinal()) {
                add(new AttributeUpgradeable(() -> EMBlocks.getFactory(EMFactoryTier.MULTIVERSAL, origMachine.getFactoryType())));
            }
            if (tier.ordinal() == EMFactoryTier.MULTIVERSAL.ordinal()) {
                add(new AttributeUpgradeable(() -> EMBlocks.getFactory(EMFactoryTier.CREATIVE, origMachine.getFactoryType())));
            }
        }
    }

}
