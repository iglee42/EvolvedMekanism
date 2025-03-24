package fr.iglee42.evolvedmekanism.mixins;

import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.*;
import mekanism.common.content.blocktype.BlockShapes;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.registries.MekanismBlockTypes;
import mekanism.common.registries.MekanismContainerTypes;
import mekanism.common.tier.*;
import mekanism.common.tile.TileEntityBin;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.function.Supplier;

@Mixin(value = MekanismBlockTypes.class,remap = false)
public class MekanismBlockTypesMixin {


    @Inject(method = "createBin",at = @At("HEAD"), cancellable = true)
    private static  <TILE extends TileEntityBin> void evolvedmekanism$addBinUpgrade(BinTier tier, Supplier<TileEntityTypeRegistryObject<TILE>> tile, Supplier<BlockRegistryObject<?, ?>> upgradeBlock, CallbackInfoReturnable<Machine<TILE>> cir){
        if (tier.equals(BinTier.ULTIMATE)){
            cir.setReturnValue(Machine.MachineBuilder.createMachine(tile, MekanismLang.DESCRIPTION_BIN)
                    .with(new AttributeTier<>(tier), new AttributeUpgradeable(()->EMBlocks.OVERCLOCKED_BIN))
                    .without(AttributeParticleFX.class, Attributes.AttributeSecurity.class, AttributeUpgradeSupport.class, Attributes.AttributeRedstone.class)
                    .withComputerSupport(tier, "Bin")
                    .build());
        }
    }

    @Inject(method = "createEnergyCube",at = @At("HEAD"), cancellable = true)
    private static  <TILE extends TileEntityBin> void evolvedmekanism$addEnergyCubeUpgrade(EnergyCubeTier tier, Supplier<TileEntityTypeRegistryObject<TILE>> tile, Supplier<BlockRegistryObject<?, ?>> upgradeBlock, CallbackInfoReturnable<Machine<TILE>> cir){
        if (tier.equals(EnergyCubeTier.ULTIMATE)){
            cir.setReturnValue(Machine.MachineBuilder.createMachine(tile, MekanismLang.DESCRIPTION_ENERGY_CUBE)
                    .withGui(() -> MekanismContainerTypes.ENERGY_CUBE)
                    .withEnergyConfig(tier::getMaxEnergy)
                    .with(new AttributeTier<>(tier), new AttributeUpgradeable(()->EMBlocks.OVERCLOCKED_ENERGY_CUBE), new AttributeStateFacing(BlockStateProperties.FACING))
                    .without(AttributeParticleFX.class, AttributeStateActive.class, AttributeUpgradeSupport.class)
                    .withComputerSupport(tier, "EnergyCube")
                    .build());
        }
    }

    @Inject(method = "createChemicalTank",at = @At("HEAD"), cancellable = true)
    private static <TILE extends TileEntityBin> void evolvedmekanism$addChemicalTankUpgrade(ChemicalTankTier tier, Supplier<TileEntityTypeRegistryObject<TILE>> tile, Supplier<BlockRegistryObject<?, ?>> upgradeBlock, CallbackInfoReturnable<Machine<TILE>> cir){
        if (tier.equals(ChemicalTankTier.ULTIMATE)){
            cir.setReturnValue(Machine.MachineBuilder.createMachine(tile, MekanismLang.DESCRIPTION_CHEMICAL_TANK)
                    .withGui(() -> MekanismContainerTypes.CHEMICAL_TANK)
                    .withCustomShape(BlockShapes.CHEMICAL_TANK)
                    .with(new AttributeTier<>(tier), new AttributeUpgradeable(()->EMBlocks.OVERCLOCKED_CHEMICAL_TANK))
                    .without(AttributeParticleFX.class, AttributeStateActive.class, AttributeUpgradeSupport.class)
                    .withComputerSupport(tier, "ChemicalTank")
                    .build());
        }
    }

    @Inject(method = "createFluidTank",at = @At("HEAD"), cancellable = true)
    private static <TILE extends TileEntityBin> void evolvedmekanism$addFluidTankUpgrade(FluidTankTier tier, Supplier<TileEntityTypeRegistryObject<TILE>> tile, Supplier<BlockRegistryObject<?, ?>> upgradeBlock, CallbackInfoReturnable<Machine<TILE>> cir){
        if (tier.equals(FluidTankTier.ULTIMATE)){
            cir.setReturnValue(Machine.MachineBuilder.createMachine(tile, MekanismLang.DESCRIPTION_FLUID_TANK)
                    .withGui(() -> MekanismContainerTypes.FLUID_TANK)
                    .withCustomShape(BlockShapes.FLUID_TANK)
                    .with(new AttributeTier<>(tier), new AttributeUpgradeable(()->EMBlocks.OVERCLOCKED_FLUID_TANK))
                    .without(AttributeParticleFX.class, AttributeStateFacing.class, Attributes.AttributeRedstone.class, AttributeUpgradeSupport.class)
                    .withComputerSupport(tier, "FluidTank")
                    .build());
        }
    }




}
