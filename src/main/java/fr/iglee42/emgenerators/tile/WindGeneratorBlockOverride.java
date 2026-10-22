package fr.iglee42.emgenerators.tile;

import mekanism.common.registration.impl.BlockRegistryObject;

/**
 * TileEntityWindGenerator always constructs itself against Mekanism's wind generator block.
 * The tiered tiles set this for the duration of their constructor so a mixin can pass our block instead.
 */
public final class WindGeneratorBlockOverride {

    private static final ThreadLocal<BlockRegistryObject<?, ?>> CURRENT = new ThreadLocal<>();

    private WindGeneratorBlockOverride() {
    }

    public static void push(BlockRegistryObject<?, ?> block) {
        CURRENT.set(block);
    }

    public static void pop() {
        CURRENT.remove();
    }

    public static BlockRegistryObject<?, ?> current() {
        return CURRENT.get();
    }
}
