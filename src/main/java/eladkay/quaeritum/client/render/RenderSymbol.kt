package eladkay.quaeritum.client.render

import eladkay.quaeritum.api.spell.render.ISymbolCarrier
import net.minecraft.client.renderer.GlStateManager
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.GL11


/**
 * @author WireSegal
 * Created at 8:16 PM on 7/26/17.
 */
@SideOnly(Side.CLIENT)
object RenderSymbol {

    fun renderSymbol(x: Float, y: Float, element: ISymbolCarrier) {
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
