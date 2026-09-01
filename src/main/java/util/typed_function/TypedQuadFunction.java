package util.typed_function;

import gp.impl.individual.typed_tree.ImmutableNode;
import gp.impl.individual.typed_tree.NodeCreator;
import gp.impl.individual.typed_tree.NodeFunction;
import gp.impl.individual.typed_tree.QuaternaryNode;
import util.OptionUtils;
import util.QuadFunction;
import util.Preconditions;
import util.Quadruple;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public interface TypedQuadFunction<A, B, C, D, R>
        extends NodeFunction<R, TypedQuadFunction<A, B, C, D, R>>,
        QuadFunction<A, B, C, D, R>
{
    static <A, B, C, D, R> TypedQuadFunction<A, B, C, D, R> of(
            QuadFunction<A, B, C, D, R> f,
            Class<A> leftType,
            Class<B> middleLeftType,
            Class<C> middleRightType,
            Class<D> rightType,
            Class<R> returnType
    ) {
        return new TypedQuadFunctionImpl<>(
                f,
                leftType,
                middleLeftType,
                middleRightType,
                rightType,
                returnType
        );
    }

    @Override
    default int arity() {
        return 4;
    }

    Class<A> leftType();

    Class<B> middleLeftType();

    Class<C> middleRightType();

    Class<D> rightType();

    @Override
    default <Terminals> Optional<ImmutableNode<Terminals, R, ?, ?>> toNode(
            String name, NodeCreator<Terminals> childSupplier
    ) {
        return OptionUtils.and(
                childSupplier.get(leftType()),
                () -> childSupplier.get(middleLeftType()),
                () -> childSupplier.get(middleRightType()),
                () -> childSupplier.get(rightType()),
                (left, middleLeft, middleRight, right) -> QuaternaryNode.of(
                        name,
                        this,
                        left,
                        middleLeft,
                        middleRight,
                        right
                )
        );
    }

    @Override
    default List<Class<?>> inputTypes() {
        return List.of(leftType(), middleLeftType(), middleRightType(), rightType());
    }

    @Override
    default TypedQuadFunction<A, B, C, D, R> cached() {
        final Map<Quadruple<A, B, C, D>, R> cache = new ConcurrentHashMap<>();
        return new TypedQuadFunctionImpl<>(
                (a, b, c, d) -> cache.computeIfAbsent(
                        Quadruple.of(a, b, c, d), p -> p.reduce(this)
                ),
                leftType(),
                middleLeftType(),
                middleRightType(),
                rightType(),
                returnType()
        );
    }
}

record TypedQuadFunctionImpl<A, B, C, D, R>(
        QuadFunction<A, B, C, D, R> quadFunction,
        Class<A> leftType,
        Class<B> middleLeftType,
        Class<C> middleRightType,
        Class<D> rightType,
        Class<R> returnType
) implements TypedQuadFunction<A, B, C, D, R> {
    TypedQuadFunctionImpl {
        Preconditions.allNonNull(Map.of(
                "quadFunction", quadFunction,
                "leftType", leftType,
                "middleLeftType", middleLeftType,
                "middleRightType", middleRightType,
                "rightType", rightType,
                "returnType", returnType
        ));
    }

    @Override
    public R apply(A a, B b, C c, D d) {
        return quadFunction.apply(a, b, c, d);
    }
}
