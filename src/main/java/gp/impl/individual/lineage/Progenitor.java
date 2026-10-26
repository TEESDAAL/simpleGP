package gp.impl.individual.lineage;

import gp.core.individual.Individual;
import gp.core.initializer.Initialiser;

import java.util.Optional;
public non-sealed interface Progenitor<
        Ind extends Individual<?, ?>,
        Init extends Initialiser<Ind>
> extends Tracked<Ind> {
    Ind current();
    Init source();

    @Override
    default Optional<Progenitor<?, ?>> asProgenitor() {
        return Optional.of(this);
    }
    @Override
    default Optional<Descendant<?, ?, ?>> asDescendant() {
        return Optional.empty();
    }
}
record ProgenitorImpl<
        Ind extends Individual<?, ?>,
        Init extends Initialiser<Ind>
>(
        Ind current,
        Init source
) implements Progenitor<Ind, Init> {}
