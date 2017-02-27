package eladkay.quaeritum.api.contract;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author WireSegal
 *         Created at 9:13 PM on 2/10/17.
 */
@Cancelable
public final class ContractEvent extends Event {
    private final IContractOath oath;
    private final ItemStack contractStack;
    private final EntityPlayer player;
    private final World world;
    private final BlockPos pos;

    public IContractOath getOath() {
        return oath;
    }

    public ItemStack getContractStack() {
        return contractStack;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public World getWorld() {
        return world;
    }

    public BlockPos getPos() {
        return pos;
    }

    public ContractEvent(IContractOath oath, ItemStack contractStack, EntityPlayer player, World world, BlockPos pos) {

        this.oath = oath;
        this.contractStack = contractStack;
        this.player = player;
        this.world = world;
        this.pos = pos;
    }
}
