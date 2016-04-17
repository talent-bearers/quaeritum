package eladkay.quaritum.common.item;

import eladkay.quaritum.common.item.chalk.ItemChalk;
import eladkay.quaritum.common.item.soulstones.Awakened;
import eladkay.quaritum.common.item.soulstones.Dormant;
import eladkay.quaritum.common.item.soulstones.Passionate;
import eladkay.quaritum.common.item.soulstones.Worldsoul;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

    public static Dormant dormant;
    public static Awakened awakened;
    public static Passionate passionate;
    public static Worldsoul passive;
    public static DebugItem debug;
    public static ItemChalk chalk;

    public static void init() {
        dormant = new Dormant();
        awakened = new Awakened();
        passionate = new Passionate();
        passive = new Worldsoul();
        debug = new DebugItem();
        chalk = new ItemChalk();

        GameRegistry.registerFuelHandler(passionate);
        GameRegistry.addShapelessRecipe(new ItemStack(dormant), new ItemStack(Items.slime_ball));
    }
}
