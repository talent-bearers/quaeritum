package eladkay.quaeritum.client.core

import com.teamwizardry.librarianlib.features.kotlin.times
import com.teamwizardry.librarianlib.features.methodhandles.MethodHandleHelper
import eladkay.quaeritum.api.spell.EnumLegend
import eladkay.quaeritum.api.spell.EnumSpellElement
import eladkay.quaeritum.api.spell.render.RenderUtil
import eladkay.quaeritum.client.render.RenderSymbol
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ChatLine
import net.minecraft.client.gui.GuiNewChat
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.math.MathHelper
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.*
import kotlin.math.ceil


/**
 * @author WireSegal
 * Created at 10:52 PM on 11/23/17.
 */
object ChatChanger {
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    private val map = mapOf<UUID, EnumLegend>(
            UUID.fromString("458391f5-6303-4649-b416-e4c0d18f837a") to EnumLegend.RIFTMAKER, // yrsegal
            UUID.fromString("adb68f1a-29a8-4354-bf74-d80e5f315865") to EnumLegend.LORIST, // theLorist
            UUID.fromString("c716cef1-833b-47e5-9d62-a936a9552d0d") to EnumLegend.DISCOVERY, // Eladkay
            UUID.fromString("cd3f01ee-8930-49f3-b3e6-05348338b359") to EnumLegend.TRUTH, // Escapee_
            UUID.fromString("689c0981-d8d1-4b64-898f-eb3735be92f5") to EnumLegend.DOORWAYS, // Mithion
            UUID.fromString("2ac3ed86-bf32-4f64-a010-92b095eed279") to EnumLegend.DOORWAYS, // Demoniaque1
            UUID.fromString("8c826f34-113b-4238-a173-44639c53b6e6") to EnumLegend.GROWTH, // Vazkii
            UUID.fromString("0f95811a-b3b6-4dba-ba03-4adfec7cf5ab") to EnumLegend.MYSTERIES, // Azanor
            UUID.fromString("9004a217-117a-4fca-9aae-df52436b6eb7") to EnumLegend.TWISTED, // Emoniph
            UUID.fromString("9417e568-6054-4a47-b1b5-93f747cfa4ce") to EnumLegend.TWISTED, // Elucent
            UUID.fromString("a3cc41c0-57f3-4d24-bd4c-976f800a7625") to EnumLegend.TWISTED, // EpicSquid315
            UUID.fromString("b626a673-dbae-43e8-8c41-010e49896b20") to EnumLegend.INGENUITY, // AlexIIL
            UUID.fromString("ccf731e2-bb8c-4426-8876-cbee224e34bd") to EnumLegend.INGENUITY, // KingLemming
            UUID.fromString("117de7ec-f75a-4d1f-9a8b-20e1f1b642ae") to EnumLegend.SHATTERED, // MrCodeWarrior
            UUID.fromString("562193d8-7da3-48c0-82fb-89cde9f2bcb7") to EnumLegend.ARCHIVE, // CobrasBane
            UUID.fromString("d475af59-d73c-42be-90ed-f1a78f10d452") to EnumLegend.ELSEWHERE, // Tristaric
            UUID.fromString("95fe0728-e1bd-4989-a980-3d8976aedda9") to EnumLegend.DISDAIN, // WayOfFlowingTime
            UUID.fromString("b8ad70cc-5178-4369-af46-abfa329ce305") to EnumLegend.DECLARATION, // machinespray
            UUID.fromString("7f6971c5-fb58-4519-a975-b1b5766e92d1") to EnumLegend.WONDER, // HellfirePVP
            UUID.fromString("6a0e8505-1556-4ee9-bec0-6af32f05888d") to EnumLegend.DEEP, // 115kino
            UUID.fromString("1d680bb6-2a9a-4f25-bf2f-a1af74361d69") to EnumLegend.DEEP, // KingPhygieBoo
            UUID.fromString("4bfb28a3-005d-4fc9-9238-a55c6c17b575") to EnumLegend.DEEP, // xJonL
            UUID.fromString("6c249311-f939-4e66-9f31-49b753bfb14b") to EnumLegend.SLOTH // InsomniaKitten
    )

    var chatX = 0
    var chatY = 0

    @SubscribeEvent
    fun playerName(event: PlayerEvent.NameFormat) {
        val uid = event.entityPlayer.uniqueID
        val legend = map[uid] ?: return
        event.displayname = forLegend(legend) + event.displayname
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun getChatPos(event: RenderGameOverlayEvent.Chat) {
        chatX = event.posX
        chatY = event.posY
    }

    fun forLegend(legend: Enum<*>)
            = "${TextFormatting.GOLD}${colorNum(legend.ordinal, ceil(EnumLegend.values().size.toDouble() / 16).toInt())}   ${TextFormatting.BOLD}${TextFormatting.RESET}"

    fun colorNum(index: Int, total: Int): String {
        var numStr = index.toString(16)
        numStr = "0" * (total - numStr.length) + numStr
        return numStr.map { "§$it" }.joinToString("")
    }

    fun deconstruct(colors: String): Int {
        return colors.replace("§", "").toInt(16)
    }

    private val ELEMENT_PATTERN = "(?:${TextFormatting.AQUA}§([0-9A-Fa-f]) {3}${TextFormatting.BOLD}${TextFormatting.RESET})".toRegex()
    private val LEGEND_PATTERN = "(?:${TextFormatting.GOLD}((?:§[0-9A-Fa-f])+) {3}${TextFormatting.BOLD}${TextFormatting.RESET})".toRegex()

    val GuiNewChat.lines by MethodHandleHelper.delegateForReadOnly<GuiNewChat, List<ChatLine>>(GuiNewChat::class.java, "i", "field_146253_i", "drawnChatLines")
    val GuiNewChat.scrollPos by MethodHandleHelper.delegateForReadOnly<GuiNewChat, Int>(GuiNewChat::class.java, "j", "field_146250_j", "scrollPos")


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun renderSymbols(event: RenderGameOverlayEvent.Post) {
        val chatGui = Minecraft.getMinecraft().ingameGUI.chatGUI
        if (event.type == RenderGameOverlayEvent.ElementType.CHAT) {
            val updateCounter = Minecraft.getMinecraft().ingameGUI.updateCounter
            val chatLines = chatGui.lines

            val shift = chatGui.scrollPos
            var idx = shift
            while (idx < chatLines.size && (idx - shift) < chatGui.lineCount) {
                val line = chatLines[idx]
                val text = line.chatComponent.unformattedText
                val legendMatches = LEGEND_PATTERN.findAll(text)
                for (match in legendMatches) {
                    var timeSinceCreation = updateCounter - line.updatedCounter
                    if (chatGui.chatOpen) timeSinceCreation = 0
                    if (timeSinceCreation < 200) {
                        val chatOpacity = Minecraft.getMinecraft().gameSettings.chatOpacity * 0.9f + 0.1f
                        var fadeOut = MathHelper.clamp((1 - timeSinceCreation / 200.0) * 10, 0.0, 1.0).toFloat()
                        fadeOut *= fadeOut
                        val alpha = fadeOut * chatOpacity


                        val before = text.substring(0 until (match.groups[0]?.range?.first ?: 0))
                        val id = match.groupValues[1]
                        val formatting = deconstruct(id)
                        if (formatting < EnumLegend.values().size) {
                            val element = EnumLegend.values()[formatting]
                            val x = chatX + 3 + Minecraft.getMinecraft().fontRenderer.getStringWidth(before).toFloat()
                            val y = chatY - (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT) * (idx.toFloat() - shift)
                            GlStateManager.pushMatrix()
                            GlStateManager.scale(0.5, 0.5, 0.5)
                            GlStateManager.translate(x + 0.5f, y, 0f)
                            val prevMultiplier = RenderUtil.alphaMultiplier
                            RenderUtil.alphaMultiplier *= alpha
                            RenderSymbol.renderSymbol(x, y, element)
                            RenderUtil.alphaMultiplier = prevMultiplier
                            GlStateManager.popMatrix()
                        }
                    }
                }

                val elementMatches = ELEMENT_PATTERN.findAll(text)
                for (match in elementMatches) {
                    var timeSinceCreation = updateCounter - line.updatedCounter
                    if (chatGui.chatOpen) timeSinceCreation = 0
                    if (timeSinceCreation < 200) {
                        val chatOpacity = Minecraft.getMinecraft().gameSettings.chatOpacity * 0.9f + 0.1f
                        var fadeOut = MathHelper.clamp((1 - timeSinceCreation / 200.0) * 10, 0.0, 1.0).toFloat()
                        fadeOut *= fadeOut
                        val alpha = fadeOut * chatOpacity


                        val before = text.substring(0 until (match.groups[0]?.range?.first ?: 0))
                        val id = match.groupValues[1]
                        val formatting = deconstruct(id)
                        if (formatting < EnumSpellElement.values().size) {
                            val element = EnumSpellElement.values()[formatting]
                            val x = chatX + 3 + Minecraft.getMinecraft().fontRenderer.getStringWidth(before).toFloat()
                            val y = chatY - (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT) * (idx.toFloat() - shift)
                            GlStateManager.pushMatrix()
                            GlStateManager.scale(0.5, 0.5, 0.5)
                            GlStateManager.translate(x + 0.5f, y, 0f)
                            val prevMultiplier = RenderUtil.alphaMultiplier
                            RenderUtil.alphaMultiplier *= alpha
                            RenderSymbol.renderSymbol(x, y, element)
                            RenderUtil.alphaMultiplier = prevMultiplier
                            GlStateManager.popMatrix()
                        }
                    }
                }
                idx++
            }
        }

    }
}

