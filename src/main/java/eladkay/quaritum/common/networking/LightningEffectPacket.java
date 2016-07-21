package eladkay.quaritum.common.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class LightningEffectPacket extends MessageBase<LightningEffectPacket> {
    double x;
    double y;
    double z;

    public LightningEffectPacket(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public LightningEffectPacket() {
    }

    @Override
    public void handleClientSide(LightningEffectPacket message, EntityPlayer player) {
        System.out.println("hi");
        player.worldObj.addWeatherEffect(new EntityLightningBolt(player.worldObj, message.x, message.y, message.z, true));
    }

    @Override
    public void handleServerSide(LightningEffectPacket message, EntityPlayer player) {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        NBTTagCompound tag = ByteBufUtils.readTag(buf);
        x = tag.getDouble("x");
        y = tag.getDouble("y");
        z = tag.getDouble("z");
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setDouble("x", x);
        tag.setDouble("y", y);
        tag.setDouble("z", z);
        ByteBufUtils.writeTag(buf, tag);
    }
}
