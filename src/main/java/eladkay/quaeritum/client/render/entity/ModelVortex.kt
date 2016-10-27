package eladkay.quaeritum.client.render.entity

import net.minecraft.client.model.ModelBase
import net.minecraft.client.model.ModelRenderer
import net.minecraft.entity.Entity

class ModelVortex : ModelBase() {
    internal var body: ModelRenderer
    internal var shape2: ModelRenderer
    internal var shape1: ModelRenderer
    internal var shape3: ModelRenderer

    init {
        textureWidth = 64
        textureHeight = 32
        body = ModelRenderer(this, 33, 0)
        body.addBox(-3f, -3f, -3f, 6, 6, 6)
        body.setRotationPoint(0f, 14f, 0f)
        body.setTextureSize(64, 32)
        body.mirror = true
        setRotation(body, 0f, 0f, 0f)
        shape2 = ModelRenderer(this, 0, 0)
        shape2.addBox(-4f, -4f, -4f, 8, 8, 8)
        shape2.setRotationPoint(0f, 14f, 0f)
        shape2.setTextureSize(64, 32)
        shape2.mirror = true
        setRotation(shape2, Math.PI.toFloat() / 4f, Math.PI.toFloat() / 4f, 0f)
        shape1 = ModelRenderer(this, 0, 0)
        shape1.addBox(-4f, -4f, -4f, 8, 8, 8)
        shape1.setRotationPoint(0f, 14f, 0f)
        shape1.setTextureSize(64, 32)
        shape1.mirror = true
        setRotation(shape1, 0f, Math.PI.toFloat() / 4f, Math.PI.toFloat() / 4f)
        shape3 = ModelRenderer(this, 0, 0)
        shape3.addBox(-4f, -4f, -4f, 8, 8, 8)
        shape3.setRotationPoint(0f, 14f, 0f)
        shape3.setTextureSize(64, 32)
        shape3.mirror = true
        setRotation(shape3, Math.PI.toFloat() / 4f, 0f, Math.PI.toFloat() / 4f)
    }

    override fun render(entity: Entity?, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
        super.render(entity, f, f1, f2, f3, f4, f5)
        setRotationAngles(f, f1, f2, f3, f4, f5, entity)
        body.render(f5)
        shape2.render(f5)
        shape1.render(f5)
        shape3.render(f5)
    }

    private fun setRotation(model: ModelRenderer, x: Float, y: Float, z: Float) {
        model.rotateAngleX = x
        model.rotateAngleY = y
        model.rotateAngleZ = z
    }

    override fun setRotationAngles(f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float, entity: Entity?) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity)
        val world = entity!!.worldObj ?: return

        val ratio = 0.1f
        val rot = (entity.worldObj.worldTime + f) * ratio
        shape1.rotateAngleX = rot
        shape2.rotateAngleZ = rot
        shape3.rotateAngleY = rot
    }
}
