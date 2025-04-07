package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.loot.PersonalTieredStorageContentsLootFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class EMLootFunctions {
    public static final DeferredRegister<LootItemFunctionType> REGISTER = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, EvolvedMekanism.MODID);

    public static final RegistryObject<LootItemFunctionType> TIERED_PERSONAL_STORAGE_LOOT_FUNC = REGISTER.register("tiered_personal_storage_contents", ()->new LootItemFunctionType(new PersonalTieredStorageContentsLootFunction.PersonalStorageLootFunctionSerializer()));
}