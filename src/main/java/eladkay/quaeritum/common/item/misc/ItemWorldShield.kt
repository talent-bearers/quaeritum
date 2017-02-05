package eladkay.quaeritum.common.item.misc

import com.teamwizardry.librarianlib.common.base.item.ItemMod
import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.BlockDispenser
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.EnumAction
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * @author WireSegal
 * Created at 12:15 PM on 2/5/17.
 */
class ItemWorldShield : ItemMod(LibNames.SHIELD) {
    init {
        maxStackSize = 1
        addPropertyOverride(ResourceLocation("blocking")) { stack, worldIn, entityIn ->
            if (entityIn != null && entityIn.isHandActive && entityIn.activeItemStack == stack) 1.0f else 0.0f
        }
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR)
        MinecraftForge.EVENT_BUS.register(this)
    }

    class ShieldDamage(entity: EntityLivingBase) : EntityDamageSource("${LibMisc.MOD_ID}.shield", entity) {
        init {
            setDamageBypassesArmor()
            setMagicDamage()
        }
    }

    override fun getItemUseAction(stack: ItemStack?) = EnumAction.BLOCK

    override fun getMaxItemUseDuration(stack: ItemStack) = 72000

    override fun onItemRightClick(itemStackIn: ItemStack, worldIn: World, playerIn: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        playerIn.activeHand = hand
        return ActionResult(EnumActionResult.SUCCESS, itemStackIn)
    }

    override fun getRarity(stack: ItemStack?) = EnumRarity.EPIC

    @SubscribeEvent
    fun onLivingAttacked(e: LivingAttackEvent) {
        val player = e.entityLiving
        val damage = e.source
        val enemyEntity = damage.entity
        val indirect = damage.sourceOfDamage
        if (player is EntityPlayer && enemyEntity is EntityLivingBase && indirect == enemyEntity && player.isHandActive) {
            val activeStack = player.activeItemStack
            val mainStack = player.heldItemMainhand
            if (mainStack == activeStack && activeStack != null && activeStack.item == this) {
                val lookVec = player.lookVec
                val targetVec = enemyEntity.positionVector.subtract(player.positionVector)
                val epsilon = lookVec.normalize().dotProduct(targetVec.normalize())
                if (epsilon > 0.75) {
                    e.isCanceled = true
                    if (!player.worldObj.isRemote) {
                        enemyEntity.attackEntityFrom(ShieldDamage(player), e.amount)
                        val xDif = enemyEntity.posX - player.posX
                        val zDif = enemyEntity.posZ - player.posZ
                        player.worldObj.playSound(player, enemyEntity.posX, enemyEntity.posY, enemyEntity.posZ, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.PLAYERS, 1f, 0.9f + 0.1f * Math.random().toFloat())
                        enemyEntity.heldItemMainhand?.damageItem(100, enemyEntity)
                        enemyEntity.heldItemOffhand?.damageItem(100, enemyEntity)
                        enemyEntity.knockBack(player, 1f, -xDif, -zDif)
                    }
                }
            }
        }
    }
}
