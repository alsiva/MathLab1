package edu.ifmo.math;

import picocli.CommandLine;
import java.util.*;
import java.util.concurrent.Callable;



public class Application implements Callable<Integer> {
    private final Scanner scanner = new Scanner(System.in);

    @CommandLine.Option(names = {"-a", "--accuracy"}, description = "solution accuracy")
    double accuracy = 1E-5;

    @CommandLine.Option(names = {"-f", "--file"}, description = "file to read matrix from")
    String filename;

    @Override
    public Integer call() throws Exception {
        System.out.println("Accuracy is " + accuracy);

        Matrix matrix = filename == null
                ? matrixFromConsole()
                : parseMatrixF(filename);

        LinearSystemSolver solver = new LinearSystemSolver(accuracy);
        SeidelAnswer answer;

        try {
            answer = solver.solve(matrix);
            answer.showAnswer();
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }


        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Application()).execute(args);
        System.exit(exitCode);
    }


    private double parseAccuracy(String[] args) {
        try {
            if (args.length > 3) {
                throw new IllegalArgumentException("expected ");
            } else if (args[1].equals("=")) {
                return Double.parseDouble(args[2]);
            } else {
                System.err.println("accuracy = [double]");

            }
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.err.println("accuracy = [double]");
        }
        return 0;
    }

    /*
    //Читает матрицу с консоли
    private Matrix parseMatrixC() {
        Scanner in = new Scanner(System.in);
        int j = 0, size = -1;
        double[][] elements;
        String row, num[];

        size = Integer.parseInt(in.nextLine().trim());
        if (size < 1 || 20 < size) {
            throw new RuntimeException();
        }
        elements = new double[size][size + 1];
        int i = 0;
        while (i < size) {
            row = in.nextLine().trim().replaceAll(",", ".");
            num = row.split("(\\s++)");
            if (num.length != size + 1) {
                throw new RuntimeException();
            } else {
                for (j = 0; j <= size; ++j) {
                    elements[i][j] = Double.parseDouble(num[j]);
                }
                i++;
            }
        }
        System.out.println("matrix: successfully read\n");
        return new Matrix(elements);

    }

     */

    private Matrix matrixFromConsole() throws Exception {

        System.out.println("Write matrix size");
        int size = Integer.parseInt(scanner.nextLine().trim());

        if (!(0 <= size && size <= 20)) {
            throw new Exception("Size should be in [0; 20]");
        }

        Matrix matrix = new Matrix(size, size + 1);
        System.out.println("Введите строки матрицы");
        String[] rowElements;

        for (int i = 0; i < size;i++) {
            rowElements = scanner.nextLine().trim().split("(\\s++)");
            for (int j = 0; j < size + 1; j++) {
                matrix.write(i, j, Double.parseDouble(rowElements[j]));
            }
        }

        return matrix;
    }

    //Читает матрицу с файла
    private Matrix parseMatrixF(String filename) {
        //todo parse from file
        /*
        try {
            if (args.length > 3) {
                warning(2);
            } else if (args[1].equals("-f")) {
                Scanner file = new Scanner(new File(args[2]));
                parseMatrixC(file);
            } else {
                error("matrix -f [path]", 16);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            error("matrix -f [path]", 20);
        } catch (FileNotFoundException e) {
            printf("matrix: file not found%n");
        }

         */
        return null;
    }


    private final LinearSystemSolver solver = new LinearSystemSolver(1E-5);

    private void parseSolve(Matrix linearSystem) {


        SeidelAnswer answer;

        try {
            answer = solver.solve(linearSystem);
            answer.showAnswer();
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }

    }

    private void parseShow(String[] args) {
        if (args.length == 1) {
            parseShowA();
            parseShowM(null);
        } else if (args.length > 2) {
            System.err.println("should be one argument");
        } else if (args[1].equals("-a")) {
            parseShowA();
        } else if (args[1].equals("-m")) {
            parseShowM(null);
        } else {
            System.out.println("show -a[ccuracy]");
            System.out.println("show -m[atrix]");
        }
    }

    private void parseShowA() {
        System.out.println("accuracy = " + accuracy);
    }

    private void parseShowM(Matrix matrix) {
        if (matrix.getAsArray() != null) {
            for (int i = 0; i < matrix.getRowCount(); ++i) {
                System.out.print("[");
                for (int j = 0; j < matrix.getColumnCount() - 1; ++j) {
                    System.out.print(matrix.get(i, j) + ", ");
                }
            }
        } else {
            System.out.println("Firstly enter a matrix");
        }
    }

    private void parseNothing(String[] args) {
        if (!args[0].isEmpty()) {
            System.out.println("Command not found");
        }
    }

    private void parseHelp() {
        System.out.print("" +
                " accuracy  =  [value]    Set an accuracy of the algebraic linear system of equations\n" +
                "                         solver. Usage:                                             \n" +
                "                           > accuracy = 0.0001D                                  (1)\n" +
                "                           > accuracy = 1E-5                                     (2)\n" +
                "                           > accuracy = 0.000134532234                           (3)\n" +
                " matrix        [ ][ ]    Read a data of extended matrix of a algebraic linear system\n" +
                "                         of equations from console.                                 \n" +
                "         -f    [path]    Read a data from a file. Usage:                            \n" +
                "         -g    [size]    Generate   a  new  diagonally   dominant  matrix   size  of\n" +
                "                         M[size]x[size+1].                                          \n" +
                " exit                    terminate the application.                                 \n" +
                " solve                   Solve the last read algebraic linear system of equations by\n" +
                "                         simple iteration method (Jacobi's method).                 \n" +
                " show             [ ]    Show  an  accuracy  value  and  active  matrix  of  system.\n" +
                "         -a [ccuracy]    Show only accuracy value. Example:                         \n" +
                "         -m   [atrix]    Show only and only active matrix of system. Example        \n" +
                " help                    display a help information on console.                     \n"
        );
    }

}
