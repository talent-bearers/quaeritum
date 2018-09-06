package eladkay.quaeritum.client.core

import com.teamwizardry.librarianlib.core.client.ClientTickHandler
import eladkay.quaeritum.api.spell.ElementHandler
import eladkay.quaeritum.api.spell.EnumSpellElement
import eladkay.quaeritum.api.spell.render.RenderUtil
import eladkay.quaeritum.client.gui.GuiCodex
import eladkay.quaeritum.client.render.RenderSymbol
import eladkay.quaeritum.client.render.renderWheel
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.math.MathHelper
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.GL11
import java.util.*

/**
 * Created by LordSaad.
 */
object HudSymbolRenderer {

    private var symbols = arrayOfNulls<EnumSpellElement>(9)
    private var symbolsShouldTick = BooleanArray(9)
    private var symbolsTick = IntArray(9)
    private var symbolsScale = DoubleArray(9)

    private var currentAngle = -1.0
    private var angleTick = 0.0
    private var prevAngle = 0.0
    private var angleShouldTick = false

    private var wasPreviouslyGui = false

    private var sepShouldTick = true
    private var sepTick = 0
    private var sepScale = 0.0

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    private fun addSymbol(spellElement: EnumSpellElement) {
        symbols[symbols.size - 1] = spellElement
        val index = symbols.size - 1
        symbolsTick[index] = 0
        symbolsShouldTick[index] = false
        symbolsScale[index] = 0.0
    }

    private fun clear() {
        symbols = arrayOfNulls(9)
        symbolsShouldTick = BooleanArray(9)
        Arrays.fill(symbolsShouldTick, false)
        symbolsTick = IntArray(9)
        symbolsScale = DoubleArray(9)
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun render(event: RenderGameOverlayEvent.Post) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return

        val player = Minecraft.getMinecraft().player

        val elements = ElementHandler.getReagentsTyped(player)

        val isGui = Minecraft.getMinecraft().currentScreen is GuiCodex
        val cX = event.resolution.scaledWidth / 2.0
        val cY = event.resolution.scaledHeight / 2.0
        val scaleMin = (event.resolution.scaleFactor * 15).toDouble()
        val scaleMax = scaleMin * 5
        val time = 30.0


        val wheelSize = MathHelper.sqrt(event.resolution.scaledHeight * event.resolution.scaledHeight +
                event.resolution.scaledWidth * event.resolution.scaledWidth.toFloat()) / 2
        val ratio = event.resolution.scaledHeight.toFloat() / event.resolution.scaledWidth

        renderWheel(wheelSize / 2, 2 * wheelSize, wheelSize * 3 / 4, ratio, cX.toFloat(), cY.toFloat(),
                0.05f, 0.1f, 10, Random(), *EnumSpellElement.values())

        if (elements.size > symbols.size) {
            for (i in symbols.size until elements.size) {
                symbolsTick[i] = 0
                symbolsShouldTick[i] = false
                symbolsScale[i] = 0.0
            }
            angleShouldTick = true
            angleTick = 0.0
        } else if (elements.size < symbols.size) {
            clear()
        }
        symbols = Array(elements.size) { elements[it] }

        if (wasPreviouslyGui != isGui) {
            wasPreviouslyGui = isGui
            for (i in symbols.indices) {
                symbolsTick[i] = 0
                symbolsShouldTick[i] = false
            }
            sepShouldTick = true
            sepTick = 0
        }

        val startingAngle = (event.partialTicks + ClientTickHandler.ticks) * Math.PI / 120
        val angleSep = 2.0 * Math.PI / (symbols.size + 1)
        if (currentAngle == -1.0) {
            currentAngle = angleSep
            prevAngle = currentAngle
            angleTick = time
        }

        if (currentAngle < angleSep) {
            if (!angleShouldTick) angleShouldTick = true
        }
        if (angleTick < time) {
            angleTick++
            currentAngle += angleTick / time * (angleSep - currentAngle)
        } else {
            angleShouldTick = false
            prevAngle = angleSep
        }


        symbolsShouldTick.indices
                .filter { symbolsShouldTick[it] }
                .forEach { symbolsTick[it] = symbolsTick[it] + 1 }

        // SEPARATOR //
        run {
            if (!isGui && symbols.isEmpty()) return
            if (sepShouldTick) sepTick++

            var scale = sepScale

            if (sepTick < time) {

                if (!sepShouldTick) {
                    sepShouldTick = true
                }

                if (wasPreviouslyGui) {
                    sepScale = Math.abs(1 - MathHelper.sqrt(1 - Math.pow(1 - sepTick / time, 2.0)) * (if (isGui) scaleMax else scaleMin) + event.partialTicks)
                    scale = sepScale
                } else {
                    sepScale = Math.abs(-scaleMin + -(1 - MathHelper.sqrt(1 - Math.pow(1 - sepTick / time, 2.0))) * (if (isGui) scaleMax else scaleMin) + event.partialTicks.toDouble())
                    scale = sepScale
                }
            } else if (sepShouldTick) {
                sepShouldTick = false
            }

            GlStateManager.color(1f, 1f, 1f, 1f)
            GlStateManager.enableBlend()
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE)
            GlStateManager.shadeModel(GL11.GL_SMOOTH)
            GlStateManager.disableTexture2D()
            val tess = Tessellator.getInstance()
            val buffer = tess.buffer
            buffer.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_COLOR)
            RenderUtil.renderNGon(buffer,
                    cX + MathHelper.cos(startingAngle.toFloat()) * scale - 0.5,
                    cY + MathHelper.sin(startingAngle.toFloat()) * scale - 0.5,
                    1f, 1f, 1f, 7.5, 5.0, RenderUtil.SEGMENTS_CIRCLE)
            tess.draw()
        }
        // SEPARATOR //

        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.enableBlend()
        GlStateManager.shadeModel(GL11.GL_FLAT)
        GlStateManager.enableTexture2D()
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA)

        if (symbols.isNotEmpty())
            for (i in symbols.indices) {
                val element = symbols[i] ?: continue

                var scale = symbolsScale[i]

                if (symbolsTick[i] < time) {

                    if (!symbolsShouldTick[i]) {
                        symbolsShouldTick[i] = true
                    }

                    if (wasPreviouslyGui) {
                        symbolsScale[i] = Math.abs(1 - MathHelper.sqrt(1 - Math.pow(1 - symbolsTick[i] / time, 2.0)) * (if (isGui) scaleMax else scaleMin) + event.partialTicks)
                        scale = symbolsScale[i]
                    } else {
                        symbolsScale[i] = Math.abs(-scaleMin + -(1 - MathHelper.sqrt(1 - Math.pow(1 - symbolsTick[i] / time, 2.0))) * (if (isGui) scaleMax else scaleMin) + event.partialTicks.toDouble())
                        scale = symbolsScale[i]
                    }
                } else if (symbolsShouldTick[i]) {
                    symbolsShouldTick[i] = false
                }

                val angle = startingAngle + (i + 1) * currentAngle
                val x = cX + scale * MathHelper.cos(angle.toFloat()) - 7.5
                val y = cY + scale * MathHelper.sin(angle.toFloat()) - 7.5
                RenderSymbol.renderSymbol(x.toFloat(), y.toFloat(), element)
            }

        //GlStateManager.popMatrix();
    }
}
