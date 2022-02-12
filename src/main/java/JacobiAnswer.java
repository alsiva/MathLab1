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

    private String pattern(double i, double roots, double errors) {
        int $ = 17 - String.valueOf((int) this.roots.getAbsMax()).length();
        return String.format("│ %02.0f │ % -20." + $ + "f │ % -20.16f │%n", i, roots, errors);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("+————+——————————————————————+——————————————————————+\n")
                .append("│ №i │  root (x)            │  infelicity (delta)  │\n")
                .append("+————+——————————————————————+——————————————————————+\n");

        for (int i = 0; i < roots.getRowDimention(); ++i) {
            builder.append(pattern(i + 1, roots.get(i, 0), errors.get(i, 0)));
        }
        builder.append("+————+——————————————————————+——————————————————————+\n")
                .append("> Iterations: ").append(iterations).append(" times.");

        return builder.toString();
    }

}
