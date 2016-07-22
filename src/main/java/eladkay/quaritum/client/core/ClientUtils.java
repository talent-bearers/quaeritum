package eladkay.quaritum.client.core;

import eladkay.quaritum.api.animus.AnimusHelper;
import eladkay.quaritum.common.Quaritum;
import eladkay.quaritum.common.block.flowers.BlockAnimusFlower;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ClientUtils {
    public static void addInformation(ItemStack stack, List<String> tooltip, boolean advanced) {
        TooltipHelper.tooltipIfShift(tooltip, () -> {
            if (Quaritum.isDevEnv) {
                tooltip.add("Animus: " + AnimusHelper.getAnimus(stack));
                tooltip.add("Rarity: " + AnimusHelper.getRarity(stack));
            } else for (BlockAnimusFlower.Variants variant : BlockAnimusFlower.Variants.values())
                if (variant.rarity == AnimusHelper.getRarity(stack))
                    tooltip.add("Rarity: " + TooltipHelper.local(variant.getName()));
        });
    }
}
