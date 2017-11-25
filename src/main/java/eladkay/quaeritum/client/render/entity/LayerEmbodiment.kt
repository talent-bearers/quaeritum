package eladkay.quaeritum.client.render.entity

import com.teamwizardry.librarianlib.features.utilities.client.GlUtils.useLightmap
import com.teamwizardry.librarianlib.features.utilities.client.GlUtils.withLighting
import eladkay.quaeritum.api.spell.EnumSpellElement
import eladkay.quaeritum.client.render.RenderSymbol
import eladkay.quaeritum.common.potions.PotionEmbodiment
import eladkay.quaeritum.common.potions.PotionWrath
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.RenderLivingBase
import net.minecraft.client.renderer.entity.layers.LayerRenderer
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumHandSide

/**
 * @author WireSegal
 * Created at 8:27 PM on 8/14/17.
 */
class LayerEmbodiment(val renderer: RenderLivingBase<AbstractClientPlayer>) : LayerRenderer<AbstractClientPlayer> {
    override fun doRenderLayer(player: AbstractClientPlayer, limbSwing: Float, limbSwingAmount: Float, partialTicks: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float) {
        if (PotionWrath.hasEffect(player))
            renderSide(player, EnumHand.MAIN_HAND)
        if (PotionEmbodiment.hasEffect(player))
            renderSide(player, EnumHand.OFF_HAND)
    }

    fun renderSide(player: AbstractClientPlayer, hand: EnumHand) {
        val symbol = if (hand == EnumHand.MAIN_HAND) EnumSpellElement.FIRE else EnumSpellElement.WATER
        val side = if (hand == EnumHand.MAIN_HAND) player.primaryHand else player.primaryHand.opposite()
        val sign = if (side == EnumHandSide.RIGHT) -1.75f else -1f
        useLightmap(0xf000f0) {
            withLighting(false) {
                GlStateManager.pushMatrix()
                GlStateManager.enableBlend()
                GlStateManager.disableCull()
                translateToHand(player, side)
                GlStateManager.translate(0f, 0.25f, 0f)
                GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f)
                GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f)
                GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f)
                GlStateManager.scale(0.04f, 0.04f, 0.04f)
                GlStateManager.depthMask(false)
                RenderSymbol.renderSymbol(sign * 5f, -7.5f, symbol)
                GlStateManager.depthMask(true)
                GlStateManager.popMatrix()
            }
        }
    }

    fun translateToHand(player: AbstractClientPlayer, side: EnumHandSide) {
        if (player.isSneaking)
            GlStateManager.translate(0.0f, 0.2f, 0.0f)
        (renderer.mainModel as ModelBiped).postRenderArm(0.0625f, side)
    }

    override fun shouldCombineTextures() = false
}
