package eladkay.quaritum.common.book;

import amerifrance.guideapi.GuideMod;
import amerifrance.guideapi.api.GuideAPI;
import amerifrance.guideapi.api.IGuideItem;
import amerifrance.guideapi.api.IGuideLinked;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.util.TextHelper;
import com.google.common.base.Strings;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemModBook extends ItemMod implements IGuideItem {
    public ItemModBook() {
        super(LibNames.BOOK);
        setCreativeTab(CreativeTabs.MISC);
        setMaxStackSize(1);
    }

    public static int meta() {
        for (Book book : GuideAPI.BOOKS.getValues())
            if (book.equals(ModBook.book)) return GuideAPI.BOOKS.getValues().indexOf(book);
        return -1;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (getBook(stack) != null) {
            if (!stack.hasTagCompound())
                stack.setTagCompound(new NBTTagCompound());
            player.openGui(GuideMod.instance, meta(), world, (int) player.posX, (int) player.posY, (int) player.posZ);
        }

        return super.onItemRightClick(stack, world, player, hand);
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (getBook(stack) != null && world.getBlockState(pos).getBlock() instanceof IGuideLinked) {
            IGuideLinked guideLinked = (IGuideLinked) world.getBlockState(pos).getBlock();
            Book book = getBook(stack);
            ResourceLocation entryKey = guideLinked.getLinkedEntry(world, pos, player, stack);
            for (CategoryAbstract category : book.getCategoryList()) {
                if (category.entries.containsKey(entryKey)) {
                    GuideMod.proxy.openEntry(book, category, category.entries.get(entryKey), player, stack);
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        return EnumActionResult.PASS;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getBook(stack) != null ? getUnlocalizedName() + "." + String.valueOf(meta()) : super.getUnlocalizedName(stack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return getBook(stack) != null ? getBook(stack).getLocalizedDisplayName() : super.getItemStackDisplayName(stack);
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean advanced) {
        Book book = getBook(stack);

        if (book == null)
            list.add(TextHelper.localizeEffect("text.book.warning"));

        if (book != null) {
            if (!Strings.isNullOrEmpty(book.getAuthor()))
                list.add(TextHelper.localizeEffect(book.getAuthor()));

            if (Strings.isNullOrEmpty(book.getAuthor()) && advanced)
                list.add(book.getRegistryName().getResourceDomain());
        }
    }

    // IGuideItem

    @Override
    public Book getBook(ItemStack stack) {
        return ModBook.book;
    }
}
