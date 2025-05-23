package fr.iglee42.evolvedmekanism.modules;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMModules;
import mekanism.api.annotations.ParametersAreNotNullByDefault;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IModule;
import mekanism.api.gear.config.IModuleConfigItem;
import mekanism.api.gear.config.ModuleColorData;
import mekanism.api.gear.config.ModuleConfigItemCreator;
import mekanism.common.MekanismLang;
import mekanism.common.content.gear.ModuleHelper;
import mekanism.common.lib.Color;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EvolvedMekanism.MODID)
@ParametersAreNotNullByDefault
public class ModuleAirAffinity implements ICustomModule<ModuleAirAffinity> {

    @SubscribeEvent
    public static void resetBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        if (!player.onGround() && ModuleHelper.INSTANCE.isEnabled(player.getItemBySlot(EquipmentSlot.FEET), EMModules.AIR_AFFINITY.get())) {
            if (event.getOriginalSpeed() < event.getNewSpeed() * 5) event.setNewSpeed(event.getNewSpeed() * 5F);
        }
    }

}