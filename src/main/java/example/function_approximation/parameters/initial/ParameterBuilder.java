package example.function_approximation.parameters.initial;

import example.function_approximation.parameters.FunctionApproximationParameters;
import gp.Population;
import gp.core.breeder.Breeder;
import gp.core.assessor.Assessor;
import gp.core.fitness.SingleObjectiveFitness;
import gp.core.individual.AssessedIndividual;
import gp.core.initializer.Initialiser;
import gp.core.statistic.Statistic;
import gp.impl.individual.SingleTreeIndividual;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class ParameterBuilder<X, Y> {
    private Initialiser<SingleTreeIndividual<X, Y>> initialiser;
    private Breeder<
            AssessedIndividual<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness>,
            SingleTreeIndividual<X, Y>
    > breeder;
    private Assessor<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness> trainAssessor;
    private Assessor<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness> testAssessor;
    private final List<Statistic<AssessedIndividual<
        X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness
    >, ?>> statistics = new ArrayList<>();

    public static <X, Y> ParameterBuilder<X, Y> of() {
        return new ParameterBuilder<>();
    }

    public ParameterBuilder<X, Y> initializer(Initialiser<SingleTreeIndividual<X, Y>> initialiser) {
        this.initialiser = initialiser;
        return this;
    }

    public ParameterBuilder<X, Y> breeder(
            Breeder<AssessedIndividual<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness>, SingleTreeIndividual<X, Y>> breeder
    ) {
        this.breeder = breeder;
        return this;
    }

    public ParameterBuilder<X, Y> trainEvaluator(
        Assessor<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness> assessor
    ) {
        this.trainAssessor = assessor;
        return this;
    }

    public ParameterBuilder<X, Y> testEvaluator(
        Assessor<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness> assessor
    ) {
        this.testAssessor = assessor;
        return this;
    }
    public ParameterBuilder<X, Y> addStatistic(
        Statistic<AssessedIndividual<
                    X, Y,
                    SingleTreeIndividual<X, Y>,
                    SingleObjectiveFitness
                >, ?> statistic
    ) {
        this.statistics.add(statistic);
        return this;
    }

    public FunctionApproximationParameters<
            X, Y,
            SingleTreeIndividual<X, Y>,
            Assessor<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness>
    > build() {
        Map.of(
                "Initializer", this.initialiser,
                "Breeder", this.breeder,
                "Train Evaluator", this.trainAssessor,
                "Test Evaluator", this.testAssessor
        ).forEach((key, v) -> Objects.requireNonNull(
                v,
                "Cannot build parameters with unset" + key
        ));


        final ParameterBuilder<X, Y> self = this;
        return new FunctionApproximationParameters<>() {
            @Override
            public Initialiser<SingleTreeIndividual<X, Y>> initializer() {
                return self.initialiser;
            }

            @Override
            public Breeder<AssessedIndividual<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness>, SingleTreeIndividual<X, Y>> breeder() {
                return self.breeder;
            }

            @Override
            public Assessor<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness> trainEvaluator() {
                return self.trainAssessor;
            }

            @Override
            public Assessor<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness> testEvaluator() {
                return self.testAssessor;
            }

            @Override
            public Statistic<AssessedIndividual<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness>, ?> scoreLogger() {
                final var statistics = List.copyOf(self.statistics);
                return new Statistic<AssessedIndividual<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness>, Void>() {
                    @Override
                    public Void statistic(Population<AssessedIndividual<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness>> population) {
                        statistics.forEach(s -> s.sideEffect(population));
                        return null;
                    }

                    @Override
                    public Consumer<Void> log() {
                        return _ -> {};
                    }
                };
            }

            @Override
            public int numGenerations() {
                return 50;
            }
        };
    }
}
