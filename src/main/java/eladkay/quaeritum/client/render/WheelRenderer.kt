package eladkay.quaeritum.client.render

import eladkay.quaeritum.api.spell.render.ISymbolCarrier
import eladkay.quaeritum.api.spell.render.RenderUtil
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.GlStateManager.SourceFactor
import net.minecraft.util.math.MathHelper
import org.lwjgl.opengl.GL11
import java.util.*

/**
 * @author WireSegal
 * Created at 1:41 PM on 8/11/18.
 */
private fun warpData(sections: Int, warpPoints: Int): FloatArray {
    val data = FloatArray(1 + (sections + 2) * warpPoints * 2)
    data[0] = sections.toFloat()
    return data
}

private fun index(sections: Int, section: Int, warpPoint: Int, x: Boolean) =
        1 +
                ((if (section < 0) sections - 1 - section else (section % sections)) +
                warpPoint * (sections + 2)) * 2 +
                if (x) 0 else 1

private operator fun FloatArray.get(section: Int, warpPoint: Int, x: Boolean): Float {
    val sections = this[0].toInt()
    return this[index(sections, section, warpPoint, x)]
}

private operator fun FloatArray.set(section: Int, warpPoint: Int, x: Boolean, value: Float) {
    val sections = this[0].toInt()
    this[index(sections, section, warpPoint, x)] = value
}

private fun FloatArray.fillPosition(innerRadius: Float, outerRadius: Float, ratio: Float,
                         warpPoint: Int, warpPoints: Int,
                         section: Int, sections: Int,
                         x: Float, y: Float,
                         angularWarp: Float, radialWarp: Float,
                         random: Random) {
    val angular = (random.nextFloat() - 0.5f) * angularWarp
    val radial = (random.nextFloat() - 0.5f) * radialWarp

    val relRadius = (outerRadius - innerRadius + radial) * warpPoint / (warpPoints - 1)
    val radius = relRadius + innerRadius
    val angle = (section - 0.5f) * 2 * Math.PI.toFloat() / sections + angular

    val xRatio = if (ratio > 1) 1 / ratio else 1f
    val yRatio = if (ratio < 1) ratio else 1f

    val relX = MathHelper.cos(angle) * radius * xRatio
    val relY = MathHelper.sin(angle) * radius * yRatio

    this[section, warpPoint, true] = relX + x
    this[section, warpPoint, false] = relY + y
}


private fun FloatArray.fillEdge(radius: Float, index: Int, ratio: Float,
                                warpPoint: Int, warpPoints: Int,
                                x: Float, y: Float,
                                angularWarp: Float, radialWarp: Float,
                                random: Random) {
    val angular = (random.nextFloat() - 0.5f) * angularWarp
    val radial = (random.nextFloat() - 0.5f) * radialWarp

    val realRadius = radius + radial
    val angle = warpPoint * 2 * Math.PI.toFloat() / warpPoints + angular

    val xRatio = if (ratio > 1) 1 / ratio else 1f
    val yRatio = if (ratio < 1) ratio else 1f

    val relX = MathHelper.cos(angle) * realRadius * xRatio
    val relY = MathHelper.sin(angle) * realRadius * yRatio

    this[index, warpPoint, true] = relX + x
    this[index, warpPoint, false] = relY + y

}

private fun FloatArray.renderSections(symbols: Array<out ISymbolCarrier>, warpPoints: Int) {
    GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA)
    for (s in 0 until symbols.size) {
        val symbol = symbols[s]
        val c = symbol.color()
        val a = 0x40
        gluTessellate {
            contour {
                for (i in 0 until warpPoints / symbols.size)
                    vertex(get(-1, i, true), get(-1, i, false), 0f, c, a)
                for (i in 0 until warpPoints)
                    vertex(get(s + 1, i, true), get(s + 1, i, false), 0f, c, a)
                for (i in (warpPoints / symbols.size) - 1 downTo 0)
                    vertex(get(-2, i, true), get(-2, i, false), 0f, c, a)
                for (i in warpPoints - 1 downTo 0)
                    vertex(get(s, i, true), get(s, i, false), 0f, c, a)
            }
        }
    }
}

private fun FloatArray.renderLines(symbols: Array<out ISymbolCarrier>, warpPoints: Int, ratio: Float,
                                   innerRadius: Float, outerRadius: Float, symbolRadius: Float,
                                   x: Float, y: Float) {
    GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE)

    val prev = RenderUtil.alphaMultiplier
    RenderUtil.alphaMultiplier /= 2

    val xRatio = if (ratio > 1) 1 / ratio else 1f
    val yRatio = if (ratio < 1) ratio else 1f

    for (s in 0 until symbols.size) {
        val symbol = symbols[s]
        val a = s * 2 * Math.PI.toFloat() / symbols.size

        val symX = MathHelper.cos(a) * symbolRadius * xRatio
        val symY = MathHelper.sin(a) * symbolRadius * yRatio

        RenderSymbol.renderSymbol(symX + x - 7.5f, symY + y - 7.5f, symbol)
    }

    RenderUtil.alphaMultiplier = prev
}

fun renderWheel(innerRadius: Float, outerRadius: Float, symbolRadius: Float,
                ratio: Float,
                x: Float, y: Float,
                angularWarp: Float, radialWarp: Float,
                warpPoints: Int,
                random: Random,
                vararg symbols: ISymbolCarrier) {
    val sections = symbols.size
    val pointData = warpData(sections, warpPoints)

    for (section in 0 until sections) for (warpPoint in 0 until warpPoints)
        pointData.fillPosition(innerRadius, outerRadius, ratio,
            warpPoint, warpPoints,
            section, sections,
            x, y,
            angularWarp, radialWarp,
            random)
    for (warpPoint in 0 until warpPoints)
        pointData.fillEdge(innerRadius, -1, ratio,
                warpPoint, warpPoints,
                x, y,
                angularWarp, radialWarp,
                random)
    for (warpPoint in 0 until warpPoints)
        pointData.fillEdge(innerRadius, -2, ratio,
                warpPoint, warpPoints,
                x, y,
                angularWarp, radialWarp,
                random)

    GlStateManager.enableBlend()
    GlStateManager.shadeModel(GL11.GL_SMOOTH)
    GlStateManager.disableTexture2D()

    pointData.renderSections(symbols, warpPoints)
    pointData.renderLines(symbols, warpPoints, ratio, innerRadius, outerRadius, symbolRadius, x, y)

    GlStateManager.shadeModel(GL11.GL_FLAT)
    GlStateManager.enableTexture2D()
}
