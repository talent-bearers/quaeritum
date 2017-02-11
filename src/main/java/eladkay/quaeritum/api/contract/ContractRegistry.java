package eladkay.quaeritum.api.contract;

import eladkay.quaeritum.api.misc.RegistryEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

/**
 * @author WireSegal
 *         Created at 9:19 PM on 2/10/17.
 */
public final class ContractRegistry {
    private static final RegistryNamespaced<ResourceLocation, IContractOath> oaths = new RegistryNamespaced<>();
    private static int lastId = 0;

    @NotNull
    public static IContractOath registerOath(@NotNull ResourceLocation id, @NotNull IContractOath oath) {
        oaths.register(lastId++, id, oath);
        return oath;
    }

    @Nullable
    public static IContractOath getOathFromName(@NotNull ResourceLocation id) {
        return oaths.getObject(id);
    }

    @Nullable
    public static IContractOath getOathFromId(@NotNull int id) {
        return oaths.getObjectById(id);
    }

    @Nullable
    public static ResourceLocation getIdFromOath(@NotNull IContractOath oath) {
        return oaths.getNameForObject(oath);
    }

    @NotNull
    public static Iterator<RegistryEntry<ResourceLocation, IContractOath>> getOaths() {
        return RegistryEntry.iterateOverRegistry(oaths);
    }

}
