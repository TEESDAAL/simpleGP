package gp.impl.individual.typed_tree;

import utils.Preconditions;
import utils.TriFunction;
import utils.typed_functions.TypedBiFunction;
import utils.typed_functions.TypedFunction;
import utils.typed_functions.TypedTriFunction;

import java.util.function.BiFunction;
import java.util.function.Function;

public record NamedNodeFunction<R, F extends NodeFunction<R, F>>(
    String name, F function
) {
    public NamedNodeFunction {
        Preconditions.allNonNull(name, function);
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
            TypedTriFunction.of(nonTerminal, leftType, middleType, rightType, returnType)
        );
    }
}
