package eladkay.quaritum.common.block

import com.teamwizardry.librarianlib.common.base.block.BlockMod
import eladkay.quaritum.api.lib.LibMisc
import eladkay.quaritum.api.rituals.PositionedBlockChalk
import eladkay.quaritum.common.block.chalk.BlockChalk
import eladkay.quaritum.common.block.chalk.BlockChalkTempest
import eladkay.quaritum.common.block.flowers.BlockAnimusFlower
import eladkay.quaritum.common.block.tile.TileEntityBlueprint
import eladkay.quaritum.common.block.tile.TileEntityFoundationStone
import eladkay.quaritum.common.lib.LibNames
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry

/**
 * @author WireSegal
 * *         Created at 5:00 PM on 4/16/16.
 */
object ModBlocks {
    var blueprint: BlockMod
    var chalk: BlockChalk
    var flower: BlockAnimusFlower
    var crystal: CrystalSoul
    var foundation: BlockFoundationStone
    var tempest: BlockChalkTempest

    init {
        blueprint = BlockBlueprint(LibNames.BLUEPRINT)
        chalk = BlockChalk()
        flower = BlockAnimusFlower()
        crystal = CrystalSoul()
        foundation = BlockFoundationStone()
        tempest = BlockChalkTempest()

        GameRegistry.registerTileEntity(TileEntityBlueprint::class.java, ResourceLocation(LibMisc.MOD_ID, LibNames.BLUEPRINT).toString())
        GameRegistry.registerTileEntity(TileEntityFoundationStone::class.java, ResourceLocation(LibMisc.MOD_ID, LibNames.FOUNDATION).toString())

        PositionedBlockChalk.chalk = chalk
        PositionedBlockChalk.tempest = tempest
    }
}
