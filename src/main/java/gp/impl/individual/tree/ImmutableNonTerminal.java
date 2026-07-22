package gp.impl.individual.tree;

import utils.operators.Operator;

import java.util.List;
import java.util.Objects;


/**
 * An immutable non-terminal node record implementation.
 */
public final class ImmutableNonTerminal<Terminals, Input, Output> extends NonTerminal<
    Terminals, Input, Output,
    ImmutableNode<Terminals, ?, Input, ?, ?>
    > implements ImmutableNode<
    Terminals, Input, Output,
    ImmutableNonTerminal<Terminals, Input, Output>,
    MutableNonTerminal<Terminals, Input, Output>
    > {
    final int maximumArity;
    /**
     * @param name        The name of this non-terminal node
     * @param function    The operator function
     * @param children    The immutable child nodes
     * @param inputType   The input type class
     * @param returnType  The output type class
     */
    public ImmutableNonTerminal(
        String name,
        Operator<Input, Output> function,
        ImmutableNode<Terminals, ?, Input, ?, ?>[] children,
        Class<Input> inputType,
        Class<Output> returnType
    ) {
        super(name, function, children, inputType, returnType);
        this.maximumArity = super.maximumArity();
    }



    @Override
    protected int maximumArity() {
        return maximumArity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, function, children, inputType, returnType);
    }

    @Override
    public String toString() {
        return "ImmutableNonTerminal[" +
            "name=" + name + ", " +
            "function=" + function + ", " +
            "children=" + children + ", " +
            "inputType=" + inputType + ", " +
            "returnType=" + returnType + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;

        final ImmutableNonTerminal<?, ?, ?> that = (ImmutableNonTerminal<?, ?, ?>) o;
        return this.maximumArity == that.maximumArity
            && name.equals(that.name)
            && function.equals(that.function)
            && children.equals(that.children)
            && inputType.equals(that.inputType)
            && returnType.equals(that.returnType);
    }
}

