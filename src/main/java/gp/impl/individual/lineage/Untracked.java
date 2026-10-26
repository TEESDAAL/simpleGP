package gp.impl.individual.lineage;

import java.util.Optional;

public final class Untracked implements Lineage {
    private Untracked() {}
    static Untracked instance = new Untracked();
    @Override
    public Optional<Progenitor<?, ?>> asProgenitor() {
        return Optional.empty();
    }

    @Override
    public Optional<Descendant<?, ?, ?>> asDescendant() {
        return Optional.empty();
    }
}
