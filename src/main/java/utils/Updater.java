package utils;

public interface Updater<T> {
    T newValue(T oldValue);
}
