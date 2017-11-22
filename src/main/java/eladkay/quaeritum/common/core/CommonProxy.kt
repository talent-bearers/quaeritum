package eladkay.quaeritum.common.core

import com.teamwizardry.librarianlib.features.network.PacketHandler
import eladkay.quaeritum.api.animus.AnimusHelper
import eladkay.quaeritum.client.core.ClientEventHandler
import eladkay.quaeritum.client.gui.GUIHandler
import eladkay.quaeritum.common.Quaeritum
import eladkay.quaeritum.common.block.ModBlocks
import eladkay.quaeritum.common.crafting.ModRecipes
import eladkay.quaeritum.common.entity.ModEntities
import eladkay.quaeritum.common.item.ModItems
import eladkay.quaeritum.common.networking.RemainingItemRenderPacket
import eladkay.quaeritum.common.potions.*
import eladkay.quaeritum.common.rituals.ModDiagrams
import eladkay.quaeritum.common.rituals.ModWorks
import eladkay.quaeritum.common.spell.BasicSpells
import eladkay.quaeritum.common.spell.SpellEventHandler
import net.minecraft.client.particle.Particle
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLInterModComms
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.NetworkRegistry

/**
 * @author WireSegal
 * *         Created at 4:29 PM on 4/16/16.
 */
open class CommonProxy {
    open fun pre(e: FMLPreInitializationEvent) {
        NetworkRegistry.INSTANCE.registerGuiHandler(Quaeritum.instance, GUIHandler)
        SpellEventHandler
        ModBlocks
        ModTab
        ModItems
        PotionRooted; PotionSoulgaze; PotionIronskin; PotionPathwalker; PotionVampirism; PotionEmbodiment; PotionWrath
        ModPotionTypes
        BasicSpells
        SightHandler
        ModEntities.init()
        ModRecipes.init()
        ClientEventHandler().init()
        AnimusHelper.Network.EventHandler()
        ModDiagrams.init()
        ModWorks.init()
        FMLInterModComms.sendMessage("Waila", "register", "eladkay.quaeritum.common.compat.waila.Waila.onWailaCall")
    }

    open fun init(e: FMLInitializationEvent) {

    }

    open fun post(e: FMLPostInitializationEvent) {

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
