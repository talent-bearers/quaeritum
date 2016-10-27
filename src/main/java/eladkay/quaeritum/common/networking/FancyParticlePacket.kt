package eladkay.quaeritum.common.networking

import eladkay.quaeritum.common.Quaeritum
import eladkay.quaeritum.common.core.LogHelper
import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.network.ByteBufUtils

class FancyParticlePacket : MessageBase<FancyParticlePacket> {
    private var x: Double = 0.toDouble()
    private var y: Double = 0.toDouble()
    private var z: Double = 0.toDouble()
    private var amount = 100

    constructor() {
        x = 0.0
        y = 0.0
        z = 0.0
    }

    constructor(xc: Double, yc: Double, zc: Double, amount: Int) {
        x = xc
        y = yc
        z = zc
        this.amount = amount
    }

    override fun fromBytes(buf: ByteBuf) {

        /*x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        amount = buf.readInt();*/
        val tag = ByteBufUtils.readTag(buf)
        x = tag.getDouble("x")
        y = tag.getDouble("y")
        z = tag.getDouble("z")
        amount = tag.getInteger("amount")
    }

    override fun toBytes(buf: ByteBuf) {
        val tag = NBTTagCompound()
        tag.setDouble("x", x)
        tag.setDouble("y", y)
        tag.setDouble("z", z)
        tag.setInteger("amount", amount)
        ByteBufUtils.writeTag(buf, tag)
    }

    override fun handleClientSide(message: FancyParticlePacket, player: EntityPlayer?) {

        //do packety stuff
        for (i in 0..message.amount - 1) {
            //world, x, y, z
            if (Minecraft.getMinecraft().theWorld != null)
                Quaeritum.proxy!!.spawnStafflikeParticles(
                        Minecraft.getMinecraft().theWorld, //world
                        message.x + Minecraft.getMinecraft().theWorld.rand.nextFloat() * Minecraft.getMinecraft().theWorld.rand.nextDouble(), //x
                        message.y + Minecraft.getMinecraft().theWorld.rand.nextDouble() - 0.5, //y
                        message.z + Minecraft.getMinecraft().theWorld.rand.nextFloat() * Minecraft.getMinecraft().theWorld.rand.nextDouble() //z
                )
            //world, x, y, z, motion, rgb
            /*Quaeritum.proxy.spawnParticleMagixFX(
                    Minecraft.getMinecraft().theWorld, //world
                    x + 2 * (Minecraft.getMinecraft().theWorld.rand.nextFloat() - 0.5), //x
                    y + 2.0 * (Minecraft.getMinecraft().theWorld.rand.nextFloat() - 0.5) + 1.0, //y
                    z + 2.0 * (Minecraft.getMinecraft().theWorld.rand.nextFloat() - 0.5), //z
                    x, y + 1.0, z,  //motion
                    255, 0, 0); //rgb*/
        }
        //Player x, y, z, partial ticks (Use 1 for now), TE x, y, z, total ticks (use world time), Item x, y, z

    }

    override fun handleServerSide(message: FancyParticlePacket, player: EntityPlayer) {
        LogHelper.logDebug("x = $x y = $y z = $z")
    }
}
