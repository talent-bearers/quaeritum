package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.LibrarianLib
import eladkay.quaeritum.common.item.chalk.ItemChalk
import eladkay.quaeritum.common.item.chalk.ItemChalkTempest
import eladkay.quaeritum.common.item.misc.*
import eladkay.quaeritum.common.item.soulstones.*

object ModItems {

    var dormant: ItemDormantSoulstone
    var awakened: ItemAwakenedSoulstone
    var passionate: ItemPassionateSoulstone
    var passive: ItemWorldSoulstone
    var oppressive: ItemOppressiveSoulstone
    var attuned: ItemAttunedSoulstone
    lateinit var debug: ItemDebug
    var chalk: ItemChalk
    var fertilizer: ItemFertilizer
    var altas: ItemReagentAtlas
    var riftmakerPart: ItemRiftmakerPart
    var worldBlade: ItemWorldBlade
    var picture: ItemPicture
    var book: ItemBook
    var transducer: ItemTransducer
    var tempest: ItemChalkTempest
    lateinit var hollower: ItemHollower
    lateinit var placer: ItemPlacer

    init {
        dormant = ItemDormantSoulstone()
        awakened = ItemAwakenedSoulstone()
        passionate = ItemPassionateSoulstone()
        passive = ItemWorldSoulstone()
        oppressive = ItemOppressiveSoulstone()
        attuned = ItemAttunedSoulstone()
        chalk = ItemChalk()
        fertilizer = ItemFertilizer()
        altas = ItemReagentAtlas()
        riftmakerPart = ItemRiftmakerPart()
        worldBlade = ItemWorldBlade()
        picture = ItemPicture()
        transducer = ItemTransducer()
        tempest = ItemChalkTempest()
        book = ItemBook()
        if (LibrarianLib.DEV_ENVIRONMENT) {
            debug = ItemDebug()
            hollower = ItemHollower()
            placer = ItemPlacer()
        }


    }
}
