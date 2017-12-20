package eladkay.quaeritum.api.misc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author WireSegal
 * Created at 9:06 PM on 2/6/17.
 */
public final class SightEvent extends Event {

    private final EntityPlayer player;
    private boolean hasThirdEye = false;

    public SightEvent(EntityPlayer player) {
        this.player = player;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public boolean getHasThirdEye() {
        return hasThirdEye;
    }

    public void setHasThirdEye(boolean hasThirdEye) {
        this.hasThirdEye = hasThirdEye;
    }
}
