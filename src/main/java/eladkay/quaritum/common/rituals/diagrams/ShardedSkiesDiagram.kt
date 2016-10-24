package eladkay.quaritum.common.rituals.diagrams

import amerifrance.guideapi.api.IPage
import amerifrance.guideapi.page.PageText
import com.google.common.collect.Lists
import eladkay.quaritum.api.lib.LibBook
import eladkay.quaritum.api.rituals.IDiagram
import eladkay.quaritum.api.rituals.PositionedBlock
import eladkay.quaritum.client.core.TooltipHelper
import eladkay.quaritum.common.block.ModBlocks
import eladkay.quaritum.common.block.flowers.BlockAnimusFlower
import eladkay.quaritum.common.book.ModBook
import eladkay.quaritum.common.book.PageDiagram
import eladkay.quaritum.common.networking.FancyParticlePacket
import eladkay.quaritum.common.networking.NetworkHelper
import net.minecraft.entity.item.EntityItem
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import java.util.*

class ShardedSkiesDiagram : IDiagram {

    override fun constructBook() {
        pages.add(PageText(TooltipHelper.local(LibBook.ENTRY_SHARDED_SKIES_PAGE1)))
        val list = ArrayList<PositionedBlock>()
        buildChalks(list)
        pages.add(PageDiagram(list, requiredItems))
        ModBook.register(ModBook.pagesDiagrams, LibBook.ENTRY_SHARDED_SKIES_NAME, pages, ItemStack(ModBlocks.flower))
    }

    override fun getUnlocalizedName(): String {
        return "rituals.quaritum.shardedsky"
    }

    override fun run(world: World, pos: BlockPos, tes: TileEntity) {
        val item = EntityItem(world, pos.x.toDouble(), (pos.y + 2).toDouble(), pos.z.toDouble(), ItemStack(ModBlocks.flower, 1, BlockAnimusFlower.Variants.COMMON.ordinal))

        for (stack in IDiagram.Helper.entitiesAroundAltar(tes, 4.0)) {
            if (!IDiagram.Helper.isEntityItemInList(stack, requiredItems)) continue
            val server = tes.world as WorldServer
            server.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, stack.position.x + 0.5, stack.position.y + 1, stack.position.z + 0.5, 1, 0.1, 0, 0.1, 0)
            stack.setDead()
        }
        world.spawnEntityInWorld(item)
    }

    override fun onPrepUpdate(world: World, pos: BlockPos, tile: TileEntity, ticksRemaining: Int): Boolean {
        // NetworkHelper.tellEveryoneAround(new FancyParticlePacket(pos.getX(), pos.getY(), pos.getZ(), 100), world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 16);
        var flag = false
        for (stack in IDiagram.Helper.entitiesAroundAltar(tile, 4.0))
            if (IDiagram.Helper.isEntityItemInList(stack, requiredItems)) {
                try {
                    NetworkHelper.tellEveryoneAround(FancyParticlePacket(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 50), world.provider.dimension, pos.x, pos.y, pos.z, 16)
                } catch (server: NoClassDefFoundError) {
                }

                flag = true
            }
        return flag
    }

    override fun getPrepTime(world: World, pos: BlockPos, tile: TileEntity): Int {
        return 50
    }

    override fun hasRequiredItems(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        return IDiagram.Helper.matches(IDiagram.Helper.stacksAroundAltar(tile, 4.0), requiredItems)
    }

    override fun canRitualRun(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        return true
    }

    val requiredItems: ArrayList<ItemStack>
        get() {
            val list = Lists.newArrayList<ItemStack>()
            list.add(ItemStack(Blocks.RED_FLOWER))
            return list
        }

    override fun buildChalks(chalks: List<PositionedBlock>) {
        //NO-OP
    }

    companion object {

        var pages: MutableList<IPage> = ArrayList()
    }

}
