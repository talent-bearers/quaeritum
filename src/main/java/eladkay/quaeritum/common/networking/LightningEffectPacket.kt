package eladkay.quaeritum.common.networking

import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.network.ByteBufUtils

class LightningEffectPacket : MessageBase<LightningEffectPacket> {
    internal var x: Double = 0.toDouble()
    internal var y: Double = 0.toDouble()
    internal var z: Double = 0.toDouble()

    constructor(x: Double, y: Double, z: Double) {
        this.x = x
        this.y = y
        this.z = z
    }

    constructor() {
    }

    override fun handleClientSide(message: LightningEffectPacket, player: EntityPlayer?) {
        println("hi")
        Minecraft.getMinecraft().theWorld.addWeatherEffect(EntityLightningBolt(Minecraft.getMinecraft().theWorld, message.x, message.y, message.z, true))
    }

    override fun handleServerSide(message: LightningEffectPacket, player: EntityPlayer) {
    }

    override fun fromBytes(buf: ByteBuf) {
        val tag = ByteBufUtils.readTag(buf)
        x = tag.getDouble("x")
        y = tag.getDouble("y")
        z = tag.getDouble("z")
    }

    override fun toBytes(buf: ByteBuf) {
        val tag = NBTTagCompound()
        tag.setDouble("x", x)
        tag.setDouble("y", y)
        tag.setDouble("z", z)
        ByteBufUtils.writeTag(buf, tag)
    }
}
