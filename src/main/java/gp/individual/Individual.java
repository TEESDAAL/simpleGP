package gp.individual;

public interface Individual<T, R> {
    R evaluate(T terminals);
}