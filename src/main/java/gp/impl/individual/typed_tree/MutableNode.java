package gp.impl.individual.typed_tree;

import util.Updater;

public non-sealed interface MutableNode<
        Terminals, R,
        Self extends MutableNode<Terminals, R, Self, Immutable>,
        Immutable extends ImmutableNode<Terminals, R, Immutable, Self>
> extends Node<Terminals, R, Self, Immutable, Self> {

    static <Terminals, T> MutableNode<Terminals, T, ?, ?> returnTypeCompatible(
            MutableNode<Terminals, ?, ?, ?> node,
            Class<T> type
    ) {
        if (!type.isAssignableFrom(node.returnType())) {
            throw new IllegalArgumentException(
                    "Required node which returned a value compatible with "
                            + type + " got node returning "
                            + node.returnType() + " instead"
            );
        }
        //noinspection unchecked
        return (MutableNode<Terminals, T, ?, ?>) node;
    }


    Self setName(Updater<String> updater);
}
