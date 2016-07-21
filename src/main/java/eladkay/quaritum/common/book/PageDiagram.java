package eladkay.quaritum.common.book;

import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.gui.GuiBase;
import amerifrance.guideapi.gui.GuiEntry;
import eladkay.quaritum.api.lib.LibNBT;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.api.util.ItemNBTHelper;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PageDiagram implements IPage {
    private static ItemStack blueprintStack = new ItemStack(ModBlocks.blueprint);

    static {
        ItemNBTHelper.setBoolean(blueprintStack, LibNBT.FLAT, true);
    }

    private List<PositionedBlock> mb;
    private List<ItemStack> items;

    public PageDiagram(List<PositionedBlock> blocks, List<ItemStack> requiredItems) {
        mb = blocks;
        items = requiredItems;
    }

    private static int getXFromPos(BlockPos pos, GuiBase gui, int scale) {
        return gui.guiLeft + gui.xSize / 2 + pos.getX() * 16 / scale - 14 + scale * 6;
    }

    private static int getYFromPos(BlockPos pos, GuiBase gui, int scale) {
        return gui.guiTop + gui.ySize / 2 + pos.getZ() * 16 / scale - 34 + scale * 5;
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
            Map<String, Integer> map = new HashMap<>();
            mats.add(I18n.format("quaritum.itemsRequired"));
            for (ItemStack stack : items) {
                String name = stack.getDisplayName();
                if (!map.containsKey(name)) map.put(name, stack.stackSize);
                else map.put(name, stack.stackSize + map.get(name));
            }
            mats.addAll(map.keySet().stream()
                    .map(name -> " " + TextFormatting.AQUA + map.get(name) + " " + TextFormatting.GRAY + name)
                    .collect(Collectors.toList()));


            eladkay.quaritum.common.core.RenderHelper.renderTooltip(mx, my, mats);
        }
        if (mx >= x + 96 && mx < x + 16 + 96 && my >= y && my < y + 16) {
            List<String> mats = new ArrayList<>();
            mats.add(I18n.format("quaritum.chalksRequired"));
            if (mb.size() > 0) {
                List<String> list = new ArrayList<>();
                for (PositionedBlock block : mb) {
                    String name = PositionedBlockHelper.getStackFromChalk(block).getDisplayName();
                    if (!list.contains(name)) list.add(name);
                }
                mats.addAll(list.stream()
                        .map(name -> " " + TextFormatting.GRAY + name)
                        .collect(Collectors.toList()));
            }
            else mats.add(" " + I18n.format("quaritum.nochalk"));

            eladkay.quaritum.common.core.RenderHelper.renderTooltip(mx, my, mats);
        }
        GlStateManager.popMatrix();


        Vec3i dims = PositionedBlockHelper.getDimensions(mb);
        int shift = dims.getX() >= 4 || dims.getZ() >= 4 ? 2 : 1;
        double s = shift == 2 ? 0.5 : 1;

        GlStateManager.pushMatrix();
        GlStateManager.scale(s, s, s);
        Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(blueprintStack,
                (getXFromPos(BlockPos.ORIGIN, gui, shift)) * shift, (getYFromPos(BlockPos.ORIGIN, gui, shift)) * shift);
        for (PositionedBlock block : mb)
            Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(PositionedBlockHelper.getStackFromChalk(block, true),
                    (getXFromPos(block.getPos(), gui, shift)) * shift, (getYFromPos(block.getPos(), gui, shift)) * shift);
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

}
