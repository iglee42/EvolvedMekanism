package fr.iglee42.evolvedmekanism.modules;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMModules;
import mekanism.api.annotations.ParametersAreNotNullByDefault;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IModule;
import mekanism.common.content.gear.ModuleHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EvolvedMekanism.MODID)
@ParametersAreNotNullByDefault
public class ModuleCapturing implements ICustomModule<ModuleCapturing> {
    @SubscribeEvent
    public static void addEggDrop(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        if (event.getSource().getEntity() == null) return;
        Entity killerE = event.getSource().getEntity();
        if (!(killerE instanceof LivingEntity killer)) return;
        if ( ModuleHelper.INSTANCE.isEnabled(killer.getItemBySlot(EquipmentSlot.MAINHAND), EMModules.CAPTURING.get())) {
            IModule<?> module = ModuleHelper.INSTANCE.load(killer.getItemBySlot(EquipmentSlot.MAINHAND), EMModules.CAPTURING);
            if (module != null && module.isEnabled()){
                SpawnEggItem item = ForgeSpawnEggItem.fromEntityType(entity.getType());
                if (item == null) return;
                int level = module.getInstalledCount();
                int valueToReach = switch (level){
                  case 1 -> 5;
                  case 2 -> 15;
                  case 3 -> 25;
                    default -> 0;
                };
                if (entity.level().random.nextInt(1000) >= valueToReach) return;
                event.getDrops().add(new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(item)));
            }
        }
    }
}
