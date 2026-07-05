package example.function_approximation.parameters;

import gp.core.initializers.TypedNonTerminal;
import utils.operators.BinaryOperator;
import utils.operators.Operator;
import utils.operators.UnaryOperator;

import java.util.Arrays;
import java.util.List;

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

    private final String symbol;
    private final Operator<Double, Double> op;

    TypedNonTerminal<Double, Double> into() {
        return new TypedNonTerminal<>(this.symbol, op, Double.class, Double.class);
    }

    public static List<TypedNonTerminal<Double, Double>> all() {
        return Arrays.stream(DoubleNonTerminals.values())
                .map(DoubleNonTerminals::into)
                .toList();
    }

    DoubleNonTerminals(String symbol, BinaryOperator<Double, Double> o) {
        this.symbol = symbol;
        this.op = o;
    }
    DoubleNonTerminals(String symbol, UnaryOperator<Double, Double> o) {
        this.symbol = symbol;
        this.op = o;
    }

}
