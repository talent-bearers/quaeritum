package eladkay.quaeritum.client.render

import com.teamwizardry.librarianlib.core.client.ClientTickHandler
import eladkay.quaeritum.api.spell.EnumSpellElement
import net.minecraft.client.renderer.GlStateManager
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.GL11


/**
 * @author WireSegal
 * Created at 8:16 PM on 7/26/17.
 */
@SideOnly(Side.CLIENT)
object RenderSymbol {

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onHUDRender(e: RenderGameOverlayEvent.Post) {
        if (e.type != RenderGameOverlayEvent.ElementType.ALL) return

        val x = e.resolution.scaledWidth / 2
        val y = e.resolution.scaledHeight * 3 / 4

        val symbolIdx = (ClientTickHandler.ticksInGame / 100) % EnumSpellElement.values().size
        renderSymbol(x, y, EnumSpellElement.values()[symbolIdx])
    }

    fun renderSymbol(x: Int, y: Int, element: EnumSpellElement) {
        val symbols = element.symbolInstructions
        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE)
        GlStateManager.shadeModel(GL11.GL_SMOOTH)
        GlStateManager.disableTexture2D()
        for (symbol in symbols) symbol.render(element, x, y)
        GlStateManager.shadeModel(GL11.GL_FLAT)
        GlStateManager.enableTexture2D()
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA)
    }
}
