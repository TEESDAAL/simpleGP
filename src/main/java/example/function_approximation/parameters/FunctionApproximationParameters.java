package example.function_approximation.parameters;

import gp.breeder.Breeder;
import gp.breeder.Initializer;
import gp.breeder.NaiveBreeder;
import gp.fitness.Goal;
import gp.genetic_operators.Identity;
import gp.genetic_operators.SubtreeMutation;
import gp.individual.EvaluatedIndividual;
import gp.initializers.BaseInitializer;
import gp.initializers.TypedNonTerminal;
import gp.initializers.TypedTerminal;
import gp.random.ProbabilisticElement;
import gp.random.WeightedRandomSampler;
import gp.selectors.TournamentSelection;
import gp.single_tree.SingleObjectiveEvaluator;
import gp.single_tree.SingleObjectiveFitness;
import gp.single_tree.SingleTreeIndividual;
import gp.single_tree.SingleTreeInitializer;
import gp.utils.Operator;
import gp.utils.Pair;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public interface FunctionApproximationParameters<T> extends FunctionApproximationParams<T> {
    List<TypedTerminal<T, ?>> terminals();
    List<TypedNonTerminal<?, ?>> nonTerminals();
    Stream<Pair<T, Double>> trainingData();
    Stream<Pair<T, Double>> testingData();
    double error(Stream<Double> outputs, Stream<Double> trueOutputs);
    Goal goal();
    long seed();


    default Random random() {
        return new Random(seed());
    }

    @Override
    default Initializer<SingleTreeIndividual<T, Double>> initializer() {
        return new SingleTreeInitializer<>(BaseInitializer.grow(
                this.random(),
                this.terminals(),
                this.nonTerminals(),
                this.populationSize(),
                this.maxTries(),
                this.maxDepth(),
                Double.class
        ));
    }

    default int populationSize() {
        return 100;
    }

    default int maxTries() {
        return 100;
    }

    default int maxDepth() {
        return 7;
    }

    @Override
    default SingleObjectiveEvaluator<T> trainEvaluator() {
        return SingleObjectiveEvaluator.of(
                (ind) -> this.error(
                        trainingData().map(Pair::first).map(ind::evaluate),
                        trainingData().map(Pair::second)
                ), this.goal()
        );
    }

    @Override
    default SingleObjectiveEvaluator<T> testEvaluator() {
        return SingleObjectiveEvaluator.of(
                (ind) -> this.error(
                        testingData().map(Pair::first).map(ind::evaluate),
                        testingData().map(Pair::second)
                ), this.goal()
        );
    }

    default List<ProbabilisticElement<Operator<SingleTreeIndividual<T, Double>, SingleTreeIndividual<T, Double>>>> operatorProbabilities() {
        return List.of(
                ProbabilisticElement.of(
                        0.2,
                        SingleTreeIndividual.operator(new SubtreeMutation<>(
                                this.random(), this.terminals(), this.nonTerminals(),
                                this.maxDepth(), this.maxTries()
                                )
                        )),
                ProbabilisticElement.of(0.8, new Identity<>())
        );
    }

    @Override
    default Breeder<EvaluatedIndividual<T, Double, SingleTreeIndividual<T, Double>, SingleObjectiveFitness>, SingleTreeIndividual<T, Double>> breeder() {
        return new NaiveBreeder<T, Double, SingleTreeIndividual<T, Double>, SingleObjectiveFitness>(
                new WeightedRandomSampler<>(this.operatorProbabilities(), this.random()),
                this.populationSize(),
                new TournamentSelection<>(this.random(), tournamentSize())
        );
    }

    default int tournamentSize() {
        return 7;
    }
}
