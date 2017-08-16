package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper
import eladkay.quaeritum.api.spell.ElementHandler
import eladkay.quaeritum.api.spell.EnumSpellElement
import eladkay.quaeritum.api.spell.SpellParser
import eladkay.quaeritum.api.spell.render.RenderUtil
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.math.MathHelper
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import java.awt.Color

/**
 * @author WireSegal
 * Created at 10:01 PM on 8/3/17.
 */
class ItemEvoker : ItemMod(LibNames.SOUL_EVOKER), IItemColorProvider {
    companion object {
        fun getEvocationFromStack(stack: ItemStack): Array<EnumSpellElement> {
            if (stack.item !is ItemEvoker) return arrayOf()
            return ElementHandler.fromBytes(ItemNBTHelper.getByteArray(stack, "elements")?: byteArrayOf())
        }

        fun setStackEvocation(stack: ItemStack, elements: Array<EnumSpellElement>) {
            if (stack.item !is ItemEvoker) return
            ItemNBTHelper.setIntArray(stack, "elements", ElementHandler.fromElements(elements))
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
                    var xBucket = 0f
                    var yBucket = 0f
                    for (element in elements) {
                        val c = element.color()
                        val r = c and 0xff0000 shr 16
                        val g = c and 0x00ff00 shr 8
                        val b = c and 0x0000ff
                        val h = Color.RGBtoHSB(r, g, b, hsb)[0]
                        xBucket += MathHelper.cos(h * 2 * Math.PI.toFloat())
                        yBucket += MathHelper.sin(h * 2 * Math.PI.toFloat())
                    }
                    val angle = RenderUtil.fastAtan2(yBucket / elements.size, xBucket / elements.size) / (2 * Math.PI.toFloat())
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

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.tooltipIfShift(tooltip) {
            val parser = SpellParser(getEvocationFromStack(stack))
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
