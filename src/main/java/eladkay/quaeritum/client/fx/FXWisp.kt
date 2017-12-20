/**
 * This class was created by . It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 *
 * File Created @ [? (GMT)]
 */
package eladkay.quaeritum.client.fx

import eladkay.quaeritum.common.lib.LibLocations
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.Particle
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.Entity
import net.minecraft.world.World
import net.minecraftforge.fml.client.FMLClientHandler
import org.lwjgl.opengl.GL11
import java.util.*

class FXWisp(world: World, internal var initialX: Double, internal var initialY: Double, internal var initialZ: Double, size: Float, red: Float, green: Float, blue: Float, distanceLimit: Boolean, depthTest: Boolean, maxAgeMul: Float) : Particle(world, initialX, initialY, initialZ, 0.0, 0.0, 0.0) {
    private val moteParticleScale: Float
    private val moteHalfLife: Int
    var distanceLimit = true
    var tinkle = false
    var blendmode = 1
    internal var tt: Long = 0
    // Queue values
    private var f: Float = 0.toFloat()
    private var f1: Float = 0.toFloat()
    private var f2: Float = 0.toFloat()
    private var f3: Float = 0.toFloat()
    private var f4: Float = 0.toFloat()
    private var f5: Float = 0.toFloat()
    private var depthTest = true

    init {
        particleRed = red
        particleGreen = green
        particleBlue = blue
        particleAlpha = 0.5f // So MC renders us on the alpha layer, value not actually used
        particleGravity = 0f
        motionX = 0.0
        motionY = 0.0
        motionZ = 0.0
        particleScale *= size
        moteParticleScale = particleScale
        particleMaxAge = (28.0 / (Math.random() * 0.3 + 0.7) * maxAgeMul).toInt()
        this.depthTest = depthTest

        moteHalfLife = particleMaxAge / 2
        setSize(0.01f, 0.01f)
        val renderentity = FMLClientHandler.instance().client.renderViewEntity

        if (distanceLimit) {
            var visibleDistance = 50
            if (!FMLClientHandler.instance().client.gameSettings.fancyGraphics)
                visibleDistance = 25

            if (renderentity == null || renderentity.getDistance(posX, posY, posZ) > visibleDistance)
                particleMaxAge = 0
        }

        prevPosX = posX
        prevPosY = posY
        prevPosZ = posZ
    }

    private fun renderQueued(tessellator: Tessellator, depthEnabled: Boolean) {
        if (depthEnabled)
            ParticleRenderDispatcher.wispFxCount++
        else
            ParticleRenderDispatcher.depthIgnoringWispFxCount++

        var agescale = particleAge.toFloat() / moteHalfLife.toFloat()
        if (agescale > 1f)
            agescale = 2 - agescale

        particleScale = moteParticleScale * agescale

        val f10 = 0.5f * particleScale
        val f11 = (prevPosX + (posX - prevPosX) * f - Particle.interpPosX).toFloat()
        val f12 = (prevPosY + (posY - prevPosY) * f - Particle.interpPosY).toFloat()
        val f13 = (prevPosZ + (posZ - prevPosZ) * f - Particle.interpPosZ).toFloat()
        val combined = 15 shl 20 or (15 shl 4)
        val k3 = combined shr 16 and 0xFFFF
        val l3 = combined and 0xFFFF
        tessellator.buffer.pos(f11 - f1 * f10 - f4 * f10.toDouble(), (f12 - f2 * f10).toDouble(), f13 - f3 * f10 - f5 * f10.toDouble()).tex(0.0, 1.0).lightmap(k3, l3).color(particleRed, particleGreen, particleBlue, 0.5f).endVertex()
        tessellator.buffer.pos((f11 - f1 * f10 + f4 * f10).toDouble(), (f12 + f2 * f10).toDouble(), (f13 - f3 * f10 + f5 * f10).toDouble()).tex(1.0, 1.0).lightmap(k3, l3).color(particleRed, particleGreen, particleBlue, 0.5f).endVertex()
        tessellator.buffer.pos(f11 + f1 * f10 + f4 * f10.toDouble(), (f12 + f2 * f10).toDouble(), f13 + f3 * f10 + f5 * f10.toDouble()).tex(1.0, 0.0).lightmap(k3, l3).color(particleRed, particleGreen, particleBlue, 0.5f).endVertex()
        tessellator.buffer.pos((f11 + f1 * f10 - f4 * f10).toDouble(), (f12 - f2 * f10).toDouble(), (f13 + f3 * f10 - f5 * f10).toDouble()).tex(0.0, 0.0).lightmap(k3, l3).color(particleRed, particleGreen, particleBlue, 0.5f).endVertex()
    }

    override fun renderParticle(wr: BufferBuilder, entity: Entity?, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
        this.f = f
        this.f1 = f1
        this.f2 = f2
        this.f3 = f3
        this.f4 = f4
        this.f5 = f5

        if (depthTest)
            queuedRenders.add(this)
        else
            queuedDepthIgnoringRenders.add(this)
    }

    override fun onUpdate() {
        tt++
        prevPosX = posX
        prevPosY = posY
        prevPosZ = posZ

        if (particleAge++ >= particleMaxAge)
            setExpired()

        /* motionY -= 0.04D * particleGravity;
        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        motionX *= 0.98000001907348633D;
        motionY *= 0.98000001907348633D;
        motionZ *= 0.98000001907348633D;*/
        posX = initialX + Math.sin(Math.toRadians(tt.toDouble()))
        posY = initialY
        posZ = initialZ + Math.cos(Math.toRadians(tt.toDouble()))
    }

    fun setGravity(value: Float) {
        particleGravity = value
    }

    fun setSpeed(mx: Float, my: Float, mz: Float) {
        motionX = mx.toDouble()
        motionY = my.toDouble()
        motionZ = mz.toDouble()
    }

    companion object {

        val particles = LibLocations.MISC_WISP_LARGE

        private val queuedRenders = ArrayDeque<FXWisp>()
        private val queuedDepthIgnoringRenders = ArrayDeque<FXWisp>()

        fun dispatchQueuedRenders(tessellator: Tessellator) {
            ParticleRenderDispatcher.wispFxCount = 0
            ParticleRenderDispatcher.depthIgnoringWispFxCount = 0//
            GlStateManager.color(1.0f, 1.0f, 1.0f, 0.75f)
            Minecraft.getMinecraft().renderEngine.bindTexture(particles)

            if (!queuedRenders.isEmpty()) {
                tessellator.buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR)
                for (wisp in queuedRenders)
                    wisp.renderQueued(tessellator, true)
                tessellator.draw()
            }

            if (!queuedDepthIgnoringRenders.isEmpty()) {
                GlStateManager.disableDepth()
                tessellator.buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR)
                for (wisp in queuedDepthIgnoringRenders)
                    wisp.renderQueued(tessellator, false)
                tessellator.draw()
                GlStateManager.enableDepth()
            }

            queuedRenders.clear()
            queuedDepthIgnoringRenders.clear()
        }
    }
}
