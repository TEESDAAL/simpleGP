package gp.impl.individual.tree.compiled_representation;

import gp.core.individual.Individual;
import gp.impl.individual.tree.*;
import utils.operators.Operator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/// This class will be awful to debug, if you need to then go through the uncompiled node.
@SuppressWarnings({"rawtypes", "unchecked"})
public final class CompiledIndividual<Terminals, Output> implements Individual<Terminals, Output> {
    final int maximumArity;
    final Instruction[] program;

    CompiledIndividual(int maximumArity, Instruction[] program) {
        this.maximumArity = maximumArity;
        this.program = program;
    }

    public static <Output, Terminals, Input> CompiledIndividual<Terminals,Output> compile(
            ImmutableNode<Terminals,Input,Output,?,?> tree
    ) {
        // Size is computed upon construction
        final List<Instruction<?,?>> instructions = new ArrayList<>(tree.size());
        final int[] currentDepth = {0};
        final int[] maxStackDepth = {0};
        tree.extract(new TreeExtractor<Void>() {
            @Override
            public Void terminal(Terminal<?, ?> node) {
                instructions.add(new Instruction<>(node.extractor(), true));

                currentDepth[0] += 1;
                maxStackDepth[0] = Math.max(
                        maxStackDepth[0],
                        currentDepth[0]
                );
                return null;
            }

            @Override
            public Void nonTerminal(NonTerminal<?, ?, ?, ?> node) {
                for (final var child : node.children()) {
                    child.extract(this);
                }
                instructions.add(new Instruction<>(node.function(), false));

                // consume children, produce result
                currentDepth[0] -= node.numChildren() - 1;

                maxStackDepth[0] = Math.max(
                        maxStackDepth[0],
                        currentDepth[0]
                );

                return null;
            }
        });

        return new CompiledIndividual<>(maxStackDepth[0], instructions.toArray(Instruction[]::new));
    }


    @Override
    public Output evaluate(Terminals terminals) {
        final Object[] terminalAsArray = new Object[] {terminals};
        final Deque<Object> executionStack = new ArrayDeque<>(maximumArity);

        for (final Instruction instruction : program) {
            final Operator op = instruction.operator();
            if (instruction.isTerminal()) {
                executionStack.push(op.produce(terminalAsArray));
            } else {
                final int arity = op.arity();
                final Object[] args = new Object[arity];
                for (int i=0;i<arity;i++) {
                    args[i] = executionStack.pop();
                }
                executionStack.push(
                        op.produce(args)
                );

            }
        }
        assert executionStack.size() == 1;
        return (Output) executionStack.poll();
    }
}

// opIndex -ve if terminal
record Instruction<I, O>(Operator<I, O> operator, boolean isTerminal) {}
