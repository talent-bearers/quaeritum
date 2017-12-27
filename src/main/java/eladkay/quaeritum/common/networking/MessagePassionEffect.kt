package eladkay.quaeritum.common.networking

import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.network.PacketBase
import com.teamwizardry.librarianlib.features.saving.Save
import com.teamwizardry.librarianlib.features.utilities.client.ClientRunnable
import eladkay.quaeritum.client.lib.LibParticles
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import java.awt.Color

/**
 * @author WireSegal
 * Created at 9:56 PM on 12/25/17.
 */
@PacketRegister(Side.CLIENT)
class MessagePassionEffect(@Save var from: Vec3d = Vec3d.ZERO, @Save var motion: Vec3d = Vec3d.ZERO) : PacketBase() {
    override fun handle(ctx: MessageContext) {
        ClientRunnable.run {
            LibParticles.embers(20, 0.5f, from, Color(0x805920), 0.5, motion)
        }
    }
}
