package eladkay.quaeritum.api.spell;

import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 * Created at 3:52 AM on 7/2/17.
 */
public interface IAlchemicalSpell {
    /**
     * Gets the pattern required by this spell, with max size eight. (8 is max symbol storage.)
     * Should be consistent between gets.
     * Example:
     * [AIR, ENTROPY, EARTH] for a lightning spell.
     *
     * @return The spell pattern.
     */
    @Nonnull
    EnumSpellElement[] getPattern();

    /**
     * Performs the spell effect.
     *
     * @param player         The player casting the spell. Use this for world context.
     * @param trailingAether The number of Æther symbols that trail after this spell. This should be used as a potency value.
     * @param totalSpells    The number of spells contained within the symbol set. This should lower potency.
     */
    void performEffect(@Nonnull EntityPlayer player, int trailingAether, int totalSpells);

    /**
     * Gets the type of the spell, for use in display elements.
     * See {@link EnumSpellType} for descriptions of the elements.
     *
     * @return The type of the spell.
     */
    @Nonnull
    EnumSpellType getTypeOfSpell();

    /**
     * Gets the simple name of the spell, for localization.
     * quaeritum.spell.`localization key`.name is the translated key.
     *
     * @return The last part of a spell
     */
    @Nonnull
    String getLocalizationKey();
}
