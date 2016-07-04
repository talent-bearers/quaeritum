package eladkay.quaritum.common.networking;

import eladkay.quaritum.common.Quartium;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class FancyParticlePacket extends MessageBase<FancyParticlePacket> {
    private int x;
    private int y;
    private int z;
    private int amount = 100;
    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        amount = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(amount);
    }

    public FancyParticlePacket() {
        x = -193;
        y = 72;
        z = 143;
        amount = 100;
    }
    public FancyParticlePacket(int xc, int yc, int zc, int amount) {
        x = xc;
        y = yc;
        z = zc;
        this.amount = amount;
    }
    @Override
    public void handleClientSide(FancyParticlePacket message, EntityPlayer player) {
        //do packety stuff
        for (int i = 0; i < 100; i++) {
            //world, x, y, z
            Quartium.proxy.spawnStafflikeParticles(
                    player.worldObj, //world
                    x + (player.worldObj.rand.nextDouble()), //x
                    y + player.worldObj.rand.nextDouble() - 0.5D, //y
                    z + (player.worldObj.rand.nextDouble()) //z
            );
        }
    }

    @Override
    public void handleServerSide(FancyParticlePacket message, EntityPlayer player) {    }
}
