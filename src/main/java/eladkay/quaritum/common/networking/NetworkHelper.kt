package eladkay.quaritum.common.networking

import eladkay.quaritum.api.lib.LibMisc
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side

object NetworkHelper {
    var instance: SimpleNetworkWrapper

    init {
        instance = SimpleNetworkWrapper(LibMisc.MOD_ID)
        try {
            instance.registerMessage<FancyParticlePacket, FancyParticlePacket>(FancyParticlePacket::class.java, FancyParticlePacket::class.java, 0, Side.CLIENT)
            instance.registerMessage<LightningEffectPacket, LightningEffectPacket>(LightningEffectPacket::class.java, LightningEffectPacket::class.java, 1, Side.CLIENT)
            instance.registerMessage<FancyLineParticlePacket, FancyLineParticlePacket>(FancyLineParticlePacket::class.java, FancyLineParticlePacket::class.java, 2, Side.CLIENT)
        } catch (server: NoClassDefFoundError) {
            server.printStackTrace()
        }

        //todo this is bad
        //Caused by: java.lang.NoClassDefFoundError: net/minecraft/client/entity/EntityPlayerSP
    }

    //lol
    fun tellEveryone(message: IMessage) {
        instance.sendToAll(message)
    }

    fun tellEveryoneAround(message: IMessage, dim: Int, x: Int, y: Int, z: Int, range: Int) {
        instance.sendToAllAround(message, NetworkRegistry.TargetPoint(dim, x.toDouble(), y.toDouble(), z.toDouble(), range.toDouble()))
    }

    fun tellEveryoneAround(message: IMessage, dim: Int, pos: BlockPos, range: Int) {
        instance.sendToAllAround(message, NetworkRegistry.TargetPoint(dim, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), range.toDouble()))
    }

    fun tellEveryoneAround(message: IMessage, point: NetworkRegistry.TargetPoint) {
        instance.sendToAllAround(message, point)
    }
}
