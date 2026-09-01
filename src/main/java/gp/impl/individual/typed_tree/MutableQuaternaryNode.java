package gp.impl.individual.typed_tree;

import util.Updater;
import util.typed_function.TypedQuadFunction;

import java.util.List;

public final class MutableQuaternaryNode<
        Terminals,
        A, B, C, D, R
> implements QuaternaryNode<
        Terminals,
        A, B, C, D, R,
        MutableQuaternaryNode<Terminals, A, B, C, D, R>,
        MutableNode<Terminals, ?, ?, ?>
>, MutableNonTerminal<
        Terminals, R,
        MutableQuaternaryNode<Terminals, A, B, C, D, R>,
        ImmutableQuaternaryNode<Terminals, A, B, C, D, R>
> {
    String name;
    MutableNode<Terminals, A, ?, ?> left;
    MutableNode<Terminals, B, ?, ?> middleLeft;
    MutableNode<Terminals, C, ?, ?> middleRight;
    MutableNode<Terminals, D, ?, ?> right;
    TypedQuadFunction<A, B, C, D, R> combiner;

    public MutableQuaternaryNode(
            String name,
            TypedQuadFunction<A, B, C, D, R> combiner,
            MutableNode<Terminals, A, ?, ?> left,
            MutableNode<Terminals, B, ?, ?> middleLeft,
            MutableNode<Terminals, C, ?, ?> middleRight,
            MutableNode<Terminals, D, ?, ?> right
    ) {
        this.name = name;
        this.left = left;
        this.middleLeft = middleLeft;
        this.middleRight = middleRight;
        this.right = right;
        this.combiner = combiner;
    }

    @Override
    public MutableQuaternaryNode<Terminals, A, B, C, D, R> self() {
        return this;
    }

    @Override
    public String name() {
        return this.name;
    }

    public MutableQuaternaryNode<Terminals, A, B, C, D, R> setName(
            Updater<String> updater
    ) {
        this.name = updater.newValue(this.name);
        return this;
    }

    @Override
    public MutableNode<Terminals, A, ?, ?> left() {
        return this.left;
    }

    public MutableQuaternaryNode<Terminals, A, B, C, D, R> setLeft(
            Updater<MutableNode<Terminals, A, ?, ?>> updater
    ) {
        this.left = updater.newValue(this.left);
        return this;
    }

    @Override
    public MutableNode<Terminals, B, ?, ?> middleLeft() {
        return this.middleLeft;
    }

    public MutableQuaternaryNode<Terminals, A, B, C, D, R> setMiddleLeft(
            Updater<MutableNode<Terminals, B, ?, ?>> updater
    ) {
        this.middleLeft = updater.newValue(this.middleLeft);
        return this;
    }

    @Override
    public MutableNode<Terminals, C, ?, ?> middleRight() {
        return this.middleRight;
    }

    public MutableQuaternaryNode<Terminals, A, B, C, D, R> setMiddleRight(
            Updater<MutableNode<Terminals, C, ?, ?>> updater
    ) {
        this.middleRight = updater.newValue(this.middleRight);
        return this;
    }

    @Override
    public MutableNode<Terminals, D, ?, ?> right() {
        return this.right;
    }

    public MutableQuaternaryNode<Terminals, A, B, C, D, R> setRight(
            Updater<MutableNode<Terminals, D, ?, ?>> updater
    ) {
        this.right = updater.newValue(this.right);
        return this;
    }

    @Override
    public TypedQuadFunction<A, B, C, D, R> combiner() {
        return combiner;
    }

    public MutableQuaternaryNode<Terminals, A, B, C, D, R> setCombiner(
            Updater<TypedQuadFunction<A, B, C, D, R>> updater
    ) {
        this.combiner = updater.newValue(this.combiner);
        return this;
    }


    @Override
    public List<MutableNode<Terminals, ?, ?, ?>> children() {
        return List.of(this.left, this.middleLeft, this.middleRight, this.right);
    }

    @Override
    public MutableQuaternaryNode<Terminals, A, B, C, D, R> replaceChild(
            int index, Updater<MutableNode<Terminals, ?, ?, ?>> node
    ) {
        return switch (index) {
            case 0 -> this.setLeft(
                    left -> MutableNode.<Terminals, A>returnTypeCompatible(
                            node.newValue(left), this.combiner.leftType()
                    )
            );
            case 1 -> this.setMiddleLeft(
                    middleLeft -> MutableNode.<Terminals, B>returnTypeCompatible(
                            node.newValue(middleLeft), this.combiner.middleLeftType()
                    )
            );
            case 2 -> this.setMiddleRight(
                    middleRight -> MutableNode.<Terminals, C>returnTypeCompatible(
                            node.newValue(middleRight), this.combiner.middleRightType()
                    )
            );
            case 3 -> this.setRight(
                    right -> MutableNode.<Terminals, D>returnTypeCompatible(
                            node.newValue(right), this.combiner.rightType()
                    )
            );
            default -> throw new IllegalArgumentException(
                    "Cannot set index " + index + "only supports indices 0,1,2"
            );
        };
    }
}
