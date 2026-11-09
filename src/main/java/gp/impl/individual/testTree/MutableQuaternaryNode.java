package gp.impl.individual.testTree;

import gp.impl.individual.testTree.visitor.TreeVisitor;
import util.Updater;
import util.typed_function.TypedQuadFunction;

import java.util.List;

public final class MutableQuaternaryNode<T, A, B, C, D, R> implements QuaternaryNode<
        T, A, B, C, D, R, Mutable
>, MutableNonTerminal<T, R> {
    private String name;
    private MutableNode<T, A, ?> left;
    private MutableNode<T, B, ?> middleLeft;
    private MutableNode<T, C, ?> middleRight;
    private MutableNode<T, D, ?> right;
    private TypedQuadFunction<A, B, C, D, R> combiner;

    public MutableQuaternaryNode(
            String name,
            TypedQuadFunction<A, B, C, D, R> combiner,
            MutableNode<T, A, ?> left,
            MutableNode<T, B, ?> middleLeft,
            MutableNode<T, C, ?> middleRight,
            MutableNode<T, D, ?> right
    ) {
        this.name = name;
        this.left = left;
        this.middleLeft = middleLeft;
        this.middleRight = middleRight;
        this.right = right;
        this.combiner = combiner;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public <U> U visit(TreeVisitor<T, R, U> visitor) {
        return visitor.mutableNonTerminal(this);
    }

    public MutableQuaternaryNode<T, A, B, C, D, R> updateName(
            Updater<String> updater
    ) {
        this.name = updater.newValue(this.name);
        return this;
    }

    @Override
    public MutableNode<T, A, ?> left() {
        return this.left;
    }

    public MutableQuaternaryNode<T, A, B, C, D, R> updateLeft(
            Updater<MutableNode<T, A, ?>> updater
    ) {
        this.left = updater.newValue(this.left);
        return this;
    }

    @Override
    public MutableNode<T, B, ?> middleLeft() {
        return this.middleLeft;
    }

    public MutableQuaternaryNode<T, A, B, C, D, R> updateMiddleLeft(
            Updater<MutableNode<T, B, ?>> updater
    ) {
        this.middleLeft = updater.newValue(this.middleLeft);
        return this;
    }

    @Override
    public MutableNode<T, C, ?> middleRight() {
        return this.middleRight;
    }

    public MutableQuaternaryNode<T, A, B, C, D, R> updateMiddleRight(
            Updater<MutableNode<T, C, ?>> updater
    ) {
        this.middleRight = updater.newValue(this.middleRight);
        return this;
    }

    @Override
    public MutableNode<T, D, ?> right() {
        return this.right;
    }

    public MutableQuaternaryNode<T, A, B, C, D, R> updateRight(
            Updater<MutableNode<T, D, ?>> updater
    ) {
        this.right = updater.newValue(this.right);
        return this;
    }

    @Override
    public TypedQuadFunction<A, B, C, D, R> combiner() {
        return combiner;
    }

    public MutableQuaternaryNode<T, A, B, C, D, R> updateCombiner(
            Updater<TypedQuadFunction<A, B, C, D, R>> updater
    ) {
        this.combiner = updater.newValue(this.combiner);
        return this;
    }


    @Override
    public MutableQuaternaryNode<T, A, B, C, D, R> replaceChild(
            int index,
            Updater<MutableNode<T, ?, ?>> node
    ) {
        return switch (index) {
            case 0 -> this.updateLeft(
                    left -> MutableNode.returnTypeCompatible(
                            node.newValue(left), this.combiner.leftType()
                    )
            );
            case 1 -> this.updateMiddleLeft(
                    middleLeft -> MutableNode.returnTypeCompatible(
                            node.newValue(middleLeft), this.combiner.middleLeftType()
                    )
            );
            case 2 -> this.updateMiddleRight(
                    middleRight -> MutableNode.returnTypeCompatible(
                            node.newValue(middleRight), this.combiner.middleRightType()
                    )
            );
            case 3 -> this.updateRight(
                    right -> MutableNode.returnTypeCompatible(
                            node.newValue(right), this.combiner.rightType()
                    )
            );
            default -> throw new IllegalArgumentException(
                    "Cannot update index " + index + "only supports indices 0,1,2"
            );
        };
    }


    public ImmutableQuaternaryNode<T, A, B, C, D, R> immutableCopy() {
        return new ImmutableQuaternaryNode<>(
                this.name(),
                this.combiner(),
                this.left().immutableCopy(),
                this.middleLeft().immutableCopy(),
                this.middleRight().immutableCopy(),
                this.right().immutableCopy()
        );
    }

    @Override
    public List<MutableNode<T, ?, ?>> children() {
        return List.of(this.left, this.middleLeft, this.middleRight, this.right);
    }
}
