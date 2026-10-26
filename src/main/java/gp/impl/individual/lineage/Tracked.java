package gp.impl.individual.lineage;

import gp.core.individual.LineagedIndividual;

public sealed interface Tracked<Ind extends LineagedIndividual<?, ?, ?, ?>> extends Lineage permits Descendant, Progenitor  {
    Ind current();
}
