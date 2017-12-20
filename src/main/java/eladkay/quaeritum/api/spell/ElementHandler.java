package eladkay.quaeritum.api.spell;

import eladkay.quaeritum.api.internal.InternalHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.util.EnumActionResult.*;

/**
 * @author WireSegal
 * Created at 10:27 PM on 7/28/17.
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
        main:
        for (IItemHandlerModifiable inventory : inventories)
            for (int slot = 0; slot < inventory.getSlots(); slot++) {
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
    public static byte[] getReagents(@NotNull EntityPlayer player) {
        NBTTagCompound persistent = persistentCompound(player);
        if (!persistent.hasKey("quaeritum.reagents", Constants.NBT.TAG_BYTE_ARRAY))
            persistent.setTag("quaeritum.reagents", new NBTTagByteArray(new byte[0]));
        return persistent.getByteArray("quaeritum.reagents");
    }

    @NotNull
    public static EnumSpellElement[] getReagentsTyped(@NotNull EntityPlayer player) {
        byte[] elements = getReagents(player);
        return fromBytes(elements);
    }

    @NotNull
    public static EnumSpellElement[] fromBytes(@NotNull byte[] bytes) {
        EnumSpellElement[] ret = new EnumSpellElement[bytes.length];
        for (int i = 0; i < bytes.length; i++)
            ret[i] = EnumSpellElement.values()[bytes[i] % EnumSpellElement.values().length];
        return ret;
    }

    @NotNull
    public static byte[] fromElements(@NotNull EnumSpellElement[] elements) {
        byte[] ret = new byte[elements.length];
        for (int i = 0; i < elements.length; i++)
            ret[i] = (byte) elements[i].ordinal();
        return ret;
    }

    public static void clearReagents(@NotNull EntityPlayer player) {
        NBTTagCompound persistent = persistentCompound(player);
        persistent.removeTag("quaeritum.reagents");
        InternalHandler.getInternalHandler().syncAnimusData(player);
    }

    public static void setReagents(@NotNull EntityPlayer player, @NotNull EnumSpellElement... element) {
        setReagents(player, fromElements(element));
        InternalHandler.getInternalHandler().syncAnimusData(player);
    }

    public static void setReagents(@NotNull EntityPlayer player, @NotNull byte[] elements) {
        NBTTagCompound persistent = persistentCompound(player);
        persistent.setByteArray("quaeritum.reagents", elements);
    }

    @NotNull
    private static EnumActionResult addReagent(@NotNull EntityPlayer player, @NotNull EnumSpellElement element, boolean take) {
        byte[] list = getReagents(player);
        if (list.length >= 8) return PASS;
        EnumActionResult result = takeReagent(player, element, take);
        if (result == SUCCESS && take) {
            byte[] array = Arrays.copyOf(list, list.length + 1);
            array[list.length] = (byte) element.ordinal();
            setReagents(player, array);
            return SUCCESS;
        }
        return result;
    }

    @NotNull
    public static EnumActionResult probeReagents(@NotNull EntityPlayer player, @NotNull EnumSpellElement... element) {
        if (element.length == 0) return PASS;
        for (EnumSpellElement anElement : element)
            if (addReagent(player, anElement, false) != SUCCESS) return FAIL;
        return SUCCESS;
    }

    @NotNull
    public static EnumActionResult addReagents(@NotNull EntityPlayer player, @NotNull EnumSpellElement... element) {
        EnumActionResult result = probeReagents(player, element);
        if (result != SUCCESS) return result;
        for (EnumSpellElement anElement : element)
            addReagent(player, anElement, true);
        InternalHandler.getInternalHandler().syncAnimusData(player);
        return SUCCESS;
    }
}
