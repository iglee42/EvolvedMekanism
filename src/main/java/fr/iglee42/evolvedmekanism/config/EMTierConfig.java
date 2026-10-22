package fr.iglee42.evolvedmekanism.config;

import fr.iglee42.evolvedmekanism.tiers.cable.EMCableTier;
import fr.iglee42.evolvedmekanism.tiers.cable.EMConductorTier;
import fr.iglee42.evolvedmekanism.tiers.cable.EMPipeTier;
import fr.iglee42.evolvedmekanism.tiers.cable.EMTransporterTier;
import fr.iglee42.evolvedmekanism.tiers.cable.EMTubeTier;
import fr.iglee42.evolvedmekanism.tiers.storage.EMBinTier;
import fr.iglee42.evolvedmekanism.tiers.storage.EMChemicalTankTier;
import fr.iglee42.evolvedmekanism.tiers.storage.EMEnergyCubeTier;
import fr.iglee42.evolvedmekanism.tiers.storage.EMFluidTankTier;
import fr.iglee42.evolvedmekanism.tiers.storage.EMInductionCellTier;
import fr.iglee42.evolvedmekanism.tiers.storage.EMInductionProviderTier;
import mekanism.api.math.FloatingLong;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedDoubleValue;
import mekanism.common.config.value.CachedFloatingLongValue;
import mekanism.common.config.value.CachedIntValue;
import mekanism.common.config.value.CachedLongValue;
import mekanism.common.tier.BinTier;
import mekanism.common.tier.CableTier;
import mekanism.common.tier.ChemicalTankTier;
import mekanism.common.tier.ConductorTier;
import mekanism.common.tier.EnergyCubeTier;
import mekanism.common.tier.FluidTankTier;
import mekanism.common.tier.InductionCellTier;
import mekanism.common.tier.InductionProviderTier;
import mekanism.common.tier.PipeTier;
import mekanism.common.tier.TransporterTier;
import mekanism.common.tier.TubeTier;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Locale;

public class EMTierConfig extends BaseMekanismConfig {

    private final ForgeConfigSpec configSpec;

    EMTierConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        addEnergyCubeCategory(builder);
        addFluidTankCategory(builder);
        addChemicalTankCategory(builder);
        addBinCategory(builder);
        addInductionCategory(builder);
        addTransmittersCategory(builder);
        configSpec = builder.build();
    }

    private void addEnergyCubeCategory(ForgeConfigSpec.Builder builder) {
        builder.comment("Evolved Mekanism energy cube settings").push("energy_cubes");
        for (EnergyCubeTier tier : new EnergyCubeTier[]{EMEnergyCubeTier.OVERCLOCKED, EMEnergyCubeTier.QUANTUM, EMEnergyCubeTier.DENSE, EMEnergyCubeTier.MULTIVERSAL}) {
            String tierName = tierName(tier);
            CachedFloatingLongValue capacity = energyValue(builder, tierName + "Capacity", tier.getBaseMaxEnergy(), "Maximum energy stored by this tier.");
            CachedFloatingLongValue output = energyValue(builder, tierName + "Output", tier.getBaseOutput(), "Maximum energy output per tick for this tier.");
            tier.setConfigReference(capacity, output);
        }
        builder.pop();
    }

    private void addFluidTankCategory(ForgeConfigSpec.Builder builder) {
        builder.comment("Evolved Mekanism fluid tank settings").push("fluid_tanks");
        for (FluidTankTier tier : new FluidTankTier[]{EMFluidTankTier.OVERCLOCKED, EMFluidTankTier.QUANTUM, EMFluidTankTier.DENSE, EMFluidTankTier.MULTIVERSAL}) {
            String tierName = tierName(tier);
            CachedIntValue capacity = intValue(builder, tierName + "Capacity", tier.getBaseStorage(), "Maximum fluid stored by this tier.");
            CachedIntValue output = intValue(builder, tierName + "Output", tier.getBaseOutput(), "Maximum fluid output per tick for this tier.");
            tier.setConfigReference(capacity, output);
        }
        builder.pop();
    }

    private void addChemicalTankCategory(ForgeConfigSpec.Builder builder) {
        builder.comment("Evolved Mekanism chemical tank settings").push("chemical_tanks");
        for (ChemicalTankTier tier : new ChemicalTankTier[]{EMChemicalTankTier.OVERCLOCKED, EMChemicalTankTier.QUANTUM, EMChemicalTankTier.DENSE, EMChemicalTankTier.MULTIVERSAL}) {
            String tierName = tierName(tier);
            CachedLongValue capacity = longValue(builder, tierName + "Capacity", tier.getBaseStorage(), "Maximum chemical stored by this tier.");
            CachedLongValue output = longValue(builder, tierName + "Output", tier.getBaseOutput(), "Maximum chemical output per tick for this tier.");
            tier.setConfigReference(capacity, output);
        }
        builder.pop();
    }

    private void addBinCategory(ForgeConfigSpec.Builder builder) {
        builder.comment("Evolved Mekanism bin settings").push("bins");
        for (BinTier tier : new BinTier[]{EMBinTier.OVERCLOCKED, EMBinTier.QUANTUM, EMBinTier.DENSE, EMBinTier.MULTIVERSAL}) {
            tier.setConfigReference(intValue(builder, tierName(tier) + "Capacity", tier.getBaseStorage(), "Maximum item count stored by this tier."));
        }
        builder.pop();
    }

    private void addInductionCategory(ForgeConfigSpec.Builder builder) {
        builder.comment("Evolved Mekanism induction matrix settings").push("induction");
        for (InductionCellTier tier : new InductionCellTier[]{EMInductionCellTier.OVERCLOCKED, EMInductionCellTier.QUANTUM, EMInductionCellTier.DENSE, EMInductionCellTier.MULTIVERSAL, EMInductionCellTier.CREATIVE}) {
            tier.setConfigReference(energyValue(builder, "cell" + capitalize(tierName(tier)) + "Capacity", tier.getBaseMaxEnergy(), "Maximum energy stored by this induction cell tier."));
        }
        for (InductionProviderTier tier : new InductionProviderTier[]{EMInductionProviderTier.OVERCLOCKED, EMInductionProviderTier.QUANTUM, EMInductionProviderTier.DENSE, EMInductionProviderTier.MULTIVERSAL, EMInductionProviderTier.CREATIVE}) {
            tier.setConfigReference(energyValue(builder, "provider" + capitalize(tierName(tier)) + "Output", tier.getBaseOutput(), "Maximum energy output per tick for this induction provider tier."));
        }
        builder.pop();
    }

    private void addTransmittersCategory(ForgeConfigSpec.Builder builder) {
        builder.comment("Evolved Mekanism transmitter settings").push("transmitters");
        builder.comment("Energy cable settings").push("energy");
        for (CableTier tier : new CableTier[]{EMCableTier.OVERCLOCKED, EMCableTier.QUANTUM, EMCableTier.DENSE, EMCableTier.MULTIVERSAL, EMCableTier.CREATIVE}) {
            tier.setConfigReference(energyValue(builder, tierName(tier) + "Capacity", tier.getBaseCapacity(), "Maximum energy transferred per tick by this cable tier."));
        }
        builder.pop();

        builder.comment("Fluid pipe settings").push("fluid");
        for (PipeTier tier : new PipeTier[]{EMPipeTier.OVERCLOCKED, EMPipeTier.QUANTUM, EMPipeTier.DENSE, EMPipeTier.MULTIVERSAL, EMPipeTier.CREATIVE}) {
            String tierName = tierName(tier);
            CachedIntValue capacity = intValue(builder, tierName + "Capacity", tier.getBaseCapacity(), "Maximum fluid stored by this pipe tier.");
            CachedIntValue pull = intValue(builder, tierName + "PullAmount", tier.getBasePull(), "Maximum fluid pulled per operation by this pipe tier.");
            tier.setConfigReference(capacity, pull);
        }
        builder.pop();

        builder.comment("Chemical tube settings").push("chemical");
        for (TubeTier tier : new TubeTier[]{EMTubeTier.OVERCLOCKED, EMTubeTier.QUANTUM, EMTubeTier.DENSE, EMTubeTier.MULTIVERSAL, EMTubeTier.CREATIVE}) {
            String tierName = tierName(tier);
            CachedLongValue capacity = longValue(builder, tierName + "Capacity", tier.getBaseCapacity(), "Maximum chemical stored by this tube tier.");
            CachedLongValue pull = longValue(builder, tierName + "PullAmount", tier.getBasePull(), "Maximum chemical pulled per operation by this tube tier.");
            tier.setConfigReference(capacity, pull);
        }
        builder.pop();

        builder.comment("Logistical transporter settings").push("items");
        for (TransporterTier tier : new TransporterTier[]{EMTransporterTier.OVERCLOCKED, EMTransporterTier.QUANTUM, EMTransporterTier.DENSE, EMTransporterTier.MULTIVERSAL, EMTransporterTier.CREATIVE}) {
            String tierName = tierName(tier);
            CachedIntValue pull = intValue(builder, tierName + "PullAmount", tier.getBasePull(), "Maximum items pulled per operation by this transporter tier.");
            CachedIntValue speed = intValue(builder, tierName + "Speed", tier.getBaseSpeed(), "Transport speed for this transporter tier.");
            tier.setConfigReference(pull, speed);
        }
        builder.pop();

        builder.comment("Thermodynamic conductor settings").push("heat");
        for (ConductorTier tier : new ConductorTier[]{EMConductorTier.OVERCLOCKED, EMConductorTier.QUANTUM, EMConductorTier.DENSE, EMConductorTier.MULTIVERSAL, EMConductorTier.CREATIVE}) {
            String tierName = tierName(tier);
            CachedDoubleValue conduction = doubleValue(builder, tierName + "InverseConduction", tier.getBaseConduction(), 1, "Inverse heat conduction for this conductor tier.");
            CachedDoubleValue capacity = doubleValue(builder, tierName + "HeatCapacity", tier.getBaseHeatCapacity(), 1, "Heat capacity for this conductor tier.");
            CachedDoubleValue insulation = doubleValue(builder, tierName + "Insulation", tier.getBaseConductionInsulation(), 0, "Heat conduction insulation for this conductor tier.");
            tier.setConfigReference(conduction, capacity, insulation);
        }
        builder.pop();
        builder.pop();
    }

    private CachedFloatingLongValue energyValue(ForgeConfigSpec.Builder builder, String name, FloatingLong defaultValue, String comment) {
        return CachedFloatingLongValue.define(this, builder, comment, name, defaultValue);
    }

    private CachedLongValue longValue(ForgeConfigSpec.Builder builder, String name, long defaultValue, String comment) {
        return CachedLongValue.wrap(this, builder.comment(comment).defineInRange(name, defaultValue, 1, Long.MAX_VALUE));
    }

    private CachedIntValue intValue(ForgeConfigSpec.Builder builder, String name, int defaultValue, String comment) {
        return CachedIntValue.wrap(this, builder.comment(comment).defineInRange(name, defaultValue, 1, Integer.MAX_VALUE));
    }

    private CachedDoubleValue doubleValue(ForgeConfigSpec.Builder builder, String name, double defaultValue, double minimum, String comment) {
        return CachedDoubleValue.wrap(this, builder.comment(comment).defineInRange(name, defaultValue, minimum, Double.MAX_VALUE));
    }

    private static String tierName(Object tier) {
        return tier.toString().toLowerCase(Locale.ROOT);
    }

    private static String capitalize(String value) {
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    @Override
    public String getFileName() {
        return "tiers";
    }

    @Override
    public ForgeConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public ModConfig.Type getConfigType() {
        return ModConfig.Type.SERVER;
    }
}
