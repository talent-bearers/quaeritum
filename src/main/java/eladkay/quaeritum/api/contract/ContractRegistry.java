package eladkay.quaeritum.api.contract;

import com.google.common.collect.HashBiMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * @author WireSegal
 *         Created at 9:19 PM on 2/10/17.
 */
public final class ContractRegistry {
    private static final HashBiMap<String, IContractOath> oaths = HashBiMap.create();

    @NotNull
    public static IContractOath registerOath(@NotNull String id, @NotNull IContractOath oath) {
        oaths.put(id, oath);
        return oath;
    }

    @Nullable
    public static IContractOath getOathFromId(@NotNull String id) {
        return oaths.get(id);
    }

    @Nullable
    public static String getIdFromOath(@NotNull IContractOath oath) {
        return oaths.inverse().get(oath);
    }

    @NotNull
    public static Collection<String> getAllOathIds() {
        return oaths.keySet();
    }

    @NotNull
    public static Collection<IContractOath> getAllOaths() {
        return oaths.values();
    }
}
