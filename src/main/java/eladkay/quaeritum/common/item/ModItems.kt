package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.core.LibrarianLib
import com.teamwizardry.librarianlib.features.base.item.ItemModShield
import eladkay.quaeritum.common.block.ModBlocks
import eladkay.quaeritum.common.item.misc.*
import eladkay.quaeritum.common.item.soulstones.*

object ModItems {

    val dormant = ItemDormantSoulstone()
    val awakened = ItemAwakenedSoulstone()
    val passionate = ItemPassionateSoulstone()
    val passive = ItemWorldSoulstone()
    val attuned = ItemAttunedSoulstone()
    val fertilizer = ItemFertilizer()
    val riftmakerPart = ItemRiftmakerPart()
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
