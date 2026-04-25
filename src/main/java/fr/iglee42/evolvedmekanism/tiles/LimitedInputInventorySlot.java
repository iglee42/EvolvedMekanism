package fr.iglee42.evolvedmekanism.tiles;

import java.util.Objects;
import java.util.function.Predicate;
import mekanism.api.IContentsListener;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.slot.BasicInventorySlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public class LimitedInputInventorySlot extends BasicInventorySlot {

    public static LimitedInputInventorySlot at(@Nullable IContentsListener listener, int x, int y) {
        return at(alwaysTrue, listener, x, y);
    }

    public static LimitedInputInventorySlot at(Predicate<@NotNull ItemStack> isItemValid, @Nullable IContentsListener listener, int x, int y) {
        return at(alwaysTrue, isItemValid, listener, x, y);
    }

    public static LimitedInputInventorySlot at(Predicate<@NotNull ItemStack> insertPredicate, Predicate<@NotNull ItemStack> isItemValid, @Nullable IContentsListener listener,
                                               int x, int y) {
        Objects.requireNonNull(insertPredicate, "Insertion check cannot be null");
        Objects.requireNonNull(isItemValid, "Item validity check cannot be null");
        return new LimitedInputInventorySlot(BasicInventorySlot.DEFAULT_LIMIT,insertPredicate, isItemValid, listener, x, y);
    }

    public static LimitedInputInventorySlot at(int limit,@Nullable IContentsListener listener, int x, int y) {
        return at(limit,alwaysTrue, listener, x, y);
    }

    public static LimitedInputInventorySlot at(int limit,Predicate<@NotNull ItemStack> isItemValid, @Nullable IContentsListener listener, int x, int y) {
        return at(limit,alwaysTrue, isItemValid, listener, x, y);
    }

    public static LimitedInputInventorySlot at(int limit,Predicate<@NotNull ItemStack> insertPredicate, Predicate<@NotNull ItemStack> isItemValid, @Nullable IContentsListener listener,
                                               int x, int y) {
        Objects.requireNonNull(insertPredicate, "Insertion check cannot be null");
        Objects.requireNonNull(isItemValid, "Item validity check cannot be null");
        return new LimitedInputInventorySlot(limit,insertPredicate, isItemValid, listener, x, y);
    }

    protected LimitedInputInventorySlot(int limit,Predicate<@NotNull ItemStack> insertPredicate, Predicate<@NotNull ItemStack> isItemValid, @Nullable IContentsListener listener, int x, int y) {
        super(limit,notExternal, (stack, automationType) -> insertPredicate.test(stack), isItemValid, listener, x, y);
        obeyStackLimit = false;
        setSlotType(ContainerSlotType.INPUT);
    }
}