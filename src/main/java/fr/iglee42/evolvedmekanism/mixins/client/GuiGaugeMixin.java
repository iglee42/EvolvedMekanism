package fr.iglee42.evolvedmekanism.mixins.client;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.GuiElement;
import mekanism.client.gui.element.gauge.GaugeInfo;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.gauge.GuiTankGauge;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.interfaces.ISideConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = GuiGauge.class, remap = false)
public abstract class GuiGaugeMixin {

    @Unique
    private static final ResourceLocation EVOLVEDMEKANISM$EXTRA_GAUGE = EvolvedMekanism.rl("gui/gauge/extra.png");

    @Redirect(method = "drawBackground", at = @At(value = "INVOKE", target = "Lmekanism/client/gui/element/gauge/GaugeInfo;getResourceLocation()Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation evolvedmekanism$extraGaugeTexture(GaugeInfo info) {
        if (evolvedmekanism$isExtraGauge(info) && Minecraft.getInstance().getResourceManager().getResource(EVOLVEDMEKANISM$EXTRA_GAUGE).isPresent()) {
            return EVOLVEDMEKANISM$EXTRA_GAUGE;
        }
        return info.getResourceLocation();
    }

    @Unique
    private boolean evolvedmekanism$isExtraGauge(GaugeInfo info) {
        if (info == GaugeInfo.YELLOW) {
            return true;
        }
        if (!((Object) this instanceof GuiTankGauge<?, ?> gauge)) {
            return false;
        }
        try {
            Object tank = gauge.getTank();
            if (tank == null || !(((GuiElement) (Object) this).gui() instanceof GuiMekanismTile<?, ?> gui)) {
                return false;
            }
            TileEntityMekanism tile = gui.getMenu().getTileEntity();
            return tile instanceof ISideConfiguration config && config.getActiveDataType(tank) == DataType.EXTRA;
        } catch (Exception e) {
            return false;
        }
    }
}
