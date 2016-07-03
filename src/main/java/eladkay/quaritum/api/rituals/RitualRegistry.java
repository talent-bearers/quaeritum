package eladkay.quaritum.api.rituals;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class RitualRegistry {
    public static BiMap<String, IDiagram> mapDiagrams = HashBiMap.create();
    public static BiMap<String, IWork> mapWorks = HashBiMap.create();

    public static IDiagram registerDiagram(IDiagram ritual, String name) {
        FMLLog.info("Registering diagram \"" + name + "\".");

        if (!mapDiagrams.containsKey(name)) {
            mapDiagrams.put(name, ritual);
        } else {
            FMLLog.warning("Diagram \"" + name + "\" registered twice. Report this to the author of " + Loader.instance().activeModContainer().getModId() + ".");
            return null;
        }

        return ritual;
    }

    public static IWork registerWork(IWork ritual, String name) {
        FMLLog.info("Registering work \"" + name + "\".");

        if (!mapWorks.containsKey(name)) {
            mapWorks.put(name, ritual);
        } else {
            FMLLog.warning("Work \"" + name + "\" registered twice. Report this to the author of " + Loader.instance().activeModContainer().getModId() + ".");
            return null;
        }

        return ritual;
    }

    public static Collection<IDiagram> getDiagramList() {
        return mapDiagrams.values();
    }

    public static Collection<IWork> getWorkList() {
        return mapWorks.values();
    }

    public static IDiagram getDiagramByName(String name) {
        return mapDiagrams.get(name);
    }

    public static IWork getWorkByName(String name) {
        return mapWorks.get(name);
    }

    public static String getRitualName(IDiagram diagram) {
        return mapDiagrams.inverse().get(diagram);
    }

    public static String getWorkName(IWork work) {
        return mapWorks.inverse().get(work);
    }
}
