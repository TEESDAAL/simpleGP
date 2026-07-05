package example.function_approximation;

import example.function_approximation.parameters.initial.ParameterBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.Pair;
import utils.random.SourceOfRandom;

import java.util.ArrayList;
import java.util.List;

public class TestConsistency {
    @Test
    public void testConsistency() {
        var finalPops = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            System.out.println("Run "+i);
            SourceOfRandom rand = new SourceOfRandom(42);
            System.out.println(rand.get().nextInt());
            var params = ParameterBuilder.<Pair<Double, Double>, Double>of()
                .initializer(new DefaultInitializer(rand.get()))
                .breeder(new DefaultBreeder(rand.get()))
                .testEvaluator(new DefaultEvaluator(rand.get(), 10))
                .trainEvaluator(new DefaultEvaluator(rand.get(), 600))
                .build();

            finalPops.add(new FunctionApproximator<>(
                params.initializer(),
                params.trainEvaluator(),
                params.breeder(),
                params.testEvaluator(),
                params.scoreLogger()
            ).train(5));
        }

        Assertions.assertTrue(allEqual(finalPops), "Final populations are not equal");
    }

    public <E> boolean allEqual(List<E> list) {
        for (E s : list) {
            if (!s.equals(list.getFirst()))
                return false;
        }
        return true;
    }
}
