package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.features.base.item.IGlowingItem
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import eladkay.quaeritum.client.gui.GUIHandler.GUI_CODEX
import eladkay.quaeritum.common.Quaeritum
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * @author WireSegal
 * Created at 6:04 PM on 7/31/17.
 */
class ItemCodex : ItemMod(LibNames.CODEX), IGlowingItem {
    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        val stack = playerIn.getHeldItem(handIn)
        playerIn.openGui(Quaeritum.instance, GUI_CODEX, worldIn, 0, 0, 0)
        return ActionResult(EnumActionResult.SUCCESS, stack)
    }

    @SideOnly(Side.CLIENT)
    override fun transformToGlow(itemStack: ItemStack, model: IBakedModel) = IGlowingItem.Helper.wrapperBake(model, false, 1)

    @SideOnly(Side.CLIENT)
    override fun shouldDisableLightingForGlow(itemStack: ItemStack, model: IBakedModel) = true
}
