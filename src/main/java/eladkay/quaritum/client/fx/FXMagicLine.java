package eladkay.quaritum.client.fx;

import eladkay.quaritum.common.lib.LibLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Random;

//copied with permission from elucent's roots
public class FXMagicLine extends Particle {

    //0 = none, 1 = elucent, 2 = wiresegal
    private static final int ROTATION_MODE = 0;
    public double colorR = 0;
    public double colorG = 0;
    public double colorB = 0;
    public int lifetime = 8;
    public ResourceLocation texture = LibLocations.MAGICLINEFX;
    Random random = new Random();
    double initialX, initialY, initialZ;
    long tt;
    float xRotate;
    float zRotate;
    private double rotationModifier = 0.2f;
    public FXMagicLine(World worldIn, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b) {
        super(worldIn, x, y, z, 0, 0, 0);
        initialX = x;
        initialY = y;
        initialZ = z;
        this.colorR = r;
        this.colorG = g;
        this.colorB = b;
        if (this.colorR > 1.0)
            this.colorR = this.colorR / 255.0;
        if (this.colorG > 1.0)
            this.colorG = this.colorG / 255.0;
        if (this.colorB > 1.0)
            this.colorB = this.colorB / 255.0;
        this.setRBGColorF(1, 1, 1);
        this.particleMaxAge = 8;
        this.particleGravity = 0.0f;
        this.motionX = (vx - x) / (double) this.particleMaxAge;
        this.motionY = (vy - y) / (double) this.particleMaxAge;
        this.motionZ = (vz - z) / (double) this.particleMaxAge;
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texture.toString());
        this.setParticleTexture(sprite);
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        tt++;
        float lifeCoeff = ((float) this.particleMaxAge - (float) this.particleAge) / (float) this.particleMaxAge;
        float alpha = 0.5f;
        this.particleRed = Math.min(1.0f, (float) colorR * (1.5f - lifeCoeff) + lifeCoeff);
        this.particleGreen = Math.min(1.0f, (float) colorG * (1.5f - lifeCoeff) + lifeCoeff);
        this.particleBlue = Math.min(1.0f, (float) colorB * (1.5f - lifeCoeff) + lifeCoeff);
        GlStateManager.enableBlend();
        this.particleAlpha = alpha;
        if (ROTATION_MODE == 2) {
            //wiresegal's approach
            //https://www.youtube.com/watch?v=V-tja0YnCM8
            xRotate = (float) Math.sin(tt * rotationModifier / 2) / 2F;
            zRotate = (float) Math.cos(tt * rotationModifier) / 2 / 2F;
            posX = xRotate + initialX;
            posZ = zRotate + initialZ;
            motionX = -xRotate;
            motionZ = -zRotate;
        } else if (ROTATION_MODE == 1) {
            //elucent's approach
            //https://www.youtube.com/watch?v=WUurkKbtRRI
            posX = initialX + Math.sin(Math.toRadians(tt));
            posY = initialY;
            posZ = initialZ + Math.cos(Math.toRadians(tt));
        }
    }

}
