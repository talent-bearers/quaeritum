package eladkay.quaritum.common.block.chalk;

import eladkay.quaritum.common.block.flowers.BlockAnimusFlower;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.util.IStringSerializable;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum EnumChalkColor implements IStringSerializable {
    WHITE,
    ORANGE,
    MAGENTA,
    LIGHT_BLUE,
    YELLOW,
    LIME,
    PINK,
    GRAY,
    SILVER,
    CYAN,
    PURPLE,
    BLUE,
    BROWN,
    GREEN,
    RED,
    BLACK,
    TEMPEST;
    public static String[] vars = new String[EnumChalkColor.values().length];

    static {
        for (EnumChalkColor var : EnumChalkColor.values()) {
            vars[var.ordinal()] = LibNames.CHALK + String.join(",", Arrays.asList(var.getName().split("_")).stream().map(BlockAnimusFlower::capitalizeFirst).collect(Collectors.joining()));
            System.out.println(LibNames.CHALK + String.join(",", Arrays.asList(var.getName().split("_")).stream().map(BlockAnimusFlower::capitalizeFirst).collect(Collectors.joining())));
        }

    }


    public static EnumChalkColor of(int meta) {
        if (meta < 0 || meta > values().length) return null;
        return values()[meta];
    }

    @Override
    public String getName() {
        return toString().toLowerCase();
    }
}
