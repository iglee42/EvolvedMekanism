package fr.iglee42.evolvedmekanism.registries;

import mekanism.common.base.IChemicalConstant;

public enum EMChemicalConstants implements IChemicalConstant {
    NITROGEN("nitrogen", 0xFF87C4D3, 0, 77.36F, 807),
    CRYONOCTIS("cryonoctis", 0xFFFFAAD5, 0, 77.36F, 807);

    private final String name;
    private final int color;
    private final int lightLevel;
    private final float temperature;
    private final float density;

    EMChemicalConstants(String name, int color, int lightLevel, float temperature, float density) {
        this.name = name;
        this.color = color;
        this.lightLevel = lightLevel;
        this.temperature = temperature;
        this.density = density;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public float getTemperature() {
        return temperature;
    }

    @Override
    public float getDensity() {
        return density;
    }

    @Override
    public int getLightLevel() {
        return lightLevel;
    }
}
