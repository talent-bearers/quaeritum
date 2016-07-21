package eladkay.quaritum.common.block.tile;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.rituals.IWork;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.api.rituals.RitualRegistry;
import eladkay.quaritum.common.core.PositionedBlockHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class TileEntityFoundationStone extends TileMod {
    public RitualStage stage = RitualStage.IDLE;
    public long ticksExisted = 0;
    public IWork currentWork = null;

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (currentWork != null) {
                stage = RitualStage.IN_PROGRESS;
                if (!currentWork.updateTick(worldObj, pos, this, ticksExisted)) currentWork = null;
                ticksExisted++;
            }
            if (currentWork == null) {
                stage = RitualStage.IDLE;
                ticksExisted = 0;
            }
        }
    }

    private IWork getBestRitual() {
        IWork bestWork = null;
        int highestChalks = -1;
        for (IWork ritual : RitualRegistry.getWorkList()) {
            boolean requirementsMet = ritual.canRitualRun(this.getWorld(), pos, this);
            List<PositionedBlock> blocks = Lists.newArrayList();
            ritual.buildPositions(blocks);
            int chalks = PositionedBlockHelper.getChalkPriority(blocks, this, ritual.getUnlocalizedName());
            if (requirementsMet && highestChalks < chalks) {
                bestWork = ritual;
                highestChalks = chalks;
            }
        }
        return bestWork;
    }

    private void runRitual(IWork ritual, EntityPlayer player) {
        if (worldObj.isRemote || ritual == null) return;
        currentWork = ritual;
        ritual.initialTick(worldObj, pos, this, player);
        stage = RitualStage.IN_PROGRESS;
    }

    public boolean onBlockActivated(EntityPlayer player) {
        if (currentWork == null)
            runRitual(getBestRitual(), player);
        return true;
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        compound.setInteger("Stage", stage.ordinal());
        String workName = RitualRegistry.getWorkName(currentWork);
        compound.setString("Work", workName == null ? "" : workName);
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        stage = RitualStage.values()[compound.getInteger("Stage")];
        String WorkName = compound.getString("Work");
        currentWork = WorkName.equals("") ? null : RitualRegistry.getWorkByName(WorkName);
    }

    public enum RitualStage {
        IDLE, IN_PROGRESS
    }
}
