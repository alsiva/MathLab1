public class LinearSystemSolver {

    private final double defaultAccuracy;

    public LinearSystemSolver() {
        this.defaultAccuracy = 1E-5; // 1/10000
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

        Matrix a = coefficients.div(diagonal).minus(Matrix.identity(diagonal.length));
        Matrix b = freeMembers.div(diagonal);

        Matrix prev, next = b.copy();
        int iters = 0;
        do {
            prev = next.copy();
            next = b.minus(a.mult(prev)); // X(k) = B - A * X(k-1)
            iters++;
        } while (next.minus(prev).getAbsMax() > Math.abs(accuracy)); // |X(k) - X(k-1)| > |accuracy|

        Matrix errors = next.minus(prev);

        return new JacobiAnswer(next, errors, iters);
    }



}
