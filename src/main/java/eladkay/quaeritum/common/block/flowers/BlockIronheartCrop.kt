package eladkay.quaeritum.common.block.flowers

import com.teamwizardry.librarianlib.features.base.block.BlockModCrops
import eladkay.quaeritum.common.block.ModBlocks
import eladkay.quaeritum.common.item.ModItems
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

/**
 * @author WireSegal
 * Created at 10:14 PM on 12/12/17.
 */
class BlockIronheartCrop : BlockModCrops(LibNames.FLOWER + "_iron_crop") {
    override fun getCrop(): Item {
        return ModBlocks.ironheart.itemForm!!
    }

    override fun getSeed(): Item {
        return ModItems.resourceSeed
    }

    override fun getDrops(drops: NonNullList<ItemStack>, world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int) {
        val rand = if (world is World) world.rand else RANDOM

        val count = quantityDropped(state, fortune, rand)
        for (i in 0 until count) {
            val item = this.getItemDropped(state, rand, fortune)
            if (item != Items.AIR)
                drops.add(ItemStack(item, 1, this.damageDropped(state)))
        }
    }
}
