import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Application {

    public static void main(String[] args) {

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
                    //todo parseAccuracy
                    break;
                case "matrix":
                    parseMatrix(args, in);
                    break;
                case "solve":
                    parseSolve(args);
                    break;
                case "show":
                    //todo parseShow
                    break;
                case "help":
                    //todo parseHelp
                    break;
                case "exit":
                    //todo parseExit
                default:
                    //todo parseNothing
                    break;
            }
        }
    }

    private double accuracy;
    private int size; // Размер линейного уравнения
    private double[][] elements;


    //todo parseAccuracy
    private void parseAccuracy(String[] args) {

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
                //todo warning
            } else if (args[1].equals("-f")) {
                Scanner file = new Scanner(new File(args[2]));
                parseMatrixC(file, true);
            } else if (args[1].equals("-g")) {
                //todo parseMatrix generated
            } else {
                //todo error
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            //todo error
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
            //todo usage
        } catch (NumberFormatException e) {
            printf("matrix: invalid format [size=int]%n");
        } catch (RuntimeException e) {
            printf("matrix: allowed only [1..20]%n");
        }
    }


    private final LinearSystemSolver solver = new LinearSystemSolver();

    private void parseSolve(String[] args) {
        if (args.length > 1) {
            //todo warning
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

}
