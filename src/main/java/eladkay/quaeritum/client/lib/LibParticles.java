package eladkay.quaeritum.client.lib;

import com.teamwizardry.librarianlib.features.math.interpolate.InterpFunction;
import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import eladkay.quaeritum.api.util.InterpScale;
import eladkay.quaeritum.api.util.RandUtil;
import eladkay.quaeritum.common.lib.LibLocations;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.awt.*;

public class LibParticles {

	private static Color darkGray = new Color(0x1c1c1c);

	/**
	 * Recommended values:
	 *
	 * @param verticalMin       0.01
	 * @param verticalMax       0.1
	 * @param horizontalScatter 0.03
	 */
	public static void SMOKE(World world, Vec3d pos, double verticalMin, double verticalMax, double horizontalScatter) {
		ParticleBuilder builder = new ParticleBuilder(30);
		builder.setRender(LibLocations.INSTANCE.getPARTICLE_SMOKE());
		builder.setColor(darkGray);
		ParticleSpawner.spawn(builder, world, new StaticInterp<>(pos), RandUtil.nextInt(1, 3), 0, (aFloat, particleBuilder) -> {
			particleBuilder.setAlphaFunction(new InterpScale(1, 0));
			particleBuilder.setLifetime(RandUtil.nextInt(20, 35));
			particleBuilder.setScale(RandUtil.nextInt(3, 7));
			particleBuilder.setRotation(RandUtil.nextFloat());
			particleBuilder.setMotion(new Vec3d(RandUtil.nextDouble(-horizontalScatter, horizontalScatter), RandUtil.nextDouble(verticalMin, verticalMax), RandUtil.nextDouble(-horizontalScatter, horizontalScatter)));
		});
	}

	/**
	 * Recommended values:
	 *
	 * @param color   0x893000
	 * @param scatter 0.4
	 */
	public static void EMBERS(World world, Vec3d pos, Color color, double scatter) {
		ParticleBuilder builder = new ParticleBuilder(10);
		builder.setRender(LibLocations.INSTANCE.getPARTICLE_SPARKLE());
		builder.disableRandom();
		InterpFunction<Vec3d> function = new StaticInterp<>(pos);

		builder.setColor(color);

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
