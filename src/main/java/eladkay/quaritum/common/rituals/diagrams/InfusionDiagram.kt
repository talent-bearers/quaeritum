package eladkay.quaritum.common.rituals.diagrams

import amerifrance.guideapi.api.IPage
import amerifrance.guideapi.page.PageText
import com.google.common.collect.Lists
import com.teamwizardry.librarianlib.client.util.TooltipHelper
import eladkay.quaritum.api.animus.AnimusHelper
import eladkay.quaritum.api.animus.INetworkProvider
import eladkay.quaritum.api.lib.LibBook
import eladkay.quaritum.api.rituals.IDiagram
import eladkay.quaritum.api.rituals.PositionedBlock
import eladkay.quaritum.api.rituals.PositionedBlockChalk
import eladkay.quaritum.common.block.ModBlocks
import eladkay.quaritum.common.block.flowers.BlockAnimusFlower
import eladkay.quaritum.common.book.ModBook
import eladkay.quaritum.common.book.PageDiagram
import eladkay.quaritum.common.item.ModItems
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import java.util.*

class InfusionDiagram : IDiagram {

    override fun getUnlocalizedName(): String {
        return "rituals.quaritum.infusion"
    }

    override fun run(world: World, pos: BlockPos, tes: TileEntity) {
        /* EntityItem item = new EntityItem(world, pos.getX(), pos.getY() + 2, pos.getZ(), ItemAwakenedSoulstone.withAnimus(100, 2));
        EntityItem item2 = new EntityItem(world, pos.getX(), pos.getY() + 2, pos.getZ(), new ItemStack(Blocks.GOLD_BLOCK));

        world.spawnEntityInWorld(item);
        world.spawnEntityInWorld(item2);*/
        for (stack in IDiagram.Helper.entitiesAroundAltar(tes, 4.0)) {
            val server = tes.world as WorldServer
            server.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, stack.position.x + 0.5, stack.position.y + 1.0, stack.position.z + 0.5, 1, 0.1, 0.0, 0.1, 0.0)
            stack.setDead()
        }
        val stack = IDiagram.Helper.getNearestAttunedSoulstone(tes, 4.0)
        if (stack == null || stack.item !is INetworkProvider || !(stack.item as INetworkProvider).isReceiver(stack))
            return
        val player = (stack.item as INetworkProvider).getPlayer(stack)
        AnimusHelper.Network.setInfused(player, true)
    }

    override fun canRitualRun(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        return true
    }

    override fun hasRequiredItems(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        return IDiagram.Helper.matches(IDiagram.Helper.stacksAroundAltar(tile, 4.0), requiredItems)
    }

    val requiredItems: ArrayList<ItemStack>
        get() {
            val list = Lists.newArrayList<ItemStack>()
            list.add(ItemStack(ModItems.dormant))
            list.add(ItemStack(ModBlocks.flower, 4, BlockAnimusFlower.Variants.ARCANE.ordinal))
            list.add(ItemStack(ModBlocks.crystal))
            return list
        }

    override fun buildChalks(chalks: MutableList<PositionedBlock>) {
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(-1, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(0, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(1, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(-1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(-1, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(0, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(1, 0, 1)))
    }

    override fun constructBook() {
        pages.add(PageText(TooltipHelper.local(LibBook.ENTRY_INFUSION_PAGE1)))
        val l = Lists.newArrayList<PositionedBlock>()
        buildChalks(l)
        pages.add(PageDiagram(l, requiredItems))
        ModBook.register(ModBook.pagesDiagrams, LibBook.ENTRY_INFUSION_NAME, pages, ItemStack(ModBlocks.crystal))

    }

    companion object {
        var pages: MutableList<IPage> = ArrayList()
    }
}
