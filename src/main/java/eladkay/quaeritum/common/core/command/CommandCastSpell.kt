package eladkay.quaeritum.common.core.command

import eladkay.quaeritum.api.spell.EnumSpellElement
import eladkay.quaeritum.api.spell.SpellParser
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.command.WrongUsageException
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import java.util.*

/**
 * @author WireSegal
 * Created at 8:12 PM on 7/10/17.
 */
object CommandCastSpell : CommandBase() {
    override fun getName(): String {
        return "q-cast"
    }

    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<out String>) {
        if (args.isEmpty()) throw WrongUsageException(getUsage(sender))

        val player = getPlayer(server, sender, args[0])

        val elements = args.slice(1 until args.size).flatMap(this::breakString).toTypedArray()

        SpellParser(elements).cast(player)
    }

    fun breakString(input: String): List<EnumSpellElement> {
        if (input.length == 1)
            return listOf(EnumSpellElement.values().firstOrNull { it.representation.toString() == input }
                    ?: throw CommandException("quaeritum.command.is_not_element", input))

        val enumDirect = EnumSpellElement.values().firstOrNull { it.name.toLowerCase(Locale.ROOT) == input }
        if (enumDirect != null) return listOf(enumDirect)

        val list = mutableListOf<EnumSpellElement>()
        for (i in input)
            list.addAll(breakString(i.toString()))
        return list
    }

    override fun getUsage(sender: ICommandSender): String {
        return "quaeritum.command.usage"
    }

    override fun getTabCompletions(server: MinecraftServer, sender: ICommandSender, args: Array<out String>, targetPos: BlockPos?): List<String> {
        return if (args.size == 1) getListOfStringsMatchingLastWord(args, *server.onlinePlayerNames) else EnumSpellElement.values().map { it.representation.toString() }
    }
}
