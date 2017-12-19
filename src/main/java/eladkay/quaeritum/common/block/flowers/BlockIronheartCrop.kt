package eladkay.quaeritum.common.block.flowers

import com.teamwizardry.librarianlib.features.base.block.BlockModCrops
import eladkay.quaeritum.common.block.ModBlocks
import eladkay.quaeritum.common.item.ModItems
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.util.NonNullList
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

/**
 * @author WireSegal
 * Created at 10:14 PM on 12/12/17.
 */
class BlockIronheartCrop : BlockModCrops(LibNames.FLOWER + "_iron_crop", 4) {
    override fun getCrop(): Item {
        return ModBlocks.ironheart.itemForm!!
    }

    override fun getSeed(): Item {
        return ModItems.resourceSeed
    }

    override fun getBonemealAgeIncrease(worldIn: World): Int {
        return MathHelper.getInt(worldIn.rand, 1, 2)
    }

    override fun getPickBlock(state: IBlockState, target: RayTraceResult?, world: World, pos: BlockPos, player: EntityPlayer?): ItemStack {
        return if (!isMaxAge(state))
            ItemStack(getSeed())
        else ItemStack(getCrop())
    }


    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        return AABBS[state.getValue(getAgeProperty())]
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

    override fun onEntityCollidedWithBlock(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity) {
        if (isMaxAge(state) && entityIn is EntityLivingBase)
            entityIn.attackEntityFrom(DamageSource.CACTUS, 3.0f)
    }

    companion object {
        val AABBS = arrayOf(
                AxisAlignedBB(0.3, -0.0625, 0.3, 0.7, 0.0625, 0.7),
                AxisAlignedBB(0.3, -0.0625, 0.3, 0.7, 0.1875, 0.7),
                AxisAlignedBB(0.3, -0.0625, 0.3, 0.7, 0.5875, 0.7),
                AxisAlignedBB(0.3, -0.0625, 0.3, 0.7, 0.6375, 0.7)
        )
    }
}
