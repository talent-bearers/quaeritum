package eladkay.quaritum.common.block.chalk

import eladkay.quaritum.common.block.flowers.BlockAnimusFlower
import eladkay.quaritum.common.lib.LibNames
import net.minecraft.util.IStringSerializable
import java.util.*
import java.util.stream.Collectors

enum class EnumChalkColor : IStringSerializable {
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

    override fun getName(): String {
        return toString().toLowerCase()
    }

    companion object {
        var vars = arrayOfNulls<String>(EnumChalkColor.values().size)

        init {
            for (`var` in EnumChalkColor.values()) {
                vars[`var`.ordinal] = LibNames.CHALK + arrayOf(Arrays.asList<String>(*`var`.getName().split("_".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()).stream().map(Function<String, String> { BlockAnimusFlower.capitalizeFirst(it) }).collect(Collectors.joining())).joinToString(",")
                println(LibNames.CHALK + arrayOf(Arrays.asList<String>(*`var`.getName().split("_".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()).stream().map(Function<String, String> { BlockAnimusFlower.capitalizeFirst(it) }).collect(Collectors.joining())).joinToString(","))
            }

        }


        fun of(meta: Int): EnumChalkColor? {
            if (meta < 0 || meta > values().size) return null
            return values()[meta]
        }
    }
}
