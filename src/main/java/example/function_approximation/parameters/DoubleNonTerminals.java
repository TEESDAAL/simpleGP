package example.function_approximation.parameters;

import gp.impl.individual.typed_tree.NamedNodeFunction;
import utils.typed_functions.TypedBiFunction;
import utils.typed_functions.TypedFunction;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum DoubleNonTerminals {
    NEG("neg", a -> -a),
    MIN("min", Math::min),
    MAX("max", Math::max),
    MUL("%", (a, b) -> a*b),
    DIV("%", (a, b) -> {
        if (b == 0) {return 1.0;}
        return a / b;
    }),
    PLUS("+", Double::sum),
    SUB("-", (a, b) -> a - b);

    private final NamedNodeFunction<Double, ?> op;

    public static List<NamedNodeFunction<Double, ?>> all() {
        return Arrays.stream(DoubleNonTerminals.values())
            .<NamedNodeFunction<Double, ?>>map(t -> t.op)
            .toList();
    }

    DoubleNonTerminals(String symbol, BiFunction<Double, Double, Double> f) {
        this.op = new NamedNodeFunction<>(
            symbol,
            TypedBiFunction.of(
                f, Double.class, Double.class, Double.class
            )
        );
    }

    DoubleNonTerminals(String symbol, Function<Double, Double> f) {
        this.op = new NamedNodeFunction<>(
            symbol,
            TypedFunction.of(
                f, Double.class, Double.class
            )
        );
    }

}
