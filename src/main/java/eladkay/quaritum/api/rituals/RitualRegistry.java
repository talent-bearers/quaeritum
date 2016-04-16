package eladkay.quaritum.api.rituals;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RitualRegistry {
    public static Map<String, IRitual> map = new HashMap<>();
    public static Map<String, List<ItemStack>> requiredItems = new HashMap<>();

    public static IRitual registerRitual(IRitual ritual, String name)  throws Exception {
        FMLLog.info("Registering ritual \"" + name + "\".");

        if (!map.containsKey(name)) {
            map.put(name, ritual);
            if (ritual.getRequiredItems() != null && ritual.getRequiredItems().size() != 0)
                requiredItems.put(name, ritual.getRequiredItems());
            else {
                FMLLog.warning("Ritual \"" + name + "\" has no identifier items. Report this to the author of " + Loader.instance().activeModContainer().getModId() + ".");
                return null;
            }
        } else {
            FMLLog.warning("Ritual \"" + name + "\" registered twice. Report this to the author of " + Loader.instance().activeModContainer().getModId() + ".");
            return null;
        }

        return ritual;
    }

    public static Collection<IRitual> getRitualList() {
        return map.values();
    }

    public static IRitual getRitualByName(String name) {
        return map.get(name);
    }
}
