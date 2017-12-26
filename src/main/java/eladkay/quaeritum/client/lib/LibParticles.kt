package eladkay.quaeritum.client.lib

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner
import com.teamwizardry.librarianlib.features.particle.spawn
import eladkay.quaeritum.api.util.InterpScale
import eladkay.quaeritum.api.util.RandUtil
import eladkay.quaeritum.common.lib.LibLocations
import net.minecraft.client.Minecraft
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.awt.Color

object LibParticles {

    private val darkGray = Color(0x1c1c1c)

    /**
     * Recommended values:
     *
     * @param verticalMin       0.01
     * @param verticalMax       0.1
     * @param horizontalScatter 0.03
     */
    @SideOnly(Side.CLIENT)
    fun smoke(pos: Vec3d, verticalMin: Double, verticalMax: Double, horizontalScatter: Double) {
        val builder = ParticleBuilder(30)
        val world = Minecraft.getMinecraft().world
        builder.setRender(LibLocations.PARTICLE_SMOKE)
        builder.setColor(darkGray)
        ParticleSpawner.spawn(builder, world, StaticInterp(pos), RandUtil.nextInt(1, 3), 0, { _, particleBuilder ->
            particleBuilder.setAlphaFunction(InterpScale(1f, 0f))
            particleBuilder.setLifetime(RandUtil.nextInt(20, 35))
            particleBuilder.setScale(RandUtil.nextInt(3, 7).toFloat())
            particleBuilder.setRotation(RandUtil.nextFloat())
            particleBuilder.setMotion(Vec3d(RandUtil.nextDouble(-horizontalScatter, horizontalScatter), RandUtil.nextDouble(verticalMin, verticalMax), RandUtil.nextDouble(-horizontalScatter, horizontalScatter)))
        })
    }

    /**
     * Recommended values:
     *
     * @param color   0x893000
     * @param scatter 0.4
     */
    @SideOnly(Side.CLIENT)
    fun embers(lifetime: Int, size: Float, pos: Vec3d, color: Color, scatter: Double) {
        val builder = ParticleBuilder(lifetime)
        val world = Minecraft.getMinecraft().world
        builder.setRender(LibLocations.PARTICLE_SPARKLE)
        builder.disableRandom()
        val function = StaticInterp(pos)

        builder.setColor(color)

        ParticleSpawner.spawn(builder, world, function, 2, 0, { _, particleBuilder ->
            particleBuilder.setAlphaFunction(InterpScale(1f, 0f))
            particleBuilder.setScaleFunction(InterpScale(7 * size, 0.3f * size))
        })
        if (scatter != 0.0) {
            ParticleSpawner.spawn(builder, world, function, 3, 0, { _, particleBuilder ->
                particleBuilder.setAlphaFunction(InterpScale(1f, 0f))
                particleBuilder.setScaleFunction(InterpScale(3 * size, 0.3f * size))
                val offset = Vec3d(
                        RandUtil.nextDouble(-scatter, scatter),
                        RandUtil.nextDouble(-scatter, scatter),
                        RandUtil.nextDouble(-scatter, scatter))
                particleBuilder.setPositionOffset(offset)
            })
            ParticleSpawner.spawn(builder, world, function, 4, 0, { _, particleBuilder ->
                particleBuilder.setAlphaFunction(InterpScale(1f, 0f))
                particleBuilder.setScaleFunction(InterpScale(1 * size, 0.3f * size))
                val offset = Vec3d(
                        RandUtil.nextDouble(-scatter, scatter),
                        RandUtil.nextDouble(-scatter, scatter),
                        RandUtil.nextDouble(-scatter, scatter))
                particleBuilder.setPositionOffset(offset)
            })
        }
    }
}
