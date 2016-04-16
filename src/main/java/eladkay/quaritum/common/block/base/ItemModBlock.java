package eladkay.quaritum.common.block.base;


import eladkay.quaritum.client.core.ModelHandler;
import eladkay.quaritum.common.lib.LibMisc;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

public class ItemModBlock extends ItemBlock implements ModelHandler.IVariantHolder {

    private ModelHandler.IModBlock modBlock;

    public ItemModBlock(Block block) {
        super(block);
        this.modBlock = (ModelHandler.IModBlock) block;
        if (this.getVariants().length > 1) {
            this.setHasSubtypes(true);
        }
        ModelHandler.variantCache.add(this);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public ItemBlock setUnlocalizedName(String unlocalizedName) {
        GameRegistry.register(this, new ResourceLocation(LibMisc.MOD_ID, unlocalizedName));
        return super.setUnlocalizedName(unlocalizedName);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int dmg = stack.getItemDamage();
        String[] variants = this.getVariants();
        String name;
        if (dmg >= variants.length) {
            name = this.modBlock.getBareName();
        } else {
            name = variants[dmg];
        }

        return "tile." + LibMisc.MOD_ID + ":" + name;
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        String[] variants = this.getVariants();
        if (variants.length == 0)
            subItems.add(new ItemStack(itemIn));
        for (int i = 0; i < variants.length; i++) {
            subItems.add(new ItemStack(itemIn, 1, i));
        }
    }

    @Override
    public ItemMeshDefinition getCustomMeshDefinition() {
        return this.modBlock.getCustomMeshDefinition();
    }

    @Override
    public String[] getVariants() {
        return this.modBlock.getVariants();
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return this.modBlock.getBlockRarity(stack);
    }
}

