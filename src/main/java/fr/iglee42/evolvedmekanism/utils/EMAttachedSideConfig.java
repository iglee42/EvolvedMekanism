package fr.iglee42.evolvedmekanism.utils;

import mekanism.api.RelativeSide;
import mekanism.common.attachments.component.AttachedSideConfig;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.util.EnumUtils;
import net.minecraft.Util;

import java.util.EnumMap;
import java.util.Map;

public class EMAttachedSideConfig {
    public static final AttachedSideConfig CHEMIXER_MACHINE = Util.make(() -> {
        Map<TransmissionType, AttachedSideConfig.LightConfigInfo> configInfo = new EnumMap<>(TransmissionType.class);
        configInfo.put(TransmissionType.ITEM, AttachedSideConfig.LightConfigInfo.EXTRA_MACHINE);
        configInfo.put(TransmissionType.CHEMICAL,AttachedSideConfig.LightConfigInfo.INPUT_ONLY);
        configInfo.put(TransmissionType.ENERGY, AttachedSideConfig.LightConfigInfo.INPUT_ONLY);
        return new AttachedSideConfig(configInfo);
    });

    public static final AttachedSideConfig MELTER_MACHINE = Util.make(() -> {
        Map<TransmissionType, AttachedSideConfig.LightConfigInfo> configInfo = new EnumMap<>(TransmissionType.class);
        configInfo.put(TransmissionType.ITEM, AttachedSideConfig.LightConfigInfo.MACHINE);
        configInfo.put(TransmissionType.FLUID, AttachedSideConfig.LightConfigInfo.RIGHT_OUTPUT);
        configInfo.put(TransmissionType.HEAT,AttachedSideConfig.LightConfigInfo.INPUT_ONLY);
        return new AttachedSideConfig(configInfo);
    });

    public static final AttachedSideConfig SOLIDIFICATION_MACHINE = Util.make(() -> {
        Map<TransmissionType, AttachedSideConfig.LightConfigInfo> configInfo = new EnumMap<>(TransmissionType.class);
        configInfo.put(TransmissionType.ITEM, AttachedSideConfig.LightConfigInfo.MACHINE);
        configInfo.put(TransmissionType.FLUID,AttachedSideConfig.LightConfigInfo.TWO_INPUT_AND_OUT);
        configInfo.put(TransmissionType.ENERGY, AttachedSideConfig.LightConfigInfo.INPUT_ONLY);
        return new AttachedSideConfig(configInfo);
    });



}
