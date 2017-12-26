package eladkay.quaeritum.api.internal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

/**
 * @author WireSegal
 * Created at 11:50 AM on 7/29/17.
 */
public interface IInternalHandler {
    void syncAnimusData(@NotNull EntityPlayer player);

    @NotNull
    Map<UUID, NBTTagCompound> getSaveData();

    void markSaveDataDirty();
}
