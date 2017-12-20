package eladkay.quaeritum.api.machines;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author WireSegal
 * Created at 10:07 AM on 2/1/17.
 */
public class BaseCentrifugeRecipe implements ICentrifugeRecipe {
    @NotNull
    private final List<ItemStack> inputOne;
    @Nullable
    private final List<ItemStack> inputTwo;
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
        return inputOne;
    }

    @Nullable
    public List<ItemStack> getInputTwo() {
        return inputTwo;
    }

    @NotNull
    public ItemStack getOutput() {
        return output;
    }

    private List<ItemStack> convertInputItem(@NotNull Object input) {
        List<ItemStack> ret = Lists.newArrayList();
        if (input instanceof ItemStack)
            ret.add(ItemHandlerHelper.copyStackWithSize((ItemStack) input, 1));
        else if (input instanceof Item)
            ret.add(new ItemStack((Item) input));
        else if (input instanceof Block)
            ret.add(new ItemStack((Block) input));
        else if (input instanceof String)
            OreDictionary.getOres((String) input).stream()
                    .map(stack -> ItemHandlerHelper.copyStackWithSize(stack, 1))
                    .forEach(ret::add);
        else
            throw new RuntimeException("Invalid ore recipe input: " + input);
        return ret;
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

    private boolean matches(@Nullable ItemStack stack, @Nullable List<ItemStack> inputs) {
        if (stack == null || inputs == null) return false;
        for (ItemStack ore : inputs)
            if (OreDictionary.itemMatches(ore, stack, false))
                return true;
        return false;
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
