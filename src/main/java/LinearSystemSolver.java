public class LinearSystemSolver {

    private final double defaultAccuracy;

    public LinearSystemSolver() {
        this.defaultAccuracy = 1E-5; // 0.00001D
    }

    public LinearSystemSolver(double defaultAccuracy) {
        this.defaultAccuracy = defaultAccuracy;
    }

    public JacobiAnswer solveByJacobi(LinearSystem linearSystem, double accuracy) {
        int[] rowIndices = linearSystem.getPermutedRowsIfPossible();

        if (rowIndices == null) {
            throw new RuntimeException("Невозможно получить диагонально доминантную матрицу с помощью перестановок строк");
        } else {
            linearSystem.doRowPermutation(rowIndices);
        }
        Matrix coefficients = linearSystem.getMatrixCoefficients();
        Matrix freeMembers = linearSystem.getMatrixFreeMembers();


        return iterate(coefficients, freeMembers, accuracy);
    }

    public JacobiAnswer iterate(Matrix coefficients, Matrix freeMembers, double accuracy) {
        double[] diagonal = coefficients.getDiagonalArray();

        Matrix prev, next = freeMembers.div(diagonal);// Нулевая итерация
        int iters = 0;
        do {
            prev = next.copy();

            for (int i = 0; i < next.getRowDimention(); ++i) { //Пробегаемся по каждому иксу

                double freeSum = freeMembers.get(i,0) / coefficients.get(i, i);

                double keyPlusOneSum = 0;
                for (int j = 0; j <= i-1; ++j) { //k+1 иксы
                    keyPlusOneSum += next.get(j, 0) * coefficients.get(i,j) / coefficients.get(i,i);
                }

                double keySum = 0;
                for (int j = i + 1; j < next.getRowDimention(); ++j) { //k иксы
                    keySum += prev.get(j, 0) * coefficients.get(i,j) / coefficients.get(i,i);
                }

                next.write(i, 0, freeSum - keyPlusOneSum - keySum);
            }

            iters++;
        } while (next.minus(prev).getAbsMax() > Math.abs(accuracy)); // |X(k) - X(k-1)| > |accuracy|

        Matrix errors = next.minus(prev);

        return new JacobiAnswer(next, errors, iters);
    }



}
