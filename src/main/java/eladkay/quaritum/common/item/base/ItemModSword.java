package eladkay.quaritum.common.item.base;

import eladkay.quaritum.api.lib.LibMisc;
import eladkay.quaritum.client.core.ModelHandler;
import eladkay.quaritum.client.core.TooltipHelper;
import eladkay.quaritum.common.Quartium;
import eladkay.quaritum.common.core.CreativeTab;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemModSword extends ItemSword implements ModelHandler.IVariantHolder {

    private String[] variants;
    private String bareName;

    public ItemModSword(String name, Item.ToolMaterial material, String... variants) {
        super(material);
        setUnlocalizedName(name);
        CreativeTab.set(this);
        if(variants.length > 1)
            setHasSubtypes(true);

        if(variants.length == 0)
            variants = new String[] { name };

        bareName = name;
        this.variants = variants;
        /*try {
            ModelHandler.variantCache.add(this);
        } catch(NoClassDefFoundError server) {}*/
        Quartium.proxy.addToVariantCache(this);
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

    @Nonnull
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

        return "item." + LibMisc.MOD_ID + ":" + name;
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        String[] variants = this.getVariants();

        for (int i = 0; i < variants.length; i++) {
            subItems.add(new ItemStack(itemIn, 1, i));
        }
    }
}
