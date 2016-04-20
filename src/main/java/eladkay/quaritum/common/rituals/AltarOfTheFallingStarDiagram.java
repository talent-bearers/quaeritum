package eladkay.quaritum.common.rituals;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.rituals.EnumRitualDuration;
import eladkay.quaritum.api.rituals.EnumRitualType;
import eladkay.quaritum.api.rituals.IRitual;
import eladkay.quaritum.api.rituals.PositionedChalk;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.ArrayList;

public class AltarOfTheFallingStarDiagram implements IRitual {
    @Override
    public String getUnlocalizedName() {
        return "rituals.quaritum.altarofthefallingstar";
    }

    @Override
    public EnumRitualType getRitualType() {
        return EnumRitualType.DIAGRAM;
    }

    @Override
    public EnumRitualDuration getRitualDuration() {
        return EnumRitualDuration.INSTANT;
    }

    @Override
    public boolean runOnce(World world, EntityPlayer player, BlockPos pos) {
        /*List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.add(8, 8, 8).add(-8, -8, -8)));
        int total = 0;
        EntityPlayer receiver = null;
        for (EntityItem entity : list) {
            if (entity.getEntityItem().getItem() instanceof Dormant && entity.getEntityItem().getTagCompound().getBoolean(LibNames.TAG_ATTUNED)) {
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
            } else if (entity.getEntityItem().getItem() instanceof Awakened) {
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
    public boolean runDurable(World world, EntityPlayer player, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canRitualRun(World world, EntityPlayer player, BlockPos pos, TileEntity tile) {
       /* List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.add(8, 8, 8).add(-8, -8, -8)));
        boolean a = false;
        boolean b = false;
        for (EntityItem entity : list) {
            if (entity.getEntityItem().getItem() instanceof Dormant && entity.getEntityItem().getTagCompound().getBoolean(LibNames.TAG_ATTUNED))
                a = true;
            else if (entity.getEntityItem().getItem() instanceof Awakened) b = true;
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
    public String getCanonicalName() {
        return "Altar of the Falling Star";
    }

    @Override
    public ArrayList<PositionedChalk> getRequiredPositionedChalk() {
        ArrayList list = Lists.newArrayList();
        return list;
    }

    @Override
    public ArrayList<ArrayList<PositionedChalk>> getPossibleRequiredPositionedChalks() {
        ArrayList list = Lists.newArrayList();
        return list;
    }
}
