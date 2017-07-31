package eladkay.quaeritum.api.spell;

import eladkay.quaeritum.api.internal.InternalHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.minecraft.util.EnumActionResult.PASS;
import static net.minecraft.util.EnumActionResult.SUCCESS;

/**
 * @author WireSegal
 *         Created at 10:27 PM on 7/28/17.
 */
public final class ElementHandler {

    @NotNull
    public static EnumActionResult takeReagent(@NotNull EntityPlayer player, @NotNull EnumSpellElement element) {
        return takeReagent(player, element, true);
    }

    @NotNull
    public static EnumActionResult takeReagent(@NotNull EntityPlayer player, @NotNull EnumSpellElement element, boolean take) {
        List<IItemHandlerModifiable> inventories = SpellInventoryEvent.getHandlers(player, element);
        EnumActionResult result = PASS;
        main: for (IItemHandlerModifiable inventory : inventories) for (int slot = 0; slot < inventory.getSlots(); slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack.getItem() instanceof ISpellReagent) {
                ISpellReagent item = (ISpellReagent) stack.getItem();
                ActionResult<ItemStack> actionResult = item.consumeCharge(stack, element, 1);
                if (actionResult.getType() != PASS && result == PASS)
                    result = actionResult.getType();

                if (actionResult.getType() != PASS && take)
                    inventory.setStackInSlot(slot, actionResult.getResult());

                if (actionResult.getType() == SUCCESS)
                    break main;
            }
        }

        return result;
    }

    @NotNull
    private static NBTTagCompound persistentCompound(@NotNull EntityPlayer player) {
        NBTTagCompound customData = player.getEntityData();
        if (!customData.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
            customData.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
        return customData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
    }

    @NotNull
    public static NBTTagList getReagents(@NotNull EntityPlayer player) {
        NBTTagCompound persistent = persistentCompound(player);
        if (!persistent.hasKey("quaeritum.reagents", Constants.NBT.TAG_LIST))
            persistent.setTag("quaeritum.reagents", new NBTTagList());
        else {
            NBTTagList list = persistent.getTagList("quaeritum.reagents", Constants.NBT.TAG_BYTE);
            if (list.hasNoTags() || list.getTagType() != Constants.NBT.TAG_BYTE)
                persistent.setTag("quaeritum.reagents", new NBTTagList());
        }
        return persistent.getTagList("quaeritum.reagents", Constants.NBT.TAG_BYTE);
    }

    public static void clearReagents(@NotNull EntityPlayer player) {
        NBTTagCompound persistent = persistentCompound(player);
        persistent.removeTag("quaeritum.reagents");
        InternalHandler.getInternalHandler().syncAnimusData(player);
    }

    @NotNull
    private static EnumActionResult addReagent(@NotNull EntityPlayer player, @NotNull EnumSpellElement element) {
        NBTTagList list = getReagents(player);
        if (list.tagCount() >= 8) return PASS;
        EnumActionResult result = takeReagent(player, element);
        if (result == SUCCESS) {
            list.appendTag(new NBTTagByte((byte) element.ordinal()));
            return SUCCESS;
        }
        return result;
    }

    @NotNull
    public static EnumActionResult[] addReagents(@NotNull EntityPlayer player, @NotNull EnumSpellElement... element) {
        EnumActionResult[] arr = new EnumActionResult[element.length];
        for (int i = 0; i < element.length; i++)
            arr[i] = addReagent(player, element[i]);
        if (!player.world.isRemote)
            InternalHandler.getInternalHandler().syncAnimusData(player);
        return arr;
    }
}
