package gp.single_tree;

import gp.individual.Individual;
import gp.tree.ImmutableNode;
import gp.tree.Node;
import gp.utils.Operator;

import java.util.ArrayList;
import java.util.List;

public record SingleTreeIndividual<T, Out>(ImmutableNode<T, ?, Out, ?, ?> tree) implements Individual<T, Out> {
    public static <Out, T> SingleTreeIndividual<T, Out> of(ImmutableNode<T, ?, Out, ?, ?> individual) {
        return new SingleTreeIndividual<>(individual);
    }

    @Override
    public Out evaluate(T terminals) {
        return this.tree.evaluate(terminals);
    }

    public static <T, Out> Operator<SingleTreeIndividual<T, Out>, SingleTreeIndividual<T, Out>> operator(
            Operator<Node<T, ?, Out, ?, ?>, ImmutableNode<T, ?, Out, ?, ?>> nodeOperator
    ) {
        return new Operator<>() {
            @Override
            public SingleTreeIndividual<T, Out> produce(List<SingleTreeIndividual<T, Out>> parents) {
                List<Node<T, ?, Out, ?, ?>> trees = new ArrayList<>(parents.size());
                for (SingleTreeIndividual<T, Out> parent : parents) {
                    trees.add(parent.tree);
                }
                return SingleTreeIndividual.of(nodeOperator.produce(trees));

            }

            @Override
            public Integer size() {
                return nodeOperator.size();
            }
        };
    }
}
