package eladkay.quaeritum.common.core

import eladkay.quaeritum.api.animus.AnimusHelper
import eladkay.quaeritum.client.core.ClientEventHandler
import eladkay.quaeritum.common.block.ModBlocks
//import eladkay.quaeritum.common.book.ModBook
import eladkay.quaeritum.common.compat.mt.CraftTweaker
import eladkay.quaeritum.common.crafting.ModRecipes
import eladkay.quaeritum.common.entity.ModEntities
import eladkay.quaeritum.common.item.ModItems
import eladkay.quaeritum.common.networking.NetworkHelper
import eladkay.quaeritum.common.rituals.ModDiagrams
import eladkay.quaeritum.common.rituals.ModWorks
import net.minecraft.client.particle.Particle
import net.minecraft.world.World
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLInterModComms
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

/**
 * @author WireSegal
 * *         Created at 4:29 PM on 4/16/16.
 */
open class CommonProxy {
    open fun pre(e: FMLPreInitializationEvent) {
        ModBlocks
        ModItems
        //ModBook
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

    @Deprecated("")
    open fun wispFX(world: World, x: Double, y: Double, z: Double, r: Float, g: Float, b: Float, size: Float, motionx: Float, motiony: Float, motionz: Float, maxAgeMul: Float) {
        //NO-OP
    }

    @Deprecated("")
    @JvmOverloads fun wispFX(world: World, x: Double, y: Double, z: Double, r: Float, g: Float, b: Float, size: Float, gravity: Float = 0f, maxAgeMul: Float = 1f) {
        wispFX(world, x, y, z, r, g, b, size, 0f, -gravity, 0f, maxAgeMul)
    }

    @Deprecated("")
    fun wispFX(world: World, x: Double, y: Double, z: Double, r: Float, g: Float, b: Float, size: Float, motionx: Float, motiony: Float, motionz: Float) {
        wispFX(world, x, y, z, r, g, b, size, motionx, motiony, motionz, 1f)
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

}
