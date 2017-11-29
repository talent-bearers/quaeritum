package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.core.LibrarianLib
import com.teamwizardry.librarianlib.core.client.ClientTickHandler
import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import com.teamwizardry.librarianlib.features.kotlin.isNotEmpty
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper
import eladkay.quaeritum.api.spell.ElementHandler
import eladkay.quaeritum.api.spell.EnumSpellElement
import eladkay.quaeritum.api.spell.SpellParser
import eladkay.quaeritum.api.spell.render.RenderUtil
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import net.minecraft.util.math.MathHelper
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.registries.IForgeRegistryEntry
import java.awt.Color

/**
 * @author WireSegal
 * Created at 10:01 PM on 8/3/17.
 */
class ItemEvoker : ItemMod(LibNames.SOUL_EVOKER), IItemColorProvider {
    companion object {
        fun hasEvocation(stack: ItemStack): Boolean {
            return getEvocationFromStack(stack).isNotEmpty()
        }

        fun getEvocationFromStack(stack: ItemStack): Array<EnumSpellElement> {
            if (stack.maxStackSize != 1) return arrayOf()
            if (!ItemNBTHelper.getNBT(stack, false).hasKey("quaeritum_elements")) return arrayOf()
            return ElementHandler.fromBytes(ItemNBTHelper.getByteArray(stack, "quaeritum_elements")?: byteArrayOf())
        }

        fun setStackEvocation(stack: ItemStack, elements: Array<EnumSpellElement>) {
            if (stack.maxStackSize != 1) return
            if (elements.isEmpty())
                ItemNBTHelper.removeEntry(stack, "quaeritum_elements")
            else
                ItemNBTHelper.setIntArray(stack, "quaeritum_elements", ElementHandler.fromElements(elements))

            val tag = stack.tagCompound
            if (tag != null && tag.hasNoTags())
                stack.tagCompound = null
        }

        init {
            MinecraftForge.EVENT_BUS.register(this)
        }

        @SubscribeEvent
        fun onTooltip(e: ItemTooltipEvent) {
            if (hasEvocation(e.itemStack) && e.itemStack.item !is ItemEvoker) {
                val parser = SpellParser(getEvocationFromStack(e.itemStack))
                if (parser.spells.isNotEmpty()) {
                    if (GuiScreen.isShiftKeyDown())
                        for (i in parser.spells.reversed())
                            e.toolTip.add(1, SpellParser.localized(i).setStyle(Style().setColor(TextFormatting.GRAY)).formattedText)
                    else
                        e.toolTip.add(1, TooltipHelper.local("${LibrarianLib.MODID}.shiftinfo").replace("&".toRegex(), "§"))
                }
            }
        }
    }

    init {
        setMaxStackSize(1)
    }

    private val hsb = floatArrayOf(0f, 0f, 0f)

    override val itemColorFunction: ((stack: ItemStack, tintIndex: Int) -> Int)?
        get() = { stack, tint ->
            if (tint == 1) {
                val elements = getEvocationFromStack(stack)
                if (elements.isEmpty()) -1 else {
                    val angle = if (!elements.any { it != EnumSpellElement.AETHER }) {
                        ClientTickHandler.ticksInGame * 2L % 360L / 360.0f
                    } else {
                        var xBucket = 0f
                        var yBucket = 0f
                        var size = 0
                        for (element in elements) {
                            val c = element.color ?: continue
                            size++
                            val r = c.red
                            val g = c.green
                            val b = c.blue
                            val h = Color.RGBtoHSB(r, g, b, hsb)[0]
                            xBucket += MathHelper.cos(h * 2 * Math.PI.toFloat())
                            yBucket += MathHelper.sin(h * 2 * Math.PI.toFloat())
                        }
                        RenderUtil.fastAtan2(yBucket / size, xBucket / size) / (2 * Math.PI.toFloat())
                    }
                    Color.HSBtoRGB(angle, elements.size / 8f, 1f)
                }
            } else -1
        }

    override fun onLeftClickEntity(stack: ItemStack, player: EntityPlayer, entity: Entity): Boolean {
        var reagents = ElementHandler.getReagentsTyped(player)
        var clear = true

        val evocation = ItemEvoker.getEvocationFromStack(stack)
        if (reagents.isEmpty() && ElementHandler.probeReagents(player, *evocation) == EnumActionResult.SUCCESS) {
            reagents = evocation
            clear = false
        }

        if (player.isSneaking || player.heldItemMainhand.item is ItemEvoker) {

            SpellParser(reagents).cast(player)
            // todo fwoosh
            if (clear)
                ElementHandler.clearReagents(player)
        }

        return true
    }

    @SideOnly(Side.CLIENT)
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<String>, advanced: ITooltipFlag) {
        val parser = SpellParser(getEvocationFromStack(stack))
        if (parser.spells.isNotEmpty()) TooltipHelper.tooltipIfShift(tooltip) {
            parser.spells.mapTo(tooltip) { SpellParser.localized(it).setStyle(Style().setColor(TextFormatting.GRAY)).formattedText }
        }
    }

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        val stack = playerIn.getHeldItem(handIn)
        if (worldIn.isRemote) return ActionResult(EnumActionResult.SUCCESS, stack)
        val evocation = getEvocationFromStack(stack)
        val playerReagents = ElementHandler.getReagentsTyped(playerIn)
        if (evocation.isEmpty() && playerIn.isSneaking) {
            ElementHandler.clearReagents(playerIn)
            setStackEvocation(stack, playerReagents)
        } else if (evocation.isNotEmpty()) {
            if (playerReagents.isNotEmpty())
                if (playerIn.isSneaking) ElementHandler.clearReagents(playerIn)
                else {
                    // todo breaking sound
                    return ActionResult(EnumActionResult.SUCCESS, stack)
                }

            if (playerIn.isSneaking && playerReagents.isEmpty()) {
                setStackEvocation(stack, arrayOf())
                ElementHandler.setReagents(playerIn, *evocation)
            } else if (ElementHandler.addReagents(playerIn, *evocation) != EnumActionResult.SUCCESS)
                // todo breaking sound
                return ActionResult(EnumActionResult.SUCCESS, stack)
        }
        return ActionResult(EnumActionResult.SUCCESS, stack)
    }
}

object EvocationRecipe : IForgeRegistryEntry.Impl<IRecipe>(), IRecipe {
    override fun getRemainingItems(inv: InventoryCrafting): NonNullList<ItemStack> {
        val ret = NonNullList.withSize(inv.sizeInventory, ItemStack.EMPTY)
        for (i in ret.indices) {
            val stack = inv.getStackInSlot(i)
            if (ItemEvoker.hasEvocation(stack))
                ret[i] = stack.copy().apply {
                    count = 1
                    ItemEvoker.setStackEvocation(this, arrayOf())
                }
        }
        return ret
    }

    override fun getCraftingResult(inv: InventoryCrafting): ItemStack {
        var item = ItemStack.EMPTY
        var evoker = ItemStack.EMPTY

        for (i in 0 until inv.sizeInventory) {
            val stack = inv.getStackInSlot(i)
            if (stack.isNotEmpty) {
                if (ItemEvoker.hasEvocation(stack)) {
                    if (evoker.isNotEmpty)
                        return ItemStack.EMPTY
                    evoker = stack
                    continue
                }

                if (item.isNotEmpty)
                    return ItemStack.EMPTY
                item = stack
            }
        }

        if (item.isEmpty || evoker.isEmpty)
            return ItemStack.EMPTY

        val itemCopy = item.copy()
        ItemEvoker.setStackEvocation(itemCopy, ItemEvoker.getEvocationFromStack(evoker))
        return itemCopy
    }

    override fun getRecipeOutput(): ItemStack = ItemStack.EMPTY

    override fun canFit(width: Int, height: Int): Boolean {
        return true
    }

    override fun isHidden(): Boolean {
        return true
    }

    override fun matches(inv: InventoryCrafting, worldIn: World?): Boolean {
        var item = ItemStack.EMPTY
        var evoker = ItemStack.EMPTY

        for (i in 0 until inv.sizeInventory) {
            val stack = inv.getStackInSlot(i)
            if (stack.isNotEmpty) {
                if (ItemEvoker.hasEvocation(stack)) {
                    if (evoker.isNotEmpty)
                        return false
                    evoker = stack
                    continue
                }

                if (item.isNotEmpty)
                    return false
                item = stack
            }
        }

        return item.isNotEmpty && evoker.isNotEmpty
    }
}
