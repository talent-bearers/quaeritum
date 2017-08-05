package eladkay.quaeritum.api.spell.render;

/**
 * @author WireSegal
 *         Created at 8:17 PM on 7/26/17.
 */
public interface ISymbolCarrier {

    int color();

    ISymbolInstruction[] getSymbolInstructions();
}
