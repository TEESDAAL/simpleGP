package gp.impl.individual.lineage;

public sealed interface Tracked extends Lineage permits Descendant, Progenitor  {
    @Override
    default boolean isTracked() {
        return true;
    }
}
