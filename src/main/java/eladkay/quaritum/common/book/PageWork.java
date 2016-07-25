package eladkay.quaritum.common.book;

import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.gui.GuiBase;
import amerifrance.guideapi.gui.GuiEntry;
import com.google.common.collect.Maps;
import eladkay.quaritum.api.lib.LibNBT;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.api.util.ItemNBTHelper;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.core.PositionedBlockHelper;
import eladkay.quaritum.common.lib.LibLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PageWork implements IPage {
    private static ItemStack foundationStack = new ItemStack(ModBlocks.foundation);

    static {
        ItemNBTHelper.setBoolean(foundationStack, LibNBT.FLAT, true);
    }

    private List<PositionedBlock> mb;

    public PageWork(List<PositionedBlock> blocks) {
        mb = blocks;
    }

    private static double getXFromPos(BlockPos pos, GuiBase gui, int scale) {
        return gui.guiLeft + gui.xSize / 3 + (pos.getX()) * 32 / (3 * scale) + scale * 3 + 22;
    }

    private static double getYFromPos(BlockPos pos, GuiBase gui, int scale) {
        return gui.guiTop + gui.ySize / 3 + (pos.getZ()) * 32 / (3 * scale) - 34 + scale * 3 + 48;
    }

    @Override
    public void draw(Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mx, int my, GuiBase gui, FontRenderer fontRendererObj) {
        TextureManager render = Minecraft.getMinecraft().renderEngine;
        render.bindTexture(LibLocations.MULTIBLOCK);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableAlpha();
        GlStateManager.color(1F, 1F, 1F, 1F);
        gui.drawTexturedModalRect(gui.guiLeft + 24, gui.guiTop, 0, 0, Math.min(gui.width, 256), Math.min(256, gui.height));
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();

        final float maxX = 90, maxY = 60;
        GlStateManager.pushMatrix();
        GlStateManager.translate(gui.guiLeft + gui.width / 2, gui.guiTop + 90, gui.publicZLevel + 100F);

        float diag = (float) Math.sqrt(getXSize() * getXSize() + getZSize() * getZSize());
        float height = getYSize();
        float scaleX = maxX / diag;
        float scaleY = maxY / height;
        float scale = -Math.min(scaleY, scaleX);
        GlStateManager.scale(scale, scale, scale);

        GlStateManager.rotate(-20F, 1, 0, 0);

        GlStateManager.popMatrix();

        GlStateManager.enableRescaleNormal();
        RenderHelper.enableGUIStandardItemLighting();
        int x = guiLeft + 15 + 24;
        int y = guiTop + 25;

        Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(new ItemStack(ModBlocks.foundation), x + 104, y + 120);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();

        GlStateManager.pushMatrix();
        GlStateManager.translate(0F, 0F, 200F);
        if (mx >= x + 96 + 8 && mx < x + 16 + 96 + 8 && my >= y + 120 && my < y + 136) {
            List<String> mats = new ArrayList<>();
            mats.add(I18n.format("quaritum.blocksrequired"));
            if (mb.size() > 0) {
                List<String> list = new ArrayList<>();
                for (PositionedBlock block : mb) {
                    String name = "";
                    if (block.getState().getBlock() == ModBlocks.chalk || block.getState().getBlock() == ModBlocks.tempest)
                        name = PositionedBlockHelper.getStackFromChalk(block, false).getDisplayName().replace("Tempest", TextFormatting.GOLD + "Tempest");
                    else if (Item.getItemFromBlock(block.getState().getBlock()) != null)
                        name = Item.getItemFromBlock(block.getState().getBlock()).getItemStackDisplayName(new ItemStack(Item.getItemFromBlock(block.getState().getBlock()), 1, block.getState().getBlock().getMetaFromState(block.getState())));
                    list.add(name);
                }
                HashMap<String, Integer> map = Maps.newHashMap();
                for (String name : list)
                    map.putIfAbsent(name, Collections.frequency(list, name));

                mats.addAll(map.keySet().stream()
                        .map(name -> " " + TextFormatting.AQUA + map.get(name) + " " + TextFormatting.GRAY + name)
                        .collect(Collectors.toList()));
            } else mats.add(" " + I18n.format("quaritum.noblocks"));

            eladkay.quaritum.common.core.RenderHelper.renderTooltip(mx, my, mats);
        }
        GlStateManager.popMatrix();


        Vec3i dims = PositionedBlockHelper.getDimensions(mb);
        int shift = dims.getX() >= 4 || dims.getZ() >= 4 ? 2 : 1;
        double s = shift == 2 ? 0.5 : 1;

        GlStateManager.pushMatrix();
        GlStateManager.scale(s, s, s);
        for (PositionedBlock block : mb)
            renderItemIntoGUI(PositionedBlockHelper.getStackFromChalk(block, true),
                    (getXFromPos(block.getPos(), gui, shift)) * shift, (getYFromPos(block.getPos(), gui, shift)) * shift, s);
        GlStateManager.popMatrix();

    }


    @Override
    public void drawExtras(Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX, int mouseY, GuiBase guiBase, FontRenderer fontRendererObj) {

    }

    @Override
    public boolean canSee(Book book, CategoryAbstract category, EntryAbstract entry, EntityPlayer player, ItemStack bookStack, GuiEntry guiEntry) {
        return true;
    }

    @Override
    public void onLeftClicked(Book book, CategoryAbstract category, EntryAbstract entry, int mouseX, int mouseY, EntityPlayer player, GuiEntry guiEntry) {
    }

    @Override
    public void onRightClicked(Book book, CategoryAbstract category, EntryAbstract entry, int mouseX, int mouseY, EntityPlayer player, GuiEntry guiEntry) {
    }

    @Override
    public void onInit(Book book, CategoryAbstract category, EntryAbstract entry, EntityPlayer player, ItemStack bookStack, GuiEntry gui) {
    }

    public float getYSize() {
        return PositionedBlockHelper.getDimensions(mb).getY();
    }

    public float getZSize() {
        return PositionedBlockHelper.getDimensions(mb).getY();
    }

    public float getXSize() {
        return PositionedBlockHelper.getDimensions(mb).getY();
    }

    public void renderItemIntoGUI(ItemStack stack, double x, double y, double s) {
        this.renderItemModelIntoGUI(stack, x, y, Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, null, null), s);
    }


    protected void renderItemModelIntoGUI(ItemStack stack, double x, double y, IBakedModel bakedmodel, double s) {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        setupGuiTransform(x, y, bakedmodel.isGui3d());
        bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.NONE, false);
        GlStateManager.scale(s / 2 * 3, s / 2 * 3, s / 2 * 3);
        GlStateManager.rotate(90F, 1F, 0F, 0F);
        renderItem(stack, bakedmodel);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
    }

    public void renderItem(ItemStack stack, IBakedModel model) {
        if (stack != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);

            if (model.isBuiltInRenderer()) {
                int rgb = Minecraft.getMinecraft().getItemColors().getColorFromItemstack(stack, 1);
                GlStateManager.color(argb(rgb).getRed(), argb(rgb).getGreen(), argb(rgb).getBlue(), argb(rgb).getAlpha());
                GlStateManager.enableRescaleNormal();
                TileEntityItemStackRenderer.instance.renderByItem(stack);
            } else {
                renderModel(model, Minecraft.getMinecraft().getItemColors().getColorFromItemstack(stack, 1), stack);
                if (stack.hasEffect())
                    renderEffect(model);
            }

            GlStateManager.popMatrix();
        }
    }

    private void renderQuads(VertexBuffer renderer, List<BakedQuad> quads, int color, @Nullable ItemStack stack) {
        boolean flag = color == -1 && stack != null;
        int i = 0;

        for (int j = quads.size(); i < j; ++i) {
            BakedQuad bakedquad = quads.get(i);
            int k = color;

            if (flag && bakedquad.hasTintIndex()) {
                k = Minecraft.getMinecraft().getItemColors().getColorFromItemstack(stack, bakedquad.getTintIndex());

                if (EntityRenderer.anaglyphEnable) {
                    k = TextureUtil.anaglyphColor(k);
                }

                k = k | -16777216;
            }

            net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
        }
    }

    private void renderModel(IBakedModel model, int color, @Nullable ItemStack stack) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.ITEM);
        for (EnumFacing enumfacing : EnumFacing.values())
            renderQuads(vertexbuffer, model.getQuads(null, enumfacing, 0L), color, stack);
        renderQuads(vertexbuffer, model.getQuads(null, null, 0L), color, stack);
        tessellator.draw();
    }

    public static Color argb(int color) {
        float a = ((color >> 24) & 0xff) / 255f;
        float r = ((color >> 16) & 0xff) / 255f;
        float g = ((color >> 8) & 0xff) / 255f;
        float b = ((color) & 0xff) / 255f;
        return new Color(r, g, b, a);
    }

    private void setupGuiTransform(double xPosition, double yPosition, boolean isGui3d) {
        GlStateManager.translate((float) xPosition, (float) yPosition, 100.0F + Minecraft.getMinecraft().getRenderItem().zLevel);
        GlStateManager.translate(8.0F, 8.0F, 0.0F);
        GlStateManager.scale(1.0F, -1.0F, 1.0F);
        GlStateManager.scale(16.0F, 16.0F, 16.0F);

        if (isGui3d)
            GlStateManager.enableLighting();
        else
            GlStateManager.disableLighting();

    }

    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    private void renderEffect(IBakedModel model) {
        GlStateManager.depthMask(false);
        GlStateManager.depthFunc(514);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
        Minecraft.getMinecraft().getTextureManager().bindTexture(RES_ITEM_GLINT);
        GlStateManager.matrixMode(5890);
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
        GlStateManager.translate(f, 0.0F, 0.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
        Minecraft.getMinecraft().getRenderItem().renderModel(model, -8372020);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f1 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
        GlStateManager.translate(-f1, 0.0F, 0.0F);
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
        Minecraft.getMinecraft().getRenderItem().renderModel(model, -8372020);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableLighting();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }

}
