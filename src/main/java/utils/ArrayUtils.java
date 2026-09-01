package utils;

import java.lang.reflect.Array;
import java.util.function.Function;

public final class ArrayUtils {
    public static <T> T[] mapInPlace(T[] array, Function<T, T> f) {
        for (int i = 0; i < array.length; i++) {
            array[i] = f.apply(array[i]);
        }
        return array;
    }

    public static <T, R> R[] map(T[] array, Function<T, R> f, Class<R> type) {
        @SuppressWarnings("unchecked")
        final R[] newArr = (R[]) Array.newInstance(type, array.length);
        for (int i = 0; i < array.length; i++) {
            newArr[i] = f.apply(array[i]);
        }
        return newArr;
    }

}
