package eladkay.quaeritum.client.render

import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.util.glu.GLU
import org.lwjgl.util.glu.GLUtessellator
import org.lwjgl.util.glu.GLUtessellatorCallbackAdapter

/**
 * @author WireSegal
 * Created at 3:13 PM on 8/11/18.
 */

data class Vertex(val x: Double, val y: Double, val z: Double,
                  val r: Float, val g: Float, val b: Float, val a: Float = 1f)

class BufferTessellationCallback(val ts: Tessellator) : GLUtessellatorCallbackAdapter() {

    val bf: BufferBuilder = ts.buffer

    override fun vertex(vertexData: Any) {
        val vx = vertexData as Vertex
        bf.pos(vx.x, vx.y, vx.z).color(vx.r, vx.g, vx.b, vx.a).endVertex()
    }

    override fun end() = ts.draw()

    override fun begin(type: Int) = bf.begin(type, DefaultVertexFormats.POSITION_COLOR)
}

@DslMarker
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class GluDsl

@GluDsl
class GluPolygonDsl(val tess: GLUtessellator) {

    val contour = GluContourDsl(tess)

    inline fun contour(call: GluContourDsl.() -> Unit) {
        tess.gluTessBeginContour()
        contour.call()
        tess.gluTessEndContour()
    }
}

@GluDsl
@Suppress("NOTHING_TO_INLINE")
class GluContourDsl(val tess: GLUtessellator) {
    inline fun vertex(x: Double, y: Double, z: Double,
                      r: Float, g: Float, b: Float, a: Float = 1f) =
            tess.gluTessVertex(doubleArrayOf(x, y, z), 0, Vertex(x, y, z, r, g, b, a))

    inline fun vertex(x: Float, y: Float, z: Float,
                      r: Float, g: Float, b: Float, a: Float = 1f) =
            vertex(x.toDouble(), y.toDouble(), z.toDouble(), r, g, b, a)

    inline fun vertex(x: Double, y: Double, z: Double,
                      r: Int, g: Int, b: Int, a: Int = 0xff) =
            vertex(x, y, z, r / 255f, g / 255f, b / 255f, a / 255f)

    inline fun vertex(x: Float, y: Float, z: Float,
                      r: Int, g: Int, b: Int, a: Int = 0xff) =
            vertex(x.toDouble(), y.toDouble(), z.toDouble(), r, g, b, a)

    inline fun vertex(x: Double, y: Double, z: Double, c: Int, a: Int = 0xff) =
            vertex(x, y, z, (0xff0000 and c) shr 16, (0xff00 and c) shr 8, 0xff and c, a)

    inline fun vertex(x: Float, y: Float, z: Float, c: Int, a: Int = 0xff) =
            vertex(x.toDouble(), y.toDouble(), z.toDouble(), c, a)
}

inline fun gluTessellate(call: GluPolygonDsl.() -> Unit) {
    val callback = BufferTessellationCallback(Tessellator.getInstance())

    val tess = GLU.gluNewTess()
    tess.gluTessCallback(GLU.GLU_TESS_BEGIN, callback)
    tess.gluTessCallback(GLU.GLU_TESS_END, callback)
    tess.gluTessCallback(GLU.GLU_TESS_VERTEX, callback)

    tess.gluTessBeginPolygon(null)
    GluPolygonDsl(tess).call()
    tess.gluTessEndPolygon()

    tess.gluDeleteTess()
}
