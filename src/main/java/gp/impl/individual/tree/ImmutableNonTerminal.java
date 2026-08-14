package gp.impl.individual.tree;

import utils.ArrayUtils;
import utils.operators.Operator;

import java.util.Arrays;
import java.util.Objects;


/**
 * An immutable non-terminal node record implementation.
 *
 * @param <Terminals> The terminals that the leaves extract from.
 * @param <Input> The input type to this node.
 * @param <Output> The output of this node.
 */
public final class ImmutableNonTerminal<Terminals, Input, Output> extends NonTerminal<
    Terminals, Input, Output,
    ImmutableNode<Terminals, ?, Input, ?, ?>
    > implements ImmutableNode<
    Terminals, Input, Output,
    ImmutableNonTerminal<Terminals, Input, Output>,
    MutableNonTerminal<Terminals, Input, Output>
    > {
    private final int maximumArity;
    private final int size;

    /**
     * @param name       The name of this non-terminal node
     * @param function   The operator function
     * @param children   The immutable child nodes
     * @param inputType  The input type class
     * @param returnType The output type class
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
        this.size = super.size();
    }

    @Override
    public ImmutableNonTerminal<Terminals, Input, Output> immutableCopy() {
        return this;
    }

    @Override
    public int maximumArity() {
        return maximumArity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            name,
            function,
            Arrays.hashCode(children),
            inputType,
            returnType
        );
    }

    @Override
    public Output evaluate(Terminals terminals) {
        return this.manualOutput(ArrayUtils.map(
            children, inputType,
            c -> c.evaluate(terminals)
        ));
    }

    @Override
    public String toString() {
        return "ImmutableNonTerminal["
            + "name=" + name + ", "
            + "function=" + function + ", "
            + "children=" + Arrays.toString(children) + ", "
            + "inputType=" + inputType + ", "
            + "returnType=" + returnType + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) { return false; }

        final ImmutableNonTerminal<?, ?, ?> that = (ImmutableNonTerminal<?, ?, ?>) o;
        return this.maximumArity == that.maximumArity
            && name.equals(that.name)
            && function.equals(that.function)
            && Arrays.equals(children, that.children)
            && inputType.equals(that.inputType)
            && returnType.equals(that.returnType);
    }

    @Override
    public int size() {
        return this.size;
    }
}
