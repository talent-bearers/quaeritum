package eladkay.quaritum.common.rituals;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.PositionedBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class AltarOfTheFallingStarDiagram implements IDiagram {
    @Override
    public String getUnlocalizedName() {
        return "rituals.quaritum.altarofthefallingstar";
    }


    @Override
    public boolean run(World world, EntityPlayer player, BlockPos pos) {
        /*List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.add(8, 8, 8).add(-8, -8, -8)));
        int total = 0;
        EntityPlayer receiver = null;
        for (EntityItem entity : list) {
            if (entity.getEntityItem().getItem() instanceof ItemDormantSoulstone && entity.getEntityItem().getTagCompound().getBoolean(LibNames.TAG_ATTUNED)) {
                receiver = new EntityPlayer(world, new com.mojang.authlib.GameProfile(entity.getEntityItem().getTagCompound().getUniqueId(LibNames.TAG_UUID), entity.getEntityItem().getTagCompound().getString(LibNames.TAG_OWNER))) {
                    @Override
                    public boolean isSpectator() {
                        return false;
                    }

                    @Override
                    public boolean isCreative() {
                        return false;
                    }
                };
                entity.setDead();
            } else if (entity.getEntityItem().getItem() instanceof ItemAwakenedSoulstone) {
                total += entity.getEntityItem().getTagCompound().getInteger(LibNames.TAG_ANIMUS);
                entity.setDead();
            }
        }
        if (receiver != null && total > 0) {
            receiver.getEntityData().setInteger(LibNames.TAG_ANIMUS_ON_ENTITY, 50);
        }*/
        player.addChatComponentMessage(new TextComponentString("Consider it done."));
        return true;
    }


    @Override
    public boolean canRitualRun(World world, EntityPlayer player, BlockPos pos, TileEntity tile) {
       /* List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.add(8, 8, 8).add(-8, -8, -8)));
        boolean a = false;
        boolean b = false;
        for (EntityItem entity : list) {
            if (entity.getEntityItem().getItem() instanceof ItemDormantSoulstone && entity.getEntityItem().getTagCompound().getBoolean(LibNames.TAG_ATTUNED))
                a = true;
            else if (entity.getEntityItem().getItem() instanceof ItemAwakenedSoulstone) b = true;
        }
        return a && b;
        */
        return true;
    }

    @Override
    public ArrayList<ItemStack> getRequiredItems() {
        ArrayList<ItemStack> list = Lists.newArrayList();
        list.add(new ItemStack(Items.diamond));
        return list;
        //return Lists.newArrayList();

    }

    @Override
    public List<PositionedBlock> buildChalks(@Nonnull List<PositionedBlock> chalks) {
        return chalks;
    }
}
