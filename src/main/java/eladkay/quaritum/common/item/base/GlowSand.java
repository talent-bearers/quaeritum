package eladkay.quaritum.common.item.base;

import com.google.common.collect.Lists;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GlowSand extends ItemMod {
    public GlowSand() {
        super(LibNames.GLOWSANDPLACER);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(!(entityIn instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) entityIn;
        List<Block> list = Lists.newArrayList();
        list.addAll(OreDictionary.getOres("glowSand").stream().filter(stack0 -> Block.getBlockFromItem(stack0.getItem()) != null).map(stack1 -> Block.getBlockFromItem(stack1.getItem())).collect(Collectors.toList()));
        if(list.size() > 0) {
            player.inventory.addItemStackToInventory(new ItemStack(list.get(new Random().nextInt(list.size()))));
            player.inventory.getStackInSlot(itemSlot).stackSize--;
            if(player.inventory.getStackInSlot(itemSlot).stackSize <= 0) player.inventory.clearMatchingItems(this, -1, -1, null);
        }

    }
}
