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

    public CraftingStage stage = CraftingStage.IDLE;
    public int stageTicks = 0;

    public enum CraftingStage {
        IDLE, PARTICLES, CRAFTING
    }
    public World getWorld() {
        return worldObj;
    }
    @Override
    public void updateEntity() {
        //todo
    }

    private IDiagram getValidRitual() {
        IDiagram bestFit = null;
        int best = -1;
        for (IDiagram ritual : RitualRegistry.getDiagramList()) {
            boolean foundAll = ritual.hasRequiredItems(worldObj, pos, this);
            boolean requirementsMet = ritual.canRitualRun(this.getWorld(), pos, this);
            List<PositionedBlock> blocks = Lists.newArrayList();
            ritual.buildChalks(blocks);
            int chalks = PositionedBlockHelper.getChalkPriority(blocks, this, ritual.getUnlocalizedName());

            if (foundAll && requirementsMet && chalks > best) {
                best = chalks;
                bestFit = ritual;
            }
        }
        runRitual(bestFit);
        return null;
    }

    private boolean runRitual(IDiagram ritual) {
        if(ritual != null && !worldObj.isRemote) {
            ritual.run(worldObj, pos, this);
            return true;
        } else return false;
    }

    public boolean onBlockActivated() {
        return runRitual(getValidRitual());
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        compound.setInteger("Stage", stage.ordinal());
        compound.setInteger("StageTicks", stageTicks);
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        stage = CraftingStage.values()[compound.getInteger("Stage")];
        stageTicks = compound.getInteger("StageTicks");
    }
}
