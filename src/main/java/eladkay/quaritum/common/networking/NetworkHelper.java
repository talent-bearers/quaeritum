package eladkay.quaritum.common.networking;

import eladkay.quaritum.api.lib.LibMisc;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHelper {
    public static SimpleNetworkWrapper instance;
    public static void init() {
        instance = new SimpleNetworkWrapper(LibMisc.MOD_ID);
        instance.registerMessage(FancyParticlePacket.class, FancyParticlePacket.class, 0, Side.CLIENT);
        instance.registerMessage(LightningEffectPacket.class, LightningEffectPacket.class, 1, Side.CLIENT);
    }
    //lol
    public static void tellEveryone(IMessage message) {
        instance.sendToAll(message);
    }
    public static void tellEveryoneAround(IMessage message, int dim, int x, int y, int z, int range) {
        instance.sendToAllAround(message, new NetworkRegistry.TargetPoint(dim, x, y, z, range));
    }
    public static void tellEveryoneAround(IMessage message, NetworkRegistry.TargetPoint point) {
        instance.sendToAllAround(message, point);
    }
}
