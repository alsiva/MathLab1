package edu.ifmo.math;

import picocli.CommandLine;

import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;


public class Application implements Callable<Integer> {
    @CommandLine.Option(names = {"-a", "--accuracy"}, description = "solution accuracy")
    double accuracy = 1E-5;

    @CommandLine.Option(names = {"-f", "--file"}, description = "file to read matrix from")
    String filename;

    @Override
    public Integer call() throws Exception {
        System.out.println("Accuracy is " + accuracy);

        Matrix matrix = readMatrix();

        LinearSystemSolver solver = new LinearSystemSolver(accuracy);

        SeidelAnswer answer = solver.solve(matrix);

        answer.showAnswer();

        return 0;
    }

    private Matrix readMatrix() throws IOException {
        if (filename != null) {
            try (InputStream matrixFileStream = getClass().getResourceAsStream("/" + filename)) {
                if (matrixFileStream == null) {
                    throw new IllegalArgumentException(filename + " not found");
                }

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(matrixFileStream))) {
                    return parseMatrix(reader);
                }
            }
        }

        System.out.println("Enter matrix row by row, leave the row empty to finish");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            return parseMatrix(reader);
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Application()).execute(args);
        System.exit(exitCode);
    }

    private Matrix parseMatrix(BufferedReader reader) throws IOException {
        Integer columns = null;

        List<double[]> rows = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isBlank()) {
                break;
            }

            String[] lineElements = line.trim().split("(\\s++)");
            if (columns == null) {
                // first row
                columns = lineElements.length;
            } else {
                if (columns != lineElements.length) {
                    throw new IllegalArgumentException("Rows have different length");
                }
            }

            double[] row = new double[lineElements.length];

            for (int i = 0; i < lineElements.length; i++) {
                try {
                    row[i] = Double.parseDouble(lineElements[i]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Failed to parse double: " + e.getMessage());
                }
            }

            rows.add(row);
        }

        if (columns == null) {
            throw new IllegalArgumentException("Matrix can't be empty");
        }

        if (columns != rows.size() + 1) {
            throw new IllegalArgumentException("Matrix is not valid, " + rows.size() + " rows and " + columns + " columns");
        }

        return new Matrix(rows.toArray(double[][]::new));
    }
}
