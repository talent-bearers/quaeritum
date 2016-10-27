package eladkay.quaeritum.common.compat.mt

import com.google.common.collect.Lists
import eladkay.quaeritum.api.rituals.IDiagram
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.api.rituals.PositionedBlockChalk
import eladkay.quaeritum.api.rituals.RitualRegistry
import minetweaker.IUndoableAction
import minetweaker.MineTweakerAPI
import minetweaker.api.item.IItemStack
import minetweaker.api.minecraft.MineTweakerMC
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import stanhebben.zenscript.annotations.ZenClass
import stanhebben.zenscript.annotations.ZenMethod

@ZenClass("mods.quaeritum.Blueprint")
object DiagramMT {
    @ZenMethod
    @JvmStatic
    fun addRecipe(name: String, input: Array<IItemStack>, output: IItemStack, onPlayers: Boolean, drain: Int, rarity: Int, chalks: Array<IntArray>) {
        MineTweakerAPI.apply(Add(DiagramCrafting(name, CraftTweaker.getStacks(input), MineTweakerMC.getItemStack(output), getListOfChalks(chalks), drain, onPlayers, rarity, drain > 0)))
    }

    private fun getListOfChalks(chalks: Array<IntArray>): List<PositionedBlock> {
        val ret = Lists.newArrayList<PositionedBlock>()
        for (chal in chalks)
            ret.add(PositionedBlockChalk(EnumDyeColor.values()[chal[0]], BlockPos(chal[1], chal[2], chal[3])))
        return ret
    }

    @ZenMethod
    fun removeRecipe(output: IItemStack) {
        MineTweakerAPI.apply(Remove(MineTweakerMC.getItemStack(output)))
    }

    private class Add(private val recipe: DiagramCrafting) : IUndoableAction {

        override fun apply() {
            RitualRegistry.registerDiagram(recipe, recipe.unlocalizedName)
        }

        override fun canUndo(): Boolean {
            return true
        }

        override fun undo() {
            for (r in RitualRegistry.getDiagramList()) {
                if (r.unlocalizedName == recipe.unlocalizedName) {
                    RitualRegistry.getDiagramList().remove(r)
                    break
                }
            }

        }

        override fun describe(): String {
            return "Adding Diagram " + recipe.unlocalizedName
        }

        override fun describeUndo(): String {
            return "Removing Diagram " + recipe.unlocalizedName
        }

        override fun getOverrideKey(): Any? {
            return null
        }
    }

    private class Remove(private val output: ItemStack) : IUndoableAction {
        private var recipe: IDiagram? = null

        override fun apply() {
            for (r in RitualRegistry.getDiagramList()) {
                if (r.unlocalizedName == recipe!!.unlocalizedName) {
                    recipe = r
                    break
                }
            }

            RitualRegistry.getDiagramList().remove(recipe)
        }

        override fun canUndo(): Boolean {
            return recipe != null
        }

        override fun undo() {
            RitualRegistry.registerDiagram(recipe, recipe!!.unlocalizedName)
        }

        override fun describe(): String {
            return "Removing Diagram Recipe for " + output.displayName
        }

        override fun describeUndo(): String {
            return "Restoring Diagram Recipe for " + output.displayName
        }

        override fun getOverrideKey(): Any? {
            return null
        }
    }
}
