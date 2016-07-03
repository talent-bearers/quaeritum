package eladkay.quaritum.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ModelVortex extends ModelBase {
    ModelRenderer body;
    ModelRenderer shape2;
    ModelRenderer shape1;
    ModelRenderer shape3;

    public ModelVortex() {
        textureWidth = 64;
        textureHeight = 32;
        body = new ModelRenderer(this, 33, 0);
        body.addBox(-3F, -3F, -3F, 6, 6, 6);
        body.setRotationPoint(0F, 14F, 0F);
        body.setTextureSize(64, 32);
        body.mirror = true;
        setRotation(body, 0F, 0F, 0F);
        shape2 = new ModelRenderer(this, 0, 0);
        shape2.addBox(-4F, -4F, -4F, 8, 8, 8);
        shape2.setRotationPoint(0F, 14F, 0F);
        shape2.setTextureSize(64, 32);
        shape2.mirror = true;
        setRotation(shape2, ((float) Math.PI / 4F), ((float) Math.PI / 4F), 0F);
        shape1 = new ModelRenderer(this, 0, 0);
        shape1.addBox(-4F, -4F, -4F, 8, 8, 8);
        shape1.setRotationPoint(0F, 14F, 0F);
        shape1.setTextureSize(64, 32);
        shape1.mirror = true;
        setRotation(shape1, 0F, ((float) Math.PI / 4F), ((float) Math.PI / 4F));
        shape3 = new ModelRenderer(this, 0, 0);
        shape3.addBox(-4F, -4F, -4F, 8, 8, 8);
        shape3.setRotationPoint(0F, 14F, 0F);
        shape3.setTextureSize(64, 32);
        shape3.mirror = true;
        setRotation(shape3, ((float) Math.PI / 4F), 0F, ((float) Math.PI / 4F));
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        body.render(f5);
        shape2.render(f5);
        shape1.render(f5);
        shape3.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        World world = entity.worldObj;

        if (world == null) {
            return;
        }

        float ratio = 0.1f;
        float rot = (entity.worldObj.getWorldTime() + f) * ratio;
        shape1.rotateAngleX = rot;
        shape2.rotateAngleZ = rot;
        shape3.rotateAngleY = rot;
    }
}
