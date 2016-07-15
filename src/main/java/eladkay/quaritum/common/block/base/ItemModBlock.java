package eladkay.quaritum.common.block.base;


import eladkay.quaritum.api.lib.LibMisc;
import eladkay.quaritum.client.core.ModelHandler;
import eladkay.quaritum.common.Quartium;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemModBlock extends ItemBlock implements ModelHandler.IVariantHolder, ModelHandler.IColorProvider {

    private ModelHandler.IModBlock modBlock;

    public ItemModBlock(Block block) {
        super(block);
        this.modBlock = (ModelHandler.IModBlock) block;
        setRegistryName(new ResourceLocation(LibMisc.MOD_ID, modBlock.getBareName()));
        if (this.getVariants().length > 1) {
            this.setHasSubtypes(true);
        }
        /*try {
            ModelHandler.variantCache.add(this);
        } catch (Throwable ignored) {}*/
        Quartium.proxy.addToVariantCache(this);

    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public ItemBlock setUnlocalizedName(String unlocalizedName) {
        GameRegistry.register(this/*,new ResourceLocation(LibMisc.MOD_ID, unlocalizedName)*/);
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

    public IItemColor getColor() {
        return modBlock instanceof ModelHandler.IColorProvider ? ((ModelHandler.IColorProvider) modBlock).getColor() : null;
    }

    @Override

    public ItemMeshDefinition getCustomMeshDefinition() {
        return this.modBlock.getCustomMeshDefinition();
    }

    @Nonnull
    @Override
    public String[] getVariants() {
        return this.modBlock.getVariants();
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return this.modBlock.getBlockRarity(stack);
    }
}

