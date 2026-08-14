package utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

public enum ArrayUtils {
    ;

    public static <T, R> R[] map(T[] array, Class<R> clazz, Function<T, R> f) {
        //noinspection unchecked
        final R[] arr = (R[]) Array.newInstance(clazz, array.length);
        for (int i = 0; i < array.length; i++) {
            arr[i] = f.apply(array[i]);
        }

        return arr;
    }

    public static <T> T[] map(T[] array, Function<T, T> f) {
        return mapInPlace(Arrays.copyOf(array, array.length), f);
    }

    public static <T> T[] mapInPlace(T[] array, Function<T, T> f) {
        for (int i = 0; i < array.length; i++) {
            array[i] = f.apply(array[i]);
        }
        return array;
    }

    public static <T> int indexOf(T[] array, T t) {
        for (int i=0; i<array.length; i++) {
            if (array[i].equals(t)) {
                return i;
            }
        }

        throw new NoSuchElementException(t+" is not in input array: "+Arrays.toString(array));
    }

    public static <T> byte byteIndexOf(T[] array, T t) {
        for (byte i=0; i<array.length; i++) {
            if (array[i].equals(t)) {
                return i;
            }
        }

        throw new NoSuchElementException(t+" is not in input array: "+Arrays.toString(array));
    }

    public static byte[] toPrimitive(List<Byte> instructions) {
        final byte[] bytes = new byte[instructions.size()];
        for (int i=0; i<bytes.length; i++) {
            bytes[i] = instructions.get(i);
        }
        return bytes;
    }

    public static <T> T[] concat(T[] left, T[] right) {
        final T[] array = Arrays.copyOf(left, left.length+right.length);
        System.arraycopy(right, 0, array, left.length, right.length);
        return array;
    }

    public static <T, A extends T, B extends T> T[] concat(A[] left, B[] right, Class<T> clazz) {
        @SuppressWarnings("unchecked")
        final T[] array = (T[]) Array.newInstance(clazz, left.length+right.length);

        System.arraycopy(left, 0, array, 0, left.length);
        System.arraycopy(right, 0, array, left.length, right.length);
        return array;
    }
}
