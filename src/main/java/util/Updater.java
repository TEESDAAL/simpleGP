package util;

public interface Updater<T> {
    T newValue(T oldValue);
}
