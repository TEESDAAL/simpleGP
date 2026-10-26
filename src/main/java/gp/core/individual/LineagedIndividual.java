package gp.core.individual;

import gp.impl.individual.lineage.Lineage;


/**
 * An individual with a linage.
 *
 * @param <T> the terminal type
 * @param <R> the return type
 * @param <I> the individual type
 * @param <L> the type of linage.
 */
public interface LineagedIndividual<
        T, R,
        I extends Individual<T, R>,
        L extends Lineage
> extends Individual<T, R> {
    static <T,R,I extends Individual<T, R>, L extends Lineage> LineagedIndividual<T,R,I,L> of(I individual, L lineage) {
        return new LineagedIndividualImpl<>(individual, lineage);
    }
    I individual();
    /**
     * @return The linage of this individual
     */
    L lineage();
}

record LineagedIndividualImpl<T, R, I extends Individual<T,R>, L extends Lineage<?>>(
        I individual,
        L lineage
) implements LineagedIndividual<T, R, I, L> {
    /**
     * Evaluates the wrapped individual.
     *
     * @param terminals the terminal input
     * @return the evaluation result
     */
     public R evaluate(T terminals) {
        return this.individual().evaluate(terminals);
    }
}
