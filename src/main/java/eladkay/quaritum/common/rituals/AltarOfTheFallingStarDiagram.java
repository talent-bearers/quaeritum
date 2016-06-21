package eladkay.quaritum.common.rituals;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.animus.AnimusHelper;
import eladkay.quaritum.api.animus.ISoulstone;
import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.common.core.PositionedBlockHelper;
import eladkay.quaritum.common.item.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class AltarOfTheFallingStarDiagram implements IDiagram {
    @Nonnull
    @Override
    public String getUnlocalizedName() {
        return "rituals.quaritum.altarofthefallingstar";
    }


    @Override
    public boolean run(@Nonnull World world, EntityPlayer player, @Nonnull BlockPos pos, TileEntity te, List<ItemStack> item) {
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
        boolean flag = false;
        for (ItemStack stack : item) {
            if (!(stack.getItem() instanceof ISoulstone)) continue;
            ISoulstone ss = (ISoulstone) stack.getItem();
            AnimusHelper.Player.addAnimus(player, ss.getAnimusLevel(item.get(0)));
            AnimusHelper.Player.addAnimusRarity(player, ss.getAnimusLevel(item.get(0)));
            flag = true;
        }
        player.addChatComponentMessage(new TextComponentString("A rush of energy flows through your soul!"));
        return flag;
    }


    @Override
    public boolean canRitualRun(World world, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull TileEntity tile) {
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
        list.add(new ItemStack(ModItems.awakened));
        return list;
        //return Lists.newArrayList();

    }

    @Override
    public List<PositionedBlock> buildChalks(@Nonnull List<PositionedBlock> chalks) {
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(1, 0, 0), EnumDyeColor.RED));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(-1, 0, 0), EnumDyeColor.RED));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, 1), EnumDyeColor.RED));
        chalks.add(PositionedBlockHelper.positionedBlockWith(new BlockPos(0, 0, -1), EnumDyeColor.RED));
        return chalks;
    }
}
