package eladkay.quaeritum.common.compat.mt

import com.google.common.collect.ImmutableList
import eladkay.quaeritum.api.rituals.IDiagram
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.api.rituals.RitualRegistry
import minetweaker.MineTweakerAPI
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import net.minecraftforge.fml.common.Loader

class DiagramCrafting(internal val name: String, input: Array<ItemStack>, internal val output: ItemStack, internal val chalks: List<PositionedBlock>?, internal val animus: Int, internal val onPlayers: Boolean, internal val rarity: Int, internal val requiress: Boolean) : IDiagram {
    internal val input: ImmutableList<ItemStack>

    init {
        this.input = ImmutableList.copyOf(input)
    }

    override fun getUnlocalizedName(): String {
        return name
    }

    override fun run(world: World, pos: BlockPos, tile: TileEntity) {
        val item = EntityItem(world, pos.x.toDouble(), (pos.y + 2).toDouble(), pos.z.toDouble(), output)
        println(output.displayName)
        if (requiress)
            if (onPlayers)
                IDiagram.Helper.consumeAnimusForRitual(tile, true, animus, 0)
            else
                IDiagram.Helper.takeAnimus(animus, rarity, tile, 4.0, true)

        for (stack in IDiagram.Helper.entitiesAroundAltar(tile, 4.0)) {
            if (!IDiagram.Helper.isEntityItemInList(stack, input)) continue
            val server = tile.world as WorldServer
            server.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, stack.position.x + 0.5, stack.position.y + 1.0, stack.position.z + 0.5, 1, 0.1, 0.0, 0.1, 0.0)
            stack.setDead()
        }
        world.spawnEntityInWorld(item)
        if (Loader.isModLoaded("MineTweaker3")) {
            RitualRegistry.getDiagramList().remove(this)
            MineTweakerAPI.tweaker.load() //sorry, i let you down :(
        }
    }


    override fun canRitualRun(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        if (!requiress) return true
        if (onPlayers)
            return IDiagram.Helper.consumeAnimusForRitual(tile, false, animus, rarity)
        else
            return IDiagram.Helper.takeAnimus(animus, rarity, tile, 4.0, false)
    }

    override fun hasRequiredItems(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        return IDiagram.Helper.matches(IDiagram.Helper.stacksAroundAltar(tile, 4.0), input)
    }

    override fun buildChalks(chalks: MutableList<PositionedBlock>) {
        if (this.chalks != null)
            chalks.addAll(this.chalks)
    }
}
