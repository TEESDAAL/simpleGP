package gp.impl.individual.testTree;

public sealed interface ImmutableNonTerminal<T, R> extends ImmutableNode<
        T, R, NonTerminal
>, NonTerminalNode<T, R, Immutable> permits
       ImmutableUnaryNode, ImmutableBinaryNode, ImmutableTrinaryNode,
       ImmutableQuaternaryNode {}
