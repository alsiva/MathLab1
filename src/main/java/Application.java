import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Application {

    public static void main(String[] args) {
        new Application().run();
    }

    private void error(String arg, int space) {
        printf("error: " + (arg) + "%n");
        printf((space) + "s%n", "^");
    }

    private void usage(String arg) {
        printf("usage: " + (arg) + "%n");
    }

    private void warning(int args) {
        printf("warning: allowed " + (args) + " argument(s)%n");
    }

    private void printf(String s, Object... objects) {
        System.out.printf(s, objects);
    }


    private boolean _RUNNING_ = true;

    public void run() {
        Scanner in = new Scanner(System.in);
        String[] args;

        printf("+-------------------------------------------+%n" +
                "| %sSIMPLE ITERATION METHOD (JACOBI'S METHOD) |%n" +
                "+-------------------------------------------+%n");

        while (_RUNNING_) {
            printf("simple-iteration-method$ ");

            args = in.nextLine().trim().split("(\\s++)");

            switch (args[0].toLowerCase(Locale.ROOT)) {
                case "accuracy":
                    parseAccuracy(args);
                    break;
                case "matrix":
                    parseMatrix(args, in);
                    break;
                case "solve":
                    parseSolve(args);
                    break;
                case "show":
                    parseShow(args);
                    break;
                case "help":
                    parseHelp();
                    break;
                case "exit":
                    parseExit();
                    break;
                default:
                    parseNothing(args);
                    break;
            }
        }
    }

    private double accuracy;
    private int size; // Размер линейного уравнения
    private double[][] elements;


    //todo parseAccuracy
    private void parseAccuracy(String[] args) {
        try {
            if (args.length > 3) {
                warning(2);
            } else if (args[1].equals("=")) {
                accuracy = Double.parseDouble(args[2]);
            } else {
                error("accuracy = [double]", 17);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            usage("accuracy = [double]");
        } catch (NumberFormatException e) {
            error("accuracy = [double]", 22);
        }
    }

    private void parseMatrix(String[] args, Scanner in) {
        if (args.length == 1) {
            parseMatrixC(in, false);
        } else {
            parseMatrixF(args);
        }
    }

    //Читает матрицу с консоли
    private void parseMatrixC(Scanner in, boolean isFile) {
        int j = 0, size = -1;
        double[][] elements;
        String row, num[] = new String[0];
        try {
            size = Integer.parseInt(in.nextLine().trim());
            if (size < 1 || 20 < size) {
                throw new RuntimeException();
            }
            elements = new double[size][size + 1];
            int i = 0;
            while (i < size) {
                row = in.nextLine().trim().replaceAll(",", ".");
                if (row.isEmpty() && !isFile) {
                    continue;
                }
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
            this.size = size;
            this.elements = elements;
            printf("matrix: successfully read%n");
        } catch (NoSuchElementException e) {
            printf("matrix: invalid file data%n");
        } catch (NumberFormatException e) {
            if (size == -1) {
                printf("matrix: invalid format [size=int]%n");
            } else {
                printf("matrix: invalid format - %s%n", num[j]);
            }
        } catch (RuntimeException e) {
            if (1 <= size && size <= 20) {
                printf("matrix: dimensions must agree%n");
            } else {
                printf("matrix: allowed only [1..20]%n");
            }
        }
    }

    //Читает матрицу с файла
    private void parseMatrixF(String[] args) {
        try {
            if (args.length > 3) {
                warning(2);
            } else if (args[1].equals("-f")) {
                Scanner file = new Scanner(new File(args[2]));
                parseMatrixC(file, true);
            } else if (args[1].equals("-g")) {
                parseMatrixG(args);
            } else {
                error("matrix -f [path]", 16);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            error("matrix -f [path]", 20);
        } catch (FileNotFoundException e) {
            printf("matrix: file not found%n");
        }
    }

    //Генерирует матрицу
    private void parseMatrixG(String[] args) {
        try {
            size = Integer.parseInt(args[2]);
            if (size < 1 || 20 < size) {
                throw new RuntimeException();
            }
            Integer[] indices = new Integer[size];
            double[] sums = new double[size];
            elements = new double[size][size + 1];
            for (int i = 0; i < size; ++i) {
                indices[i] = i;
                for (int j = 0; j < size + 1; ++j) {
                    elements[i][j] = Math.random() * 10 - 5;
                    sums[i] += Math.abs(elements[i][j]);
                }
                sums[i] -= Math.abs(elements[i][size]);
            }
            List<Integer> list = Arrays.asList(indices);
            Collections.shuffle(list);
            list.toArray(indices);

            for (int i = 0; i < size; ++i) {
                elements[indices[i]][i] = sums[indices[i]] + Math.random() + 1;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            usage("matrix -g [int]");
        } catch (NumberFormatException e) {
            printf("matrix: invalid format [size=int]%n");
        } catch (RuntimeException e) {
            printf("matrix: allowed only [1..20]%n");
        }
    }


    private final LinearSystemSolver solver = new LinearSystemSolver();

    private void parseSolve(String[] args) {
        if (args.length > 1) {
            warning(0);
        } else if (elements != null) {
            Matrix matrix = new Matrix(elements, size, size + 1);
            LinearSystem linearSystem = new LinearSystem(matrix, size);
            JacobiAnswer answer;

            try {
                answer = solver.solveByJacobi(linearSystem, accuracy);
                System.out.println(answer);
            } catch (RuntimeException e) {
                printf("%s%n", e.getMessage());
            }

        } else {
            printf("solve: firstly enter a matrix%n");
        }
    }

    private void parseShow(String[] args) {
        if (args.length == 1) {
            parseShowA();
            parseShowM();
        } else if (args.length > 2) {
            warning(1);
        } else if (args[1].equals("-a")) {
            parseShowA();
        } else if (args[1].equals("-m")) {
            parseShowM();
        } else {
            usage("show -a[ccuracy]");
            usage("show -m[atrix]");
        }
    }

    private void parseShowA() {
        printf("accuracy=%.12f%n", accuracy);
    }

    private void parseShowM() {
        if (elements != null) {
            for (int i = 0; i < size; ++i) {
                printf("[");
                for (int j = 0; j < size - 1; ++j) {
                    printf("%s, ", elements[i][j]);
                }
                printf("%s] * x_%s = %s%n", elements[i][size-1], i + 1, elements[i][size]);
            }
        } else {
            printf("show: firstly enter a matrix%n");
        }
    }

    private void parseExit() {
        this._RUNNING_ = false;
    }

    private void parseNothing(String[] args) {
        if (!args[0].isEmpty()) {
            printf("%s: command not found%n", args[0]);
        }
    }

    private void parseHelp() {
        printf("" +
                " accuracy  =  [value]    Set an accuracy of the algebraic linear system of equations%n" +
                "                         solver. Usage:                                             %n" +
                "                           > accuracy = 0.0001D                                  (1)%n" +
                "                           > accuracy = 1E-5                                     (2)%n" +
                "                           > accuracy = 0.000134532234                           (3)%n" +
                " matrix        [ ][ ]    Read a data of extended matrix of a algebraic linear system%n" +
                "                         of equations from console.                                 %n" +
                "         -f    [path]    Read a data from a file. Usage:                            %n" +
                "         -g    [size]    Generate   a  new  diagonally   dominant  matrix   size  of%n" +
                "                         M[size]x[size+1].                                          %n" +
                " exit                    terminate the application.                                 %n" +
                " solve                   Solve the last read algebraic linear system of equations by%n" +
                "                         simple iteration method (Jacobi's method).                 %n" +
                " show             [ ]    Show  an  accuracy  value  and  active  matrix  of  system.%n" +
                "         -a [ccuracy]    Show only accuracy value. Example:                         %n" +
                "         -m   [atrix]    Show only and only active matrix of system. Example        %n" +
                " help                    display a help information on console.                     %n"
        );
    }

}
