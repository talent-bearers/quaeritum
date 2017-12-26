package eladkay.quaeritum.common.core

import eladkay.quaeritum.api.lib.LibMisc
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagLong
import net.minecraft.util.math.BlockPos
import net.minecraft.world.storage.WorldSavedData
import net.minecraftforge.common.util.Constants

import java.util.*

/**
 * @author WireSegal
 * Created at 8:30 PM on 12/25/17.
 */
class QuaeritumSaveData : WorldSavedData {

    val animusData = HashMap<UUID, NBTTagCompound>()
    val academies = mutableSetOf<BlockPos>()

    constructor(id: String) : super(id)
    constructor() : super(LibMisc.MOD_ID)

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        val animusCompound = NBTTagCompound()
        for ((key, value) in animusData.entries)
            animusCompound.setTag(key.toString(), value)
        compound.setTag("animus", animusCompound)

        val academyList = NBTTagList()
        for (academy in academies)
            academyList.appendTag(NBTTagLong(academy.toLong()))
        compound.setTag("academy", academyList)

        return compound
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        animusData.clear()
        val animusCompound = if (compound.hasKey("animus")) compound.getCompoundTag("animus") else compound
        for (key in animusCompound.keySet)
            animusData.put(UUID.fromString(key), animusCompound.getCompoundTag(key))

        academies.clear()
        if (compound.hasKey("academy"))
            compound.getTagList("academy", Constants.NBT.TAG_LONG)
                    .filterIsInstance<NBTTagLong>()
                    .mapTo(academies) { BlockPos.fromLong(it.long) }
    }
}
