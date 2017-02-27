package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.LibrarianLib
import eladkay.quaeritum.common.item.chalk.ItemChalk
import eladkay.quaeritum.common.item.chalk.ItemChalkTempest
import eladkay.quaeritum.common.item.misc.*
import eladkay.quaeritum.common.item.soulstones.*
import eladkay.quaeritum.common.item.spells.ItemRepulsionCirclet
import eladkay.quaeritum.common.item.spells.ItemWindwalkerCharm

object ModItems {

    val dormant: ItemDormantSoulstone = ItemDormantSoulstone()
    val awakened: ItemAwakenedSoulstone = ItemAwakenedSoulstone()
    val passionate: ItemPassionateSoulstone = ItemPassionateSoulstone()
    val passive: ItemWorldSoulstone = ItemWorldSoulstone()
    val attuned: ItemAttunedSoulstone = ItemAttunedSoulstone()
    val chalk: ItemChalk = ItemChalk()
    val fertilizer: ItemFertilizer = ItemFertilizer()
    val altas: ItemReagentAtlas = ItemReagentAtlas()
    val riftmakerPart: ItemRiftmakerPart = ItemRiftmakerPart()
    val worldBlade: ItemWorldBlade = ItemWorldBlade()
    val picture: ItemPicture = ItemPicture()
    val book: ItemBook = ItemBook()
    val tempest: ItemChalkTempest = ItemChalkTempest()
    val soulEvoker: ItemSoulEvoker = ItemSoulEvoker()
    val repulsor: ItemRepulsionCirclet = ItemRepulsionCirclet()
    val windwalker: ItemWindwalkerCharm = ItemWindwalkerCharm()
    val resource: ItemResource = ItemResource()
    val worldShield: ItemWorldShield = ItemWorldShield()
    val essence: ItemEssence = ItemEssence()
    val scroll: ItemContractScroll = ItemContractScroll()

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
