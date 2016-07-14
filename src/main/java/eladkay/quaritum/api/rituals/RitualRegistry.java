package eladkay.quaritum.api.rituals;

import com.google.common.collect.HashBiMap;
import eladkay.quaritum.api.lib.LibMisc;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;

import java.util.Collection;

public final class RitualRegistry {
    public static HashBiMap<String, IDiagram> mapDiagrams = HashBiMap.create();
    public static HashBiMap<String, IWork> mapWorks = HashBiMap.create();

    public static IDiagram registerDiagram(IDiagram ritual, String name) {
        FMLLog.info("Registering diagram \"" + name + "\".");

        ritual.constructBook();
        String modid = Loader.instance().activeModContainer().getModId();

        if (!modid.equals(LibMisc.MOD_ID)) {
            ResourceLocation rl = new ResourceLocation(name);
            name = modid + ":" + rl.getResourcePath();
        }

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

        String modid = Loader.instance().activeModContainer().getModId();

        if (!modid.equals(LibMisc.MOD_ID)) {
            ResourceLocation rl = new ResourceLocation(name);
            name = modid + ":" + rl.getResourcePath();
        }

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

    public static String getDiagramName(IDiagram diagram) {
        return mapDiagrams.inverse().get(diagram);
    }

    public static String getWorkName(IWork work) {
        return mapWorks.inverse().get(work);
    }
}
