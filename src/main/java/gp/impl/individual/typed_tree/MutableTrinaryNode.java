package gp.impl.individual.typed_tree;

import utils.Updater;
import utils.typed_functions.TypedTriFunction;

import java.util.List;

public final class MutableTrinaryNode<Terminals, A, B, C, R>
    implements TrinaryNode<
        Terminals,
        A, B, C, R,
        MutableTrinaryNode<Terminals, A, B, C, R>,
        MutableNode<Terminals, ?, ?, ?>
>, MutableNonTerminal<
    Terminals, R,
    MutableTrinaryNode<Terminals, A, B, C, R>,
    ImmutableTrinaryNode<Terminals, A, B, C, R>
> {
    String name;
    MutableNode<Terminals, A, ?, ?> left;
    MutableNode<Terminals, B, ?, ?> middle;
    MutableNode<Terminals, C, ?, ?> right;
    TypedTriFunction<A, B, C, R> combiner;

    public MutableTrinaryNode(
        String name,
        TypedTriFunction<A, B, C, R> combiner,
        MutableNode<Terminals, A, ?, ?> left,
        MutableNode<Terminals, B, ?, ?> middle,
        MutableNode<Terminals, C, ?, ?> right
    ) {
        this.name = name;
        this.left = left;
        this.middle = middle;
        this.right = right;
        this.combiner = combiner;
    }

    @Override
    public MutableTrinaryNode<Terminals, A, B, C, R> self() {
        return this;
    }

    @Override
    public String name() {
        return this.name;
    }

    public MutableTrinaryNode<Terminals, A, B, C, R> setName(Updater<String> updater) {
        this.name = updater.newValue(this.name);
        return this;
    }

    @Override
    public MutableNode<Terminals, A, ?, ?> left() {
        return this.left;
    }

    public MutableTrinaryNode<Terminals, A, B, C, R> setLeft(
        Updater<MutableNode<Terminals, A, ?, ?>> updater
    ) {
        this.left = updater.newValue(this.left);
        return this;
    }

    @Override
    public MutableNode<Terminals, B, ?, ?> middle() {
        return this.middle;
    }

    public MutableTrinaryNode<Terminals, A, B, C, R> setMiddle(
        Updater<MutableNode<Terminals, B, ?, ?>> updater
    ) {
        this.middle = updater.newValue(this.middle);
        return this;
    }

    @Override
    public MutableNode<Terminals, C, ?, ?> right() {
        return this.right;
    }

    public MutableTrinaryNode<Terminals, A, B, C, R> setRight(
        Updater<MutableNode<Terminals, C, ?, ?>> updater
    ) {
        this.right = updater.newValue(this.right);
        return this;
    }

    @Override
    public TypedTriFunction<A, B, C, R> combiner() {
        return combiner;
    }

    public MutableTrinaryNode<Terminals, A, B, C, R> setCombiner(
        Updater<TypedTriFunction<A, B, C, R>> updater
    ) {
        this.combiner = updater.newValue(this.combiner);
        return this;
    }


    @Override
    public List<MutableNode<Terminals, ?, ?, ?>> children() {
        return List.of(left(), middle(), right());
    }

    @Override
    public MutableTrinaryNode<Terminals, A, B, C, R> replaceChild(
        int index, Updater<MutableNode<Terminals, ?, ?, ?>> node
    ) {
        return switch (index) {
            case 0 -> this.setLeft(
                left -> MutableNode.<Terminals, A>returnTypeCompatible(
                    node.newValue(left), this.combiner.leftType()
                )
            );
            case 1 -> this.setMiddle(
                middle -> MutableNode.<Terminals, B>returnTypeCompatible(
                    node.newValue(middle), this.combiner.middleType()
                )
            );
            case 2 -> this.setRight(
                right -> MutableNode.<Terminals, C>returnTypeCompatible(
                    node.newValue(right), this.combiner.rightType()
                )
            );
            default -> throw new IllegalArgumentException(
                "Cannot set index "+index+"only supports indices 0,1,2"
            );
        };
    }
}
