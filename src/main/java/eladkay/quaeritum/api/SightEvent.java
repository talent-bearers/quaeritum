package eladkay.quaeritum.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author WireSegal
 *         Created at 9:06 PM on 2/6/17.
 */
public class SightEvent extends Event {

    public SightEvent(EntityPlayer player) {
        this.player = player;
    }

    private EntityPlayer player;

    private boolean hasThirdEye = false;

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
