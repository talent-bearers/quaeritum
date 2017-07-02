package eladkay.quaeritum.common.item.misc

import com.teamwizardry.librarianlib.features.base.item.ItemMod
import eladkay.quaeritum.common.block.ModBlocks
import eladkay.quaeritum.common.block.flowers.BlockAnimusFlower
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ItemFertilizer : ItemMod(LibNames.FERTILIZER) {

    override fun onItemUse(playerIn: EntityPlayer?, worldIn: World, pos: BlockPos, hand: EnumHand?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        val stack = playerIn!!.getHeldItem(hand)
        return if (applyBonemeal(stack, worldIn, pos)) EnumActionResult.SUCCESS else EnumActionResult.FAIL
    }

    companion object {

        private fun applyBonemeal(stack: ItemStack, worldIn: World, target: BlockPos): Boolean {
            return /*worldIn instanceof WorldServer && */applyBonemeal0(stack, worldIn, target)
        }

        private fun applyBonemeal0(stack: ItemStack, worldIn: World, target: BlockPos): Boolean {
            val iblockstate = worldIn.getBlockState(target)
            if (iblockstate == ModBlocks.flower.getStateFromMeta(BlockAnimusFlower.Variants.COMMON_ARCANE.ordinal)) {
                if (!worldIn.isRemote) {
                    if (Math.random() < 0.05)
                        worldIn.setBlockState(target, ModBlocks.flower.getStateFromMeta(BlockAnimusFlower.Variants.ARCANE.ordinal))
                    worldIn.playBroadcastSound(2005, target, 0)
                    spawnBonemealParticles(worldIn, target, 0)
                }
                stack.count--
            }
            return true
        }


        fun spawnBonemealParticles(worldIn: World, pos: BlockPos, amount0: Int) {
            var amount = amount0
            if (amount == 0) {
                amount = 15
            }

            val iblockstate = worldIn.getBlockState(pos)
            var i1: Int
            var d0: Double
            var d1: Double
            var d2: Double
            if (iblockstate.material !== Material.AIR) {
                i1 = 0
                while (i1 < amount) {
                    d0 = Item.itemRand.nextGaussian() * 0.02
                    d1 = Item.itemRand.nextGaussian() * 0.02
                    d2 = Item.itemRand.nextGaussian() * 0.02
                    worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, (pos.x.toFloat() + Item.itemRand.nextFloat()).toDouble(), pos.y.toDouble() + Item.itemRand.nextFloat().toDouble() * iblockstate.getBoundingBox(worldIn, pos).maxY, (pos.z.toFloat() + Item.itemRand.nextFloat()).toDouble(), d0, d1, d2)
                    ++i1
                }
            } else {
                i1 = 0
                while (i1 < amount) {
                    d0 = Item.itemRand.nextGaussian() * 0.02
                    d1 = Item.itemRand.nextGaussian() * 0.02
                    d2 = Item.itemRand.nextGaussian() * 0.02
                    worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, (pos.x.toFloat() + Item.itemRand.nextFloat()).toDouble(), pos.y.toDouble() + Item.itemRand.nextFloat().toDouble() * 1.0, (pos.z.toFloat() + Item.itemRand.nextFloat()).toDouble(), d0, d1, d2)
                    ++i1
                }
            }

        }
    }
}
