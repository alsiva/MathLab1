package edu.ifmo.math;

public class LinearSystemSolver {

    private final double accuracy;

    public LinearSystemSolver(double accuracy) {
        this.accuracy = accuracy;
    }

    public SeidelAnswer solve(Matrix matrix) {
        Matrix permutedMatrix = matrix.doRowPermutation();
        Matrix coefficients = permutedMatrix.getMatrixCoefficients();
        Matrix freeMembers = permutedMatrix.getMatrixFreeMembers();
        double[] diagonal = coefficients.getDiagonalArray();

        Matrix prev;  // Нулевая итерация
        Matrix next = freeMembers.div(diagonal);

        int totalIterations = 0;
        do {
            prev = next.copy();

            for (int i = 0; i < next.getRowCount(); ++i) { //Пробегаемся по каждому иксу
                double freeSum = freeMembers.get(i,0) / coefficients.get(i, i);

                double keyPlusOneSum = 0;
                for (int j = 0; j <= i-1; ++j) { //k+1 иксы
                    keyPlusOneSum += next.get(j, 0) * coefficients.get(i,j) / coefficients.get(i,i);
                }

                double keySum = 0;
                for (int j = i + 1; j < next.getRowCount(); ++j) { //k иксы
                    keySum += prev.get(j, 0) * coefficients.get(i,j) / coefficients.get(i,i);
                }

                next.set(i, 0, freeSum - keyPlusOneSum - keySum);
            }

            totalIterations++;
        } while (next.minus(prev).getAbsMax() > Math.abs(accuracy)); // |X(k) - X(k-1)| > |accuracy|

        Matrix errors = next.minus(prev);

        return new SeidelAnswer(next, errors, totalIterations);
    }
}
