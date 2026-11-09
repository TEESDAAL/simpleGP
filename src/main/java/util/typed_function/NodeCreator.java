package util.typed_function;

import gp.impl.individual.testTree.ImmutableNode;

public interface NodeCreator<Terminals> {
    <R> ImmutableNode<Terminals, R, ?> createIndividual(Class<R> returnType);
}
