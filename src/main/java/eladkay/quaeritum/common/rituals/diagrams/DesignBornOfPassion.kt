package eladkay.quaeritum.common.rituals.diagrams

import eladkay.quaeritum.api.animus.EnumAnimusTier
import eladkay.quaeritum.api.rituals.IDiagram
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.api.rituals.PositionedBlockChalk
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.init.SoundEvents
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.SoundCategory
import net.minecraft.util.WeightedRandom
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.oredict.OreDictionary
import java.util.*


class DesignBornOfPassion : IDiagram {

    @Suppress("UNCHECKED_CAST")
    companion object {
        private var stoneOres: MutableMap<String, Int> = mutableMapOf()
        private var netherOres: MutableMap<String, Int> = mutableMapOf()

        init {
            stoneOres.put("oreCoal", 46525)
            stoneOres.put("oreDiamond", 1265)
            stoneOres.put("oreEmerald", 780)
            stoneOres.put("oreIron", 20665)
            stoneOres.put("oreLapis", 1285)
            stoneOres.put("oreRedstone", 6885)

            netherOres.put("oreQuartz", 19600)

            try { // If Botania is present, use the Orechid table.
                val apiClazz = Class.forName("vazkii.botania.api.BotaniaAPI")
                stoneOres = apiClazz.getDeclaredField("oreWeights").get(null) as MutableMap<String, Int>
                netherOres = apiClazz.getDeclaredField("oreWeightsNether").get(null) as MutableMap<String, Int>
            } catch (ignored: ClassNotFoundException) {} catch (ignored: NoSuchFieldException) {}
        }

        // Copied from Botania with minor changes.
        fun getOreToPut(stone: Boolean, rand: Random): ItemStack {
            val values = mutableListOf<StringRandomItem>()
            val map = if (stone) stoneOres else netherOres

            if (map.isEmpty()) return ItemStack.EMPTY

            for ((k, v) in map)
                values.add(StringRandomItem(v, k))

            val ore = WeightedRandom.getRandomItem<StringRandomItem>(rand, values).s
            val ores = OreDictionary.getOres(ore)

            for (stack in ores) {
                val item = stack.item
                val clname = item.javaClass.name

                // This poem is dedicated to Greg
                //
                // Greg.
                // I get what you do when
                // others say it's a grind.
                // But take your TE ores
                // and stick them in your behind.
                if (clname.startsWith("gregtech") || clname.startsWith("gregapi"))
                    continue
                if (item !is ItemBlock)
                    continue

                return stack
            }

            return getOreToPut(stone, rand)
        }

        private class StringRandomItem(par1: Int, val s: String) : WeightedRandom.Item(par1)
    }

    override fun getUnlocalizedName(): String {
        return "ninebynine"
    }

    override fun hasRequiredItems(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        return IDiagram.Helper.consumeAnimusForRitual(tile, false, 500, EnumAnimusTier.FERRUS)
    }


    private val RANGE = 4
    private val RANGE_Y = 3

    override fun getPrepTime(world: World, pos: BlockPos, tile: TileEntity): Int {
        return 100
    }

    override fun run(world: World, pos: BlockPos, te: TileEntity) {
        if (IDiagram.Helper.consumeAnimusForRitual(te, false, 500, EnumAnimusTier.FERRUS)) {
            val possibleCoords = ArrayList<BlockPos>()

            for (orePos in BlockPos.getAllInBox(pos.add(-RANGE, -RANGE_Y, -RANGE), pos.add(RANGE, RANGE_Y, RANGE))) {
                val state = world.getBlockState(orePos)
                if (state.block.isReplaceableOreGen(state, world, orePos) { it != null && (it.block == Blocks.STONE || it.block == Blocks.NETHERRACK) })
                    possibleCoords.add(orePos)
            }

            if (possibleCoords.isNotEmpty()) {
                val target = possibleCoords[world.rand.nextInt(possibleCoords.size)]
                val state = world.getBlockState(target)
                val stone = state.block == Blocks.STONE
                val stack = getOreToPut(stone, world.rand)
                if (!stack.isEmpty) {
                    val block = Block.getBlockFromItem(stack.item)
                    val meta = stack.itemDamage
                    world.setBlockState(target, block.getStateFromMeta(meta))
                    world.playEvent(2001, target, Block.getIdFromBlock(block) + (meta shl 12))
                    world.playSound(null, target, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 2f, 1f)

                    IDiagram.Helper.consumeAnimusForRitual(te, true, 500, EnumAnimusTier.FERRUS)
                }
            }
        }
    }


    override fun canRitualRun(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        return true
    }

    override fun buildChalks(chalks: MutableList<PositionedBlock>) {
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLUE, BlockPos(-3, 0, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-2, 0, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(-1, 0, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(0, 0, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(1, 0, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(2, 0, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLUE, BlockPos(3, 0, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-3, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(-2, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(-1, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(0, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.SILVER, BlockPos(1, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.SILVER, BlockPos(2, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(3, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(-3, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(-2, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.CYAN, BlockPos(-1, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.CYAN, BlockPos(0, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.CYAN, BlockPos(1, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.SILVER, BlockPos(2, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(3, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(-3, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(-2, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.CYAN, BlockPos(-1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.CYAN, BlockPos(1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(2, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(3, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(-3, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.SILVER, BlockPos(-2, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.CYAN, BlockPos(-1, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.CYAN, BlockPos(0, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.CYAN, BlockPos(1, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(2, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(3, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-3, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.SILVER, BlockPos(-2, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.SILVER, BlockPos(-1, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(0, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(1, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(2, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(3, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLUE, BlockPos(-3, 0, 3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-2, 0, 3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(-1, 0, 3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(0, 0, 3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(1, 0, 3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(2, 0, 3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLUE, BlockPos(3, 0, 3)))
    }
}
