package eladkay.quaeritum.client.fx

import net.minecraft.client.particle.Particle
import net.minecraft.world.World

/**
 * Created by pixlepix on 11/30/14.
 */
class FXBeam(par1World: World, x: Double, y: Double, z: Double, red: Float, blue: Float, green: Float) : Particle(par1World, x, y, z, 0.0, 0.0, 0.0) {

    constructor(par1World: World, x: Double, y: Double, z: Double, red: Float, blue: Float, green: Float, longLived: Boolean) : this(par1World, x, y, z, red, blue, green) {
        this.particleMaxAge *= 5
    }

    init {
        var red = red
        var blue = blue
        var green = green
        this.motionX *= 0.10000000149011612
        this.motionY *= 0.10000000149011612
        this.motionZ *= 0.10000000149011612
        this.motionX += motionX * 0.4
        this.motionY += motionY * 0.4
        this.motionZ += motionZ * 0.4
        val offset = (Math.random() * 0.30000001192092896).toFloat()

        red -= offset
        blue -= offset
        green -= offset


        setRBGColorF(red, blue, green)

        this.particleScale *= 2f
        this.particleMaxAge = (6.0 / (Math.random() * 0.8 + 0.6)).toInt()
        this.canCollide = false
        this.isExpired = false
    }
}
