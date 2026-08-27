package utils;

import utils.random.RandomSource;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;

public enum ArrayUtils {;

    /**
     * Map an array to a new array of a different type.
     * @param array The array to map.
     * @param clazz The class of the new array.
     * @param f The function to apply to each element.
     * @return A new array of the mapped elements.
     * @param <T> The type of the elements in the input array.
     * @param <R> The type of the elements in the output array.
     */
    public static <T, R> R[] map(T[] array, Class<R> clazz, Function<T, R> f) {
        //noinspection unchecked
        final R[] arr = (R[]) Array.newInstance(clazz, array.length);
        for (int i = 0; i < array.length; i++) {
            arr[i] = f.apply(array[i]);
        }

        return arr;
    }

    /**
     * Map an array to a new array of the same type.
     * @param array The array to map.
     * @param f The function to apply to each element.
     * @return A new array of the mapped elements.
     * @param <T> The type of the elements in the array.
     */
    public static <T> T[] map(T[] array, Function<T, T> f) {
        return mapInPlace(Arrays.copyOf(array, array.length), f);
    }

    /**
     * Map an array in place.
     * @param array The array to map.
     * @param f The function to apply to each element.
     * @return A new array of the mapped elements.
     * @param <T> The type of the elements in the array.
     */
    public static <T> T[] mapInPlace(T[] array, Function<T, T> f) {
        for (int i = 0; i < array.length; i++) {
            array[i] = f.apply(array[i]);
        }
        return array;
    }

    /**
     *
     * @param array The array to search.
     * @param t The element to search for.
     * @return The index of the element.
     * @param <T> The type of the elements in the array.
     * @throws NoSuchElementException If the element is not in the array.
     */
    public static <T> int indexOf(T[] array, T t) {
        for (int i=0; i<array.length; i++) {
            if (array[i].equals(t)) {
                return i;
            }
        }

        throw new NoSuchElementException(
            t+" is not in input array: "+Arrays.toString(array)
        );
    }

    /**
     * Convert a list of bytes to a primitive byte array.
     * @param instructions The list of bytes to convert.
     * @return A primitive byte array.
     */
    public static byte[] toPrimitive(List<Byte> instructions) {
        final byte[] bytes = new byte[instructions.size()];
        for (int i=0; i<bytes.length; i++) {
            bytes[i] = instructions.get(i);
        }
        return bytes;
    }

    /**
     * Concatenate two arrays.
     * @param left The first array to concatenate.
     * @param right The second array to concatenate.
     * @return A new array that is the concatenation of the two input arrays.
     * @param <T> The type of the elements in the arrays.
     */
    public static <T> T[] concat(T[] left, T[] right) {
        final T[] array = Arrays.copyOf(left, left.length+right.length);
        System.arraycopy(right, 0, array, left.length, right.length);
        return array;
    }

    /**
     * Concatenate two arrays of different types.
     * @param left The first array to concatenate.
     * @param right The second array to concatenate.
     * @param clazz The class of the new array, which must be a parent class of both.
     * @return A new array that is the concatenation of the two input arrays.
     * @param <T> The type of the elements in the new array.
     * @param <A> The type of the elements in the first array.
     * @param <B> The type of the elements in the second array.
     */
    public static <T, A extends T, B extends T> T[] concat(
        A[] left, B[] right, Class<T> clazz
    ) {
        @SuppressWarnings("unchecked")
        final T[] array = (T[]) Array.newInstance(clazz, left.length+right.length);

        System.arraycopy(left, 0, array, 0, left.length);
        System.arraycopy(right, 0, array, left.length, right.length);
        return array;
    }

    public static <T> void forEach(T[] elements, Consumer<T> consumer) {
        for (final T t : elements) {
            consumer.accept(t);
        }
    }

    public static <T> void shuffle(T[] elements, RandomSource random) {
        /* https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle#The_modern_algorithm
        -- To shuffle an array a of n elements (indices 0..n − 1):
        for i from n − 1 down to 1 do
            j ← random integer such that 0 ≤ j ≤ i
            exchange a[j] and a[i]
         */
        for (int i=elements.length -1; i>=1; i--) {
            final int j = random.nextInt(0, i);
            ArrayUtils.swap(elements, i, j);
        }
    }

    private static <T> void swap(T[] elements, int i, int j) {
        final T temp = elements[i];
        elements[i] = elements[j];
        elements[j] = temp;
    }

    public static void shuffle(int[] elements, RandomSource random) {
        for (int i=elements.length -1; i>=1; i--) {
            final int j = random.nextInt(0, i);
            ArrayUtils.swap(elements, i, j);
        }
    }

    private static void swap(int[] elements, int i, int j) {
        final int temp = elements[i];
        elements[i] = elements[j];
        elements[j] = temp;
    }
}
