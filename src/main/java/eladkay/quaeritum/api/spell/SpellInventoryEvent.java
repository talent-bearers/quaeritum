package eladkay.quaeritum.api.spell;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.List;

/**
 * @author WireSegal
 * Created at 9:13 PM on 2/10/17.
 */
public final class SpellInventoryEvent extends Event {
    private final EntityPlayer player;
    private final EnumSpellElement element;
    private final List<IItemHandlerModifiable> handlers;

    public SpellInventoryEvent(EntityPlayer player, EnumSpellElement element, List<IItemHandlerModifiable> handlers) {
        this.player = player;
        this.element = element;
        this.handlers = handlers;
    }

    public static List<IItemHandlerModifiable> getHandlers(EntityPlayer player, EnumSpellElement element) {
        IItemHandlerModifiable playerHandler = new InvWrapper(player.inventory);
        SpellInventoryEvent event = new SpellInventoryEvent(player, element, Lists.newArrayList(playerHandler));
        MinecraftForge.EVENT_BUS.post(event);
        return event.getHandlers();
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public EnumSpellElement getElement() {
        return element;
    }

    public List<IItemHandlerModifiable> getHandlers() {
        return handlers;
    }
}
