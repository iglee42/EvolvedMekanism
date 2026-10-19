package fr.iglee42.evolvedmekanism.client.gui.scroll;

import com.mojang.datafixers.util.Pair;
import fr.iglee42.evolvedmekanism.utils.EMEnchantmentHelper;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.GuiElementHolder;
import mekanism.client.gui.element.scroll.GuiInstallableScrollList;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.jetbrains.annotations.Nullable;


public class GuiEnchantmentsScrollList extends GuiInstallableScrollList<Pair<Holder<Enchantment>, Integer>> {

    private static final ResourceLocation MODULE_SELECTION = MekanismUtils.getResource(ResourceType.GUI, "module_selection.png");

    private final Supplier<ItemStack> itemSupplier;
    private ItemStack currentItem = ItemStack.EMPTY;
    private final Map<Holder<Enchantment>, Integer> originalLevels = new HashMap<>();
    //Pending levels, only applied to the item when the player presses "set"
    private final Map<Holder<Enchantment>, Integer> pendingLevels = new HashMap<>();
    private final List<Pair<Holder<Enchantment>, Integer>> currentList = new ArrayList<>();
    @Nullable
    private Holder<Enchantment> selectedEnchantment;

    public GuiEnchantmentsScrollList(IGuiWrapper gui, int x, int y, int height, Supplier<ItemStack> itemSupplier) {
        super(gui, x, y, height, GuiElementHolder.HOLDER, GuiElementHolder.HOLDER_SIZE, MODULE_SELECTION, 112, 36);
        this.itemSupplier = itemSupplier;
        updateItemAndList(itemSupplier.get());
    }

    public void updateItemAndList(ItemStack stack) {
        currentItem = stack;
        originalLevels.clear();
        pendingLevels.clear();
        if (!stack.isEmpty() && Minecraft.getInstance().level != null) {
            Map<Holder<Enchantment>, Integer> levels = EMEnchantmentHelper.getEnchantmentsForItem(Minecraft.getInstance().level.registryAccess(), stack);
            originalLevels.putAll(levels);
            pendingLevels.putAll(levels);
        }
        if (selectedEnchantment != null && !pendingLevels.containsKey(selectedEnchantment)) {
            selectedEnchantment = null;
        }
        rebuild();
    }

    private void rebuild() {
        currentList.clear();
        pendingLevels.entrySet().stream()
                .sorted(Comparator.<Map.Entry<Holder<Enchantment>, Integer>>comparingInt(e -> e.getValue() > 0 ? 0 : (isBlocked(e.getKey()) ? 2 : 1))
                        .thenComparing(e -> e.getKey().value().description().getString()))
                .forEach(e -> currentList.add(Pair.of(e.getKey(), e.getValue())));
        selectedType = null;
        if (selectedEnchantment != null) {
            for (Pair<Holder<Enchantment>, Integer> pair : currentList) {
                if (pair.getFirst().equals(selectedEnchantment)) {
                    selectedType = pair;
                    break;
                }
            }
        }
    }

    private void recheckItem() {
        ItemStack stack = itemSupplier.get();
        if (!ItemStack.matches(currentItem, stack)) {
            updateItemAndList(stack.copy());
        }
    }

    public Map<Holder<Enchantment>, Integer> getPendingLevels() {
        return pendingLevels;
    }

    public boolean hasChanges() {
        return !pendingLevels.equals(originalLevels);
    }

    /** Total number of levels that will be added compared to the item's current enchantments. */
    public int getAddedLevels() {
        int added = 0;
        for (Map.Entry<Holder<Enchantment>, Integer> entry : pendingLevels.entrySet()) {
            added += Math.max(0, entry.getValue() - originalLevels.getOrDefault(entry.getKey(), 0));
        }
        return added;
    }

    /** An enchantment is blocked if it is currently disabled and incompatible with another enabled one. */
    private boolean isBlocked(Holder<Enchantment> enchantment) {
        if (pendingLevels.getOrDefault(enchantment, 0) > 0) {
            return false;
        }
        for (Map.Entry<Holder<Enchantment>, Integer> entry : pendingLevels.entrySet()) {
            if (entry.getValue() > 0 && !entry.getKey().equals(enchantment) && !Enchantment.areCompatible(enchantment, entry.getKey())) {
                return true;
            }
        }
        return false;
    }

    public boolean canChangeSelected(int delta) {
        if (selectedEnchantment == null) {
            return false;
        }
        int level = pendingLevels.getOrDefault(selectedEnchantment, 0);
        int newLevel = level + delta;
        if (newLevel < 0 || newLevel > selectedEnchantment.value().getMaxLevel()) {
            return false;
        }
        return delta <= 0 || level > 0 || !isBlocked(selectedEnchantment);
    }

    /** Moves one level in the given direction, or straight to the max/min level when ull is set. */
    public void stepSelected(int direction, boolean full) {
        if (selectedEnchantment == null) {
            return;
        }
        int level = pendingLevels.getOrDefault(selectedEnchantment, 0);
        int delta = full ? (direction > 0 ? selectedEnchantment.value().getMaxLevel() - level : -level) : direction;
        if (delta != 0) {
            changeSelected(delta);
        }
    }

    public void changeSelected(int delta) {
        if (canChangeSelected(delta)) {
            pendingLevels.merge(selectedEnchantment, delta, Integer::sum);
            rebuild();
        }
    }

    @Override
    protected void setSelected(@Nullable Pair<Holder<Enchantment>, Integer> newData) {
        selectedEnchantment = newData == null ? null : newData.getFirst();
        selectedType = newData;
    }

    @Override
    protected List<Pair<Holder<Enchantment>, Integer>> getCurrentInstalled() {
        return currentList;
    }

    @Override
    protected void drawName(GuiGraphics guiGraphics, Pair<Holder<Enchantment>, Integer> enchantment, int y) {
        boolean enabled = enchantment.getSecond() > 0;
        boolean blocked = isBlocked(enchantment.getFirst());
        int color = blocked ? 0x5E1D1D : (enabled ? titleTextColor() : 0x7F7F7F);
        Component name = enabled ? Enchantment.getFullname(enchantment.getFirst(), enchantment.getSecond()).copy().withStyle(ChatFormatting.RESET)
                : enchantment.getFirst().value().description().copy().withStyle(ChatFormatting.RESET);
        drawNameText(guiGraphics, y, name, color, 0.7F);
    }

    @Override
    protected ItemStack getRenderStack(Pair<Holder<Enchantment>, Integer> enchantment) {
        return EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment.getFirst(), Math.max(1, enchantment.getSecond())));
    }

    @Override
    public void renderForeground(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        recheckItem();
        super.renderForeground(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void updateTooltip(int mouseX, int mouseY) {
    }
}
