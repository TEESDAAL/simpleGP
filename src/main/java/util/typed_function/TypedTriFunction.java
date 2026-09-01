package util.typed_function;

import gp.impl.individual.typed_tree.ImmutableNode;
import gp.impl.individual.typed_tree.NodeCreator;
import gp.impl.individual.typed_tree.NodeFunction;
import gp.impl.individual.typed_tree.TrinaryNode;
import util.OptionUtils;
import util.Preconditions;
import util.TriFunction;
import util.Triple;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public interface TypedTriFunction<A, B, C, R>
        extends NodeFunction<R, TypedTriFunction<A, B, C, R>>,
        TriFunction<A, B, C, R>
{
    static <A, B, C, R> TypedTriFunction<A, B, C, R> of(
            TriFunction<A, B, C, R> triFunction,
            Class<A> leftType,
            Class<B> middleType,
            Class<C> rightType,
            Class<R> returnType
    ) {
        return new TypedTriFunctionImpl<>(
                triFunction,
                leftType,
                middleType,
                rightType,
                returnType
        );
    }

    @Override
    default int arity() {
        return 3;
    }

    Class<A> leftType();

    Class<B> middleType();

    Class<C> rightType();

    @Override
    default <Terminals> Optional<ImmutableNode<Terminals, R, ?, ?>> toNode(
            String name,
            NodeCreator<Terminals> childSupplier
    ) {
        return OptionUtils.and(
                childSupplier.get(leftType()),
                () -> childSupplier.get(middleType()),
                () -> childSupplier.get(rightType()),
                (l, m, r) -> TrinaryNode.of(
                        name,
                        this,
                        l,
                        m,
                        r
                )
        );
    }

    @Override
    default List<Class<?>> inputTypes() {
        return List.of(leftType(), rightType());
    }

    @Override
    default TypedTriFunction<A, B, C, R> cached() {
        final Map<Triple<A, B, C>, R> cache = new ConcurrentHashMap<>();
        return new TypedTriFunctionImpl<>(
                (a, b, c) -> cache.computeIfAbsent(
                        Triple.of(a, b, c),
                        p -> p.reduce(this)
                ),
                leftType(),
                middleType(),
                rightType(),
                returnType()
        );
    }
}

record TypedTriFunctionImpl<A, B, C, R>(
        TriFunction<A, B, C, R> triFunction,
        Class<A> leftType,
        Class<B> middleType,
        Class<C> rightType,
        Class<R> returnType
) implements TypedTriFunction<A, B, C, R> {
    TypedTriFunctionImpl {
        Preconditions.allNonNull(Map.of(
                "triFunction", triFunction,
                "leftType",  leftType,
                "middleType", middleType,
                "rightType", rightType,
                "returnType", returnType
        ));
    }

    @Override
    public R apply(A a, B b, C c) {
        return triFunction.apply(a, b, c);
    }
}
