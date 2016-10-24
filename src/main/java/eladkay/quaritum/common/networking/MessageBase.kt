package eladkay.quaritum.common.networking

import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side

//minemaarten advanced modding tutorials code
abstract class MessageBase<REQ : IMessage> : IMessage, IMessageHandler<REQ, REQ> {

    override fun onMessage(message: REQ, ctx: MessageContext): REQ? {
        if (ctx.side == Side.SERVER) {
            handleServerSide(message, ctx.serverHandler.playerEntity)
        } else {
            handleClientSide(message, null) //safe because it runs on the client
        }
        return null
    }

    /**
     * Handle a packet on the client side. Note this occurs after decoding has completed.

     * @param message
     * *
     * @param player  the player reference
     */
    abstract fun handleClientSide(message: REQ, player: EntityPlayer?)

    /**
     * Handle a packet on the server side. Note this occurs after decoding has completed.

     * @param message
     * *
     * @param player  the player reference
     */
    abstract fun handleServerSide(message: REQ, player: EntityPlayer)
}
