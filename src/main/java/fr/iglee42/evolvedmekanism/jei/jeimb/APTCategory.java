package fr.iglee42.evolvedmekanism.jei.jeimb;

import java.util.function.Consumer;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.config.EMConfig;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import giselle.jei_mekanism_multiblocks.client.gui.IntSliderWithButtons;
import giselle.jei_mekanism_multiblocks.client.jei.MultiblockCategory;
import giselle.jei_mekanism_multiblocks.client.jei.MultiblockWidget;
import giselle.jei_mekanism_multiblocks.client.jei.ResultWidget;
import giselle.jei_mekanism_multiblocks.client.jei.category.ICostConsumer;
import giselle.jei_mekanism_multiblocks.common.util.VolumeTextHelper;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.util.text.EnergyDisplay;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class APTCategory extends MultiblockCategory<APTCategory.APTWidget> {

    public APTCategory(IGuiHelper helper) {
        super(helper, EvolvedMekanism.rl("apt"), APTWidget.class, EvolvedMekanismLang.APT.translate(), EMBlocks.APT_PORT.getItemStack());
    }

    @Override
    protected void getRecipeCatalystItemStacks(Consumer<ItemStack> consumer) {
        super.getRecipeCatalystItemStacks(consumer);
        consumer.accept(EMBlocks.APT_CASING.getItemStack());
        consumer.accept(EMBlocks.APT_PORT.getItemStack());
        consumer.accept(EMBlocks.SUPERCHARGING_ELEMENT.getItemStack());
        consumer.accept(MekanismBlocks.STRUCTURAL_GLASS.getItemStack());
    }

    public static class APTWidget extends MultiblockWidget {

        protected IntSliderWithButtons portsWidget;
        protected IntSliderWithButtons superchargingElementsWidget;

        public APTWidget() {

        }

        @Override
        protected void collectOtherConfigs(Consumer<AbstractWidget> consumer) {
            super.collectOtherConfigs(consumer);
            consumer.accept(this.portsWidget = new IntSliderWithButtons(0, 0, 0, 0, "text.jei_mekanism_multiblocks.specs.ports", 0, 2, this.getSideBlocks()));
            this.portsWidget.getSlider().addValueChangeHanlder(this::onPortsChanged);
            consumer.accept(this.superchargingElementsWidget = new IntSliderWithButtons(0, 0, 0, 0, "text.jei_mekanism_multiblocks.result.evolvedmekanism.supercharging_elements", 0, 0, 25));
            this.superchargingElementsWidget.getSlider().addValueChangeHanlder(this::onSuperchargingElementsChanged);
        }

        @Override
        protected void collectCost(ICostConsumer consumer) {
            super.collectCost(consumer);
            int corners = this.getCornerBlocks();
            int ports = this.getPortCount();
            int sides = this.getSideBlocks() - ports;
            int superchargingElements = this.getSuperchargingElementsCount();
            int casing = 0;
            int glasses = 0;
            if (this.isUseGlass()) {
                casing = corners;
                glasses = sides;
            } else {
                casing = corners + sides;
                glasses = 0;
            }
            consumer.accept(new ItemStack(EMBlocks.APT_CASING, casing));
            consumer.accept(new ItemStack(EMBlocks.APT_PORT, ports));
            if (superchargingElements > 0) {
                consumer.accept(new ItemStack(EMBlocks.SUPERCHARGING_ELEMENT, superchargingElements));
            }
            consumer.accept(new ItemStack(this.getGlassBlock(), glasses));
        }

        @Override
        protected void collectResult(Consumer<AbstractWidget> consumer) {
            super.collectResult(consumer);
            consumer.accept(new ResultWidget(Component.translatable("text.jei_mekanism_multiblocks.result.evolvedmekanism.processing_speed"), Component.literal("x" + (this.getSuperchargingElementsCount() + 1))));
            consumer.accept(new ResultWidget(Component.translatable("text.jei_mekanism_multiblocks.result.energy_capacity"), EnergyDisplay.of(EMConfig.general.aptEnergyStorage.get()).getTextComponent()));
            consumer.accept(new ResultWidget(Component.translatable("text.jei_mekanism_multiblocks.result.input_tank"), VolumeTextHelper.formatMB(EMConfig.general.aptInputStorage.get())));
        }

        protected void onPortsChanged(int ports) {
            this.markNeedUpdate();
        }

        protected void onSuperchargingElementsChanged(int superchargingElements) {
            this.markNeedUpdate();
        }

        @Override
        public int getCornerBlocks() {
            return 73;
        }

        @Override
        public int getSideBlocks() {
            return 65;
        }

        public int getPortCount() {
            return this.portsWidget.getSlider().getValue();
        }

        public void setPortCount(int portCount) {
            this.portsWidget.getSlider().setValue(portCount);
        }

        public int getSuperchargingElementsCount() {
            return this.superchargingElementsWidget.getSlider().getValue();
        }

        public void setSuperchargingElementsCount(int superchargingElements) {
            this.superchargingElementsWidget.getSlider().setValue(superchargingElements);
        }

        @Override
        public int getDimensionWidthMin() {
            return 7;
        }

        @Override
        public int getDimensionWidthMax() {
            return 7;
        }

        @Override
        public int getDimensionLengthMin() {
            return 7;
        }

        @Override
        public int getDimensionLengthMax() {
            return 7;
        }

        @Override
        public int getDimensionHeightMin() {
            return 5;
        }

        @Override
        public int getDimensionHeightMax() {
            return 5;
        }

        @Override
        public Block getGlassBlock() {
            return MekanismBlocks.STRUCTURAL_GLASS.getBlock();
        }
    }
}
