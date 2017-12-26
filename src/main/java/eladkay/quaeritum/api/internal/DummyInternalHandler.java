package eladkay.quaeritum.api.internal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author WireSegal
 * Created at 11:50 AM on 7/29/17.
 */
public class DummyInternalHandler implements IInternalHandler {
    @Override
    public void syncAnimusData(@NotNull EntityPlayer player) {
        // NO-OP
    }

    @Override
    public @NotNull Map<UUID, NBTTagCompound> getSaveData() {
        return new HashMap<>();
    }

    @Override
    public void markSaveDataDirty() {
        // NO-OP
    }
}
