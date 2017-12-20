package eladkay.quaeritum.api.spell.render;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author WireSegal
 * Created at 8:17 PM on 7/26/17.
 */
public interface ISymbolInstruction {
    @SideOnly(Side.CLIENT)
    void render(ISymbolCarrier element, float x, float y);
}
