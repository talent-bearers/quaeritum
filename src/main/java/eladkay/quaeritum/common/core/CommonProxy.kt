package eladkay.quaeritum.common.core

import com.teamwizardry.librarianlib.common.network.PacketHandler
import eladkay.quaeritum.api.animus.AnimusHelper
import eladkay.quaeritum.client.core.ClientEventHandler
import eladkay.quaeritum.common.block.ModBlocks
//import eladkay.quaeritum.common.book.ModBook
import eladkay.quaeritum.common.compat.mt.CraftTweaker
import eladkay.quaeritum.common.crafting.ModRecipes
import eladkay.quaeritum.common.entity.ModEntities
import eladkay.quaeritum.common.item.ModItems
import eladkay.quaeritum.common.networking.NetworkHelper
import eladkay.quaeritum.common.networking.RemainingItemRenderPacket
import eladkay.quaeritum.common.potions.PotionRooted
import eladkay.quaeritum.common.rituals.ModDiagrams
import eladkay.quaeritum.common.rituals.ModWorks
import net.minecraft.client.particle.Particle
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLInterModComms
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.relauncher.Side

/**
 * @author WireSegal
 * *         Created at 4:29 PM on 4/16/16.
 */
open class CommonProxy {
    open fun pre(e: FMLPreInitializationEvent) {
        ModBlocks
        ModTab
        ModItems
        PotionRooted
        ModEntities.init()
        ModRecipes.init()
        Events().init()
        ClientEventHandler().init()
        AnimusHelper.Network.EventHandler()
        NetworkHelper
        ModDiagrams.init()
        ModWorks.init()
        ChatHelper.PacketHandler
        FMLInterModComms.sendMessage("Waila", "register", "eladkay.quaeritum.common.compat.waila.Waila.onWailaCall")
    }

    open fun init(e: FMLInitializationEvent) {

    }

    fun post(e: FMLPostInitializationEvent) {
        CraftTweaker.init()
    }

    open fun spawnParticleMagixFX(world: World, x: Double, y: Double, z: Double, vx: Double, vy: Double, vz: Double, r: Double, g: Double, b: Double) {
        //NO-OP
    }

    open fun spawnStafflikeParticles(world: World, x: Double, y: Double, z: Double) {
        //NO-OP
    }

    open fun spawnParticle(particle: Particle, world: World) {
        //NO-OP
    }

    open fun copyText(s: String) {
        //NO-OP
    }

    open fun setRemainingItemDisplay(player: EntityPlayer?, stack: ItemStack?, str: String? = null, count: Int = 0) {
        if (player !is EntityPlayerMP) return
        PacketHandler.NETWORK.sendTo(RemainingItemRenderPacket(stack, str, count), player)
    }

}
