package eladkay.quaeritum.common.compat.mt

import minetweaker.MineTweakerAPI
import minetweaker.api.item.IItemStack
import minetweaker.api.minecraft.MineTweakerMC
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.Loader

object CraftTweaker {
    fun init() {
        if (!Loader.isModLoaded("MineTweaker3")) return
        MineTweakerAPI.registerClass(DiagramMT::class.java)
    }

    fun getStacks(stacks: Array<IItemStack>): Array<ItemStack> {
        return Array(stacks.size) {
            MineTweakerMC.getItemStack(stacks[it])
        }
    }
}
