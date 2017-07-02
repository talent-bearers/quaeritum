package eladkay.quaeritum.common.networking

import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.network.PacketBase
import com.teamwizardry.librarianlib.features.saving.Save
import eladkay.quaeritum.common.Quaeritum
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side

/**
 * @author WireSegal
 * Created at 3:38 PM on 2/5/17.
 */
@PacketRegister(Side.CLIENT)
class RemainingItemRenderPacket(@Save var stack: ItemStack? = null, @Save var str: String? = null, @Save var count: Int = 0) : PacketBase() {
    override fun handle(ctx: MessageContext) = Quaeritum.proxy.setRemainingItemDisplay(null, stack, str, count)
}
