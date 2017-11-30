package eladkay.quaeritum.client.core

import com.teamwizardry.librarianlib.core.client.ClientTickHandler
import com.teamwizardry.librarianlib.core.client.RenderHookHandler.registerItemHook
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import com.teamwizardry.librarianlib.features.utilities.client.GlUtils
import com.teamwizardry.librarianlib.features.utilities.client.GlUtils.useLightmap
import com.teamwizardry.librarianlib.features.utilities.client.GlUtils.withLighting
import eladkay.quaeritum.api.spell.render.RenderUtil
import eladkay.quaeritum.api.util.RandUtil
import eladkay.quaeritum.client.fx.FXMagicLine
import eladkay.quaeritum.client.render.*
import eladkay.quaeritum.client.render.entity.LayerEmbodiment
import eladkay.quaeritum.client.render.entity.LayerSight
import eladkay.quaeritum.client.render.entity.RenderChaosborn
import eladkay.quaeritum.client.render.entity.RenderFalling
import eladkay.quaeritum.common.block.ModBlocks
import eladkay.quaeritum.common.block.machine.BlockFluidHolder
import eladkay.quaeritum.common.block.machine.BlockFluidJet
import eladkay.quaeritum.common.core.CommonProxy
import eladkay.quaeritum.common.entity.EntityChaosborn
import eladkay.quaeritum.common.entity.EntityDroppingBlock
import eladkay.quaeritum.common.entity.ModEntities
import eladkay.quaeritum.common.item.ItemEvoker
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.Particle
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.client.registry.ClientRegistry
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

        ClientRegistry.bindTileEntitySpecialRenderer(BlockFluidHolder.TileFluidColumn::class.java, RenderHolder)
        ClientRegistry.bindTileEntitySpecialRenderer(BlockFluidJet.TileJet::class.java, RenderJet)

        RenderingRegistry.registerEntityRenderingHandler(EntityChaosborn::class.java, { RenderChaosborn(it) })
        RenderingRegistry.registerEntityRenderingHandler(EntityDroppingBlock::class.java, { RenderFalling(it) })

        registerItemHook { stack, _ ->
            if (ItemEvoker.hasEvocation(stack)) {
                withLighting(false) {
                    useLightmap(0xf000f0) {
                        GlStateManager.depthMask(false)

                        val elements = ItemEvoker.getEvocationFromStack(stack)

                        val cX = 0
                        val cY = 0
                        var scale = 35

                        var startingAngle = Math.PI / 2
                        val angleSep = 2 * Math.PI / (elements.size + 1)

                        GlStateManager.pushMatrix()
                        GlStateManager.translate(0.5f, 0.785f, 0.35f)
                        if (stack.item !is ItemEvoker) {
                            GlStateManager.translate(0f, -0.125f, 0.15f)
                            GlStateManager.scale(2f, 2f, 2f)
                            GlStateManager.rotate(-45f, 0f, 0f, 1f)
                            scale = 70
                            startingAngle = (ClientTickHandler.partialTicks + ClientTickHandler.ticks) * Math.PI / 120
                        }

                        GlStateManager.disableCull()
                        GlStateManager.scale(0.0025f, 0.0025f, 0.0025f)
                        GlStateManager.rotate(90f, 1f, 0f, 0f)
                        if (stack.item !is ItemEvoker)
                            GlStateManager.rotate(90f, 0f, 0f, 1f)
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
                            RenderSymbol.renderSymbol(x.toFloat(), y.toFloat(), element)
                        }
                        GlStateManager.popMatrix()
                        GlStateManager.enableCull()

                        GlStateManager.depthMask(true)
                    }
                }
            }
            if (stack.item == ModBlocks.fluidHolder.itemForm && stack.hasTagCompound() && ItemNBTHelper.verifyExistence(stack, "fluid")) {
                val fluid = FluidStack.loadFluidStackFromNBT(ItemNBTHelper.getCompound(stack, "fluid")!!)
                if (fluid != null) {
                    GlStateManager.pushMatrix()
                    GlStateManager.translate(0.15, 0.02, 0.15)
                    GlStateManager.disableLighting()
                    GlStateManager.enableBlend()
                    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
                    val x1 = 0.05
                    val y1 = 0.00
                    val z1 = 0.05
                    val x2 = 0.65
                    val y2 = (fluid.amount / 4000.0 + 0.15) * 0.7825
                    val z2 = 0.65

                    val v = fluid.fluid.getLuminosity(fluid) shl 4
                    GlUtils.useLightmap(OpenGlHelper.lastBrightnessX, Math.max(v.toFloat(), OpenGlHelper.lastBrightnessY)) {
                        ClientUtil.renderFluidCuboid(fluid.copy(), x1, y1, z1, x2, y2, z2)
                    }
                    GlStateManager.enableLighting()
                    GlStateManager.disableBlend()
                    GlStateManager.popMatrix()
                }
            }
        }
    }



    override fun init(e: FMLInitializationEvent) {
        super.init(e)

        val skinMap = Minecraft.getMinecraft().renderManager.skinMap

        var render = skinMap["default"]
        render?.addLayer(LayerSight(render.mainModel.bipedHead))
        render?.addLayer(LayerEmbodiment(render))

        render = skinMap["slim"]
        render?.addLayer(LayerSight(render.mainModel.bipedHead))
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
