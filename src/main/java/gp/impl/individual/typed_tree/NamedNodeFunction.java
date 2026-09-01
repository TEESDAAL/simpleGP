package gp.impl.individual.typed_tree;

import util.Preconditions;
import util.QuadFunction;
import util.TriFunction;
import util.typed_function.TypedBiFunction;
import util.typed_function.TypedFunction;
import util.typed_function.TypedQuadFunction;
import util.typed_function.TypedTriFunction;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A record representing a named typed function.
 * @param name The name of the function.
 * @param function The particular typed function.
 * @param <R> The return type of the function.
 * @param <F> The type of the function.
 */
public record NamedNodeFunction<R, F extends NodeFunction<R, F>>(
        String name, F function
) {
    public NamedNodeFunction {
        Preconditions.allNonNull(Map.of(
                "name", name,
                "function", function
        ));
    }

    public static <A, R> NamedNodeFunction<R, TypedFunction<A, R>> of(
            String name,
            Function<A, R> f,
            Class<A> inputType,
            Class<R> returnType
    ) {
        return new NamedNodeFunction<>(
                name,
                TypedFunction.of(f, inputType, returnType)
        );
    }

    public static <A, B, R> NamedNodeFunction<R, TypedBiFunction<A, B, R>> of(
            String name,
            BiFunction<A, B, R> f,
            Class<A> leftType,
            Class<B> rightType,
            Class<R> returnType
    ) {
        return new NamedNodeFunction<>(
                name,
                TypedBiFunction.of(f, leftType, rightType, returnType)
        );
    }

    public static <A, B, C, R> NamedNodeFunction<R, TypedTriFunction<A, B, C, R>> of(
            String name,
            TriFunction<A, B, C, R> nonTerminal,
            Class<A> leftType,
            Class<B> middleType,
            Class<C> rightType,
            Class<R> returnType
    ) {
        return new NamedNodeFunction<>(
                name,
                TypedTriFunction.of(
                        nonTerminal,
                        leftType,
                        middleType,
                        rightType,
                        returnType
                )
        );
    }

    public static <A, B, C, D, R> NamedNodeFunction<
            R,
            TypedQuadFunction<A, B, C, D, R>
    > of(
            String name,
            QuadFunction<A, B, C, D, R> nonTerminal,
            Class<A> leftType,
            Class<B> leftMiddleType,
            Class<C> rightMiddleType,
            Class<D> rightType,
            Class<R> returnType
    ) {
        return new NamedNodeFunction<>(
                name,
                TypedQuadFunction.of(
                        nonTerminal,
                        leftType,
                        leftMiddleType,
                        rightMiddleType,
                        rightType,
                        returnType
                )
        );
    }
}
