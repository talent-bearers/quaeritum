package eladkay.quaeritum.client.core

import eladkay.quaeritum.client.fx.FXMagicLine
import eladkay.quaeritum.client.fx.FXWisp
import eladkay.quaeritum.client.render.entity.RenderChaosborn
import eladkay.quaeritum.client.render.tesr.RitualHandlerSpecialRenderers
import eladkay.quaeritum.common.Quaeritum
import eladkay.quaeritum.common.block.tile.TileEntityBlueprint
import eladkay.quaeritum.common.block.tile.TileEntityFoundationStone
import eladkay.quaeritum.common.core.CommonProxy
import eladkay.quaeritum.common.entity.EntityChaosborn
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.Particle
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.client.registry.IRenderFactory
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

class ClientProxy : CommonProxy() {
    override fun pre(e: FMLPreInitializationEvent) {
        super.pre(e)
        //GuideAPI.setModel(ModBook.book, ModelResourceLocation(LibMisc.MOD_ID + ":" + LibNames.BOOK), "inventory")
    }

    override fun init(e: FMLInitializationEvent) {
        super.init(e)

        MinecraftForge.EVENT_BUS.register(ClientTickHandler())

        RenderingRegistry.registerEntityRenderingHandler<EntityChaosborn>(EntityChaosborn::class.java, IRenderFactory<EntityChaosborn> { RenderChaosborn(it) })
        Minecraft.getMinecraft().textureMapBlocks.registerSprite(ResourceLocation("quaeritum:entity/magicParticle"))
        if (Quaeritum.isDevEnv) {
            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBlueprint::class.java, RitualHandlerSpecialRenderers.BlueprintSpecialRenderer())
            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFoundationStone::class.java, RitualHandlerSpecialRenderers.FoundationStoneSpecialRenderer())
        }
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

    //This doesn't work
    @Deprecated("")
    override fun wispFX(world: World, x: Double, y: Double, z: Double, r: Float, g: Float, b: Float, size: Float, motionx: Float, motiony: Float, motionz: Float, maxAgeMul: Float) {
        if (!doParticle(world))
            return
        //todo params 9, 10
        val wisp = FXWisp(world, x, y, z, size, r, g, b, true, true, maxAgeMul)
        wisp.setSpeed(motionx, motiony, motionz)
        Minecraft.getMinecraft().effectRenderer.addEffect(wisp)
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


}
