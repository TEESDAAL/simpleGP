package gp.genetic_operators;

import gp.utils.UnaryOperator;

public class Identity<T> implements UnaryOperator<T, T> {
    @Override
    public T produce(T parent) {
        return parent;
    }
}
