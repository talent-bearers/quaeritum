package eladkay.quaritum.common.block.tile;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.api.rituals.RitualRegistry;
import eladkay.quaritum.common.core.PositionedBlockHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

public class TileEntityBlueprint extends TileMod {

    public RitualStage stage = RitualStage.IDLE;
    public int stageTicks = 0;
    public IDiagram currentDiagram = null;

    public enum RitualStage {
        IDLE, PREP, CRAFTING
    }

    @Override
    public void updateEntity() {
        if (worldObj.isRemote) {
            if (currentDiagram != null) {
                if (stageTicks > 0) {
                    stage = RitualStage.PREP;
                    stageTicks--;
                    if (!currentDiagram.onPrepUpdate(worldObj, pos, this, stageTicks))
                        currentDiagram = null;
                } else {
                    stage = RitualStage.CRAFTING;
                    currentDiagram.run(worldObj, pos, this);
                    currentDiagram = null;
                }
            }

            if (currentDiagram == null) {
                stage = RitualStage.IDLE;
                stageTicks = 0;
            }
        }
    }

    private IDiagram getBestRitual() {
        IDiagram bestDiagram = null;
        int highestChalks = 0;
        for (IDiagram ritual : RitualRegistry.getDiagramList()) {
            boolean foundAll = ritual.hasRequiredItems(worldObj, pos, this);
            boolean requirementsMet = ritual.canRitualRun(this.getWorld(), pos, this);
            List<PositionedBlock> blocks = Lists.newArrayList();
            ritual.buildChalks(blocks);
            int chalks = PositionedBlockHelper.getChalkPriority(blocks, this, ritual.getUnlocalizedName());
            if (foundAll && requirementsMet && highestChalks < chalks) {
                bestDiagram = ritual;
                highestChalks = chalks;
            }
        }
        return bestDiagram;
    }

    private void runRitual(IDiagram ritual) {
        if (worldObj.isRemote || ritual == null) return;
        currentDiagram = ritual;
        stage = RitualStage.PREP;
        stageTicks = ritual.getPrepTime(worldObj, pos, this);
    }

    public boolean onBlockActivated() {
        if (currentDiagram == null)
            runRitual(getBestRitual());
        return true;
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        compound.setInteger("Stage", stage.ordinal());
        String diagramName = RitualRegistry.getRitualName(currentDiagram);
        compound.setString("Diagram", diagramName == null ? "" : diagramName);
        compound.setInteger("StageTicks", stageTicks);
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        stage = RitualStage.values()[compound.getInteger("Stage")];
        String diagramName = compound.getString("Diagram");
        currentDiagram = diagramName.equals("") ? null : RitualRegistry.getDiagramByName(diagramName);
        stageTicks = compound.getInteger("StageTicks");
    }

}
