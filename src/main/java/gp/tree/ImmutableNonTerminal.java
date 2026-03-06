package gp.tree;

import gp.utils.operators.Operator;

import java.util.List;


/**
 * An immutable non-terminal node record implementation.
 * @param <Terminals> The type of terminal inputs
 * @param <Input> The input type
 * @param <Output> The output type
 * @param name The name of this non-terminal node
 * @param function The operator function
 * @param children The immutable child nodes
 * @param inputType The input type class
 * @param returnType The output type class
 */
public record ImmutableNonTerminal<Terminals, Input, Output>(
        String name,
        Operator<Input, Output> function,
        List<ImmutableNode<Terminals, ?, Input, ?, ?>> children,
        Class<Input> inputType,
        Class<Output> returnType
) implements NonTerminal<
            Terminals, Input, Output,
            ImmutableNode<Terminals, ?, Input, ?, ?>
        >, ImmutableNode<
            Terminals, Input, Output,
            ImmutableNonTerminal<Terminals, Input, Output>,
            MutableNonTerminal<Terminals, Input, Output>
        > {
}

