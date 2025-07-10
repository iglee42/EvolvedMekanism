package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.loot.PersonalTieredStorageContentsLootFunction;
import mekanism.common.registration.MekanismDeferredHolder;
import mekanism.common.registration.impl.LootFunctionDeferredRegister;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class EMLootFunctions {
    public static final LootFunctionDeferredRegister REGISTER = new LootFunctionDeferredRegister(EvolvedMekanism.MODID);

    public static final MekanismDeferredHolder<LootItemFunctionType<?>,LootItemFunctionType<PersonalTieredStorageContentsLootFunction>> TIERED_PERSONAL_STORAGE_LOOT_FUNC = REGISTER.registerBasic("tiered_personal_storage_contents", ()->PersonalTieredStorageContentsLootFunction.INSTANCE);
}