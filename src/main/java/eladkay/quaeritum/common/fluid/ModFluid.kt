package eladkay.quaeritum.common.fluid

import com.teamwizardry.librarianlib.features.helpers.currentModId
import net.minecraft.block.material.Material
import net.minecraft.item.EnumRarity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry

/**
 * @author WireSegal
 * Created at 8:18 AM on 11/23/17.
 */
@Suppress("LeakingThis")
open class ModFluid(name: String, rlStill: ResourceLocation, rlFlow: ResourceLocation, bucket: Boolean) : Fluid(name, rlStill, rlFlow) {

    constructor(name: String, still: String, flow: String, bucket: Boolean) : this(
            name,
            ResourceLocation(currentModId, still),
            ResourceLocation(currentModId, flow),
            bucket)

    fun makeBlock(material: Material): ModFluid {
        BlockModFluid(this, material)
        return this
    }

    override fun setDensity(density: Int): ModFluid {
        super.setDensity(density)
        return this
    }

    override fun setEmptySound(emptySound: SoundEvent?): ModFluid {
        super.setEmptySound(emptySound)
        return this
    }

    override fun setViscosity(viscosity: Int): ModFluid {
        super.setViscosity(viscosity)
        return this
    }

    override fun setUnlocalizedName(unlocalizedName: String?): ModFluid {
        super.setUnlocalizedName(unlocalizedName)
        return this
    }

    override fun setTemperature(temperature: Int): ModFluid {
        super.setTemperature(temperature)
        return this
    }

    override fun setGaseous(isGaseous: Boolean): ModFluid {
        super.setGaseous(isGaseous)
        return this
    }

    override fun setRarity(rarity: EnumRarity?): ModFluid {
        super.setRarity(rarity)
        return this
    }

    override fun setFillSound(fillSound: SoundEvent?): ModFluid {
        super.setFillSound(fillSound)
        return this
    }

    override fun setLuminosity(luminosity: Int): ModFluid {
        super.setLuminosity(luminosity)
        return this
    }

    fun getActual(): Fluid = FluidRegistry.getFluid(name)

    init {
        FluidRegistry.registerFluid(this)
        if (bucket)
            FluidRegistry.addBucketForFluid(this)
    }
}
