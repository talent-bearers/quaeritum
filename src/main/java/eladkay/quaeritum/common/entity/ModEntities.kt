package eladkay.quaeritum.common.entity

import eladkay.quaeritum.client.render.entity.RenderFirebolt
import eladkay.quaeritum.common.Quaeritum
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.registry.EntityRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object ModEntities {
    private var id: Int = 0

    fun init() {
        registerModEntityWithEgg(EntityChaosborn::class.java, LibNames.CHAOSBORN, 15451, 45615)
        registerModEntity(EntityCircleOfTheFinalMoment::class.java, LibNames.CIRCLE)
        registerModEntity(EntityFirebolt::class.java, "firebolt", 3, true)
        registerModEntity(EntityFrostshock::class.java, "frostshock", 3, true)
        registerModEntity(EntityDroppingBlock::class.java, "dropping", 3, true)
        registerModEntity(EntityDrill::class.java, "drill", 3, true)
        registerModEntity(EntityDeagAnam::class.java, "deaganam", 3, true)
    }

    fun registerModEntityWithEgg(parEntityClass: Class<out Entity>, parEntityName: String, parEggColor: Int, parEggSpotsColor: Int, updateFrequency: Int = 3, sendsVelocity: Boolean = false) {
        EntityRegistry.registerModEntity(ResourceLocation("quaeritum:$parEntityName"), parEntityClass, parEntityName, ++id,
                Quaeritum.instance, 80, updateFrequency, sendsVelocity)
        EntityRegistry.registerEgg(ResourceLocation("quaeritum:$parEntityName"), parEggColor, parEggSpotsColor)
    }

    fun registerModEntity(parEntityClass: Class<out Entity>, parEntityName: String, updateFrequency: Int = 3, sendsVelocity: Boolean = false) {
        EntityRegistry.registerModEntity(ResourceLocation("quaeritum:$parEntityName"), parEntityClass, parEntityName, ++id,
                Quaeritum.instance, 80, updateFrequency, sendsVelocity)
    }

    @SideOnly(Side.CLIENT)
    fun initClient() {
        RenderingRegistry.registerEntityRenderingHandler(EntityFirebolt::class.java) { manager -> RenderFirebolt(manager) }
    }
}
