package gp.tree;

import gp.utils.Operator;

import java.util.List;


public record ImmutableNonTerminal<Terminals, Input, Output>(
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

