package utils;

import java.util.List;
import java.util.function.Function;

public final class ArrayUtils {
    public static <T, R> R[] mapInPlaceInvalidatingOldReferences(T[] array, Function<T, R> f) {
        for (int i = 0; i < array.length; i++) {
            ((Object[]) array)[i] = f.apply(array[i]);
        }
        //noinspection unchecked
        return (R[]) array;
    }
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T, R> R[] mapInPlaceInvalidatingOldReferences(List<T> list, Function<T, R> f) {
        for (int i = 0; i < list.size(); i++) {
            ((List) list).set(i, f.apply(list.get(i)));
        }
        return (R[]) list.toArray();
    }

    public static <T> T[] mapInPlaces(T[] array, Function<T, T> f) {
        for (int i = 0; i < array.length; i++) {
            array[i] = f.apply(array[i]);
        }
        return array;
    }

}
