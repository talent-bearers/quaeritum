package eladkay.quaeritum.api.rituals;

import eladkay.quaeritum.api.animus.AnimusHelper;
import eladkay.quaeritum.api.animus.EnumAnimusTier;
import eladkay.quaeritum.api.animus.INetworkProvider;
import eladkay.quaeritum.api.animus.ISoulstone;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public interface IDiagram {
    @NotNull
    String getUnlocalizedName();

    void run(@NotNull World world, @NotNull BlockPos pos, @NotNull TileEntity tile);

    boolean canRitualRun(@Nullable World world, @NotNull BlockPos pos, @NotNull TileEntity tile);

    boolean hasRequiredItems(@Nullable World world, @NotNull BlockPos pos, @NotNull TileEntity tile);

    default int getPrepTime(@NotNull World world, @NotNull BlockPos pos, @NotNull TileEntity tile) {
        return 0;
    }

    default boolean onPrepUpdate(@NotNull World world, @NotNull BlockPos pos, @NotNull TileEntity tile, int ticksRemaining) {
        return true;
    }

    void buildChalks(@NotNull List<PositionedBlock> chalks);

    class Helper {

        public static boolean consumeAnimusForRitual(TileEntity tes, boolean drain, int animus, EnumAnimusTier rarity) {
            ItemStack stack1 = Helper.getNearestAttunedSoulstone(tes, 4);
            if (stack1 == null) return false;
            UUID uuid = ((INetworkProvider) stack1.getItem()).getPlayer(stack1);
            return AnimusHelper.Network.requestAnimus(uuid, animus, rarity, drain);
        }

        //ffs takeanimus
        /*public static boolean consumeAnimusForRitualFromSoulstone(TileEntity tes, boolean drain, int animus, int rarity) {
            ItemStack stack1 = Helper.getNearestSoulstone(tes, 4);
            if(stack1 == null) return false;
            ISoulstone stone = (ISoulstone) stack1.getItem();
            if(stone.getAnimusLevel(stack1) < animus) return false;
            if(stone.getRarityLevel(stack1) < rarity) return false;
            if(drain) {
                AnimusHelper.addAnimus(stack1, -animus);
            }
            return true;
        }*/
        public static ItemStack getNearestAttunedSoulstone(TileEntity tile, double range) {
            MutableObject<ItemStack> ret = new MutableObject<>(null);
            stacksAroundAltar(tile, range).forEach((stack -> {
                if (stack.getItem() instanceof INetworkProvider) ret.value = stack;
            }));
            return ret.value;
        }

        public static ItemStack getNearestSoulstone(TileEntity tile, double range) {
            MutableObject<ItemStack> ret = new MutableObject<>(null);
            stacksAroundAltar(tile, range).forEach((stack -> {
                if (stack.getItem() instanceof ISoulstone) ret.value = stack;
            }));
            return ret.value;
        }

        public static boolean isEntityItemInList(EntityItem item, List<ItemStack> stacks) {
            MutableObject<Boolean> flag = new MutableObject<>(false);
            stacks.forEach((stack) -> {
                if (itemEquals(item.getItem(), stack)) flag.value = true;
            });
            return flag.value;
        }

        public static boolean isStackInList(ItemStack stack, List<ItemStack> stacks) {
            for (ItemStack stack1 : stacks) {
                if (itemEquals(stack1, stack)) return true;
            }
            return false;
        }

        public static List<EntityItem> entitiesAroundAltar(TileEntity tile, double range) {
            List<EntityItem> entities = tile.getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(tile.getPos()).grow(range));
            return entities.stream()
                    .filter(entity -> tile.getPos().add(0.5, 0.5, 0.5).distanceSq(entity.posX, entity.posY, entity.posZ) < range * range)
                    .collect(Collectors.toList());
        }

        public static List<ItemStack> stacksAroundAltar(TileEntity tile, double range) {
            return entitiesAroundAltar(tile, range).stream()
                    .map(EntityItem::getItem)
                    .collect(Collectors.toList());
        }

        public static boolean matches(List<ItemStack> items, List<ItemStack> required) {
            List<ItemStack> inputsMissing = new ArrayList<>(required);
            for (ItemStack i : items) {
                for (int j = 0; j < inputsMissing.size(); j++) {
                    ItemStack inp = inputsMissing.get(j).copy();
                    if (inp.getItemDamage() == 32767)
                        inp.setItemDamage(i.getItemDamage());

                    if (itemEquals(i, inp)) {
                        inputsMissing.remove(j);
                        break;
                    }
                }
            }
            return inputsMissing.isEmpty();
        }

        public static boolean simpleAreStacksEqual(ItemStack stack, ItemStack stack2) {
            return stack.getItem() == stack2.getItem() && stack.getItemDamage() == stack2.getItemDamage();
        }

        public static boolean takeAnimus(int amount, EnumAnimusTier rarity, TileEntity tile, double range, boolean drain) {
            EntityItem bestFit = null;
            List<EntityItem> around = entitiesAroundAltar(tile, range);
            for (EntityItem stack : around.stream().filter((stack1 ->
                    stack1.getItem().getItem() instanceof ISoulstone)).collect(Collectors.toList())) {
                if (bestFit == null) bestFit = stack;
                else if (AnimusHelper.getTier(stack.getItem()).ordinal() >= rarity.ordinal() && AnimusHelper.getAnimus(stack.getItem()) >= amount)
                    bestFit = stack;
            }
            for (EntityItem stack : around.stream().filter((stack1 ->
                    stack1.getItem().getItem() instanceof INetworkProvider &&
                            ((INetworkProvider) stack1.getItem().getItem()).isProvider(stack1.getItem()))).collect(Collectors.toList())) {
                INetworkProvider provider = (INetworkProvider) stack.getItem().getItem();
                UUID player = provider.getPlayer(stack.getItem());
                if (bestFit == null) bestFit = stack;
                else if (AnimusHelper.Network.getTier(player).ordinal() >= rarity.ordinal() && AnimusHelper.Network.getAnimus(player) >= amount)
                    bestFit = stack;
            }
            if (bestFit != null && AnimusHelper.getTier(bestFit.getItem()).ordinal() >= rarity.ordinal() && AnimusHelper.getAnimus(bestFit.getItem()) >= amount) {
                if (drain) {
                    if (bestFit.getItem().getItem() instanceof ISoulstone)
                        AnimusHelper.addAnimus(bestFit.getItem(), -amount);
                    else
                        AnimusHelper.Network.addAnimus(((INetworkProvider) bestFit.getItem().getItem()).getPlayer(bestFit.getItem()), -amount);
                }
                return true;
            }
            return false;

        }

        public static boolean itemEquals(ItemStack stack, Object stack2) {
            if (stack2 instanceof String) {

                for (ItemStack orestack : OreDictionary.getOres((String) stack2)) {
                    ItemStack cstack = orestack.copy();

                    if (cstack.getItemDamage() == 32767) cstack.setItemDamage(stack.getItemDamage());
                    if (stack.isItemEqual(cstack)) return true;
                }

            } else return stack2 instanceof ItemStack && simpleAreStacksEqual(stack, (ItemStack) stack2);
            return false;
        }

        public static class MutableObject<T> {
            public T value;

            public MutableObject(T value) {
                this.value = value;
            }

        }
    }
}

