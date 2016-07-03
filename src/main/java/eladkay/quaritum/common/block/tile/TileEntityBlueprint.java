package eladkay.quaritum.common.block.tile;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.api.rituals.RitualRegistry;
import eladkay.quaritum.common.core.PositionedBlockHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityBlueprint extends TileMod {

    public CraftingStage stage = CraftingStage.IDLE;
    public int stageTicks = 0;

    public enum CraftingStage {
        IDLE, PARTICLES, CRAFTING
    }

    @Override
    public void updateEntity() {
        //todo
    }

    private IDiagram getValidRitual() {
        for (IDiagram ritual : RitualRegistry.getDiagramList()) {
            boolean foundAll = ritual.hasRequiredItems(worldObj, pos, this);
            boolean requirementsMet = ritual.canRitualRun(this.getWorld(), pos, this);
            List<PositionedBlock> blocks = Lists.newArrayList();
            ritual.buildChalks(blocks);
            boolean chalks = PositionedBlockHelper.isChalkSetupValid(blocks, this, ritual.getUnlocalizedName());
            if (foundAll && requirementsMet && chalks) {
                return ritual;
            }
        }
        return null;
    }

    private boolean runRitual(IDiagram ritual) {
        return ritual != null && (worldObj.isRemote || ritual.run(worldObj, pos, this));
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
