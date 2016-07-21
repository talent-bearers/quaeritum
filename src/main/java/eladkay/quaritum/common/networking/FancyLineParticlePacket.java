package eladkay.quaritum.common.networking;


import eladkay.quaritum.common.Quaritum;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FancyLineParticlePacket extends MessageBase<FancyLineParticlePacket> {

    public double r;
    public double g;
    public double b;
    private BlockPos from;
    private BlockPos to;

    public FancyLineParticlePacket(BlockPos from, BlockPos to, double r, double g, double b) {
        this.from = from;
        this.to = to;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public FancyLineParticlePacket() {
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        from = BlockPos.fromLong(buf.readLong());
        to = BlockPos.fromLong(buf.readLong());
        r = buf.readDouble();
        g = buf.readDouble();
        b = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(from.toLong());
        buf.writeLong(to.toLong());
        buf.writeDouble(r);
        buf.writeDouble(g);
        buf.writeDouble(b);
    }

    @Override
    public void handleClientSide(FancyLineParticlePacket msg, EntityPlayer player) {
        //Particle
        World world = Minecraft.getMinecraft().theWorld;
        Minecraft.getMinecraft().addScheduledTask(() -> {
            Vec3d velocity = new Vec3d(msg.to.subtract(msg.from));
            velocity = velocity.normalize();
            double dist = Math.sqrt(msg.to.distanceSq(msg.from));
            int density = (int) 5D;
            for (int count = 0; count < dist * density; count++) {
                double i = count / density;
                double xp = msg.from.getX() + (velocity.xCoord * i) + 0.25;
                double yp = msg.from.getY() + (velocity.yCoord * i) + 0.25;
                double zp = msg.from.getZ() + (velocity.zCoord * i) + 0.25;
                Quaritum.proxy.spawnParticleMagixFX(world, xp, yp, zp, velocity.xCoord * 0.1, 0.15, velocity.zCoord, msg.r, msg.g, msg.b);
                //ParticleHelper.spawnParticle("redBeamOfAwesome", xp, yp, zp, velocity.xCoord * 0.1, 0.15, velocity.zCoord, msg.r, msg.g, msg.b);
                //System.out.println("xp=" + xp + "&yp=" + yp + "&zp=" + zp + "&count=" + count);
            }

        });
    }

    @Override
    public void handleServerSide(FancyLineParticlePacket message, EntityPlayer player) {

    }
}
