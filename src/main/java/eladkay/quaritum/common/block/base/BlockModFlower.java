package eladkay.quaritum.common.block.base;

import eladkay.quaritum.api.animus.IFlower;
import eladkay.quaritum.client.core.ModelHandler;
import eladkay.quaritum.common.Quaritum;
import eladkay.quaritum.common.core.CreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author WireSegal
 *         Created at 4:18 PM on 4/30/16.
 */
public abstract class BlockModFlower extends BlockBush implements IFlower, ModelHandler.IModBlock {

    private String[] variants;
    private String bareName;

    public BlockModFlower(String name, Material materialIn, String... variants) {
        super(materialIn);
        this.variants = variants;
        if (variants.length == 0) {
            this.variants = new String[] {name};
        }
        this.bareName = name;
        this.setUnlocalizedName(name);

        if (shouldRegisterInCreative())
            CreativeTab.set(this);
    }

    public boolean shouldRegisterInCreative() {
        return true;
    }

    public boolean shouldHaveItemForm() {
        return true;
    }

    @Override
    public Block setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        setRegistryName(name);
        GameRegistry.register(this);
        if (shouldHaveItemForm())
            GameRegistry.register(new ItemModBlock(this)/*, new ResourceLocation(LibMisc.MOD_ID, name)*/);
        else
            Quaritum.proxy.addToVariantCache(this);
        //ModelHandler.variantCache.add(this);

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
}
