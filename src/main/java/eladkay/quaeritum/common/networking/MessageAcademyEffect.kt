package eladkay.quaeritum.common.networking

import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.network.PacketBase
import com.teamwizardry.librarianlib.features.saving.Save
import com.teamwizardry.librarianlib.features.utilities.client.ClientRunnable
import eladkay.quaeritum.client.lib.LibParticles
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import java.awt.Color

/**
 * @author WireSegal
 * Created at 9:56 PM on 12/25/17.
 */
@PacketRegister(Side.CLIENT)
class MessageAcademyEffect(@Save var from: BlockPos = BlockPos.ORIGIN, @Save var to: Array<BlockPos> = arrayOf()) : PacketBase() {
    override fun handle(ctx: MessageContext) {
        ClientRunnable.run {
            val cent = Vec3d(from).add(0.5, 1.5, 0.5)
            to.forEach {
                var dir = Vec3d(it).subtract(Vec3d(from))
                dir = dir.add(0.0, -dir.y / 2, 0.0)
                dir = dir.normalize()
                (0 until 10)
                        .map { cent.add(dir.scale(1.5 + it / 6.0)) }
                        .forEach { vec -> LibParticles.embers(50, 0.5f, vec, colorFromLocation(it), 0.35) }
            }
        }
    }

    companion object {
        fun colorFromLocation(pos: BlockPos): Color {
            val seed = (pos.x xor pos.y xor pos.z) * 255 xor pos.hashCode()
            return Color(Color.HSBtoRGB(seed * 0.005F, 0.8f, 1f))
        }
    }
}
