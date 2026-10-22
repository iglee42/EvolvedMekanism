package fr.iglee42.evolvedmekanism.mixins.client;

import mekanism.client.gui.element.gauge.GuiTankGauge;
import mekanism.client.gui.element.gauge.GuiTankGauge.ITankInfoHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = GuiTankGauge.class, remap = false)
public interface GuiTankGaugeAccessor {

    @Accessor(value = "infoHandler", remap = false)
    ITankInfoHandler<?> evolvedmekanism$getInfoHandler();
}
