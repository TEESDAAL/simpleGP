package util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class IndividualSet<T> {
    final Map<T, T> cache = new ConcurrentHashMap<>();

    /**
     * @param <T> The type of the element inside the cache.
     * @return a new empty cache of type T.
     */
    public static <T> IndividualSet<T> empty() {
        return new IndividualSet<>();
    }

    /**
     * @param element The element to check if it is inside the cache.
     * @return true if the element is in the cache.
     */
    public boolean seen(T element) {
        return cache.containsKey(element);
    }

    /**
     * Try `maxTries` times to generate a T or until we find one that
     * is unseen to this cache and then return it.
     *
     * @param getter   The producer of T
     * @param maxTries The maximum number of times we can call `getter.get()`.
     * @return A hopefully unique individual.
     */
    public T repeatUntilAbsent(Supplier<T> getter, int maxTries) {
        for (int i = 1; i < maxTries; i++) {
            final T t = getter.get();
            if (!cache.containsKey(t)) {
                cache.put(t, t);
                return t;
            }
        }
        return cache.computeIfAbsent(
                getter.get(),
                i -> i
        );
    }

    /**
     * Get a unique individual from the getter.
     * If this is not possible it will loop forever
     *
     * @param getter The supplier of T
     * @return A unique T
     */
    public T repeatForeverUntilAbsent(Supplier<T> getter) {
        while (true) {
            final T t = getter.get();
            if (!cache.containsKey(t)) {
                cache.put(t, t);
                return t;
            }
        }
    }

    /**
     * Get the cached value that is equivalent to element if it exists,
     * otherwise insert it into the cache and return it.
     *
     * @param element The element to return from the cache.
     * @return The cached version of element, otherwise element.
     */
    public T getOrInsert(T element) {
        return cache.computeIfAbsent(element, t -> t);
    }
}


