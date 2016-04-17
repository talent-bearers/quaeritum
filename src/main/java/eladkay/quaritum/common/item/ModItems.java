package eladkay.quaritum.common.item;

import eladkay.quaritum.common.item.soulstones.Awakened;
import eladkay.quaritum.common.item.soulstones.Dormant;
import eladkay.quaritum.common.item.soulstones.Passionate;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

    public static Dormant dormant;
    public static Awakened awakened;
    public static Passionate passionate;

    public static void init() {
        dormant = new Dormant(LibNames.DORMANT_SOULSTONE);
        awakened = new Awakened(LibNames.AWAKENED_SOULSTONE);
        passionate = new Passionate(LibNames.PASSIONATE_SOULSTONE);

        GameRegistry.registerFuelHandler(passionate);
        GameRegistry.addShapelessRecipe(new ItemStack(dormant), new ItemStack(Items.slime_ball));
    }
}
