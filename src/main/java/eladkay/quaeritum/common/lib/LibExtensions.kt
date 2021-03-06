package eladkay.quaeritum.common.lib

/**
 * Created by Elad on 10/24/2016.
 */
fun String.capitalizeFirst(): String {
    return substring(0, 1).toUpperCase() + substring(1)
}

infix operator fun Int.plus(string: String) = "$this$string"
infix operator fun String.plus(int: Int) = "$this$int"
infix operator fun Long.plus(int: Int) = int.toLong() + this
infix fun Long.equals(int: Int) = int.toLong() == this
fun <K, V> K.collect(v: V) = this
fun <K, V> MutableMap<K, V>.putIfAbsent(key: K, value: V): V? {
    var v = get(key)
    if (v == null) {
        v = put(key, value)
    }
    return v
}

fun <T, V> Array<T>.add(provider: (V) -> T, array: Array<V>) {
    for (index in array.indices) this[index] = provider(array[index])
}

fun arrayOfStrings(int: Int): Array<String> {
    return Array<String>(int, { "" })
}
