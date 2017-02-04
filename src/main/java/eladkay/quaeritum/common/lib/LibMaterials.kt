package eladkay.quaeritum.common.lib

import eladkay.quaeritum.api.lib.LibMisc
import net.minecraft.item.Item
import net.minecraftforge.common.util.EnumHelper

object LibMaterials {
    val TEMPESTEEL: Item.ToolMaterial = EnumHelper.addToolMaterial("${LibMisc.MOD_ID}:mystic", 4, 0, 10f, 5f, 26)
}
