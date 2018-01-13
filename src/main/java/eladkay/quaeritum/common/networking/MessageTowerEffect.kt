package eladkay.quaeritum.common.networking

import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.network.PacketBase
import com.teamwizardry.librarianlib.features.saving.Save
import com.teamwizardry.librarianlib.features.utilities.client.ClientRunnable
import eladkay.quaeritum.api.util.RandUtil
import eladkay.quaeritum.client.lib.LibParticles
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import java.awt.Color

/**
 * @author WireSegal
 * Created at 9:56 PM on 12/25/17.
 */
@PacketRegister(Side.CLIENT)
class MessageTowerEffect(@Save var pos: Vec3d = Vec3d.ZERO, @Save var target: Vec3d = Vec3d.ZERO) : PacketBase() {
    override fun handle(ctx: MessageContext) {
        ClientRunnable.run {
            val angle = RandUtil.nextFloat(2 * Math.PI.toFloat())
            val x = MathHelper.cos(angle) * 4.0
            val y = MathHelper.sin(angle) * 4.0

            val from = pos.addVector(x, 0.125, y)
            val motion = target.subtract(from).scale(0.075)

            LibParticles.embers(10, 0.5f, from, Color(0xF0F040), 0.0, motion)
        }
    }
}
