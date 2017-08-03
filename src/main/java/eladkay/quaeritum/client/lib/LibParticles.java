package eladkay.quaeritum.client.lib;

import com.teamwizardry.librarianlib.features.math.interpolate.InterpFunction;
import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import eladkay.quaeritum.api.lib.LibMisc;
import eladkay.quaeritum.api.util.InterpScale;
import eladkay.quaeritum.api.util.RandUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.awt.*;

public class LibParticles {

	public static void EMBERS(World world, Vec3d pos, Color color, double scatter) {
		ParticleBuilder builder = new ParticleBuilder(10);
		builder.setRender(new ResourceLocation(LibMisc.MOD_ID, "particles/sparkle_blurred"));
		builder.disableRandom();
		InterpFunction<Vec3d> function = new StaticInterp<>(pos);

		builder.setColor(color);
		//new Color(0x893000)

		ParticleSpawner.spawn(builder, world, function, 2, 0, (aFloat, particleBuilder) -> {
			particleBuilder.setAlphaFunction(new InterpScale(1, 0));
			particleBuilder.setScaleFunction(new InterpScale(7, 0.3f));
		});
		ParticleSpawner.spawn(builder, world, function, 3, 0, (aFloat, particleBuilder) -> {
			particleBuilder.setAlphaFunction(new InterpScale(1, 0));
			particleBuilder.setScaleFunction(new InterpScale(3, 0.3f));
			Vec3d offset = new Vec3d(
					RandUtil.nextDouble(-scatter, scatter),
					RandUtil.nextDouble(-scatter, scatter),
					RandUtil.nextDouble(-scatter, scatter));
			particleBuilder.setPositionOffset(offset);
		});
		ParticleSpawner.spawn(builder, world, function, 4, 0, (aFloat, particleBuilder) -> {
			particleBuilder.setAlphaFunction(new InterpScale(1, 0));
			particleBuilder.setScaleFunction(new InterpScale(1, 0.3f));
			Vec3d offset = new Vec3d(
					RandUtil.nextDouble(-scatter, scatter),
					RandUtil.nextDouble(-scatter, scatter),
					RandUtil.nextDouble(-scatter, scatter));
			particleBuilder.setPositionOffset(offset);
		});
	}
}
