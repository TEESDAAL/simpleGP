package gp.utils;

public class Mutable<T> {
    private T value;

    Mutable(T value) {
        this.value = value;
    }
    public static <T> Mutable<T> of(T value) {
        return new Mutable<>(value);
    }

    public T get() {
        return value;
    }
    public void set(T value) {
        this.value = value;
    }
}
