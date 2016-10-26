package eladkay.quaritum.common.core

import com.teamwizardry.librarianlib.client.util.TooltipHelper
import eladkay.quaritum.api.lib.LibMisc
import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side

object ChatHelper {
    private val DELETION_ID_1 = 2535277
    private val DELETION_ID_2 = 2535272
    private var lastAdded: Int = 0

    private fun sendNoSpamMessages1(messages: Array<out ITextComponent>) {
        val chat = Minecraft.getMinecraft().ingameGUI.chatGUI
        for (i in DELETION_ID_1 + messages.size - 1..lastAdded) {
            chat.deleteChatLine(i)
        }
        for (i in messages.indices) {
            chat.printChatMessageWithOptionalDeletion(messages[i], DELETION_ID_1 + i)
        }
        lastAdded = DELETION_ID_1 + messages.size - 1
    }

    private fun sendNoSpamMessages2(messages: Array<out ITextComponent>) {
        val chat = Minecraft.getMinecraft().ingameGUI.chatGUI
        for (i in DELETION_ID_2 + messages.size - 1..lastAdded) {
            chat.deleteChatLine(i)
        }
        for (i in messages.indices) {
            chat.printChatMessageWithOptionalDeletion(messages[i], DELETION_ID_2 + i)
        }
        lastAdded = DELETION_ID_2 + messages.size - 1
    }

    fun wrap(s: String): ITextComponent {
        return TextComponentString(s)
    }

    fun wrap(vararg s: String): Array<ITextComponent> {
        return Array(s.size) {
            wrap(s[it])
        }
    }

    fun wrapFormatted(s: String, vararg args: Any): ITextComponent {
        return TextComponentTranslation(s, *args)
    }

    fun sendChat(player: EntityPlayer, vararg lines: String) {
        sendChat(player, *wrap(*lines))
    }

    fun sendChatUnloc(player: EntityPlayer, vararg unlocLines: String) {
        sendChat(player, *TextHelper.localizeAll(unlocLines))
    }

    fun sendChat(player: EntityPlayer, vararg lines: ITextComponent) {
        for (c in lines) {
            player.addChatComponentMessage(c)
        }
    }

    @JvmStatic fun sendNoSpamClientUnloc1(unlocLines: Array<String>) {
        sendNoSpamClient1(*unlocLines.map { TextComponentTranslation(it) }.toTypedArray())
    }

    @JvmStatic fun sendNoSpamClientUnloc2(unlocLines: Array<String>) {
        sendNoSpamClient1(*unlocLines.map { TextComponentTranslation(it) }.toTypedArray())
    }

    @JvmStatic fun sendNoSpamClient1(lines: Array<String>) {
        sendNoSpamClient1(*wrap(*lines))
    }

    @JvmStatic fun sendNoSpamClient2(lines: Array<String>) {
        sendNoSpamClient2(*wrap(*lines))
    }

    fun sendNoSpamClient1(vararg lines: ITextComponent) {
        sendNoSpamMessages1(lines)
    }

    fun sendNoSpamClient2(vararg lines: ITextComponent) {
        sendNoSpamMessages2(lines)
    }

    fun sendNoSpam1(player: EntityPlayer, vararg lines: String) {
        sendNoSpam1(player, *wrap(*lines))
    }

    fun sendNoSpam2(player: EntityPlayer, vararg lines: String) {
        sendNoSpam2(player, *wrap(*lines))
    }

    fun sendNoSpam2(player: EntityPlayer, vararg lines: ITextComponent) {
        if (player is EntityPlayerMP) {
            sendNoSpam2(player, *lines)
        }
    }

    fun sendNoSpam1(player: EntityPlayer, vararg lines: ITextComponent) {
        if (player is EntityPlayerMP) {
            sendNoSpam1(player, *lines)
        }
    }

    fun sendNoSpamUnloc1(player: EntityPlayerMP, vararg unlocLines: String) {
        sendNoSpam1(player, *TextHelper.localizeAll(unlocLines))
    }

    fun sendNoSpamUnloc2(player: EntityPlayerMP, vararg unlocLines: String) {
        sendNoSpam2(player, *TextHelper.localizeAll(unlocLines))
    }

    fun sendNoSpam1(player: EntityPlayerMP, vararg lines: String) {
        sendNoSpam1(player, *wrap(*lines))
    }

    fun sendNoSpam2(player: EntityPlayerMP, vararg lines: String) {
        sendNoSpam2(player, *wrap(*lines))
    }

    fun sendNoSpam2(player: EntityPlayerMP, vararg lines: ITextComponent) {
        if (lines.size > 0)
            PacketHandler.INSTANCE.sendTo(PacketNoSpamChat(2, *lines), player)
    }

    fun sendNoSpam1(player: EntityPlayerMP, vararg lines: ITextComponent) {
        if (lines.size > 0)
            PacketHandler.INSTANCE.sendTo(PacketNoSpamChat(1, *lines), player)
    }

    class PacketNoSpamChat : IMessage {

        var chatLines: Array<out ITextComponent>
        var type: Int = 0

        constructor() {
            this.chatLines = arrayOf<ITextComponent>()
            this.type = 1
        }

        constructor(type: Int, vararg lines: ITextComponent) {
            this.chatLines = lines
            this.type = type
        }

        override fun fromBytes(buf: ByteBuf) {
            val int = buf.readInt()
            chatLines = Array(int) {
                ITextComponent.Serializer.jsonToComponent(ByteBufUtils.readUTF8String(buf))
            }
            type = buf.readInt()
        }

        override fun toBytes(buf: ByteBuf) {
            buf.writeInt(chatLines.size)
            for (c in chatLines) {
                ByteBufUtils.writeUTF8String(buf, ITextComponent.Serializer.componentToJson(c))
            }
            buf.writeInt(type)
        }

        class Handler : IMessageHandler<PacketNoSpamChat, IMessage> {
            override fun onMessage(message: PacketNoSpamChat, ctx: MessageContext): IMessage? {
                if (message.type == 1)
                    sendNoSpamMessages1(message.chatLines)
                else
                    sendNoSpamMessages2(message.chatLines)
                return null
            }
        }
    }

    private object TextHelper {
        fun localize(input: String, vararg format: Any): String {
            return TooltipHelper.local(input)
        }

        fun localizeAll(input: Array<out String>): Array<String> {
            return input.map { localize(it) }.toTypedArray()
        }
    }

    object PacketHandler {
        val INSTANCE = SimpleNetworkWrapper(LibMisc.MOD_ID + "_chat")

        init {
            INSTANCE.registerMessage<ChatHelper.PacketNoSpamChat, IMessage>(ChatHelper.PacketNoSpamChat.Handler::class.java, ChatHelper.PacketNoSpamChat::class.java, 0, Side.CLIENT)
        }

    }
}
