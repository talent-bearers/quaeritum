package eladkay.quaritum.common.networking;

import eladkay.quaritum.api.lib.LibMisc;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHelper {
    public static SimpleNetworkWrapper instance;

    public static void init() {
        instance = new SimpleNetworkWrapper(LibMisc.MOD_ID);
        try {
            instance.registerMessage(FancyParticlePacket.class, FancyParticlePacket.class, 0, Side.CLIENT);
            instance.registerMessage(LightningEffectPacket.class, LightningEffectPacket.class, 1, Side.CLIENT);
            instance.registerMessage(FancyLineParticlePacket.class, FancyLineParticlePacket.class, 2, Side.CLIENT);
        } catch (NoClassDefFoundError server) {
            server.printStackTrace();
        }
        //todo this is bad
        //Caused by: java.lang.NoClassDefFoundError: net/minecraft/client/entity/EntityPlayerSP
    }

    //lol
    public static void tellEveryone(IMessage message) {
        instance.sendToAll(message);
    }

    public static void tellEveryoneAround(IMessage message, int dim, int x, int y, int z, int range) {
        instance.sendToAllAround(message, new NetworkRegistry.TargetPoint(dim, x, y, z, range));
    }

    public static void tellEveryoneAround(IMessage message, int dim, BlockPos pos, int range) {
        instance.sendToAllAround(message, new NetworkRegistry.TargetPoint(dim, pos.getX(), pos.getY(), pos.getZ(), range));
    }

    public static void tellEveryoneAround(IMessage message, NetworkRegistry.TargetPoint point) {
        instance.sendToAllAround(message, point);
    }
}
