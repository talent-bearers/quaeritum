package eladkay.quaeritum.client.render

import com.teamwizardry.librarianlib.features.kotlin.plus
import eladkay.quaeritum.common.Quaeritum
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.GL11
import java.util.regex.Pattern

/**
 * @author WireSegal
 * Created at 5:33 PM on 11/5/16.
 */
object RemainingItemsRenderHandler {

    private val maxTicks = 30
    private val leaveTicks = 20

    private var stack: ItemStack? = null
    private var customString: String? = null
    private var ticks: Int = 0
    private var count: Int = 0

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun onScreenRender(e: RenderGameOverlayEvent.Post) {
        if (e.type == RenderGameOverlayEvent.ElementType.ALL)
            render(e.resolution, e.partialTicks)
    }

    @SideOnly(Side.CLIENT)
    fun render(resolution: ScaledResolution, partTicks: Float) {
        if (ticks > 0 && stack != null) {
            val pos = maxTicks - ticks
            val mc = Minecraft.getMinecraft()
            val x = resolution.scaledWidth / 2 + 10 + Math.max(0, pos - leaveTicks)
            val y = resolution.scaledHeight / 2

            val start = maxTicks - leaveTicks
            val alpha = if (ticks + partTicks > start) 1f else (ticks + partTicks) / start

            GlStateManager.disableAlpha()
            GlStateManager.enableBlend()
            GlStateManager.enableRescaleNormal()
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

            GlStateManager.color(1f, 1f, 1f, alpha)
            RenderHelper.enableGUIStandardItemLighting()
            val xp = x + (16f * (1f - alpha)).toInt()
            GlStateManager.translate(xp.toDouble(), y.toDouble(), 0.0)
            GlStateManager.scale(alpha, 1f, 1f)
            mc.renderItem.renderItemAndEffectIntoGUI(stack, 0, 0)
            GlStateManager.scale(1f / alpha, 1f, 1f)
            GlStateManager.translate(-xp.toDouble(), -y.toDouble(), 0.0)
            RenderHelper.disableStandardItemLighting()
            GlStateManager.color(1f, 1f, 1f, 1f)
            GlStateManager.enableBlend()

            var text = ""

            if (customString == null) {
                if (stack != null) {
                    text = TextFormatting.GREEN + stack!!.displayName
                    if (count >= 0) {
                        val max = stack!!.maxStackSize
                        val stacks = count / max
                        val rem = count % max

                        if (stacks == 0)
                            text = "" + count
                        else
                            text = count.toString() + " (" + TextFormatting.AQUA + stacks + TextFormatting.RESET + "*" + TextFormatting.GRAY + max + TextFormatting.RESET + "+" + TextFormatting.YELLOW + rem + TextFormatting.RESET + ")"
                    } else if (count == -1)
                        text = "\u221E"
                }
            } else
                text = customString!!

            val color = 0x00FFFFFF or ((alpha * 0xFF).toInt() shl 24)
            mc.fontRenderer.drawStringWithShadow(text, x + 20f, y + 6f, color)

            GlStateManager.disableBlend()
            GlStateManager.enableAlpha()
        }
    }

    @SideOnly(Side.CLIENT)
    fun tick() {
        if (ticks > 0)
            --ticks
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun clientTick(e: TickEvent.ClientTickEvent) {
        if (e.phase == TickEvent.Phase.END)
            tick()
    }

    fun set(stack: ItemStack?, str: String? = null, count: Int = 0) {
        this.stack = stack
        this.count = count
        customString = str
        ticks = if (stack == null) 0 else maxTicks
    }

    fun set(player: EntityPlayer, displayStack: ItemStack, pattern: Pattern) {
        val count = (0..player.inventory.sizeInventory - 1)
                .mapNotNull { player.inventory.getStackInSlot(it) }
                .filter { pattern.matcher(it.unlocalizedName).find() }
                .sumBy { it.count }

        Quaeritum.proxy.setRemainingItemDisplay(null, displayStack, null, count)
    }

}
