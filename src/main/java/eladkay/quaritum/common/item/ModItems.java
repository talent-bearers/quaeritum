package eladkay.quaritum.common.item;

import eladkay.quaritum.common.item.soulstones.Dormant;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

    public static Dormant dormant;

    public static void init() {
        dormant = new Dormant(LibNames.DORMANT_SOULSTONE);
        GameRegistry.addShapelessRecipe(new ItemStack(dormant), new ItemStack(Items.slime_ball));
        System.out.println("Working");
    }
}
