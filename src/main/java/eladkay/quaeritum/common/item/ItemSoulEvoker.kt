package eladkay.quaeritum.common.item

import baubles.api.BaublesApi
import com.teamwizardry.librarianlib.client.core.ClientTickHandler
import com.teamwizardry.librarianlib.client.util.TooltipHelper
import com.teamwizardry.librarianlib.client.util.glColor
import com.teamwizardry.librarianlib.common.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.common.base.item.ItemMod
import com.teamwizardry.librarianlib.common.network.PacketHandler
import com.teamwizardry.librarianlib.common.util.ItemNBTHelper
import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.api.spell.ISpellProvider
import eladkay.quaeritum.common.Quaeritum
import eladkay.quaeritum.common.lib.LibNames
import eladkay.quaeritum.common.networking.SelectSlotPacket
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import net.minecraftforge.client.event.MouseEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import java.awt.Color



/**
 * @author WireSegal
 * Created at 5:18 PM on 11/5/16.
 */
class ItemSoulEvoker() : ItemMod(LibNames.SOUL_EVOKER), IItemColorProvider {

    /*
        todo:
        showing cooldowns/spells in HUD while equipped
     */

    companion object {
        val TAG_SLOT = "slot"
        val MAX_SLOT = 7

        init {
            MinecraftForge.EVENT_BUS.register(this)
        }

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        fun onKeyInput(e: MouseEvent) {
            val player = Minecraft.getMinecraft().thePlayer
            if (Keyboard.isCreated() && e.dwheel != 0 && player.isSneaking) {
                val world = Minecraft.getMinecraft().theWorld
                var theStack = player.heldItemMainhand
                var theHand = EnumHand.MAIN_HAND
                if (theStack == null || theStack.item !is ItemSoulEvoker) {
                    theStack = player.heldItemOffhand
                    theHand = EnumHand.OFF_HAND
                }

                if (theStack == null || theStack.item !is ItemSoulEvoker)
                    return

                e.isCanceled = true

                val slot = getSlot(theStack)
                val allSlots = getAvailableSlots()
                val coeff = if (e.dwheel < 0) -1 else 1
                val newSlot = if (allSlots.none { it })
                    (slot + coeff + MAX_SLOT) % MAX_SLOT
                else {
                    var token = (slot + coeff + MAX_SLOT) % MAX_SLOT
                    var pointer = allSlots[token]
                    while (!pointer) {
                        token = (token + coeff + MAX_SLOT) % MAX_SLOT
                        pointer = allSlots[token]
                    }
                    token

                }
                world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.PLAYERS, 0.6F, (1.0F + (player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.2F) * 0.7F)
                PacketHandler.NETWORK.sendToServer(SelectSlotPacket(newSlot, theHand))

                val baubles = BaublesApi.getBaublesHandler(player) ?: return
                val stack = baubles.getStackInSlot(newSlot)
                if (stack != null && stack.item is ISpellProvider) {
                    val spell = (stack.item as ISpellProvider).getSpell(stack, newSlot)
                    if (spell != null) {
                        Quaeritum.proxy.setRemainingItemDisplay(null, spell.getIconStack(stack, newSlot), spell.getSpellName(stack, newSlot))
                        return
                    }
                }
                Quaeritum.proxy.setRemainingItemDisplay(null, theStack, TooltipHelper.local("misc.${LibMisc.MOD_ID}.noSpell"))
            }
        }

        @SideOnly(Side.CLIENT)
        fun getAvailableSlots(): List<Boolean> {
            val baubles = BaublesApi.getBaublesHandler(Minecraft.getMinecraft().thePlayer) ?: return (0..7).map { false }
            return (0 until 7).map {
                val stack = baubles.getStackInSlot(it)
                stack?.item is ISpellProvider
            }
        }

        private val HUD = ResourceLocation(LibMisc.MOD_ID, "textures/gui/gauge.png")

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        fun onRenderHUD(e: RenderGameOverlayEvent.Pre) {
            if (e.type == RenderGameOverlayEvent.ElementType.ALL) {
                val mc = Minecraft.getMinecraft()
                val mainStack = mc.thePlayer.heldItemMainhand
                val offStack = mc.thePlayer.heldItemOffhand
                if (mainStack?.item != ModItems.soulEvoker && offStack?.item != ModItems.soulEvoker)
                    return

                val selectedSlots = mutableListOf<Int>()
                if (mainStack != null && mainStack.item == ModItems.soulEvoker)
                    selectedSlots.add(getSlot(mainStack))
                if (offStack != null && offStack.item == ModItems.soulEvoker)
                    selectedSlots.add(getSlot(offStack))

                val baubles = BaublesApi.getBaublesHandler(mc.thePlayer) ?: return

                mc.renderEngine.bindTexture(HUD)

                val allSlots = getAvailableSlots()

                GlStateManager.pushMatrix()
                GlStateManager.scale(4f, 4f, 4f)
                GlStateManager.translate(10F, 2F, 0F)
                for (count in 0 until 7) {
                    val stack = baubles.getStackInSlot(count)

                    val percentage = if (stack == null || ISpellProvider.getCooldown(stack) == 0 || ISpellProvider.getMaxCooldown(stack) == 0)
                        1F
                    else
                        1 - (ISpellProvider.getCooldown(stack).toFloat() / ISpellProvider.getMaxCooldown(stack))

                    val height = (percentage * 42).toInt()

                    GlStateManager.enableBlend()
                    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
                    GlStateManager.pushMatrix()
                    GlStateManager.rotate(90F, 0.0F, 0.0F, 1.0F)
                    GlStateManager.rotate(count * 90 / 6f, 0.0F, 0.0F, 1.0F)
                    GlStateManager.translate(0.0F, -64F, 0.0F)
                    if (count in selectedSlots)
                        GlStateManager.translate(0.0F, -5F, 0.0F)
                    GlStateManager.pushMatrix()
                    GlStateManager.scale(0.5, 0.5, 0.5)
                    if (allSlots[count])
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F)
                    else
                        GlStateManager.color(0F, 0F, 0F, 0.5F)
                    mc.ingameGUI.drawTexturedModalRect(0, 0, 0, 0, 16, 54)
                    if (allSlots[count]) {
                        if (percentage != 1F)
                            COLORS_FROM_SLOT[count].glColor()
                        else
                            COLORS_FROM_SLOT[count].pulseColor().glColor()
                        mc.ingameGUI.drawTexturedModalRect(4, 6, 16, 0, 24, height)
                    }
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F)
                    GlStateManager.popMatrix()
                    GlStateManager.translate(0.0F, -10F, 0.0F)
                    if (allSlots[count]) {
                        GlStateManager.rotate(180F, 0F, 0F, 1F)
                        GlStateManager.translate(-8F, -8F, 0.0F)
                        GlStateManager.scale(0.5F, 0.5F, 0.5F)
                        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
                        GlStateManager.enableRescaleNormal()
                        RenderHelper.enableGUIStandardItemLighting()
                        mc.renderItem.renderItemIntoGUI(stack, 0, 0)
                        RenderHelper.disableStandardItemLighting()
                        GlStateManager.disableRescaleNormal()
                        mc.renderEngine.bindTexture(HUD)
                    }
                    GlStateManager.popMatrix()
                }
                GlStateManager.popMatrix()
            }
        }

        fun setSlot(itemStackIn: ItemStack, newSlot: Int) {
            ItemNBTHelper.setInt(itemStackIn, TAG_SLOT, (newSlot + MAX_SLOT) % MAX_SLOT)
        }

        fun getSlot(itemStackIn: ItemStack) = (ItemNBTHelper.getInt(itemStackIn, TAG_SLOT, 0) + MAX_SLOT) % MAX_SLOT

        fun Color.pulseColor(variance: Int = 24, pulseSpeed: Float = 0.2f): Color {
            val add = (MathHelper.sin(ClientTickHandler.ticksInGame * pulseSpeed) * variance).toInt()
            val newColor = Color(
                    Math.max(Math.min(red + add, 255), 0),
                    Math.max(Math.min(green + add, 255), 0),
                    Math.max(Math.min(blue + add, 255), 0))
            return newColor
        }

        private val COLORS_FROM_SLOT = arrayOf(
                Color(0x73CECE), // Amulet
                Color(0x8B9E9B), // Ring 1
                Color(0x8B9E9B), // Ring 2
                Color(0x6B3F2E), // Belt
                Color(0xD36363), // Head
                Color(0x404454), // Body
                Color(0x75AA77)  // Charm
        )
    }

    init {
        addPropertyOverride(ResourceLocation(LibMisc.MOD_ID, "evocation")) { itemStack, world, entityLivingBase ->
            ItemNBTHelper.getInt(itemStack, TAG_SLOT, 0).toFloat()
        }
        setMaxStackSize(1)
    }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { itemStack, i ->
            if (i == 1) {
                COLORS_FROM_SLOT[getSlot(itemStack) % COLORS_FROM_SLOT.size].pulseColor().rgb
            } else 0xFFFFFF
        }

    override fun onItemRightClick(itemStackIn: ItemStack, worldIn: World, playerIn: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val slot = getSlot(itemStackIn)
        val baubles = BaublesApi.getBaublesHandler(playerIn)

        if (playerIn.isSneaking) {
            val newSlot = (slot + 1) % MAX_SLOT
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.PLAYERS, 0.6F, (1.0F + (playerIn.worldObj.rand.nextFloat() - playerIn.worldObj.rand.nextFloat()) * 0.2F) * 0.7F)
            if (!worldIn.isRemote)
                setSlot(itemStackIn, newSlot)

            val stack = baubles.getStackInSlot(newSlot)
            if (stack != null && stack.item is ISpellProvider) {
                val spell = (stack.item as ISpellProvider).getSpell(stack, newSlot)
                if (spell != null) {
                    Quaeritum.proxy.setRemainingItemDisplay(playerIn, spell.getIconStack(stack, newSlot), spell.getSpellName(stack, newSlot))
                    return ActionResult(EnumActionResult.SUCCESS, itemStackIn)
                }
            }
            Quaeritum.proxy.setRemainingItemDisplay(playerIn, itemStackIn, TooltipHelper.local("misc.${LibMisc.MOD_ID}.noSpell"))

            return ActionResult(EnumActionResult.SUCCESS, itemStackIn)
        } else {
            val stack = baubles.getStackInSlot(slot)
            if (stack != null && stack.item is ISpellProvider) {
                val spell = (stack.item as ISpellProvider).getSpell(stack, slot)
                if (spell != null) {
                    if (ISpellProvider.getCooldown(stack) == 0) {
                        val success = spell.onCast(playerIn, stack, slot)
                        if (success) {
                            val cooldown = spell.getCooldown(playerIn, stack, slot)
                            if (cooldown > 0) ISpellProvider.setCooldown(stack, cooldown, true)
                            return ActionResult(EnumActionResult.SUCCESS, itemStackIn)
                        }
                    }
                }
            }
        }

        return ActionResult(EnumActionResult.PASS, itemStackIn)
    }

    override fun onItemUse(stack: ItemStack, playerIn: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        return onItemRightClick(stack, worldIn, playerIn, hand).type
    }
}
