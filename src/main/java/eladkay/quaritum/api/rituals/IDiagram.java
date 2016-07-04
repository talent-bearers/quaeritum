package eladkay.quaritum.api.rituals;

import eladkay.quaritum.common.block.tile.TileEntityBlueprint;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface IDiagram {
    @Nonnull
    String getUnlocalizedName();

    void run(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile);
    boolean canRitualRun(@Nullable World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile);
    boolean hasRequiredItems(@Nullable World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile);

    default int getPrepTime(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile) {
        return 0;
    }
    default boolean onPrepUpdate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull TileEntityBlueprint tile, int ticksRemaining) {
        return true;
    }

    void buildChalks(@Nonnull List<PositionedBlock> chalks);

    class Helper {

        private static class MutableBoolean {
            boolean value;
            public MutableBoolean(boolean value) {
                this.value = value;
            }

        }
        public static boolean isEntityItemInList(EntityItem item, List<ItemStack> stacks) {
            MutableBoolean flag = new MutableBoolean(false);
            stacks.forEach((stack) -> { if(itemEquals(item.getEntityItem(), stack)) flag.value = true; });
            return flag.value;
        }
        public static boolean isStackInList(ItemStack stack, List<ItemStack> stacks) {
            for(ItemStack stack1 : stacks) {
                if(itemEquals(stack1, stack)) return true;
            }
            return false;
        }
        public static List<EntityItem> entitiesAroundAltar(TileEntity tile, double range) {
            List<EntityItem> entities = tile.getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(tile.getPos().add(3, 2, 3), tile.getPos().add(-3, -2, -3)));
            return entities.stream()
                    .filter(entity -> tile.getPos().add(0.5, 0.5, 0.5).distanceSq(entity.posX, entity.posY, entity.posZ) < range * range)
                    .collect(Collectors.toList());
        }

        public static List<ItemStack> stacksAroundAltar(TileEntity tile, double range) {
            return entitiesAroundAltar(tile, range).stream()
                    .map(EntityItem::getEntityItem)
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
    }
}
