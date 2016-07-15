package eladkay.quaritum.common.compat.mt;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.api.rituals.PositionedBlockChalk;
import eladkay.quaritum.api.rituals.RitualRegistry;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

@ZenClass("mods.quaritum.Blueprint")
public class DiagramMT {
    @ZenMethod
    public static void addRecipe(String name, IItemStack[] input, IItemStack output, boolean onPlayers, int drain, int rarity, int[][] chalks) {
        MineTweakerAPI.apply(new Add(new DiagramCrafting(name, CraftTweaker.getStacks(input), MineTweakerMC.getItemStack(output), getListOfChalks(chalks), drain, onPlayers, rarity, drain > 0)));
    }
    private static List<PositionedBlock> getListOfChalks(int[][] chalks) {
        List<PositionedBlock> ret = Lists.newArrayList();
        for(int[] chal : chalks)
            ret.add(new PositionedBlockChalk(EnumDyeColor.values()[chal[0]], new BlockPos(chal[1], chal[2], chal[3])));
        return ret;
    }

    private static class Add implements IUndoableAction {
        private final DiagramCrafting recipe;

        public Add(DiagramCrafting recipe) {
            this.recipe = recipe;
        }

        @Override
        public void apply() {
            RitualRegistry.registerDiagram(recipe, recipe.getUnlocalizedName());
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            for (IDiagram r : RitualRegistry.getDiagramList()) {
                if (r.getUnlocalizedName().equals(recipe.getUnlocalizedName())) {
                    RitualRegistry.getDiagramList().remove(r);
                    break;
                }
            }

        }

        @Override
        public String describe() {
            return "Adding Diagram " + recipe.getUnlocalizedName();
        }

        @Override
        public String describeUndo() {
            return "Removing Diagram " + recipe.getUnlocalizedName();
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(MineTweakerMC.getItemStack(output)));
    }

    private static class Remove implements IUndoableAction {
        private final ItemStack output;
        private IDiagram recipe;

        public Remove(ItemStack output) {
            this.output = output;
        }

        @Override
        public void apply() {
            for (IDiagram r : RitualRegistry.getDiagramList()) {
                if (r.getUnlocalizedName().equals(recipe.getUnlocalizedName())) {
                    recipe = r;
                    break;
                }
            }

            RitualRegistry.getDiagramList().remove(recipe);
        }

        @Override
        public boolean canUndo() {
            return recipe != null;
        }

        @Override
        public void undo() {
            RitualRegistry.registerDiagram(recipe, recipe.getUnlocalizedName());
        }

        @Override
        public String describe() {
            return "Removing Diagram Recipe for " + output.getDisplayName();
        }

        @Override
        public String describeUndo() {
            return "Restoring Diagram Recipe for " + output.getDisplayName();
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }
}
