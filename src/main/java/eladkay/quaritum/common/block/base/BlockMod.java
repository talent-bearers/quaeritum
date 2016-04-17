package eladkay.quaritum.common.block.base;

import eladkay.quaritum.client.core.ModelHandler;
import eladkay.quaritum.common.core.CreativeTab;
import eladkay.quaritum.common.lib.LibMisc;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockMod extends Block implements ModelHandler.IModBlock {

    private String[] variants;
    private String bareName;

    public BlockMod(String name, Material materialIn, String... variants) {
        super(materialIn);
        this.variants = variants;
        if (variants.length == 0) {
            this.variants = new String[] {name};
        }
        this.bareName = name;
        this.variants = variants;
        this.setUnlocalizedName(name);

        if (shouldRegisterInCreative())
            CreativeTab.set(this);
    }

    public boolean shouldRegisterInCreative() {
        return true;
    }

    @Override
    public Block setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        setRegistryName(name);
        GameRegistry.register(this);
        GameRegistry.register(new ItemModBlock(this), new ResourceLocation(LibMisc.MOD_ID, name));
        return this;
    }

    @Override
    public String getBareName() {
        return bareName;
    }

    @Override
    public String[] getVariants() {
        return variants;
    }

    @Override
    public Class<Enum> getVariantEnum() {
        return null;
    }

    @Override
    public IProperty[] getIgnoredProperties() {
        return new IProperty[0];
    }

    @Override
    public ItemMeshDefinition getCustomMeshDefinition() {
        return null;
    }

    @Override
    public EnumRarity getBlockRarity(ItemStack stack) {
        return EnumRarity.COMMON;
    }
}