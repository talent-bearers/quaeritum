package eladkay.quaeritum.common.block.tile

import com.teamwizardry.librarianlib.features.autoregister.TileRegister
import com.teamwizardry.librarianlib.features.base.block.tile.TileModTickable
import com.teamwizardry.librarianlib.features.base.block.tile.module.ModuleFluid
import com.teamwizardry.librarianlib.features.base.block.tile.module.ModuleInventory
import com.teamwizardry.librarianlib.features.kotlin.isNotEmpty
import com.teamwizardry.librarianlib.features.saving.Module
import com.teamwizardry.librarianlib.features.saving.NoSync
import com.teamwizardry.librarianlib.features.saving.Save
import eladkay.quaeritum.api.alchemy.Dessications
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntityFurnace
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumParticleTypes
import net.minecraftforge.items.ItemStackHandler

@TileRegister("dessication")
class TileDessicationCrucible : TileModTickable() {
    override fun tick() {
        shouldInsert = true
        processTime += fireFuelLoop()
        shouldInsert = false
    }

    fun fireFuelLoop(): Int {
        if (currentFuelTime > 0)
            currentFuelTime--
        val fluid = inputLiquid.handler.fluid ?: return -processTime
        val recipe = Dessications.getRecipe(fluid) ?: return -processTime

        if (outputItem.handler.insertItem(0, recipe.getDriedStack(fluid), true).isNotEmpty)
            return -processTime

        if (currentFuelTime <= 0) {
            val fuel = fuelItem.handler.getStackInSlot(0)
            val value = TileEntityFurnace.getItemBurnTime(fuel)
            if (fuel.isEmpty || value == 0) return if (processTime > 0) -1 else 0
            currentFuelTime = value
            fuel.shrink(1)
        }

        if (processTime % 20 == 1)
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, 0.0, 0.0, 0.0)
        if (processTime >= 200) {
            val stack = recipe.getDriedStack(fluid)

            val addedOutputStack = outputItem.handler.insertItem(0, stack, true)

            if (addedOutputStack.isEmpty) {
                outputItem.handler.insertItem(0, stack, false)
                inputLiquid.handler.drain(500, true)
                return -processTime
            }
        }
        return 1

    }

    private var shouldInsert = false

    @Module
    val inputLiquid = ModuleFluid(2000).disallowSides(EnumFacing.DOWN)
    @Module
    @NoSync
    val fuelItem = ModuleInventory(1).disallowSides(EnumFacing.UP, EnumFacing.DOWN)
    @Module
    @NoSync
    val outputItem = ModuleInventory(object : ItemStackHandler(1) {
        override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean) = if (shouldInsert) super.insertItem(slot, stack, simulate) else stack
    }).setSides(EnumFacing.DOWN)

    @NoSync
    @Save var currentFuelTime = 0
    @NoSync
    @Save var processTime = 0

}
