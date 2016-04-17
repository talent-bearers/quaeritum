package eladkay.quaritum.common.item.base;

import eladkay.quaritum.client.core.ModelHandler;
import eladkay.quaritum.client.core.TooltipHelper;
import eladkay.quaritum.common.core.CreativeTab;
import eladkay.quaritum.common.lib.LibMisc;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;


public class ItemMod extends Item implements ModelHandler.IVariantHolder {

    private String[] variants;
    private String bareName;

    public ItemMod(String name, String... variants) {
        setUnlocalizedName(name);
        CreativeTab.set(this);
        if(variants.length > 1)
            setHasSubtypes(true);

        if(variants.length == 0)
            variants = new String[] { name };

        bareName = name;
        this.variants = variants;
    }

    public static void tooltipIfShift(List<String> tooltip, Runnable r) {
        TooltipHelper.tooltipIfShift(tooltip, r);
    }

    public static void addToTooltip(List<String> tooltip, String s, Object... format) {
        TooltipHelper.addToTooltip(tooltip, s, format);
    }

    public static String local(String s) {
        return TooltipHelper.local(s);
    }

    @Override
    public ItemMeshDefinition getCustomMeshDefinition() {
        return null;
    }

    @Override
    public String[] getVariants() {
        return variants;
    }

    @Override
    public Item setUnlocalizedName(String unlocalizedName) {
        GameRegistry.register(this, new ResourceLocation(LibMisc.MOD_ID, unlocalizedName));
        return super.setUnlocalizedName(unlocalizedName);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int dmg = stack.getItemDamage();
        String[] variants = this.getVariants();
        String name;
        if (dmg >= variants.length) {
            name = this.bareName;
        } else {
            name = variants[dmg];
        }

        return "tile." + LibMisc.MOD_ID + ":" + name;
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        String[] variants = this.getVariants();

        for (int i = 0; i < variants.length; i++) {
            subItems.add(new ItemStack(itemIn, 1, i));
        }
    }
}
