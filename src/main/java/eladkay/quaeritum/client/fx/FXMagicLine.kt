package eladkay.quaeritum.client.fx

import eladkay.quaeritum.common.lib.LibLocations
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.Particle
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.world.World
import java.util.*

//copied with permission from elucent's roots
class FXMagicLine(worldIn: World, internal var initialX: Double, internal var initialY: Double, internal var initialZ: Double, vx: Double, vy: Double, vz: Double, r: Double, g: Double, b: Double) : Particle(worldIn, initialX, initialY, initialZ, 0.0, 0.0, 0.0) {
    var colorR = 0.0
    var colorG = 0.0
    var colorB = 0.0
    var lifetime = 8
    var texture = LibLocations.MAGICLINEFX
    internal var random = Random()
    internal var tt: Long = 0
    internal var xRotate: Float = 0.toFloat()
    internal var zRotate: Float = 0.toFloat()
    private val rotationModifier = 0.2

    init {
        this.colorR = r
        this.colorG = g
        this.colorB = b
        if (this.colorR > 1.0)
            this.colorR = this.colorR / 255.0
        if (this.colorG > 1.0)
            this.colorG = this.colorG / 255.0
        if (this.colorB > 1.0)
            this.colorB = this.colorB / 255.0
        this.setRBGColorF(1f, 1f, 1f)
        this.particleMaxAge = 8
        this.particleGravity = 0.0f
        this.motionX = (vx - initialX) / this.particleMaxAge.toDouble()
        this.motionY = (vy - initialY) / this.particleMaxAge.toDouble()
        this.motionZ = (vz - initialZ) / this.particleMaxAge.toDouble()
        val sprite = Minecraft.getMinecraft().textureMapBlocks.getAtlasSprite(texture.toString())
        this.setParticleTexture(sprite)
    }

    override fun isTransparent(): Boolean {
        return true
    }

    override fun getFXLayer(): Int {
        return 1
    }

    override fun onUpdate() {
        super.onUpdate()
        tt++
        val lifeCoeff = (this.particleMaxAge.toFloat() - this.particleAge.toFloat()) / this.particleMaxAge.toFloat()
        val alpha = 0.5f
        this.particleRed = Math.min(1.0f, colorR.toFloat() * (1.5f - lifeCoeff) + lifeCoeff)
        this.particleGreen = Math.min(1.0f, colorG.toFloat() * (1.5f - lifeCoeff) + lifeCoeff)
        this.particleBlue = Math.min(1.0f, colorB.toFloat() * (1.5f - lifeCoeff) + lifeCoeff)
        GlStateManager.enableBlend()
        this.particleAlpha = alpha
        if (ROTATION_MODE == 2) {
            //wiresegal's approach
            //https://www.youtube.com/watch?v=V-tja0YnCM8
            xRotate = Math.sin(tt * rotationModifier / 2).toFloat() / 2f
            zRotate = Math.cos(tt * rotationModifier).toFloat() / 2f / 2f
            posX = xRotate + initialX
            posZ = zRotate + initialZ
            motionX = (-xRotate).toDouble()
            motionZ = (-zRotate).toDouble()
        } else if (ROTATION_MODE == 1) {
            //elucent's approach
            //https://www.youtube.com/watch?v=WUurkKbtRRI
            posX = initialX + Math.sin(Math.toRadians(tt.toDouble()))
            posY = initialY
            posZ = initialZ + Math.cos(Math.toRadians(tt.toDouble()))
        }
    }

    companion object {

        //0 = none, 1 = elucent, 2 = wiresegal
        private val ROTATION_MODE = 0
    }

}
