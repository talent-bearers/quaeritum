package eladkay.quaeritum.common.block

import com.teamwizardry.librarianlib.features.base.block.BlockMod
import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.api.rituals.PositionedBlockChalk
import eladkay.quaeritum.common.block.chalk.BlockChalk
import eladkay.quaeritum.common.block.chalk.BlockChalkTempest
import eladkay.quaeritum.common.block.flowers.BlockAnimusFlower
import eladkay.quaeritum.common.block.machine.BlockCentrifuge
import eladkay.quaeritum.common.block.tile.TileEntityBlueprint
import eladkay.quaeritum.common.block.tile.TileEntityFoundationStone
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry

/**
 * @author WireSegal
 * *         Created at 5:00 PM on 4/16/16.
 */
object ModBlocks {
    val blueprint: BlockMod
    val chalk: BlockChalk
    val flower: BlockAnimusFlower
    val crystal: CrystalSoul
    val foundation: BlockFoundationStone
    val tempest: BlockChalkTempest

    val centrifuge: BlockCentrifuge

    init {
        blueprint = BlockBlueprint(LibNames.BLUEPRINT)
        chalk = BlockChalk()
        flower = BlockAnimusFlower()
        crystal = CrystalSoul()
        foundation = BlockFoundationStone()
        tempest = BlockChalkTempest()
        centrifuge = BlockCentrifuge()

        GameRegistry.registerTileEntity(TileEntityBlueprint::class.java, ResourceLocation(LibMisc.MOD_ID, LibNames.BLUEPRINT).toString())
        GameRegistry.registerTileEntity(TileEntityFoundationStone::class.java, ResourceLocation(LibMisc.MOD_ID, LibNames.FOUNDATION).toString())

        PositionedBlockChalk.chalk = chalk
        PositionedBlockChalk.tempest = tempest
    }
}
