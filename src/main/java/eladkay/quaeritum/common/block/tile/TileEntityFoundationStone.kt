package eladkay.quaeritum.common.block.tile

import com.teamwizardry.librarianlib.features.base.block.TileMod
import com.teamwizardry.librarianlib.features.base.block.TileModTickable
import eladkay.quaeritum.api.rituals.IWork
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.api.rituals.RitualRegistry
import eladkay.quaeritum.common.core.PositionedBlockHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound

class TileEntityFoundationStone : TileModTickable() {
    var stage = RitualStage.IDLE
    var ticksExisted: Long = 0
    var currentWork: IWork? = null

    public override fun tick() {
        if (!world.isRemote) {
            if (currentWork != null) {
                stage = RitualStage.IN_PROGRESS
                if (!currentWork!!.updateTick(world, pos, this, ticksExisted)) currentWork = null
                ticksExisted++
            }
            if (currentWork == null) {
                stage = RitualStage.IDLE
                ticksExisted = 0
            }
        }
    }

    private val bestRitual: IWork?
        get() {
            var bestWork: IWork? = null
            var highestChalks = -1
            for (ritual in RitualRegistry.getWorkList()) {
                val requirementsMet = ritual.canRitualRun(this.world, pos, this)
                val blocks = arrayListOf<PositionedBlock>()
                ritual.buildPositions(blocks)
                val chalks = PositionedBlockHelper.getChalkPriority(blocks, this, ritual.unlocalizedName)
                if (requirementsMet && highestChalks < chalks) {
                    bestWork = ritual
                    highestChalks = chalks
                }
            }
            return bestWork
        }

    private fun runRitual(ritual: IWork?, player: EntityPlayer) {
        if (world.isRemote || ritual == null) return
        currentWork = ritual
        ritual.initialTick(world, pos, this, player)
        stage = RitualStage.IN_PROGRESS
    }

    fun onBlockActivated(player: EntityPlayer): Boolean {
        if (currentWork == null)
            runRitual(bestRitual, player)
        return true
    }

    override fun writeCustomNBT(compound: NBTTagCompound, sync: Boolean) {
        compound.setInteger("Stage", stage.ordinal)
        val workName = RitualRegistry.getWorkName(currentWork)
        compound.setString("Work", workName ?: "")
    }

    override fun readCustomNBT(compound: NBTTagCompound) {
        stage = RitualStage.values()[compound.getInteger("Stage")]
        val WorkName = compound.getString("Work")
        currentWork = if (WorkName == "") null else RitualRegistry.getWorkByName(WorkName)
    }

    enum class RitualStage {
        IDLE, IN_PROGRESS
    }
}
