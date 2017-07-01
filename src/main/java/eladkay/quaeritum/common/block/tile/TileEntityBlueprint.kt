package eladkay.quaeritum.common.block.tile

import eladkay.quaeritum.api.rituals.IDiagram
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.api.rituals.RitualRegistry
import eladkay.quaeritum.common.core.PositionedBlockHelper
import net.minecraft.nbt.NBTTagCompound

class TileEntityBlueprint : TileMod() {

    var stage = RitualStage.IDLE
    var stageTicks = 0
    var currentDiagram: IDiagram? = null

    public override fun updateEntity() {
        if (!world.isRemote) {
            //LogHelper.logDebug(currentDiagram);
            if (currentDiagram != null)
                if (stageTicks > 0) {
                    stage = RitualStage.PREP
                    stageTicks--
                    if (!currentDiagram!!.onPrepUpdate(world, pos, this, stageTicks))
                        currentDiagram = null
                } else {
                    stage = RitualStage.CRAFTING
                    currentDiagram!!.run(world, pos, this)
                    currentDiagram = null
                }

            if (currentDiagram == null) {
                stage = RitualStage.IDLE
                stageTicks = 0
            }
        }
    }

    private val bestRitual: IDiagram?
        get() {
            var bestDiagram: IDiagram? = null
            var highestChalks = -1
            for (ritual in RitualRegistry.getDiagramList()) {
                val foundAll = ritual.hasRequiredItems(world, pos, this)
                val requirementsMet = ritual.canRitualRun(this.world, pos, this)
                val blocks = arrayListOf<PositionedBlock>()
                ritual.buildChalks(blocks)
                val chalks = PositionedBlockHelper.getChalkPriority(blocks, this, ritual.unlocalizedName)
                if (foundAll && requirementsMet && highestChalks < chalks) {
                    bestDiagram = ritual
                    highestChalks = chalks
                }
                println(chalks.toString() + ", " + requirementsMet + ", " + foundAll + ", " + ritual.unlocalizedName)
            }
            return bestDiagram
        }

    private fun runRitual(ritual: IDiagram?) {
        if (world.isRemote || ritual == null) return
        currentDiagram = ritual
        stage = RitualStage.PREP
        stageTicks = ritual.getPrepTime(world, pos, this)
    }

    fun onBlockActivated(): Boolean {
        if (currentDiagram == null)
            runRitual(bestRitual)
        return true
    }

    override fun writeCustomNBT(compound: NBTTagCompound) {
        compound.setInteger("Stage", stage.ordinal)
        val diagramName = RitualRegistry.getDiagramName(currentDiagram)
        compound.setString("Diagram", diagramName ?: "")
        compound.setInteger("StageTicks", stageTicks)
    }

    override fun readCustomNBT(compound: NBTTagCompound) {
        stage = RitualStage.values()[compound.getInteger("Stage")]
        val diagramName = compound.getString("Diagram")
        currentDiagram = if (diagramName == "") null else RitualRegistry.getDiagramByName(diagramName)
        stageTicks = compound.getInteger("StageTicks")
    }

    enum class RitualStage {
        IDLE, PREP, CRAFTING
    }

}
