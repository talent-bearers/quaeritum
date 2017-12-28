package eladkay.quaeritum.common.networking

import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.network.PacketBase
import com.teamwizardry.librarianlib.features.saving.Save
import com.teamwizardry.librarianlib.features.utilities.client.ClientRunnable
import eladkay.quaeritum.api.util.RandUtil
import eladkay.quaeritum.client.lib.LibParticles
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import java.awt.Color

/**
 * @author WireSegal
 * Created at 2:23 PM on 12/28/17.
 */
@PacketRegister(Side.CLIENT)
class PuffMessage(@Save var pos: Vec3d = Vec3d.ZERO,
                  @Save var verticalMin: Double = 0.02,
                  @Save var verticalMax: Double = 0.05,
                  @Save var lifetimeMin: Int = 20,
                  @Save var lifetimeMax: Int = 35,
                  @Save var color: Color = LibParticles.darkGray,
                  @Save var scatter: Double = 0.01,
                  @Save var amount: Int = RandUtil.nextInt(1, 3)) : PacketBase() {
    override fun handle(ctx: MessageContext) {
        ClientRunnable.run {
            LibParticles.smoke(lifetimeMin, lifetimeMax, pos, verticalMin, verticalMax, scatter, amount, color)
        }
    }
}
