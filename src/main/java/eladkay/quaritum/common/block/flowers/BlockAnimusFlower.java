package eladkay.quaritum.common.block.flowers;

import eladkay.quaritum.common.block.base.BlockModFlower;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BlockAnimusFlower extends BlockModFlower {

    public static IProperty<Variants> FLOWER_TYPE = PropertyEnum.create("type", Variants.class);

    public BlockAnimusFlower() {
        super(LibNames.FLOWER, Material.plants, Variants.vars);
        setDefaultState(this.blockState.getBaseState().withProperty(FLOWER_TYPE, Variants.COMMON));
    }

    public static String capitalizeFirst(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(world.getBlockState(pos)));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FLOWER_TYPE, Variants.of(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        Variants type = state.getValue(FLOWER_TYPE);
        return type.ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FLOWER_TYPE);
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

    public enum Variants implements IStringSerializable {
        COMMON(0, 20, "common"), COMMON_ARCANE(1, 40, "commonArcane"), ARCANE(2, 60, "arcane");

        public static String[] vars = new String[Variants.values().length];

        static {
            for (Variants var : Variants.values()) {
                vars[var.ordinal()] = LibNames.FLOWER + String.join(",", Arrays.asList(var.getName().split("_")).stream().map(BlockAnimusFlower::capitalizeFirst).collect(Collectors.joining()));
                System.out.println(LibNames.FLOWER + String.join(",", Arrays.asList(var.getName().split("_")).stream().map(BlockAnimusFlower::capitalizeFirst).collect(Collectors.joining())));

            }

        }

        public int rarity;
        public int amount;
        public String name;

        Variants(int rarity, int amount, String name) {
            this.rarity = rarity;
            this.amount = amount;
            this.name = name;
        }

        public static Variants of(int meta) {
            if (meta < 0 || meta > values().length) return null;
            return values()[meta];
        }

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }
}
