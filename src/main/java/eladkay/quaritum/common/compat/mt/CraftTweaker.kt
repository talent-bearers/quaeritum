package eladkay.quaritum.common.compat.mt

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
        val ret = arrayOfNulls<ItemStack>(stacks.size)
        for (i in stacks.indices)
            ret[i] = MineTweakerMC.getItemStack(stacks[i])
        return ret
    }
}
