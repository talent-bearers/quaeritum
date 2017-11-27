package eladkay.quaeritum.common.fluid

import com.teamwizardry.librarianlib.features.base.fluid.ModFluid
import net.minecraft.block.material.Material

/**
 * @author WireSegal
 * Created at 8:29 AM on 11/23/17.
 */
object ModFluids {
    val BITUMEN = ModFluid("bitumen", "fluid/bitumen", "fluid/bitumen", true)
            .setDensity(3000)
            .setViscosity(9000)
            .setVaporizes(false)
            .makeBlock(Material.WATER)
    val SWEET = ModFluid("sugar_water", "fluid/sugar_water", "fluid/sugar_water_flow", true)
            .makeBlock(Material.WATER)
    val LIGHT = ModFluid("liquid_light", "fluid/liquid_light", "fluid/liquid_light_flow", true)
            .setLuminosity(15)
            .setViscosity(500)
            .setVaporizes(false)
            .makeBlock(Material.WATER)
    val SLURRY = ModFluid("iron_slurry", "fluid/iron_slurry", "fluid/iron_slurry_flow", true)
            .setViscosity(2000)
            .setVaporizes(false)
            .makeBlock(Material.WATER)
}
