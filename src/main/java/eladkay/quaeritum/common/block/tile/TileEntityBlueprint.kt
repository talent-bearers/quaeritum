package eladkay.quaeritum.common.block.tile

import com.teamwizardry.librarianlib.features.autoregister.TileRegister
import com.teamwizardry.librarianlib.features.base.block.tile.TileModTickable
import eladkay.quaeritum.api.rituals.IDiagram
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.api.rituals.RitualRegistry
import eladkay.quaeritum.common.core.PositionedBlockHelper
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing

@TileRegister("blueprint")
class TileEntityBlueprint : TileModTickable() {

    var stage = RitualStage.IDLE
    var stageTicks = 0
    var currentDiagram: IDiagram? = null

    override fun tick() {
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

    private val mirrorStates = arrayOf(-1, 1)

    val bestRitual: IDiagram?
        get() {
            var bestDiagram: IDiagram? = null
            var highestChalks = -1
            diagram@ for (ritual in RitualRegistry.getDiagramList()) {
                for (rotation in EnumFacing.HORIZONTALS) {
                    for (mirror in mirrorStates) {
                        val blocks = arrayListOf<PositionedBlock>()
                        ritual.buildChalks(blocks)
                        PositionedBlockHelper.transform(blocks, rotation, mirror)
                        val chalks = PositionedBlockHelper.getChalkPriority(blocks, this)
                        if (highestChalks < chalks) {
                            bestDiagram = ritual
                            highestChalks = chalks
                            continue@diagram
                        }
                    }
                }
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
        if (currentDiagram == null) {
            val best = bestRitual
            if (best != null) {
                val foundAll = best.hasRequiredItems(world, pos, this)
                val requirementsMet = best.canRitualRun(this.world, pos, this)
                if (foundAll && requirementsMet)
                    runRitual(bestRitual)
            }
        }
        return true
    }

    override fun writeCustomNBT(cmp: NBTTagCompound, sync: Boolean) {
        cmp.setInteger("Stage", stage.ordinal)
        val diagramName = RitualRegistry.getDiagramName(currentDiagram)
        cmp.setString("Diagram", diagramName ?: "")
        cmp.setInteger("StageTicks", stageTicks)
    }

    override fun readCustomNBT(cmp: NBTTagCompound) {
        stage = RitualStage.values()[cmp.getInteger("Stage")]
        val diagramName = cmp.getString("Diagram")
        currentDiagram = if (diagramName == "") null else RitualRegistry.getDiagramByName(diagramName)
        stageTicks = cmp.getInteger("StageTicks")
    }

    enum class RitualStage {
        IDLE, PREP, CRAFTING
    }

}
