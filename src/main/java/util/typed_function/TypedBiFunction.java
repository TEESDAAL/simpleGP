package util.typed_function;

import gp.impl.individual.typed_tree.BinaryNode;
import gp.impl.individual.typed_tree.ImmutableNode;
import gp.impl.individual.typed_tree.NodeCreator;
import gp.impl.individual.typed_tree.NodeFunction;
import util.OptionUtils;
import util.Pair;
import util.Preconditions;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public interface TypedBiFunction<A, B, R> extends NodeFunction<
        R, TypedBiFunction<A, B, R>
>, BiFunction<A, B, R> {
    static <A, B, R> TypedBiFunction<A, B, R> of(
            BiFunction<A, B, R> f,
            Class<A> leftType,
            Class<B> rightType,
            Class<R> returnType
    ) {
        return new TypedBiFunctionImpl<>(f, leftType, rightType, returnType);
    }

    @Override
    default int arity() {
        return 2;
    }

    Class<A> leftType();

    Class<B> rightType();

    @Override
    default <Terminals> Optional<ImmutableNode<Terminals, R, ?, ?>> toNode(
            String name, NodeCreator<Terminals> childSupplier
    ) {
        return OptionUtils.and(
                childSupplier.get(leftType()),
                () -> childSupplier.get(rightType()),
                (l, r) -> BinaryNode.of(
                        name,
                        this,
                        l,
                        r
                )
        );
    }

    @Override
    default List<Class<?>> inputTypes() {
        return List.of(leftType(), rightType());
    }

    @Override
    default TypedBiFunction<A, B, R> cached() {
        final Map<Pair<A, B>, R> cache = new ConcurrentHashMap<>();
        return new TypedBiFunctionImpl<>(
                (a, b) -> cache.computeIfAbsent(Pair.of(a, b), p -> p.reduce(this)),
                leftType(),
                rightType(),
                returnType()
        );
    }
}

record TypedBiFunctionImpl<A, B, R>(
        BiFunction<A, B, R> triFunction,
        Class<A> leftType,
        Class<B> rightType,
        Class<R> returnType
) implements TypedBiFunction<A, B, R> {
    TypedBiFunctionImpl {
        Preconditions.allNonNull(Map.of(
                "triFunction", triFunction,
                "leftType", leftType,
                "rightType", rightType,
                "returnType", returnType
        ));
    }

    @Override
    public R apply(A a, B b) {
        return triFunction.apply(a, b);
    }
}
