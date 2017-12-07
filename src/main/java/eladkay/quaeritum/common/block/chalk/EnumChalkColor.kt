package eladkay.quaeritum.common.block.chalk

import eladkay.quaeritum.common.lib.LibNames
import eladkay.quaeritum.common.lib.capitalizeFirst
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
                    .map(String::capitalizeFirst)
                    .joinToString("")
        }

        fun of(meta: Int): EnumChalkColor? {
            if (meta < 0 || meta > values().size) return null
            return values()[meta]
        }
    }
}
