package eladkay.quaeritum.common.networking

import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.network.PacketBase
import com.teamwizardry.librarianlib.features.saving.Save
import eladkay.quaeritum.common.core.QuaeritumInternalHandler
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side

/**
 * @author WireSegal
 * Created at 12:04 PM on 7/29/17.
 */
@PacketRegister(Side.CLIENT)
class AnimusSync(@Save var raw: NBTTagCompound = NBTTagCompound()) : PacketBase() {
    override fun handle(ctx: MessageContext) {
        QuaeritumInternalHandler.getTrueSaveData().readFromNBT(raw)
    }
}
