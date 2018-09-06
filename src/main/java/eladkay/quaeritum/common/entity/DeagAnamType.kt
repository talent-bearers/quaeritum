package eladkay.quaeritum.common.entity

import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.awt.Color

/**
 * @author WireSegal
 * Created at 9:57 AM on 7/17/18.
 */
enum class DeagAnamType(val id: String, val color: Color, val predicate: (World, BlockPos, IBlockState) -> Boolean) {
    VIBRANT("vibrant", Color(0xff525b), { _, _, it ->
        it.material == Material.GRASS ||
                it.material == Material.GROUND ||
                it.material == Material.WOOD ||
                it.material == Material.LEAVES ||
                it.material == Material.PLANTS ||
                it.material == Material.VINE
    }),
    MANA("mana", Color(0x4ae6ff), { world, pos, _ -> world.getTileEntity(pos) != null }),
    MAGMA("magma", Color(0xff3816), { world, pos, it ->
        it.block.isFireSource(world, pos, EnumFacing.UP)
    }),
    FROSTED("frosted", Color(0xb7f8ff), { _, _, it ->
        it.material == Material.CRAFTED_SNOW ||
                it.material == Material.SNOW
    }),
    CONDUCTIVE("conductive", Color(0xaca95e), { _, _, it ->
        it.material == Material.IRON
    }),
    TAR("tar", Color(0x464646), { world, pos, _ ->
        world.getBiome(pos).isHighHumidity
    }),
    HONEY("honey", Color(0xffa220), { _, _, it ->
        it.material == Material.CAKE ||
                it.material == Material.GOURD
    })
}
