package eladkay.quaeritum.common.networking


import eladkay.quaeritum.common.Quaeritum
import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

class FancyLineParticlePacket : MessageBase<FancyLineParticlePacket> {

    var r: Double = 0.toDouble()
    var g: Double = 0.toDouble()
    var b: Double = 0.toDouble()
    private var from: BlockPos? = null
    private var to: BlockPos? = null

    constructor(from: BlockPos, to: BlockPos, r: Double, g: Double, b: Double) {
        this.from = from
        this.to = to
        this.r = r
        this.g = g
        this.b = b
    }

    constructor() {
    }


    override fun fromBytes(buf: ByteBuf) {
        from = BlockPos.fromLong(buf.readLong())
        to = BlockPos.fromLong(buf.readLong())
        r = buf.readDouble()
        g = buf.readDouble()
        b = buf.readDouble()
    }

    override fun toBytes(buf: ByteBuf) {
        buf.writeLong(from!!.toLong())
        buf.writeLong(to!!.toLong())
        buf.writeDouble(r)
        buf.writeDouble(g)
        buf.writeDouble(b)
    }

    override fun handleClientSide(msg: FancyLineParticlePacket, player: EntityPlayer?) {
        //Particle
        val world = Minecraft.getMinecraft().theWorld
        Minecraft.getMinecraft().addScheduledTask {
            var velocity = Vec3d(msg.to!!.subtract(msg.from!!))
            velocity = velocity.normalize()
            val dist = Math.sqrt(msg.to!!.distanceSq(msg.from!!))
            val density = 5.0.toInt()
            var count = 0
            while (count < dist * density) {
                val i = (count / density).toDouble()
                val xp = msg.from!!.x.toDouble() + velocity.xCoord * i + 0.25
                val yp = msg.from!!.y.toDouble() + velocity.yCoord * i + 0.25
                val zp = msg.from!!.z.toDouble() + velocity.zCoord * i + 0.25
                Quaeritum.proxy!!.spawnParticleMagixFX(world, xp, yp, zp, velocity.xCoord * 0.1, 0.15, velocity.zCoord, msg.r, msg.g, msg.b)
                count++
                //ParticleHelper.spawnParticle("redBeamOfAwesome", xp, yp, zp, velocity.xCoord * 0.1, 0.15, velocity.zCoord, msg.r, msg.g, msg.b);
                //System.out.println("xp=" + xp + "&yp=" + yp + "&zp=" + zp + "&count=" + count);
            }

        }
    }

    override fun handleServerSide(message: FancyLineParticlePacket, player: EntityPlayer) {

    }
}
