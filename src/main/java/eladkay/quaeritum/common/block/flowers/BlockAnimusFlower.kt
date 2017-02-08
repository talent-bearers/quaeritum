package eladkay.quaeritum.common.block.flowers

import eladkay.quaeritum.common.block.base.BlockModFlower
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IStringSerializable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import java.util.*

class BlockAnimusFlower : BlockModFlower(LibNames.FLOWER, Material.PLANTS, *BlockAnimusFlower.Variants.vars) {

    init {
        defaultState = this.blockState.baseState.withProperty(FLOWER_TYPE, Variants.COMMON)
    }

    override fun getPickBlock(state: IBlockState, target: RayTraceResult?, world: World, pos: BlockPos, player: EntityPlayer?): ItemStack {
        return ItemStack(Item.getItemFromBlock(this)!!, 1, this.getMetaFromState(world.getBlockState(pos)))
    }

    override fun getDrops(world: IBlockAccess?, pos: BlockPos?, state: IBlockState, fortune: Int): List<ItemStack> {
        return Arrays.asList(ItemStack(Item.getItemFromBlock(this)!!, 1, this.getMetaFromState(state)))
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(FLOWER_TYPE, Variants.of(meta)!!)
    }

    override fun getMetaFromState(state: IBlockState?): Int {
        val type = state!!.getValue(FLOWER_TYPE)
        return type.ordinal
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, FLOWER_TYPE)
    }

//    override fun getTier(stack: ItemStack): Int {
//        val `var` = Variants.of(stack.itemDamage) ?: return 0
//        return `var`.rarity
//    }
//
//    override fun getAnimusFromStack(stack: ItemStack): Int {
//        val `var` = Variants.of(stack.itemDamage) ?: return 0
//        return `var`.amount
//    }

    enum class Variants private constructor(var rarity: Int, var amount: Int, var nm: String) : IStringSerializable {
        COMMON(0, 20, "common"), COMMON_ARCANE(1, 40, "commonArcane"), ARCANE(2, 60, "arcane");

        override fun getName(): String {
            return toString().toLowerCase()
        }

        companion object {

            var vars = arrayOf<String>()

            init {
                Array(Variants.values().size) {
                    val variant = Variants.values()[it]
                    LibNames.CHALK + variant.getName()
                            .split("_".toRegex())
                            .filter(String::isNotEmpty)
                            .map { BlockAnimusFlower.capitalizeFirst(it) }
                            .joinToString("")
                }
            }

            fun of(meta: Int): Variants? {
                if (meta < 0 || meta > values().size) return null
                return values()[meta]
            }

            fun rarityToName(rarity: Int): String? {
                return if (rarity < vars.size && rarity > 0) vars[rarity] else null
            }
        }
    }

    companion object {

        var FLOWER_TYPE: IProperty<Variants> = PropertyEnum.create<Variants>("type", Variants::class.java)

        fun capitalizeFirst(str: String): String {
            return str.substring(0, 1).toUpperCase() + str.substring(1)
        }
    }
}
