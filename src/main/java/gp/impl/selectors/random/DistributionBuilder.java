package gp.impl.selectors.random;

import utils.random.RandomSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DistributionBuilder<E> {
    E defaultElement = null;
    private double accumulatedProb = 0.0;
    ArrayList<ProbabilisticElement<E>> distribution = new ArrayList<>();

    public DistributionBuilder<E> addElement(ProbabilisticElement<E> element) {
        if (this.accumulatedProb + element.probability() > 1.0) {
            throw new IllegalArgumentException("After adding element " + element + "the distribution would sum to > 1.0");
        }
        this.accumulatedProb += element.probability();
        this.distribution.add(element);
        return this;
    }

    public DistributionBuilder<E> addElement(double probability, E element) {
        this.distribution.add(ProbabilisticElement.of(probability, element));
        return this;
    }

    public DistributionBuilder<E> addDefault(E element) {
        if (this.defaultElement != null) {
            throw new IllegalArgumentException("Each distribution can only have one default element");
        }
        this.defaultElement = element;
        return this;
    }

    public WeightedRandomSampler<E> toSampler(RandomSource random) {
        if (this.accumulatedProb < 1.0 && this.defaultElement == null) {
            throw new IllegalStateException("Tried to construct a probability distribution which doesn't sum to one.");
        }
        return WeightedRandomSampler.of(
                this.distribution,
                random
        );
    }

    public List<ProbabilisticElement<E>> toList() {
        if (this.accumulatedProb < 1.0 && this.defaultElement == null) {
            throw new IllegalStateException("Tried to construct a probability distribution which doesn't sum to one.");
        }
        return Stream.concat(
                this.distribution.stream(),
                Stream.of(ProbabilisticElement.of(1.0 - accumulatedProb, this.defaultElement))
        ).toList();
    }
}
