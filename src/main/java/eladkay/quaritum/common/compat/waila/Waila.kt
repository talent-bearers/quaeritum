package eladkay.quaritum.common.compat.waila

import eladkay.quaritum.common.block.chalk.BlockChalk
import eladkay.quaritum.common.item.ModItems
import mcp.mobius.waila.api.IWailaConfigHandler
import mcp.mobius.waila.api.IWailaDataAccessor
import mcp.mobius.waila.api.IWailaDataProvider
import mcp.mobius.waila.api.IWailaRegistrar
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object Waila {
    fun onWailaCall(registrar: IWailaRegistrar) {
        println("Quaritum | Waila compat")
        try {
            registrar.registerStackProvider(ChalkStackProvider(), BlockChalk::class.java)
        } catch (e: NullPointerException) {
            println("An error occured during Waila compat init!")
            e.printStackTrace()
        }

    }

    class ChalkStackProvider : IWailaDataProvider {
        override fun getWailaStack(accessor: IWailaDataAccessor, config: IWailaConfigHandler): ItemStack {
            return ItemStack(ModItems.chalk, 1, accessor.metadata)
        }

        override fun getWailaHead(itemStack: ItemStack, currenttip: List<String>, accessor: IWailaDataAccessor, config: IWailaConfigHandler): List<String> {
            return currenttip
        }

        override fun getWailaBody(itemStack: ItemStack, currenttip: List<String>, accessor: IWailaDataAccessor, config: IWailaConfigHandler): List<String> {
            return currenttip
        }

        override fun getWailaTail(itemStack: ItemStack, currenttip: List<String>, accessor: IWailaDataAccessor, config: IWailaConfigHandler): List<String> {
            return currenttip
        }

        override fun getNBTData(player: EntityPlayerMP, te: TileEntity, tag: NBTTagCompound, world: World, pos: BlockPos): NBTTagCompound {
            return tag
        }
    }
}
