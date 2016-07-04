package eladkay.quaritum.common.core;

import eladkay.quaritum.client.core.TooltipHelper;
import eladkay.quaritum.api.lib.LibMisc;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ChatHelper {
    private static final int DELETION_ID_1 = 2535277;
    private static final int DELETION_ID_2 = 2535272;
    private static int lastAdded;

    private static void sendNoSpamMessages1(ITextComponent[] messages) {
        GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        for (int i = DELETION_ID_1 + messages.length - 1; i <= lastAdded; i++) {
            chat.deleteChatLine(i);
        }
        for (int i = 0; i < messages.length; i++) {
            chat.printChatMessageWithOptionalDeletion(messages[i], DELETION_ID_1 + i);
        }
        lastAdded = DELETION_ID_1 + messages.length - 1;
    }

    private static void sendNoSpamMessages2(ITextComponent[] messages) {
        GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        for (int i = DELETION_ID_2 + messages.length - 1; i <= lastAdded; i++) {
            chat.deleteChatLine(i);
        }
        for (int i = 0; i < messages.length; i++) {
            chat.printChatMessageWithOptionalDeletion(messages[i], DELETION_ID_2 + i);
        }
        lastAdded = DELETION_ID_2 + messages.length - 1;
    }

    public static ITextComponent wrap(String s) {
        return new TextComponentString(s);
    }

    public static ITextComponent[] wrap(String... s) {
        ITextComponent[] ret = new ITextComponent[s.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = wrap(s[i]);
        }
        return ret;
    }

    public static ITextComponent wrapFormatted(String s, Object... args) {
        return new TextComponentTranslation(s, args);
    }

    public static void sendChat(EntityPlayer player, String... lines) {
        sendChat(player, wrap(lines));
    }

    public static void sendChatUnloc(EntityPlayer player, String... unlocLines) {
        sendChat(player, TextHelper.localizeAll(unlocLines));
    }

    public static void sendChat(EntityPlayer player, ITextComponent... lines) {
        for (ITextComponent c : lines) {
            player.addChatComponentMessage(c);
        }
    }

    public static void sendNoSpamClientUnloc1(String... unlocLines) {
        sendNoSpamClient1(TextHelper.localizeAll(unlocLines));
    }

    public static void sendNoSpamClientUnloc2(String... unlocLines) {
        sendNoSpamClient1(TextHelper.localizeAll(unlocLines));
    }

    public static void sendNoSpamClient1(String... lines) {
        sendNoSpamClient1(wrap(lines));
    }

    public static void sendNoSpamClient2(String... lines) {
        sendNoSpamClient2(wrap(lines));
    }

    public static void sendNoSpamClient1(ITextComponent... lines) {
        sendNoSpamMessages1(lines);
    }

    public static void sendNoSpamClient2(ITextComponent... lines) {
        sendNoSpamMessages2(lines);
    }

    public static void sendNoSpam1(EntityPlayer player, String... lines) {
        sendNoSpam1(player, wrap(lines));
    }

    public static void sendNoSpam2(EntityPlayer player, String... lines) {
        sendNoSpam2(player, wrap(lines));
    }

    public static void sendNoSpam2(EntityPlayer player, ITextComponent... lines) {
        if (player instanceof EntityPlayerMP) {
            sendNoSpam2((EntityPlayerMP) player, lines);
        }
    }

    public static void sendNoSpam1(EntityPlayer player, ITextComponent... lines) {
        if (player instanceof EntityPlayerMP) {
            sendNoSpam1((EntityPlayerMP) player, lines);
        }
    }

    public static void sendNoSpamUnloc1(EntityPlayerMP player, String... unlocLines) {
        sendNoSpam1(player, TextHelper.localizeAll(unlocLines));
    }

    public static void sendNoSpamUnloc2(EntityPlayerMP player, String... unlocLines) {
        sendNoSpam2(player, TextHelper.localizeAll(unlocLines));
    }

    public static void sendNoSpam1(EntityPlayerMP player, String... lines) {
        sendNoSpam1(player, wrap(lines));
    }

    public static void sendNoSpam2(EntityPlayerMP player, String... lines) {
        sendNoSpam2(player, wrap(lines));
    }

    public static void sendNoSpam2(EntityPlayerMP player, ITextComponent... lines) {
        if (lines.length > 0)
            PacketHandler.INSTANCE.sendTo(new PacketNoSpamChat(2, lines), player);
    }

    public static void sendNoSpam1(EntityPlayerMP player, ITextComponent... lines) {
        if (lines.length > 0)
            PacketHandler.INSTANCE.sendTo(new PacketNoSpamChat(1, lines), player);
    }

    public static class PacketNoSpamChat implements IMessage {

        public ITextComponent[] chatLines;
        public int type;

        public PacketNoSpamChat() {
            this.chatLines = new ITextComponent[0];
            this.type = 1;
        }

        public PacketNoSpamChat(int type, ITextComponent... lines) {
            this.chatLines = lines;
            this.type = type;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            chatLines = new ITextComponent[buf.readInt()];
            for (int i = 0; i < chatLines.length; i++) {
                chatLines[i] = ITextComponent.Serializer.jsonToComponent(ByteBufUtils.readUTF8String(buf));
            }
            type = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(chatLines.length);
            for (ITextComponent c : chatLines) {
                ByteBufUtils.writeUTF8String(buf, ITextComponent.Serializer.componentToJson(c));
            }
            buf.writeInt(type);
        }

        public static class Handler implements IMessageHandler<PacketNoSpamChat, IMessage> {
            @Override
            public IMessage onMessage(PacketNoSpamChat message, MessageContext ctx) {
                if (message.type == 1)
                    sendNoSpamMessages1(message.chatLines);
                else
                    sendNoSpamMessages2(message.chatLines);
                return null;
            }
        }
    }

    private static class TextHelper {
        public static String localize(String input, Object... format) {
            return TooltipHelper.local(input);
        }

        public static String[] localizeAll(String[] input) {
            String[] ret = new String[input.length];
            for (int i = 0; i < input.length; i++)
                ret[i] = localize(input[i]);

            return ret;
        }
    }

    protected static class PacketHandler {
        public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(LibMisc.MOD_ID + "_chat");

        public static void init() {
            INSTANCE.registerMessage(ChatHelper.PacketNoSpamChat.Handler.class, ChatHelper.PacketNoSpamChat.class, 0, Side.CLIENT);
        }

    }
}
