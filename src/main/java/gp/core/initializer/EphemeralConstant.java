package gp.core.initializer;

import utils.Preconditions;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A simple record to represent an Ephemeral constant, i.e.
 *  A terminal that is potentially different every time it is sampled.
 * @param namingFunction The function that produces a new name for the
 *  newly generated terminal
 * @param constantCreator The thing that generates the new constant.
 *   It is expected that calling `.get` should give new `R`s.
 * @param returnType The return type of the terminal.
 * @param <R> The return type of the terminal.
 */
public record EphemeralConstant<R>(
    Function<R, String> namingFunction,
    Supplier<R> constantCreator,
    Class<R> returnType
) {

    /**
     * A simple record to represent an Ephemeral constant, i.e.
     *  A terminal that is potentially different every time it is sampled.
     * @param namingFunction The function that produces a new name for the
     *  newly generated terminal
     * @param constantCreator The thing that generates the new constant.
     *   It is expected that calling `.get` should give new `R`s.
     * @param returnType The return type of the terminal.
     * @throws NullPointerException if any of the parameters are null.
     */
    public EphemeralConstant {
        Preconditions.allNonNull(namingFunction, constantCreator, returnType);
    }

    /**
     * Create an EphemeralConstant , that uses `.toString` as the naming function.
     * @param constantCreator The supplier which creates the constants.
     * @param returnType The return type of this constant.
     * @return A new EphemeralConstant.
     * @param <R> The constant type.
     */
    public static <R> EphemeralConstant<R> of(Supplier<R> constantCreator, Class<R> returnType) {
        return EphemeralConstant.of(R::toString, constantCreator, returnType);
    }

    /**
     * Create an EphemeralConstant.
     * @param namingFunction The function which provides the name for the constant.
     * @param constantCreator The supplier which creates the constants.
     * @param returnType The return type of this constant.
     * @return A new EphemeralConstant.
     * @param <R> The constant type.
     */
    static <R> EphemeralConstant<R> of(
        Function<R, String> namingFunction,
        Supplier<R> constantCreator,
        Class<R> returnType
    ) {
        return new EphemeralConstant<>(namingFunction, constantCreator, returnType);
    }

    /**
     * Create a concrete terminal from `this`,
     *  expected to produce a different value on every call
     * @return a new concrete TypedTerminal
     * @param <T> The input terminal type - effectively anything as it's not used.
     */
    public <T> TypedTerminal<T, R> instantiate() {
        final R value = constantCreator.get();
        return TypedTerminal.nonCached(
            namingFunction.apply(value), _ -> value, returnType
        );
    }
}
