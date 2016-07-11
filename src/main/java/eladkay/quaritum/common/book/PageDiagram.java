package eladkay.quaritum.common.book;

import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.gui.GuiBase;
import amerifrance.guideapi.gui.GuiEntry;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.core.PositionedBlockHelper;
import eladkay.quaritum.common.item.ModItems;
import eladkay.quaritum.common.item.misc.ItemPicture;
import eladkay.quaritum.common.lib.LibLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PageDiagram implements IPage {
    private List<PositionedBlock> mb;
    private List<ItemStack> items;
    private int xt;
    private int yt;
    private Dimension dim;
    public PageDiagram(List<PositionedBlock> blocks, List<ItemStack> requiredItems) {
        mb = blocks;
        items = requiredItems;
        int x = PositionedBlockHelper.getDimensions(blocks).getX();
        int z = PositionedBlockHelper.getDimensions(blocks).getZ();
        dim = new Dimension(x, z);
        xt = 68;
        yt = 50;
    }
    public PageDiagram(List<PositionedBlock> blocks, List<ItemStack> requiredItems, Dimension dimIn) {
        mb = blocks;
        items = requiredItems;
        dim = dimIn;
        //xt = 68;
        xt = getXFromDim(dimIn);
        //yt = 50;
        yt = getYFromDim(dimIn);
    }
    public PageDiagram(List<PositionedBlock> blocks, List<ItemStack> requiredItems, int xta, int yta) {
        mb = blocks;
        items = requiredItems;
        int x = PositionedBlockHelper.getDimensions(blocks).getX();
        int z = PositionedBlockHelper.getDimensions(blocks).getZ();
        dim = new Dimension(x, z);
        xt = xta;
        yt = yta;
    }
    public PageDiagram(List<PositionedBlock> blocks, List<ItemStack> requiredItems, Dimension dimIn, int x, int yta) {
        mb = blocks;
        items = requiredItems;
        dim = dimIn;
        xt = x;
        yt = yta;

    }
    private static final double X_CONSTANT = 3.738344692 * Math.pow(10, -14);
    private static int getXFromDim(Dimension dimt) {
        //3.738344692·10^-14 x^2 - 9 x + 95
        return (int) ((X_CONSTANT * dimt.getZ() * dimt.getZ()) - (9 * dimt.getZ()) + 95);
    }
    private static final double Y_CONSTANT = 0.25;
    private static int getYFromDim(Dimension dimt) {
        //0.25 x^2 - 8x + 71.75
        return (int) ((Y_CONSTANT * dimt.getZ() * dimt.getZ()) - (8 * dimt.getZ()) + 71.75);
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

        Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(new ItemStack(ModItems.picture, 1, 1), x, y);
        Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(new ItemStack(ModBlocks.blueprint), x + 96, y);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();

        GlStateManager.pushMatrix();
        GlStateManager.translate(0F, 0F, 200F);
        if (mx >= x && mx < x + 16 && my >= y && my < y + 16) {
            List<String> mats = new ArrayList<>();
            mats.add(I18n.format("quaritum.itemsRequired"));
            for (ItemStack stack : items) {
                String size = "" + stack.stackSize;
                mats.add(" " + TextFormatting.AQUA + size + " " + TextFormatting.GRAY + stack.getDisplayName());
            }

            eladkay.quaritum.common.core.RenderHelper.renderTooltip(mx, my, mats);
        }
        if (mx >= x + 96 && mx < x + 16 + 96 && my >= y && my < y + 16) {
            List<String> mats = new ArrayList<>();
            mats.add(I18n.format("quaritum.diagramtooltip"));
            mats.add(I18n.format("quaritum.chalksnotconsumed"));
            mats.add("");
            if(mb.stream().map(PositionedBlockHelper::getStackFromChalk).collect(Collectors.toList()).size() > 3)
                mats.addAll(mb.stream().map(PositionedBlockHelper::getStackFromChalk).filter(stack -> !(stack.getItem() instanceof ItemPicture)).collect(Collectors.toList()).stream().map(stack -> TextFormatting.GRAY + stack.getDisplayName()).collect(Collectors.toList()));
            else mats.add(I18n.format("quaritum.nochalk"));

            eladkay.quaritum.common.core.RenderHelper.renderTooltip(mx, my, mats);
        }
        GlStateManager.popMatrix();
        int x0 = 0;
        int y0 = 0;
        int x1 = 0;
        for(PositionedBlock block : mb) {
            if(x1 == dim.getX()) {
                x0 = 0;
                y0 += 15;
                x1 = 0;
            }
            Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(PositionedBlockHelper.getStackFromChalk(block, true),
                        guiLeft + x0 + xt, guiTop + yt + y0);
            x0 += 15;
            x1++;
        }
    }

    @Override
    public void drawExtras(Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX, int mouseY, GuiBase guiBase, FontRenderer fontRendererObj) {

    }

    @Override
    public boolean canSee(Book book, CategoryAbstract category, EntryAbstract entry, EntityPlayer player, ItemStack bookStack, GuiEntry guiEntry) { return true; }

    @Override
    public void onLeftClicked(Book book, CategoryAbstract category, EntryAbstract entry, int mouseX, int mouseY, EntityPlayer player, GuiEntry guiEntry) { }

    @Override
    public void onRightClicked(Book book, CategoryAbstract category, EntryAbstract entry, int mouseX, int mouseY, EntityPlayer player, GuiEntry guiEntry) { }

    @Override
    public void onInit(Book book, CategoryAbstract category, EntryAbstract entry, EntityPlayer player, ItemStack bookStack, GuiEntry gui) { }

    public float getYSize() {
        return PositionedBlockHelper.getDimensions(mb).getY();
    }

    public float getZSize() {
        return PositionedBlockHelper.getDimensions(mb).getY();
    }

    public float getXSize() {
        return PositionedBlockHelper.getDimensions(mb).getY();
    }

}
