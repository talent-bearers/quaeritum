package eladkay.quaritum.api.misc;

public class NonPrimInt {
    public int value;

    public NonPrimInt(int i) {
        value = i;
    }

    public int add(int i) {
        value += i;
        return value;
    }

    public int mult(int i) {
        value *= i;
        return value;
    }

    public int div(int i) {
        value /= i;
        return value;
    }
}
