package eladkay.quaritum.common.compat.jei;

import com.google.common.collect.Lists;
import eladkay.quaritum.common.compat.jei.rituals.RitualRecipeCatagory;
import eladkay.quaritum.common.compat.jei.rituals.RitualRecipeHandler;
import eladkay.quaritum.common.compat.jei.rituals.RitualRecipeRecipe;
import mezz.jei.api.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

@JEIPlugin
public class JeiPlugin implements IModPlugin {

    private IJeiHelpers jeiHelpers;


    @Override
    public void register(IModRegistry registry) {
        jeiHelpers = registry.getJeiHelpers();
        List ritualRecipes = Lists.newArrayList();
        registry.addRecipeCategories(
                new RitualRecipeCatagory(jeiHelpers.getGuiHelper())
        );

        registry.addRecipeHandlers(
                new RitualRecipeHandler()
        );
        RitualRecipeRecipe test = new RitualRecipeRecipe();
        test.inputs.add(GameRegistry.makeItemStack("dormantSoulstone", 0, 1, "attuned:1b,owner:\"Eladkay\""));
        test.inputs.add(GameRegistry.makeItemStack("passionateSoulstone", 0, 1, ""));
        /*
        RitualRecipeRecipe shardedSky1 = new RitualRecipeRecipe();
        shardedSky1.inputs.add(new ItemStack(Blocks.red_flower));
        shardedSky1.outputs.add(new ItemStack(Blocks.yellow_flower));
        */
        ritualRecipes.add(test);
        registry.addRecipes(ritualRecipes);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
    }

}
