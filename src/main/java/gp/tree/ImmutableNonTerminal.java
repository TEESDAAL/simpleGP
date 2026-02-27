package gp.tree;

import gp.utils.Operator;

import java.util.List;


/**
 * An immutable non-terminal node record implementation.
 * @param <Terminals> The type of terminal inputs
 * @param <Input> The input type
 * @param <Output> The output type
 * @param function The operator function
 * @param children The immutable child nodes
 * @param inputType The input type class
 * @param returnType The output type class
 */
public record ImmutableNonTerminal<
        Terminals,
        Input,
        Output
>(
        Operator<Input, Output> function,
        List<ImmutableNode<Terminals, ?, Input, ?, ?>> children,
        Class<Input> inputType,
        Class<Output> returnType
)
        implements NonTerminal<
        Terminals, Input, Output,
        ImmutableNode<Terminals, ?, Input, ?, ?>
        >, ImmutableNode<
        Terminals, Input, Output,
        ImmutableNonTerminal<Terminals, Input, Output>,
        MutableNonTerminal<Terminals, Input, Output>
        > {
}

