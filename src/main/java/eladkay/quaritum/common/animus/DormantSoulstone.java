package eladkay.quaritum.common.animus;

import com.google.common.collect.Lists;
import eladkay.quaritum.client.core.TooltipHelper;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class DormantSoulstone extends ItemMod implements ISoulStone {

    public static final String TAG_ANIMUS = "animus";
    public static List<String> variants = Lists.newArrayList();

    public DormantSoulstone(String name) {
        super(name, (String[]) variants.toArray());
    }

    public DormantSoulstone() {
        super(LibNames.DORMANT_SOULSTONE);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getAnimusLevel(ItemStack stack) {
        return 0;
    }

    @Override
    public ItemStack deductAnimus(ItemStack stack, int amount) {
        return stack;
    }

    @Override
    public ItemStack addAnimus(ItemStack stack, int amount) {
        return stack;
    }

    @Override
    public int getMaxAnimus() {
        return 0;
    }

    @Override
    public boolean isRechargeable() {
        return false;
    }

    @Override
    public void doPassive(ItemStack stack) {

    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        TooltipHelper.tooltipIfShift(tooltip, () -> {
            TooltipHelper.addToTooltip(tooltip, "Animus:" + String.valueOf(stack.getTagCompound().getInteger(TAG_ANIMUS)));
        });
    }
}
