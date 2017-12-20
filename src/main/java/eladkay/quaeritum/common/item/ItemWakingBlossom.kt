package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import eladkay.quaeritum.api.lib.LibMisc
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagString
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.util.Constants

/**
 * @author WireSegal
 * Created at 9:21 PM on 12/14/17.
 */
class ItemWakingBlossom : ItemMod("waking_blossom") {

    init {
        addPropertyOverride(ResourceLocation(LibMisc.MOD_ID, "blossom")) { stack, _, _ ->
            ItemNBTHelper.getList(stack, "biomes", Constants.NBT.TAG_STRING)?.tagCount()?.toFloat() ?: 0f
        }
    }

    fun update(stack: ItemStack, world: World, posX: Int, posZ: Int): ItemStack {
        var biome = world.getBiome(BlockPos(posX, 0, posZ))
        if (biome.isMutation)
            biome = Biome.REGISTRY.getObjectById(Biome.MUTATION_TO_BASE_ID_MAP[biome]) ?: biome
        val biomeName = biome.biomeClass.name
        val allNames = ItemNBTHelper.getList(stack, "biomes", Constants.NBT.TAG_STRING) ?: NBTTagList()

        val setOf = allNames.filterIsInstance<NBTTagString>().map { it.string }.toMutableSet()
        val size = setOf.size
        setOf.add(biomeName)

        if (setOf.size != size) {
            if (setOf.size < 4)
                ItemNBTHelper.setList(stack, "biomes", NBTTagList().apply { setOf.forEach { appendTag(NBTTagString(it)) } })
            else {
                return ItemResource.Resources.AWOKEN_BLOSSOM.stackOf(stack.count)
                        .apply {
                            tagCompound = stack.tagCompound?.copy()?.apply { removeTag("biomes") }
                            if (tagCompound?.keySet?.size == 0)
                                tagCompound = null
                        }
            }
        }
        return stack
    }

    override fun onEntityItemUpdate(entityItem: EntityItem): Boolean {
        entityItem.item = update(entityItem.item, entityItem.world, entityItem.posX.toInt(), entityItem.posZ.toInt())
        return false
    }

    override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {
        if (entityIn is EntityPlayer)
            entityIn.inventory.setInventorySlotContents(itemSlot,
                    update(stack, worldIn, entityIn.posX.toInt(), entityIn.posZ.toInt()))
    }
}
