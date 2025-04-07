package fr.iglee42.evolvedmekanism.items;

import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.blocks.BlockTieredPersonalStorage;
import fr.iglee42.evolvedmekanism.inventory.personalstorage.TieredPersonalStorageItemContainer;
import fr.iglee42.evolvedmekanism.inventory.personalstorage.TieredPersonalStorageManager;
import fr.iglee42.evolvedmekanism.registries.EMContainerTypes;
import fr.iglee42.evolvedmekanism.tiers.PersonalStorageTier;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.text.EnumColor;
import mekanism.common.MekanismLang;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.item.interfaces.IDroppableContents;
import mekanism.common.item.interfaces.IGuiItem;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.tier.InductionProviderTier;
import mekanism.common.util.SecurityUtils;
import mekanism.common.util.text.EnergyDisplay;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ItemBlockTieredPersonalStorage<BLOCK extends BlockTieredPersonalStorage<?, ?>> extends ItemBlockTooltip<BLOCK> implements IDroppableContents, IGuiItem {

    private final ResourceLocation openStat;
    private final PersonalStorageTier tier;

    public ItemBlockTieredPersonalStorage(BLOCK block, ResourceLocation openStat, PersonalStorageTier tier) {
        super(block);
        this.openStat = openStat;
        this.tier = tier;
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player player, @NotNull InteractionHand hand) {
        return SecurityUtils.get().claimOrOpenGui(world, player, hand, (p, h, s) -> {
            if (!world.isClientSide) {
                TieredPersonalStorageManager.getInventoryFor(s);
            }
            getContainerType().tryOpenGui(p, h, s);
            p.awardStat(Stats.CUSTOM.get(openStat));
        });
    }

    @NotNull
    @Override
    public InteractionResult useOn(@NotNull UseOnContext context) {
        //Like super.onItemUse, except we validate the player is not null, and pass the onItemRightClick regardless of if
        // we are food or not (as we know the personal chest is never food). This allows us to open the personal chest's
        // GUI if we didn't interact with a block that caused something to happen like opening a GUI.
        InteractionResult result = place(new BlockPlaceContext(context));
        Player player = context.getPlayer();
        return result.consumesAction() || player == null ? result : use(context.getLevel(), player, context.getHand()).getResult();
    }

    @Override
    protected boolean canPlace(@NotNull BlockPlaceContext context, @NotNull BlockState state) {
        Player player = context.getPlayer();
        //Only allow placing if there is no player, it is a fake player, or the player is sneaking
        return (player == null || player instanceof FakePlayer || player.isShiftKeyDown()) && super.canPlace(context, state);
    }

    @Override
    public ContainerTypeRegistryObject<TieredPersonalStorageItemContainer> getContainerType() {
        return EMContainerTypes.TIERED_PERSONAL_STORAGE_ITEM;
    }

    @Override
    public void onDestroyed(@NotNull ItemEntity item, @NotNull DamageSource damageSource) {
        super.onDestroyed(item, damageSource);
        if (!item.level().isClientSide) {
            ItemStack stack = item.getItem();
            TieredPersonalStorageManager.getInventoryIfPresent(stack).ifPresent(inventory -> {
                if (inventory.isInventoryEmpty()) {
                    //If the inventory was actually empty we can prune the data from the storage manager
                    // (if it isn't empty we want to persist it so that server admins can recover their items)
                    TieredPersonalStorageManager.deleteInventory(stack);
                }
            });
        }
    }

    @Override
    public List<IInventorySlot> getDroppedSlots(ItemStack stack) {
        return TieredPersonalStorageManager.getInventoryIfPresent(stack)
              .map(inventory -> inventory.getInventorySlots(null))
              .orElse(Collections.emptyList());
    }

    @Override
    public PersonalStorageTier getTier() {
        return tier;
    }

    @Override
    protected void addStats(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.addStats(stack, world, tooltip, flag);
        PersonalStorageTier tier = getTier();

        tooltip.add(EvolvedMekanismLang.TIERED_STORAGE_CAPACITY.translateColored(tier.getBaseTier().getColor(), EnumColor.GRAY, tier.columns * tier.rows + " stacks"));

    }
}