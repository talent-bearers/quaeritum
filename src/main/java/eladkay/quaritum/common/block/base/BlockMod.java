package eladkay.quaritum.common.block.base;

import eladkay.quaritum.client.core.ModelHandler;
import eladkay.quaritum.common.Quaritum;
import eladkay.quaritum.common.core.CreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockMod extends Block implements ModelHandler.IModBlock {

    @Nullable
    public ItemBlock itemForm;
    private String[] variants;
    private String bareName;

    public BlockMod(String name, Material materialIn, String... variants) {
        super(materialIn);
        this.variants = variants;
        if (variants != null && variants.length == 0) {
            this.variants = new String[]{name};
        }
        this.bareName = name;
        itemForm = createItemBlock();
        this.setUnlocalizedName(name);

        if (shouldRegisterInCreative())
            CreativeTab.set(this);
        constructBook();
    }

    public boolean shouldRegisterInCreative() {
        return true;
    }

    @Nullable
    protected ItemBlock createItemBlock() {
        return new ItemModBlock(this);
    }

    @Override
    public Block setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        setRegistryName(name);
        GameRegistry.register(this);
        if (itemForm != null)
            GameRegistry.register(itemForm);
        else Quaritum.proxy.addToVariantCache(this);
        /* try {
            ModelHandler.variantCache.add(this);
        } catch (Throwable ignored) {}*/
        return this;
    }

    @Nonnull
    @Override
    public String getBareName() {
        return bareName;
    }

    @Nonnull
    @Override
    public String[] getVariants() {
        return variants;
    }

    @Nullable
    @Override
    public Class<Enum> getVariantEnum() {
        return null;
    }

    @Nonnull
    @Override
    public IProperty[] getIgnoredProperties() {
        return new IProperty[0];
    }

    @Override

    public ItemMeshDefinition getCustomMeshDefinition() {
        return null;
    }

    @Nonnull
    @Override
    public EnumRarity getBlockRarity(ItemStack stack) {
        return EnumRarity.COMMON;
    }

    public void constructBook() {
    }
}
