package eladkay.quaritum.common.core;

import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.lib.LibMisc;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WireSegal
 *         Created at 5:22 PM on 4/16/16.
 */

public class CreativeTab extends CreativeTabs {

    public static CreativeTab INSTANCE = new CreativeTab();

    private static List<Item> items = new ArrayList<>();

    public static void set(Block block) {
        items.add(Item.getItemFromBlock(block));
        block.setCreativeTab(INSTANCE);
    }

    public static void set(Item item) {
        items.add(item);
        item.setCreativeTab(INSTANCE);
    }


    List<ItemStack> list;

    public CreativeTab() {
        super(LibMisc.MOD_ID);
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(ModBlocks.blueprint);
    }

    @Override
    public Item getTabIconItem() {
        return getIconItemStack().getItem();
    }

    @Override
    public void displayAllRelevantItems(List<ItemStack> l) {
        list = l;
        items.forEach(this::addItem);
    }

    private void addItem(Item item) {
        item.getSubItems(item, this, list);
    }

}