package eladkay.quaritum.common.item;

import eladkay.quaritum.common.Quartium;
import eladkay.quaritum.common.item.chalk.ItemChalk;
import eladkay.quaritum.common.item.soulstones.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

    public static ItemDormantSoulstone dormant;
    public static ItemAwakenedSoulstone awakened;
    public static ItemPassionateSoulstone passionate;
    public static ItemWorldSoulstone passive;
    public static ItemOppressiveSoulstone oppressive;
    public static ItemAttunedSoulstone attuned;
    public static ItemDebug debug;
    public static ItemChalk chalk;

    public static void init() {
        dormant = new ItemDormantSoulstone();
        awakened = new ItemAwakenedSoulstone();
        passionate = new ItemPassionateSoulstone();
        passive = new ItemWorldSoulstone();
        oppressive = new ItemOppressiveSoulstone();
        attuned = new ItemAttunedSoulstone();
        chalk = new ItemChalk();

        if (Quartium.isDevEnv)
            debug = new ItemDebug();

        //CraftingManager.getInstance().addRecipe(new RecipeAwakenedSoulstone());
        GameRegistry.addShapedRecipe(new ItemStack(dormant), "XYX", "XZX", "XYX", 'X', new ItemStack(Blocks.soul_sand), 'Z', new ItemStack(Items.diamond), 'Y', new ItemStack(Blocks.glass));
    }
}
