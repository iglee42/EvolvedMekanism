package fr.iglee42.evolvedmekanism.client.gui;

import java.util.ArrayList;
import java.util.List;

import fr.iglee42.evolvedmekanism.client.bars.GuiCustomDynamicHorizontalRateBar;
import fr.iglee42.evolvedmekanism.config.EMConfig;
import fr.iglee42.evolvedmekanism.jei.EMJEI;
import fr.iglee42.evolvedmekanism.multiblock.apt.APTMultiblockData;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTCasing;
import mekanism.api.math.FloatingLong;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.bar.GuiBar.IBarInfoHandler;
import mekanism.client.gui.element.bar.GuiDynamicHorizontalRateBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.MekanismLang;
import mekanism.common.content.sps.SPSMultiblockData;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.lib.Color;
import mekanism.common.lib.Color.ColorFunction;
import mekanism.common.util.text.EnergyDisplay;
import mekanism.common.util.text.TextUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GuiAPT extends GuiMekanismTile<TileEntityAPTCasing, MekanismTileContainer<TileEntityAPTCasing>> {

    public GuiAPT(MekanismTileContainer<TileEntityAPTCasing> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
        imageHeight += 16;
        inventoryLabelY = imageHeight - 92;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiEnergyTab(this, ()->{
            FloatingLong using = tile.getMultiblock().progress > 0 ? EMConfig.general.aptEnergyConsumption.getOrDefault() : FloatingLong.ZERO;
            return List.of(MekanismLang.USING.translate(EnergyDisplay.of(using)));
        }));
        addRenderableWidget(new GuiGasGauge(() -> tile.getMultiblock().inputTank, () -> tile.getMultiblock().getGasTanks(null), GaugeType.STANDARD, this, 7, 17));
        addRenderableWidget(new GuiCustomDynamicHorizontalRateBar(this, new IBarInfoHandler() {
            @Override
            public Component getTooltip() {
                return MekanismLang.PROGRESS.translate(TextUtils.getPercent(Math.max(0, tile.getMultiblock().getScaledProgress())));
            }

            @Override
            public double getLevel() {
                return Math.max(0.0D, tile.getMultiblock().getScaledProgress());
            }
        }, 7, 79, 160, progress->tile.getMultiblock().getColor()));

        addRenderableWidget(new GuiEnergyGauge(tile.getMultiblock().energyContainer, GaugeType.STANDARD, this, 151, 17));
        addRenderableWidget(new GuiInnerScreen(this, 47, 17, 82, 60, () -> {
            List<Component> list = new ArrayList<>();
            APTMultiblockData multiblock = tile.getMultiblock();
            boolean active = multiblock.progress > 0;
            list.add(MekanismLang.STATUS.translate(active ? MekanismLang.ACTIVE : MekanismLang.IDLE));
            if (active) {
                list.add(MekanismLang.USING.translate(EnergyDisplay.of(EMConfig.general.aptEnergyConsumption.getOrDefault())));
            }
            return list;
        }).jeiCategories(EMJEI.APT));

    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}
