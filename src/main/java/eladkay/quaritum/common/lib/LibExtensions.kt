package eladkay.quaritum.common.lib

/**
 * Created by Elad on 10/24/2016.
 */
fun String.capitalizeFirst(): String {
    return substring(0, 1).toUpperCase() + substring(1)
}

fun <T> T.stream(): T = this
infix operator fun Int.plus(string: String) = "$this$string"
infix operator fun String.plus(int: Int) = "$this$int"
infix operator fun Long.plus(int: Int) = int.toLong() + this
infix fun Long.equals(int: Int) = int.toLong() == this
fun <K, V> MutableMap<K, V>.putIfAbsent(key: K, value: V): V? {
    var v = get(key);
    if (v == null) {
        v = put(key, value);
    }
    return v;
}