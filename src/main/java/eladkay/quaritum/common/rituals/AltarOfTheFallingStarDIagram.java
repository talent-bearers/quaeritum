package eladkay.quaritum.common.rituals;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.rituals.EnumRitualDuration;
import eladkay.quaritum.api.rituals.EnumRitualType;
import eladkay.quaritum.api.rituals.IRitual;
import eladkay.quaritum.common.item.soulstones.Awakened;
import eladkay.quaritum.common.item.soulstones.Dormant;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

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
        List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.add(8, 8, 8).add(-8, -8, -8)));
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
        }
        return true;
    }

    @Override
    public boolean runDurable(World world, EntityPlayer player, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canRitualRun(World world, EntityPlayer player, BlockPos pos, TileEntity tile) {
        List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.add(8, 8, 8).add(-8, -8, -8)));
        boolean a = false;
        boolean b = false;
        for (EntityItem entity : list) {
            if (entity.getEntityItem().getItem() instanceof Dormant && entity.getEntityItem().getTagCompound().getBoolean(LibNames.TAG_ATTUNED))
                a = true;
            else if (entity.getEntityItem().getItem() instanceof Awakened) b = true;
        }
        return a && b;
    }

    @Override
    public ArrayList<ItemStack> getRequiredItems() {
        return Lists.newArrayList();
    }

    @Override
    public String getCanonicalName() {
        return "Altar of the Falling Star";
    }
}
