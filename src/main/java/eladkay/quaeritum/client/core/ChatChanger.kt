package eladkay.quaeritum.client.core

import eladkay.quaeritum.api.spell.EnumLegend
import eladkay.quaeritum.api.spell.render.RenderUtil
import eladkay.quaeritum.client.render.RenderSymbol
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ChatLine
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
            UUID.fromString("2ac3ed86-bf32-4f64-a010-92b095eed279") to EnumLegend.DOORWAYS, // Demoniaque1
            UUID.fromString("8c826f34-113b-4238-a173-44639c53b6e6") to EnumLegend.GROWTH, // Vazkii
            UUID.fromString("0f95811a-b3b6-4dba-ba03-4adfec7cf5ab") to EnumLegend.MYSTERIES, // Azanor
            UUID.fromString("9417e568-6054-4a47-b1b5-93f747cfa4ce") to EnumLegend.TWISTED, // Elucent
            UUID.fromString("ccf731e2-bb8c-4426-8876-cbee224e34bd") to EnumLegend.INGENUITY, // KingLemming
            UUID.fromString("117de7ec-f75a-4d1f-9a8b-20e1f1b642ae") to EnumLegend.SHATTERED, // MrCodeWarrior
            UUID.fromString("562193d8-7da3-48c0-82fb-89cde9f2bcb7") to EnumLegend.ARCHIVE, // CobrasBane
            UUID.fromString("d475af59-d73c-42be-90ed-f1a78f10d452") to EnumLegend.ELSEWHERE, // Tristaric
            UUID.fromString("95fe0728-e1bd-4989-a980-3d8976aedda9") to EnumLegend.DISDAIN, // WayOfFlowingTime
            UUID.fromString("b8ad70cc-5178-4369-af46-abfa329ce305") to EnumLegend.DECLARATION, // machinespray
            UUID.fromString("7f6971c5-fb58-4519-a975-b1b5766e92d1") to EnumLegend.WONDER // HellfirePVP
    )

    var chatX = 0
    var chatY = 0

    @SubscribeEvent
    fun playerName(event: PlayerEvent.NameFormat) {
        val uid = event.entityPlayer.uniqueID
        val legend = map[uid] ?: return
        event.displayname = "${TextFormatting.GOLD}${TextFormatting.values()[legend.ordinal]}${TextFormatting.RESET}   ${TextFormatting.BOLD}${TextFormatting.RESET}${event.displayname}"
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun getChatPos(event: RenderGameOverlayEvent.Chat) {
        chatX = event.posX
        chatY = event.posY
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun renderSymbols(event: RenderGameOverlayEvent.Post) {
        val c = Minecraft.getMinecraft().ingameGUI.chatGUI
        if (event.type == RenderGameOverlayEvent.ElementType.CHAT) {
            try {
                val fLines = c.javaClass.getDeclaredField("drawnChatLines")
                fLines.isAccessible = true
                val fCounter = Minecraft.getMinecraft().ingameGUI.javaClass.superclass.getDeclaredField("updateCounter")
                fCounter.isAccessible = true
                val updateCounter: Int = fCounter.getInt(Minecraft.getMinecraft().ingameGUI)
                val chatLines: List<ChatLine> = (fLines.get(c) as List<*>).filterIsInstance<ChatLine>()

                var i = 0
                while (c.chatOpen && i < chatLines.size || !c.chatOpen && i < chatLines.size && i < 10) {
                    val l = chatLines[i]
                    val s = l.chatComponent.unformattedText
                    val re = "(.*)(?:${TextFormatting.GOLD}\u00a7([0-9A-Fa-fK-Ok-oRr])${TextFormatting.RESET}   ${TextFormatting.BOLD}${TextFormatting.RESET})".toRegex()
                    val matches = re.findAll(s)
                    for (match in matches) {
                        val j1 = updateCounter - l.updatedCounter
                        if (j1 < 200 || c.chatOpen) {
                            val f = Minecraft.getMinecraft().gameSettings.chatOpacity * 0.9f + 0.1f
                            var d0 = MathHelper.clamp((1 - j1 / 200.0) * 10, 0.0, 1.0).toFloat()
                            d0 *= d0
                            var l1 = d0 * f
                            if (c.chatOpen) l1 = 1f


                            val before = match.groupValues[1]
                            val id = match.groupValues[2]
                            val formatting = TextFormatting.values().firstOrNull { it.toString() == "\u00a7$id" }
                            if (formatting != null && formatting.ordinal < EnumLegend.values().size) {
                                val element = EnumLegend.values()[formatting.ordinal]
                                val x = chatX + 3 + Minecraft.getMinecraft().fontRenderer.getStringWidth(before).toFloat()
                                val y = chatY - (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT) * i.toFloat()
                                GlStateManager.pushMatrix()
                                GlStateManager.scale(0.5, 0.5, 0.5)
                                GlStateManager.translate(x + 0.5f, y, 0f)
                                RenderUtil.alphaMultiplier = l1
                                RenderSymbol.renderSymbol(x, y, element)
                                RenderUtil.alphaMultiplier = 1f
                                GlStateManager.popMatrix()
                            }
                        }
                    }
                    i++
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}

