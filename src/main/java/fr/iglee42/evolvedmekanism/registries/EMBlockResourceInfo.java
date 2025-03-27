package fr.iglee42.evolvedmekanism.registries;

import mekanism.common.resource.IResource;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;

public enum EMBlockResourceInfo implements IResource {
    BETTER_GOLD("better_gold", 5, 9, MapColor.COLOR_YELLOW),
    PLASLITHERITE("plaslitherite", 25, 2_400, MapColor.SNOW, NoteBlockInstrument.BASEDRUM, -1, 0, false,false,PushReaction.NORMAL),

    ;
    private final String registrySuffix;
    private final MapColor mapColor;
    private final PushReaction pushReaction;
    private final boolean portalFrame;
    private final boolean burnsInFire;
    private final NoteBlockInstrument instrument;
    private final float resistance;
    private final float hardness;
    private final int burnTime;
    //Number between 0 and 15
    private final int lightValue;

    EMBlockResourceInfo(String registrySuffix, float hardness, float resistance, MapColor mapColor) {
        this(registrySuffix, hardness, resistance, mapColor, null);
    }

    EMBlockResourceInfo(String registrySuffix, float hardness, float resistance, MapColor mapColor, @Nullable NoteBlockInstrument instrument) {
        this(registrySuffix, hardness, resistance, mapColor, instrument, -1);
    }

    EMBlockResourceInfo(String registrySuffix, float hardness, float resistance, MapColor mapColor, @Nullable NoteBlockInstrument instrument, int burnTime) {
        this(registrySuffix, hardness, resistance, mapColor, instrument, burnTime, 0);
    }

    EMBlockResourceInfo(String registrySuffix, float hardness, float resistance, MapColor mapColor, @Nullable NoteBlockInstrument instrument, int burnTime, int lightValue) {
        this(registrySuffix, hardness, resistance, mapColor, instrument, burnTime, lightValue, true, false, PushReaction.NORMAL);
    }

    EMBlockResourceInfo(String registrySuffix, float hardness, float resistance, MapColor mapColor, @Nullable NoteBlockInstrument instrument, int burnTime, int lightValue,
                        boolean burnsInFire, boolean portalFrame, PushReaction pushReaction) {
        this.registrySuffix = registrySuffix;
        this.pushReaction = pushReaction;
        this.portalFrame = portalFrame;
        this.burnsInFire = burnsInFire;
        this.burnTime = burnTime;
        this.lightValue = lightValue;
        this.resistance = resistance;
        this.hardness = hardness;
        this.instrument = instrument;
        this.mapColor = mapColor;
    }

    @Override
    public String getRegistrySuffix() {
        return registrySuffix;
    }

    public boolean isPortalFrame() {
        return portalFrame;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public boolean burnsInFire() {
        return burnsInFire;
    }

    public MapColor getMapColor() {
        return mapColor;
    }

    public BlockBehaviour.Properties modifyProperties(BlockBehaviour.Properties properties) {
        if (instrument != null) {
            properties.instrument(instrument);
        }
        return properties.mapColor(mapColor).strength(hardness, resistance).lightLevel(state -> lightValue).pushReaction(pushReaction);
    }
}