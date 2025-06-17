package fr.iglee42.evolvedmekanism.registries;

import java.util.function.UnaryOperator;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.common.ChemicalConstants;
import mekanism.common.Mekanism;
import mekanism.common.item.ItemNutritionalPasteBucket;
import mekanism.common.registration.impl.FluidDeferredRegister;
import mekanism.common.registration.impl.FluidDeferredRegister.MekanismFluidType;
import mekanism.common.registration.impl.FluidRegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.fluids.ForgeFlowingFluid.Flowing;
import net.minecraftforge.fluids.ForgeFlowingFluid.Source;

public class EMFluids {

    private EMFluids() {
    }

    public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(EvolvedMekanism.MODID);


    public static final FluidRegistryObject<MekanismFluidType, Source, Flowing, LiquidBlock, BucketItem> MOLTEN_COAL = registerMolten("coal",0x000000);


    private static FluidRegistryObject<MekanismFluidType, Source, Flowing, LiquidBlock, BucketItem> registerMolten(String name,int color){
        return FLUIDS.register("molten_" + name, properties -> properties.tint(color).texture(EvolvedMekanism.rl("block/molten_still"),EvolvedMekanism.rl("block/molten_flowing")));
    }
}