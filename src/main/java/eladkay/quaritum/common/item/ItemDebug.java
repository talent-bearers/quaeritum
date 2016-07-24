package eladkay.quaritum.common.item;

import eladkay.quaritum.api.animus.AnimusHelper;
import eladkay.quaritum.common.Quaritum;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.block.chalk.BlockChalk;
import eladkay.quaritum.common.core.ChatHelper;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.Objects;

public class ItemDebug extends ItemMod {

    public ItemDebug() {
        super(LibNames.DEBUG);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (!worldIn.isRemote) //this doesn't work if not inverted and it doesn't matter anyways because this debug item only exists in a dev environment
            if (GuiScreen.isShiftKeyDown()) {
                AnimusHelper.Network.addAnimus(playerIn, 50);
                AnimusHelper.Network.addRarity(playerIn, 1);
                ChatHelper.sendNoSpam2(playerIn, "Added 50, current animus level for " + playerIn.getName() + " is: " + AnimusHelper.Network.getAnimus(playerIn));
            } else if (GuiScreen.isCtrlKeyDown()) {
                AnimusHelper.Network.addAnimus(playerIn, -50);
                ChatHelper.sendNoSpam2(playerIn, "Took 50, current animus level for " + playerIn.getName() + " is: " + AnimusHelper.Network.getAnimus(playerIn));
            } else
                ChatHelper.sendNoSpam2(playerIn, "Current animus level for " + playerIn.getName() + " is: " + AnimusHelper.Network.getAnimus(playerIn) + " and current rarity is " + AnimusHelper.Network.getRarity(playerIn));

        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }

    /*@Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(RayHelper.raycast(entityIn, 32) == null || !isSelected) return;
        BlockPos pos = RayHelper.raycast(entityIn, 32).getBlockPos();
        IBlockState state = worldIn.getBlockState(pos);
        if(state.getBlock() == ModBlocks.blueprint)
            renderAABB(Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), pos.getX() - 4, pos.getY(), pos.getZ() - 4, pos.getX() + 4, pos.getY(), pos.getZ() + 4, 255, 223, 127);
         else if(state.getBlock() == ModBlocks.foundation)
            renderAABB(Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), pos.getX() - 12, pos.getY() + 1, pos.getZ() - 12, pos.getX() + 12, pos.getY() + 25, pos.getZ() + 12, 255, 223, 127);
    }*/

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn.isSneaking()) {
            String out = "";
            boolean flag = false;
            IBlockState state = worldIn.getBlockState(pos);
            if (state.getBlock() == ModBlocks.blueprint) {
                for (BlockPos shift : BlockPos.getAllInBox(new BlockPos(-4, 0, -4), new BlockPos(4, 0, 4))) {
                    BlockPos bpos = pos.add(shift);
                    IBlockState bstate = worldIn.getBlockState(bpos);
                    if (bstate.getBlock() == ModBlocks.chalk) {
                        if (flag) out += "\n";
                        flag = true;
                        out += "chalks.add(new PositionedBlockChalk(EnumDyeColor." + bstate.getValue(BlockChalk.COLOR).name() + ", new BlockPos(" + shift.getX() + ", " + shift.getY() + ", " + shift.getZ() + ")));";
                    } else if (bstate.getBlock() == ModBlocks.tempest) {
                        if (flag) out += "\n";
                        flag = true;
                        out += "chalks.add(new PositionedBlockChalk(null, new BlockPos(" + shift.getX() + ", " + shift.getY() + ", " + shift.getZ() + ")));";
                    }
                }
                if (!Objects.equals(out, ""))
                    Quaritum.proxy.copyText(out);
                if (!worldIn.isRemote) {
                    if (!Objects.equals(out, ""))
                        playerIn.addChatComponentMessage(new TextComponentString(TextFormatting.GREEN + "Copied Diagrammic information to clipboard"));
                    else
                        playerIn.addChatComponentMessage(new TextComponentString(TextFormatting.RED + "Output is empty!"));
                }
            } else if (state.getBlock() == ModBlocks.foundation) {
                for (BlockPos shift : BlockPos.getAllInBox(new BlockPos(-12, 1, -12), new BlockPos(12, 25, 12))) {
                    BlockPos bpos = pos.add(shift);
                    IBlockState bstate = worldIn.getBlockState(bpos);
                    if (bstate.getBlock() == ModBlocks.chalk) {
                        if (flag) out += "\n";
                        flag = true;
                        out += "chalks.add(new PositionedBlockChalk(EnumDyeColor." + bstate.getValue(BlockChalk.COLOR).name() + ", new BlockPos(" + shift.getX() + ", " + shift.getY() + ", " + shift.getZ() + ")));";
                    } else if (bstate.getBlock() == ModBlocks.tempest) {
                        if (flag) out += "\n";
                        flag = true;
                        out += "chalks.add(new PositionedBlockChalk(null, new BlockPos(" + shift.getX() + ", " + shift.getY() + ", " + shift.getZ() + ")));";
                    } else if (bstate.getBlock() != Blocks.AIR) {
                        if (flag) out += "\n";
                        flag = true;
                        if (Objects.equals(bstate.getBlock().getRegistryName().getResourceDomain(), "minecraft"))
                            out += "chalks.add(new PositionedBlock(Blocks." + bstate.getBlock().getRegistryName().getResourcePath().toUpperCase() + ".getDefaultState(), new BlockPos(" + shift.getX() + ", " + shift.getY() + ", " + shift.getZ() + "), null)); // " + bstate;
                        else
                            out += "chalks.add(new PositionedBlock(UNKNOWN, new BlockPos(" + shift.getX() + ", " + shift.getY() + ", " + shift.getZ() + "), null); // " + bstate;
                    }
                }
                if (!Objects.equals(out, ""))
                    Quaritum.proxy.copyText(out);
                if (!worldIn.isRemote) {
                    if (!Objects.equals(out, ""))
                        playerIn.addChatComponentMessage(new TextComponentString(TextFormatting.GREEN + "Copied Work information to clipboard"));
                    else
                        playerIn.addChatComponentMessage(new TextComponentString(TextFormatting.RED + "Output is empty!"));
                }
            }
        }
        return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

}
