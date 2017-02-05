package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.LibrarianLib
import eladkay.quaeritum.common.item.chalk.ItemChalk
import eladkay.quaeritum.common.item.chalk.ItemChalkTempest
import eladkay.quaeritum.common.item.misc.*
import eladkay.quaeritum.common.item.soulstones.*
import eladkay.quaeritum.common.item.spells.ItemRepulsionCirclet
import eladkay.quaeritum.common.item.spells.ItemWindwalkerCharm

object ModItems {

    val dormant: ItemDormantSoulstone
    val awakened: ItemAwakenedSoulstone
    val passionate: ItemPassionateSoulstone
    val passive: ItemWorldSoulstone
    val attuned: ItemAttunedSoulstone
    val chalk: ItemChalk
    val fertilizer: ItemFertilizer
    val altas: ItemReagentAtlas
    val riftmakerPart: ItemRiftmakerPart
    val worldBlade: ItemWorldBlade
    val picture: ItemPicture
    val book: ItemBook
    val tempest: ItemChalkTempest
    val soulEvoker: ItemSoulEvoker
    val repulsor: ItemRepulsionCirclet
    val windwalker: ItemWindwalkerCharm
    val resource: ItemResource
    val worldShield: ItemWorldShield

    lateinit var debug: ItemDebug
    lateinit var hollower: ItemHollower
    lateinit var placer: ItemPlacer

    init {
        dormant = ItemDormantSoulstone()
        awakened = ItemAwakenedSoulstone()
        passionate = ItemPassionateSoulstone()
        passive = ItemWorldSoulstone()
        attuned = ItemAttunedSoulstone()
        chalk = ItemChalk()
        fertilizer = ItemFertilizer()
        altas = ItemReagentAtlas()
        riftmakerPart = ItemRiftmakerPart()
        worldBlade = ItemWorldBlade()
        picture = ItemPicture()
        tempest = ItemChalkTempest()
        book = ItemBook()
        soulEvoker = ItemSoulEvoker()
        repulsor = ItemRepulsionCirclet()
        windwalker = ItemWindwalkerCharm()
        resource = ItemResource()
        worldShield = ItemWorldShield()

        if (LibrarianLib.DEV_ENVIRONMENT) {
            debug = ItemDebug()
            hollower = ItemHollower()
            placer = ItemPlacer()
        }


    }
}
