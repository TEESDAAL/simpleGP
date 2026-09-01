package util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public enum Cache {;
    public static <R, T> Function<T, R> cacheFunction(Function<T,R> function) {
        final Map<T, R> cache = new ConcurrentHashMap<>();
        return t -> cache.computeIfAbsent(t, function);
    }
}
