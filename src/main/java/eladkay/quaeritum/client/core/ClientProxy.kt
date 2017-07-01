package eladkay.quaeritum.client.core

import eladkay.quaeritum.client.fx.FXMagicLine
import eladkay.quaeritum.client.render.RemainingItemsRenderHandler
import eladkay.quaeritum.client.render.entity.LayerSight
import eladkay.quaeritum.client.render.entity.RenderChaosborn
import eladkay.quaeritum.common.core.CommonProxy
import eladkay.quaeritum.common.entity.EntityChaosborn
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.Particle
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

class ClientProxy : CommonProxy() {
    override fun pre(e: FMLPreInitializationEvent) {
        super.pre(e)
        RemainingItemsRenderHandler
    }

    override fun init(e: FMLInitializationEvent) {
        super.init(e)

        val skinMap = Minecraft.getMinecraft().renderManager.skinMap

        var render = skinMap["default"]
        render?.addLayer(LayerSight)

        render = skinMap["slim"]
        render?.addLayer(LayerSight)

        MinecraftForge.EVENT_BUS.register(ClientTickHandler())

        RenderingRegistry.registerEntityRenderingHandler<EntityChaosborn>(EntityChaosborn::class.java, { RenderChaosborn(it) })
        Minecraft.getMinecraft().textureMapBlocks.registerSprite(ResourceLocation("quaeritum:entity/magicParticle"))
    }


    override fun spawnParticleMagixFX(world: World, x: Double, y: Double, z: Double, vx: Double, vy: Double, vz: Double, r: Double, g: Double, b: Double) {
        if (!doParticle(world))
            return
        val particle = FXMagicLine(world, x, y, z, vx, vy, vz, r, g, b)
        Minecraft.getMinecraft().effectRenderer.addEffect(particle)
    }

    override fun spawnParticle(particle: Particle, world: World) {
        if (!doParticle(world))
            return
        Minecraft.getMinecraft().effectRenderer.addEffect(particle)
    }

    override fun spawnStafflikeParticles(world: World, x: Double, y: Double, z: Double) {
        spawnParticleMagixFX(world, x + 2 * (world.rand.nextFloat() - 0.5), y + 2.0 * (world.rand.nextFloat() - 0.5) + 1.0, z + 2.0 * (world.rand.nextFloat() - 0.5), x, y + 1.0, z, 0.0, 0.0, 0.0)
    }

    private fun doParticle(world: World): Boolean {
        if (!world.isRemote)
            return false

        /*if(!ConfigHandler.useVanillaParticleLimiter)
            return true;*/

        var chance = 1f
        if (Minecraft.getMinecraft().gameSettings.particleSetting == 1)
            chance = 0.6f
        else if (Minecraft.getMinecraft().gameSettings.particleSetting == 2)
            chance = 0.2f

        return chance == 1f || Math.random() < chance
    }

    override fun copyText(s: String) {
        val stringSelection = StringSelection(s)
        val board = Toolkit.getDefaultToolkit().systemClipboard
        board.setContents(stringSelection, null)
    }

    override fun setRemainingItemDisplay(player: EntityPlayer?, stack: ItemStack?, str: String?, count: Int) {
        RemainingItemsRenderHandler.set(stack, str, count)
    }
}
