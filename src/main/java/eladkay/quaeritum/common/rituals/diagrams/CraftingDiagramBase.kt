package eladkay.quaeritum.common.rituals.diagrams

import com.google.common.collect.ImmutableList
import eladkay.quaeritum.api.animus.EnumAnimusTier
import eladkay.quaeritum.api.rituals.IDiagram
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldServer

abstract class CraftingDiagramBase : IDiagram {
    val output: ItemStack
    internal val name: String
    internal val input: ImmutableList<ItemStack>
    internal val animus: Int
    internal val rarity: EnumAnimusTier
    internal val onPlayers: Boolean
    internal val requiress: Boolean

    constructor(name: String, input: Array<ItemStack>, output: ItemStack, animus: Int, onPlayers: Boolean, rarity: EnumAnimusTier, requiress: Boolean) {
        this.name = name
        this.input = ImmutableList.copyOf(input)
        this.output = output
        this.animus = animus
        this.rarity = rarity
        this.onPlayers = onPlayers
        this.requiress = requiress
    }

    constructor(name: String, input: Array<ItemStack>, output: Item, animus: Int, onPlayers: Boolean, rarity: EnumAnimusTier, requiress: Boolean) {
        this.name = name
        this.input = ImmutableList.copyOf(input)
        this.output = ItemStack(output)
        this.animus = animus
        this.rarity = rarity
        this.onPlayers = onPlayers
        this.requiress = requiress
    }

    override fun getUnlocalizedName(): String {
        return name
    }

    override fun run(world: World, pos: BlockPos, tile: TileEntity) {
        val item = EntityItem(world, pos.x.toDouble(), (pos.y + 2).toDouble(), pos.z.toDouble(), output)
        if (requiress)
            if (onPlayers)
                IDiagram.Helper.consumeAnimusForRitual(tile, true, animus, EnumAnimusTier.VERDIS)
            else
                IDiagram.Helper.takeAnimus(animus, rarity, tile, 4.0, true)

        for (stack in IDiagram.Helper.entitiesAroundAltar(tile, 4.0)) {
            if (!IDiagram.Helper.isEntityItemInList(stack, input)) continue
            val server = tile.world as WorldServer
            server.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, stack.position.x + 0.5, stack.position.y + 1.0, stack.position.z + 0.5, 1, 0.1, 0.0, 0.1, 0.0)
            stack.setDead()
        }
        println(item.item.item)
        world.spawnEntity(item)
    }

    override fun canRitualRun(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        return !requiress || if (onPlayers) IDiagram.Helper.consumeAnimusForRitual(tile, false, animus, rarity) else IDiagram.Helper.takeAnimus(animus, rarity, tile, 4.0, false)
    }

    override fun hasRequiredItems(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        return IDiagram.Helper.matches(IDiagram.Helper.stacksAroundAltar(tile, 4.0), input)
    }
}
