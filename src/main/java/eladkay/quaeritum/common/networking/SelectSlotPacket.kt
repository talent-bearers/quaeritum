package eladkay.quaeritum.common.networking

import com.teamwizardry.librarianlib.common.network.PacketBase
import com.teamwizardry.librarianlib.common.util.autoregister.PacketRegister
import com.teamwizardry.librarianlib.common.util.saving.Save
import eladkay.quaeritum.common.item.ItemSoulEvoker
import net.minecraft.util.EnumHand
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side

/**
 * @author WireSegal
 * Created at 9:50 AM on 2/6/17.
 */
@PacketRegister(Side.SERVER)
class SelectSlotPacket(@Save var slot: Int = 0, @Save var hand: EnumHand = EnumHand.MAIN_HAND) : PacketBase() {
    override fun handle(ctx: MessageContext) {
        val stack = ctx.serverHandler.playerEntity.getHeldItem(hand)
        if (stack != null)
            ItemSoulEvoker.setSlot(stack, slot)
    }
}
