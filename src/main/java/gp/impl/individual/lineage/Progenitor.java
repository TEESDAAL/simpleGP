package gp.impl.individual.lineage;

import gp.core.initializer.Initialiser;

import java.util.Optional;
public non-sealed interface Progenitor<
        Init extends Initialiser<?>
> extends Tracked {
    Init source();

    @Override
    default Optional<Progenitor<?>> asProgenitor() {
        return Optional.of(this);
    }
    @Override
    default Optional<Descendant<?, ?>> asDescendant() {
        return Optional.empty();
    }
}
record ProgenitorImpl<
        Init extends Initialiser<?>
>(
        Init source
) implements Progenitor<Init> {}
