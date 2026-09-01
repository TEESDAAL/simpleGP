package util.typed_function;

import gp.impl.individual.typed_tree.ImmutableNode;
import gp.impl.individual.typed_tree.NodeCreator;
import gp.impl.individual.typed_tree.NodeFunction;
import gp.impl.individual.typed_tree.UnaryNode;
import util.Preconditions;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public interface TypedFunction<A, R> extends
        NodeFunction<R, TypedFunction<A, R>>,
        Function<A, R>
{
    static <A, R> TypedFunction<A, R> of(
            Function<A, R> f,
            Class<A> inputType,
            Class<R> returnType
    ) {
        return new TypedFunctionImpl<>(f, inputType, returnType);
    }

    @Override
    default int arity() {
        return 1;
    }

    Class<A> inputType();

    @Override
    default <Terminals> Optional<ImmutableNode<Terminals, R, ?, ?>> toNode(
            String name, NodeCreator<Terminals> childSupplier
    ) {
        return childSupplier.get(inputType()).map(
                n -> UnaryNode.of(
                        name,
                        this,
                        n
                )
        );
    }

    @Override
    default List<Class<?>> inputTypes() {
        return List.of(inputType());
    }
}

record TypedFunctionImpl<A, R>(
        Function<A, R> function,
        Class<A> inputType,
        Class<R> returnType
) implements TypedFunction<A, R> {
    TypedFunctionImpl {
        Preconditions.allNonNull(Map.of(
                "function", function,
                "inputType", inputType,
                "returnType", returnType
        ));
    }

    @Override
    public R apply(A a) {
        return function.apply(a);
    }

    @Override
    public TypedFunction<A, R> cached() {
        final Map<A, R> cache = new ConcurrentHashMap<>();
        return new TypedFunctionImpl<>(
                a -> cache.computeIfAbsent(a, this),
                inputType,
                returnType
        );
    }
}
