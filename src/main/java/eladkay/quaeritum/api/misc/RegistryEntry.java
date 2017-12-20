package eladkay.quaeritum.api.misc;

import net.minecraft.util.registry.RegistryNamespaced;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Iterator;

/**
 * @author WireSegal
 * Created at 12:52 PM on 2/11/17.
 */
public final class RegistryEntry<K, V> {
    private final int id;
    @NotNull
    private final K key;
    @NotNull
    private final V value;

    public RegistryEntry(int id, @Nonnull K key, @Nonnull V value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }

    @NotNull
    public static <K, V> Iterator<RegistryEntry<K, V>> iterateOverRegistry(@NotNull RegistryNamespaced<K, V> registry) {
        return new Iterator<RegistryEntry<K, V>>() {
            Iterator<V> underlying = registry.iterator();

            @Override
            public boolean hasNext() {
                return underlying.hasNext();
            }

            @Override
            public RegistryEntry<K, V> next() {
                V next = underlying.next();
                K key = registry.getNameForObject(next);

                assert key != null;

                return new RegistryEntry<>(registry.getIDForObject(next), key, next);
            }
        };
    }

    public int getId() {
        return id;
    }

    @Nonnull
    public K getKey() {
        return key;
    }

    @Nonnull
    public V getValue() {
        return value;
    }

    public int component1() {
        return getId();
    }

    @NotNull
    public K component2() {
        return getKey();
    }

    @NotNull
    public V component3() {
        return getValue();
    }

    @NotNull
    @Override
    public String toString() {
        return "RegistryEntry{" +
                "id=" + id +
                ", key=" + key +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistryEntry<?, ?> that = (RegistryEntry<?, ?>) o;
        return id == that.id && key.equals(that.key) && value.equals(that.value);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + key.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
