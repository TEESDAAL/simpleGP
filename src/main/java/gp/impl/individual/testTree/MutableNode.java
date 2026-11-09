package gp.impl.individual.testTree;

public sealed interface MutableNode<Terminals, R, T extends NodeType> extends Node<
        Terminals, R, Mutable, T
> permits MutableTerminal, MutableNonTerminal {
    static <Terminals, R> MutableNode<Terminals, R, ?> returnTypeCompatible(
            MutableNode<Terminals, ?, ?> node,
            Class<R> returnType
    ) {
        if (!returnType.isAssignableFrom(node.returnType())) {
            throw new IllegalArgumentException(
                    "Required node which returned a value compatible with "
                            + returnType + " got node returning "
                            + node.returnType() + " instead"
            );
        }
        //noinspection unchecked
        return (MutableNode<Terminals, R, ?>) node;
    }
}
