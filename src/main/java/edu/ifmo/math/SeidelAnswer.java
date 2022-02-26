package edu.ifmo.math;

public class SeidelAnswer {

    private final Matrix roots;
    private final Matrix errors;
    private final int iterations;


    public SeidelAnswer(Matrix roots, Matrix errors, int iterations) {
        this.roots = roots;
        this.errors = errors;
        this.iterations = iterations;
    }

    private String pattern(double i, double roots, double errors) {
        return String.format("│ %02.0f │ % -20.16f │ % -20.16f │%n", i, roots, errors);
    }

    public void showAnswer() {
        StringBuilder builder = new StringBuilder();

        builder
            .append("│ №  │  root (x)            │  infelicity (delta)  │\n")
            .append("│——————————————————————————————————————————————————│\n");

        for (int i = 0; i < roots.getRowCount(); ++i) {
            builder.append(pattern(i + 1, roots.get(i, 0), errors.get(i, 0)));
        }
        builder.append("\nTotal ").append(iterations).append(" iterations.");

        System.out.println(builder);
    }

}
