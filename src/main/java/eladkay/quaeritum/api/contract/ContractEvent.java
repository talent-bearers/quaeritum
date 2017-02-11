package eladkay.quaeritum.api.contract;

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
    private final World world;
    private final BlockPos pos;

    public ContractEvent(IContractOath oath, World world, BlockPos pos) {
        this.oath = oath;
        this.world = world;
        this.pos = pos;
    }

    public IContractOath getOath() {
        return oath;
    }

    public World getWorld() {
        return world;
    }

    public BlockPos getPos() {
        return pos;
    }
}
