package eladkay.quaritum.common.block.flowers;

import eladkay.quaritum.common.block.base.BlockModFlower;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class BlockAnimusFlower extends BlockModFlower {

    public static IProperty<Variants> FLOWER_TYPE = PropertyEnum.create("type", Variants.class);

    public enum Variants implements IStringSerializable {
        COMMON(0, 20), COMMON_ARCANE(1, 20), ARCANE(2, 20);

        public int rarity, amount;

        Variants(int rarity, int amount) {
            this.rarity = rarity;
            this.amount = amount;
        }

        @Override
        public String getName() {
            return toString().toLowerCase();
        }

        public static String[] vars = new String[Variants.values().length];

        static {
            for (Variants var : Variants.values())
                vars[var.ordinal()] = LibNames.FLOWER + capitalizeFirst(var.getName());
        }

        public static Variants of(int meta) {
            if (meta < 0 || meta > values().length) return null;
            return values()[meta];
        }
    }

    public static String capitalizeFirst(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FLOWER_TYPE);
    }

    public BlockAnimusFlower() {
        super(LibNames.FLOWER, Material.plants, Variants.vars);
        setDefaultState(this.blockState.getBaseState().withProperty(FLOWER_TYPE, Variants.COMMON));
    }

    @Override
    public int getRarity(ItemStack stack) {
        Variants var = Variants.of(stack.getItemDamage());
        if (var == null) return 0;
        return var.rarity;
    }

    @Override
    public int getAnimusFromStack(ItemStack stack) {
        Variants var = Variants.of(stack.getItemDamage());
        if (var == null) return 0;
        return var.amount;
    }
}
