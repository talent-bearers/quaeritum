package eladkay.quaritum.common.block.chalk

import eladkay.quaritum.common.block.flowers.BlockAnimusFlower
import eladkay.quaritum.common.lib.LibNames
import net.minecraft.util.IStringSerializable

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
        var vars = Array(EnumChalkColor.values().size) {
            val variant = EnumChalkColor.values()[it]
            LibNames.CHALK + variant.getName()
                    .split("_".toRegex())
                    .filter(String::isNotEmpty)
                    .map { BlockAnimusFlower.capitalizeFirst(it) }
                    .joinToString("")
        }


        fun of(meta: Int): EnumChalkColor? {
            if (meta < 0 || meta > values().size) return null
            return values()[meta]
        }
    }
}
