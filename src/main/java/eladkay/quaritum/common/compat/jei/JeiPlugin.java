package eladkay.quaritum.common.compat.jei;

import amerifrance.guideapi.api.GuideAPI;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.book.ItemModBook;
import eladkay.quaritum.common.item.ModItems;
import mezz.jei.api.*;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

@JEIPlugin
public class JeiPlugin implements IModPlugin {

    private IJeiHelpers jeiHelpers;


    @Override
    public void register(IModRegistry registry) {

        jeiHelpers = registry.getJeiHelpers();
        //jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModItems.picture, 1, OreDictionary.WILDCARD_VALUE));
        //No need because nooping in the getsubitems
        jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(GuideAPI.guideBook, 1, ItemModBook.meta()));
        registry.addDescription(ts(ModItems.book), "quaritum.bookdescjei");
        registry.addRecipeCategories(new DiagramRecipeCatagory());
        registry.addRecipeCategoryCraftingItem(ts(ModBlocks.blueprint), "quaritum:diagram");
    }

    private ItemStack ts(Item i) {
        return new ItemStack(i);
    }

    private ItemStack ts(Block i) {
        return new ItemStack(i);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

    }

    private class DiagramRecipeCatagory implements IRecipeCategory {

        @Nonnull
        @Override
        public String getUid() {
            return "quaritum:diagram";
        }

        @Nonnull
        @Override
        public String getTitle() {
            return "Diagram Crafting";
        }

        @Nonnull
        @Override
        public IDrawable getBackground() {
            return new IDrawable() {
                @Override
                public int getWidth() {
                    return 32;
                }

                @Override
                public int getHeight() {
                    return 32;
                }

                @Override
                public void draw(@Nonnull Minecraft minecraft) {

                }

                @Override
                public void draw(@Nonnull Minecraft minecraft, int xOffset, int yOffset) {

                }
            };
        }

        @Override
        public void drawExtras(@Nonnull Minecraft minecraft) {

        }

        @Override
        public void drawAnimations(@Nonnull Minecraft minecraft) {

        }

        @Override
        public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {

        }
    }

}

