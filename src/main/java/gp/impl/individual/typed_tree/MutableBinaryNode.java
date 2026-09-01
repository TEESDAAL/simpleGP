package gp.impl.individual.typed_tree;

import util.typed_function.TypedBiFunction;
import util.Updater;

import java.util.List;

public final class MutableBinaryNode<A, B, Terminals, R> implements BinaryNode<
        A, B, Terminals, R,
        MutableBinaryNode<A, B, Terminals, R>,
        MutableNode<Terminals, ?, ?, ?>
>, MutableNonTerminal<
        Terminals, R,
        MutableBinaryNode<A, B, Terminals, R>,
        ImmutableBinaryNode<A, B, Terminals, R>
> {
    String name;
    MutableNode<Terminals, A, ?, ?> left;
    MutableNode<Terminals, B, ?, ?> right;
    TypedBiFunction<A, B, R> combiner;

    public MutableBinaryNode(
            String name,
            MutableNode<Terminals, A, ?, ?> left,
            MutableNode<Terminals, B, ?, ?> right,
            TypedBiFunction<A, B, R> combiner
    ) {
        this.name = name;
        this.left = left;
        this.right = right;
        this.combiner = combiner;
    }


    @Override
    public MutableBinaryNode<A, B, Terminals, R> self() {
        return this;
    }

    @Override
    public String name() {
        return this.name;
    }

    public MutableBinaryNode<A, B, Terminals, R> setName(Updater<String> updater) {
        this.name = updater.newValue(this.name);
        return this;
    }


    public MutableBinaryNode<A, B, Terminals, R> setLeft(
            Updater<MutableNode<Terminals, A, ?, ?>> updater
    ) {
        this.left = updater.newValue(this.left);
        return this;
    }


    public MutableBinaryNode<A, B, Terminals, R> setRight(
            Updater<MutableNode<Terminals, B, ?, ?>> updater
    ) {
        this.right = updater.newValue(this.right);
        return this;
    }

    @Override
    public MutableNode<Terminals, A, ?, ?> left() {
        return this.left;
    }

    @Override
    public MutableNode<Terminals, B, ?, ?> right() {
        return this.right;
    }

    @Override
    public TypedBiFunction<A, B, R> combiner() {
        return combiner;
    }

    public MutableBinaryNode<A, B, Terminals, R> setCombiner(
            Updater<TypedBiFunction<A, B, R>> updater
    ) {
        this.combiner = updater.newValue(this.combiner);
        return this;
    }

    @Override
    public List<MutableNode<Terminals, ?, ?, ?>> children() {
        return List.of(left(), right());
    }

    @Override
    public MutableBinaryNode<A, B, Terminals, R> replaceChild(
            int index, Updater<MutableNode<Terminals, ?, ?, ?>> node
    ) {
        return switch (index) {
            case 0 -> this.setLeft(
                    left -> MutableNode.<Terminals, A>returnTypeCompatible(
                            node.newValue(left), this.combiner.leftType()
                    )
            );
            case 1 -> this.setRight(
                    right -> MutableNode.<Terminals, B>returnTypeCompatible(
                            node.newValue(right), this.combiner.rightType()
                    )
            );
            default -> throw new IllegalArgumentException(
                    "Binary node only supports setting children of index 1, 0"
            );
        };
    }
}
