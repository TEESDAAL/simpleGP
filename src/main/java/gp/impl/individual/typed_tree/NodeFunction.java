package gp.impl.individual.typed_tree;

import java.util.List;
import java.util.Optional;

public interface NodeFunction<R, Self extends NodeFunction<R, Self>> {
    <Terminals> Optional<ImmutableNode<Terminals, R, ?, ?>> toNode(
            String name, NodeCreator<Terminals> childSupplier
    );

    Class<R> returnType();

    List<Class<?>> inputTypes();

    int arity();

    Self cached();
}
