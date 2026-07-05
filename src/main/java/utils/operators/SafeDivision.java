package utils.operators;

public class SafeDivision implements BinaryOperator<Double, Double>{
    @Override
    public Double produce(Double parent1, Double parent2) {
        if (parent2 == 0) {
            return 1.0;
        }
        return parent1 / parent2;
    }
}
