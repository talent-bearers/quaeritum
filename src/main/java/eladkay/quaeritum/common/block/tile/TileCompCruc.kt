package eladkay.quaeritum.common.block.tile

import com.teamwizardry.librarianlib.features.autoregister.TileRegister
import com.teamwizardry.librarianlib.features.base.block.TileMod
import com.teamwizardry.librarianlib.features.base.block.module.ModuleFluid
import com.teamwizardry.librarianlib.features.base.block.module.ModuleInventory
import com.teamwizardry.librarianlib.features.kotlin.isNotEmpty
import com.teamwizardry.librarianlib.features.saving.Module
import com.teamwizardry.librarianlib.features.saving.Save
import eladkay.quaeritum.api.alchemy.AlchemicalCompositions
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraftforge.fml.common.registry.GameRegistry

/**
 * Created by Elad on 8/22/2017.
 */
@TileRegister("compound_crucible")
class TileCompCruc : TileMod(), ITickable {
    override fun update() {
        if(currentFuelTime == 0) {
            val fuel = fuelItem.handler.getStackInSlot(0)
            if (fuel.isEmpty || fuelValue(fuel) == -1) return
            currentFuelTime = fuelValue(fuel)
            fuel.shrink(1)
        } else {
            currentFuelTime++
            val liquid = inputLiquid.handler.fluid ?: return
            val item = inputItem.handler.getStackInSlot(0)
            if(item.isEmpty || liquid.amount < 500) return
            if(outputItem.handler.getStackInSlot(0).count >= outputItem.handler.getStackInSlot(0).maxStackSize)
                return
            val recipe = AlchemicalCompositions.getRecipe(liquid, item) ?: return
            val stack = recipe.getCompositeStack(liquid, item)
            if(outputItem.handler.getStackInSlot(0).isItemEqual(stack)) {
                val addedOutputStack = outputItem.handler.insertItem(0, stack, false)
                if(addedOutputStack.isNotEmpty) {
                    inputItem.handler.getStackInSlot(0).shrink(1)
                    inputLiquid.handler.drain(500, true)
                }

            }

        }
    }

    @Module
    val inputLiquid = ModuleFluid(2000).setSides(EnumFacing.UP)
    @Module
    val inputItem = ModuleInventory(1).setSides(EnumFacing.WEST, EnumFacing.EAST)
    @Module
    val fuelItem = ModuleInventory(1).setSides(EnumFacing.NORTH, EnumFacing.SOUTH)
    @Module
    val outputItem = ModuleInventory(1).setSides(EnumFacing.DOWN)
    @Save var currentFuelTime = 0


    fun fuelValue(itemStack: ItemStack): Int {
        val ret = GameRegistry.getFuelValue(itemStack)
        return if(ret == 0) -1 else ret
    }

}