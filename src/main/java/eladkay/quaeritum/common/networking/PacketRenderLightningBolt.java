package eladkay.quaeritum.common.networking;

import com.teamwizardry.librarianlib.features.autoregister.PacketRegister;
import com.teamwizardry.librarianlib.features.network.PacketBase;
import com.teamwizardry.librarianlib.features.saving.Save;
import eladkay.quaeritum.api.misc.LightningGenerator;
import eladkay.quaeritum.api.util.RandUtil;
import eladkay.quaeritum.api.util.RandUtilSeed;
import eladkay.quaeritum.client.core.LightningRenderer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

/**
 * Created by LordSaad.
 */
@PacketRegister(Side.CLIENT)
public class PacketRenderLightningBolt extends PacketBase {

    @Save
    private Vec3d point1;
    @Save
    private Vec3d point2;
    @Save
    private long seed;

    public PacketRenderLightningBolt() {
    }

    public PacketRenderLightningBolt(Vec3d point1, Vec3d point2, long seed) {
        this.point1 = point1;
        this.point2 = point2;
        this.seed = seed;
    }

    @Override
    public void handle(@NotNull MessageContext messageContext) {
        LightningRenderer.INSTANCE.addBolt(new LightningGenerator(point1, point2, new RandUtilSeed(seed)).generate(), RandUtil.nextInt(30, 40));
    }
}
