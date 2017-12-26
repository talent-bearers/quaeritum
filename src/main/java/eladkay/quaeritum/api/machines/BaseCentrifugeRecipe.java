package eladkay.quaeritum.api.machines;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreIngredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author WireSegal
 * Created at 10:07 AM on 2/1/17.
 */
public class BaseCentrifugeRecipe implements ICentrifugeRecipe {
    @NotNull
    private final Ingredient inputOne;
    @Nullable
    private final Ingredient inputTwo;
    @NotNull
    private final ItemStack output;
    private int steamRequired = 2000;
    private boolean requiresHeat = false;

    public BaseCentrifugeRecipe(@NotNull Object inputOne, @Nullable Object inputTwo, @NotNull ItemStack output) {
        this.inputOne = convertInputItem(inputOne);
        if (inputTwo != null)
            this.inputTwo = convertInputItem(inputTwo);
        else
            this.inputTwo = null;
        this.output = output;
    }

    public boolean requiresHeat() {
        return requiresHeat;
    }

    public int steamRequired() {
        return steamRequired;
    }

    public BaseCentrifugeRecipe setRequiresHeat(boolean requiresHeat) {
        this.requiresHeat = requiresHeat;
        return this;
    }

    public BaseCentrifugeRecipe setSteamRequired(int steamRequired) {
        this.steamRequired = steamRequired;
        return this;
    }

    @NotNull
    public List<ItemStack> getInputOne() {
        return Lists.newArrayList(inputOne.getMatchingStacks());
    }

    @Nullable
    public List<ItemStack> getInputTwo() {
        if (inputTwo == null) return null;
        return Lists.newArrayList(inputTwo.getMatchingStacks());
    }

    @NotNull
    public ItemStack getOutput() {
        return output;
    }

    private Ingredient convertInputItem(@NotNull Object input) {
        if (input instanceof ItemStack)
            return Ingredient.fromStacks((ItemStack) input);
        else if (input instanceof Item)
            return Ingredient.fromItem((Item) input);
        else if (input instanceof Block && Item.getItemFromBlock((Block) input) != Items.AIR)
            return Ingredient.fromItem(Item.getItemFromBlock((Block) input));
        else if (input instanceof String)
            return new OreIngredient((String) input);
        else
            throw new RuntimeException("Invalid ore recipe input: " + input);
    }

    @Override
    public boolean matches(@NotNull IItemHandler handler, boolean heated) {
        if (!heated && requiresHeat) return false;

        ItemStack itemOne = handler.extractItem(0, 1, true);
        ItemStack itemTwo = handler.extractItem(1, 1, true);
        if (matches(itemOne, inputOne))
            return (inputTwo == null || matches(itemTwo, inputTwo));
        return matches(itemOne, inputTwo) && matches(itemTwo, inputOne);
    }

    private boolean matches(@Nullable ItemStack stack, @Nullable Ingredient inputs) {
        return inputs != null && stack != null && inputs.apply(stack);
    }

    @Override
    public int steamRequired(@NotNull IItemHandler handler, boolean heated) {
        return steamRequired;
    }

    @Override
    public void consumeInputs(@NotNull IItemHandler handler, boolean heated) {
        ItemStack itemOne = handler.extractItem(0, 1, true);
        ItemStack itemTwo = handler.extractItem(1, 1, true);
        if (matches(itemOne, inputOne)) {
            handler.extractItem(0, 1, false);
            if (matches(itemTwo, inputTwo))
                handler.extractItem(1, 1, false);
        } else if (matches(itemOne, inputTwo)) {
            handler.extractItem(0, 1, false);
            if (matches(itemTwo, inputOne))
                handler.extractItem(1, 1, false);
        }
    }

    @NotNull
    @Override
    public ItemStack getOutput(@NotNull IItemHandler handler, boolean heated) {
        return output.copy();
    }
}
