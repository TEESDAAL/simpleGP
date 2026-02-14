package gp.breeder;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface Parallelizeable {
    boolean shouldParallelize();

    /**
     * @return the amount of jobs to run in serial
     * E.g. if this returns 5, the parallelize operation will group the stream into n groups of 5
     * Then it will run n task in parallel, and each of those n tasks will run the operation in 5 times in series.
     */
    default int batchSize() {
        return 1;
    }

    default <T> Stream<T> generate(Supplier<T> supplier) {
        return Stream.generate(
                () -> Stream.generate(supplier).limit(batchSize())
        ).parallel().flatMap(Function.identity());
    }
    default <T> Stream<T> generateN(Supplier<T> supplier, int n) {
        return this.parallelize(IntStream.range(0, n).boxed(), ignored -> supplier.get());
    }

    default <T, U> Stream<U> parallelize(Stream<T> stream, Function<? super T, ? extends U> mapper) {
        if (shouldParallelize()) {
            if (batchSize() == 1) {
                return stream.parallel().map(mapper);
            }
            return group(stream, this.batchSize())
                    .parallel()
                    .flatMap(l-> l.map(mapper));
        }

        return stream.map(mapper);
    }

    static <T> Stream<Stream<T>> group(Stream<T> stream, int groupSize) {
       Iterator<T> source = stream.iterator();
       return Stream.generate(() -> {
            List<T> list = new ArrayList<>();

            for (int i = 0; i < groupSize; i++) {
                if (!source.hasNext()) {
                    break;
                }
                list.add(source.next());
            }
            return list;
        }).takeWhile(list -> !list.isEmpty())
               .map(Collection::stream);
    }
}


