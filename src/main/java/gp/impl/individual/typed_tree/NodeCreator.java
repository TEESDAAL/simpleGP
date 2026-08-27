package gp.impl.individual.typed_tree;

import java.util.Optional;

public interface NodeCreator<Terminals> {
    <R> Optional<ImmutableNode<Terminals, R, ?, ?>> get(Class<R> returnType);
}
