package gp.tree;

import gp.utils.UnaryOperator;

public final class MutableTerminal<Terminals, Output> implements
        Terminal<Terminals, Output>,
        MutableNode<
                Terminals, Terminals, Output,
        MutableTerminal<Terminals, Output>,
        ImmutableTerminal<Terminals, Output>
        > {

    private UnaryOperator<Terminals, Output> extractor;
    private final Class<Output> returnType;

    public MutableTerminal(UnaryOperator<Terminals, Output> extractor, Class<Output> returnType) {
        this.extractor = extractor;
        this.returnType = returnType;
    }

    @Override
    public UnaryOperator<Terminals, Output> extractor() {
        return this.extractor;
    }

    public MutableTerminal<Terminals, Output> setExtractor(UnaryOperator<Terminals, Output> extractor) {
        this.extractor = extractor;
        return this;
    }

    @Override
    public Class<Output> returnType() {
        return returnType;
    }



}

