public class JacobiAnswer {

    private Matrix roots;

    public Matrix getRoots() {
        return roots;
    }

    private Matrix errors;

    public Matrix getErrors() {
        return errors;
    }

    private int iterations;

    public int getIterations() {
        return iterations;
    }


    public JacobiAnswer(Matrix roots, Matrix errors, int iterations) {
        this.roots = roots;
        this.errors = errors;
        this.iterations = iterations;
    }


}
