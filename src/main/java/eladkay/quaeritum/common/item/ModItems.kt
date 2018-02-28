package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.core.LibrarianLib
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.base.item.ItemModBook
import com.teamwizardry.librarianlib.features.base.item.ItemModSeed
import com.teamwizardry.librarianlib.features.gui.provided.book.hierarchy.book.Book
import eladkay.quaeritum.common.ItemPassionDrive
import eladkay.quaeritum.common.ItemTwinDrive
import eladkay.quaeritum.common.ItemVibrancyDrive
import eladkay.quaeritum.common.block.ModBlocks
import eladkay.quaeritum.common.item.misc.*
import eladkay.quaeritum.common.item.soulstones.*
import eladkay.quaeritum.common.rituals.diagrams.ItemStarMap
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList

object ModItems {

    val dormant = ItemDormantSoulstone()
    val awakened = ItemAwakenedSoulstone()
    val passionate = ItemPassionateSoulstone()
    val vibrant = ItemVibrantSoulstone()
    val passive = ItemWorldSoulstone()
    val attuned = ItemAttunedSoulstone()
    val fertilizer = ItemFertilizer()
    val worldBlade = ItemWorldBlade()
    val picture = ItemPicture()
    val resource = ItemResource()
    val worldShield = ItemWorldShield()
    val essence = ItemEssence()
    val scroll = ItemContractScroll()
    val codex = ItemCodex()
    val reagentBag = ItemReagentBag()
    val evoker = ItemEvoker()
    val resourceSeed = ItemModSeed("ironheart_seed", ModBlocks.ironCrop).makeSeedBehavior()
    val wakingBlossom = ItemWakingBlossom()
    val opium = ItemOpium()
    val starMap = ItemStarMap()
    val tempestArc = ItemTempestArc()
    val passionDrive = ItemPassionDrive()
    val vibrancyDrive = ItemVibrancyDrive()
    val twinDrive = ItemTwinDrive()
    val oathScar = ItemOathScar()

    val book = ItemModBook("book", Book("book"))

    val hiddenBook = object : ItemMod("riftbook") {
        override fun getSubItems(tab: CreativeTabs, subItems: NonNullList<ItemStack>) {
            // NO-OP
        }
    }

    lateinit var debug: ItemDebug
    lateinit var hollower: ItemHollower
    lateinit var placer: ItemPlacer

    init {
        if (LibrarianLib.DEV_ENVIRONMENT) {
            debug = ItemDebug()
            hollower = ItemHollower()
            placer = ItemPlacer()
        }
    }
}
