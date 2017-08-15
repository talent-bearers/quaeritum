package eladkay.quaeritum.client.core

import com.teamwizardry.librarianlib.core.client.RenderHookHandler.registerItemHook
import com.teamwizardry.librarianlib.features.utilities.client.GlUtils.useLightmap
import com.teamwizardry.librarianlib.features.utilities.client.GlUtils.withLighting
import eladkay.quaeritum.api.spell.render.RenderUtil
import eladkay.quaeritum.api.util.RandUtil
import eladkay.quaeritum.client.fx.FXMagicLine
import eladkay.quaeritum.client.render.RemainingItemsRenderHandler
import eladkay.quaeritum.client.render.RenderSymbol
import eladkay.quaeritum.client.render.RenderSymbol.renderSymbol
import eladkay.quaeritum.client.render.entity.LayerEmbodiment
import eladkay.quaeritum.client.render.entity.LayerSight
import eladkay.quaeritum.client.render.entity.RenderChaosborn
import eladkay.quaeritum.client.render.entity.RenderFalling
import eladkay.quaeritum.common.core.CommonProxy
import eladkay.quaeritum.common.entity.EntityChaosborn
import eladkay.quaeritum.common.entity.EntityDroppingBlock
import eladkay.quaeritum.common.entity.ModEntities
import eladkay.quaeritum.common.item.ItemEvoker
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.Particle
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.lwjgl.opengl.GL11
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

class ClientProxy : CommonProxy() {
    override fun pre(e: FMLPreInitializationEvent) {
        super.pre(e)
        RenderSymbol
        HudSymbolRenderer.INSTANCE
        RemainingItemsRenderHandler
        LightningRenderer.INSTANCE
        RiftRenderer(Vec3d(0.0, 20.0, 0.0), RandUtil.nextLong(0, 10000))
        ModEntities.initClient()

        RenderingRegistry.registerEntityRenderingHandler(EntityChaosborn::class.java, { RenderChaosborn(it) })
        RenderingRegistry.registerEntityRenderingHandler(EntityDroppingBlock::class.java, { RenderFalling(it) })

        registerItemHook { stack, _ ->
            if (stack.item is ItemEvoker) {
                withLighting(false) {
                    useLightmap(0xf000f0) {
                        GlStateManager.depthMask(false)

                        val elements = ItemEvoker.getEvocationFromStack(stack)

                        val cX = 0
                        val cY = 0
                        val scale = 35

                        val startingAngle = Math.PI / 2
                        val angleSep = 2 * Math.PI / (elements.size + 1)

                        GlStateManager.pushMatrix()
                        GlStateManager.translate(0.5f, 0.785f, 0.35f)
                        GlStateManager.scale(0.0025f, 0.0025f, 0.0025f)
                        GlStateManager.rotate(90f, 1f, 0f, 0f)
                        GlStateManager.color(1f, 1f, 1f, 1f)
                        GlStateManager.enableBlend()
                        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE)
                        GlStateManager.shadeModel(GL11.GL_SMOOTH)
                        GlStateManager.disableTexture2D()
                        val tess = Tessellator.getInstance()
                        val buffer = tess.buffer
                        buffer.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_COLOR)
                        RenderUtil.renderNGon(buffer,
                                cX + MathHelper.cos(startingAngle.toFloat()) * scale - 0.5,
                                cY + MathHelper.sin(startingAngle.toFloat()) * scale - 0.5,
                                1f, 1f, 1f, 7.5, 5.0, RenderUtil.SEGMENTS_CIRCLE)
                        tess.draw()
                        GlStateManager.shadeModel(GL11.GL_FLAT)
                        GlStateManager.enableTexture2D()
                        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA)

                        for (idx in elements.indices) {
                            val element = elements[idx]
                            val angle = startingAngle + (idx + 1) * angleSep
                            val x = cX + scale * MathHelper.cos(angle.toFloat()) - 7.5
                            val y = cY + scale * MathHelper.sin(angle.toFloat()) - 7.5
                            renderSymbol(x.toFloat(), y.toFloat(), element)
                        }
                        GlStateManager.popMatrix()

                        GlStateManager.depthMask(true)
                    }
                }
            }
        }
    }

    override fun init(e: FMLInitializationEvent) {
        super.init(e)

        val skinMap = Minecraft.getMinecraft().renderManager.skinMap

        var render = skinMap["default"]
        render?.addLayer(LayerSight)
        render?.addLayer(LayerEmbodiment(render))

        render = skinMap["slim"]
        render?.addLayer(LayerSight)
        render?.addLayer(LayerEmbodiment(render))

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
