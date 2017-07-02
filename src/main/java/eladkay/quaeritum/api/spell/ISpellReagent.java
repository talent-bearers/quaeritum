package eladkay.quaeritum.api.spell;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 *         Created at 11:20 PM on 7/1/17.
 */
public interface ISpellReagent {
    /**
     * Checks how many Spell Charges are contained within a stack. This SHOULD take size into account.
     * @param stack The item stack being checked.
     * @param element The element to check for.
     * @return -1 if this stack cannot hold that elemental type, otherwise the number of charges it holds of that type.
     */
    int chargesForElement(@Nonnull ItemStack stack, @Nonnull EnumSpellElement element);

    /**
     * Consume a Spell Charge from a reagent. This is used for elemental spells.
     * @param stack The item stack being consumed from.
     * @param element The element to consume.
     * @return An action result containing the result stack, and:
     *  SUCCESS, if the charge was successfully taken
     *  FAIL, if the reagent stack can hold that element but is empty somehow
     *  PASS, if the reagent stack doesn't hold that element
     */
    @Nonnull
    ActionResult<ItemStack> consumeCharge(@Nonnull ItemStack stack, @Nonnull EnumSpellElement element);
}
