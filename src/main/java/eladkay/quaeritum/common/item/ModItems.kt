package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.core.LibrarianLib
import eladkay.quaeritum.common.block.ModBlocks
import eladkay.quaeritum.common.item.chalk.ItemChalk
import eladkay.quaeritum.common.item.chalk.ItemChalkTempest
import eladkay.quaeritum.common.item.misc.*
import eladkay.quaeritum.common.item.soulstones.*

object ModItems {

    val dormant: ItemDormantSoulstone = ItemDormantSoulstone()
    val awakened: ItemAwakenedSoulstone = ItemAwakenedSoulstone()
    val passionate: ItemPassionateSoulstone = ItemPassionateSoulstone()
    val passive: ItemWorldSoulstone = ItemWorldSoulstone()
    val attuned: ItemAttunedSoulstone = ItemAttunedSoulstone()
    val chalk: ItemChalk = ItemChalk()
    val fertilizer: ItemFertilizer = ItemFertilizer()
    val riftmakerPart: ItemRiftmakerPart = ItemRiftmakerPart()
    val worldBlade: ItemWorldBlade = ItemWorldBlade()
    val picture: ItemPicture = ItemPicture()
    val tempest: ItemChalkTempest = ItemChalkTempest()
    val resource: ItemResource = ItemResource()
    val worldShield: ItemWorldShield = ItemWorldShield()
    val essence: ItemEssence = ItemEssence()
    val scroll: ItemContractScroll = ItemContractScroll()
    val codex: ItemCodex = ItemCodex()
    val reagentBag: ItemReagentBag = ItemReagentBag()
    val evoker: ItemEvoker = ItemEvoker()
    val resourceSeed: ItemModSeed = ItemModSeed("ironheart_seed", ModBlocks.ironCrop)

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
