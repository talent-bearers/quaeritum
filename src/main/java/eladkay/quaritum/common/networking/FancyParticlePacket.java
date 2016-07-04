package eladkay.quaritum.common.networking;

import eladkay.quaritum.common.Quartium;
import eladkay.quaritum.common.core.LogHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class FancyParticlePacket extends MessageBase<FancyParticlePacket> {
    private int x;
    private int y;
    private int z;
    private int amount = 100;
    @Override
    public void fromBytes(ByteBuf buf) {

        /*x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        amount = buf.readInt();*/
        NBTTagCompound tag = ByteBufUtils.readTag(buf);
        x = tag.getInteger("x");
        y = tag.getInteger("y");
        z = tag.getInteger("z");
        amount = tag.getInteger("amount");
        LogHelper.logDebug("x = " + x + " y = " + y + " z = " + z);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("x", x);
        tag.setInteger("y", y);
        tag.setInteger("z", z);
        tag.setInteger("amount", amount);
        ByteBufUtils.writeTag(buf, tag);
        LogHelper.logDebug("x = " + x + " y = " + y + " z = " + z);
    }

    public FancyParticlePacket() {
        x = 0;
        y = 0;
        z = 0;
    }
    public FancyParticlePacket(int xc, int yc, int zc, int amount) {
        x = xc;
        y = yc;
        z = zc;
        this.amount = amount;
        LogHelper.logDebug("x = " + x + " y = " + y + " z = " + z);
    }
    static final float ROTATION_MODIFIER = 0.2f;

    @Override
    public void handleClientSide(FancyParticlePacket message, EntityPlayer player) {
        LogHelper.logDebug("x = " + x + " y = " + y + " z = " + z);
        long ticks = player.worldObj.getTotalWorldTime();
        float xRotate = (float) Math.sin(ticks * ROTATION_MODIFIER) / 2F;
        float zRotate = (float) Math.cos(ticks * ROTATION_MODIFIER) / 2F;

        //do packety stuff
        for (int i = 0; i < message.amount; i++) {
            //world, x, y, z
            Quartium.proxy.spawnStafflikeParticles(
                    player.worldObj, //world
                    message.x + (player.worldObj.rand.nextFloat()) * (player.worldObj.rand.nextDouble()), //x
                    message.y + player.worldObj.rand.nextDouble() - 0.5D, //y
                    message.z + (player.worldObj.rand.nextFloat()) * (player.worldObj.rand.nextDouble()) //z
            );
        }
    }

    @Override
    public void handleServerSide(FancyParticlePacket message, EntityPlayer player) {
        LogHelper.logDebug("x = " + x + " y = " + y + " z = " + z);
    }
}