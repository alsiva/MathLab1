public class Matrix {

    //Массив для хранения элементов матрицы
    private final double[][] elements;

    //Количество строк и столбцов
    private final int row, column;

    //Создаёт нулевую матрицу размером row * column
    public Matrix(int row, int column) {
        this.elements = new double[row][column];
        this.row = row;
        this.column = column;

        if (row <= 0 || column <= 0) {
            throw new NumberFormatException("Количество строк и столбцов должно быть больше нуля");
        }

    }

    //Создаёт матрицу из двумерного массива
    public Matrix(double[][] elements, int row, int column) {
        this.elements = elements;
        this.row = row;
        this.column = column;

        if (row <= 0 || column <= 0) {
            throw new NumberFormatException("Количество строк и столбцов должно быть больше нуля");
        }

        this.checkMatrixDimensions(elements);
    }

    //Проверяет что двумерный массив правильный
    private void checkMatrixDimensions(double[][] elements) {
        if (elements.length != row) {
            throw new IllegalArgumentException("Количество строк в массиве не совпадает с row");
        }

        for (int i = 0; i < row; ++i) {
            if (elements[i].length != column) {
                throw new IllegalArgumentException("Количество столбцов в элементе массива не совпадает с column");
            }
        }
    }

    //Получить подматрицу
    public Matrix subMatrix(int i0, int j0, int i1, int j1) {
        Matrix matrix = new Matrix(i1 - i0 + j1, j1 - j0 + 1); //Создаём нулевую подматрицу
        double[][] elements = matrix.getAsArray();

        try {
            for (int i = i0; i <= i1; ++i) {
                for (int j = j0; j <= j1; ++j) {
                    elements[i-i0][j-j0] = this.elements[i][j];
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Индексы подматрицы выходят за границы");
        }

        return matrix;
    }

    //Получить элемент матрицы
    public double get(int i, int j) {
        return this.elements[i][j];
    }

    //Возвращает наибольший по модулю элемент матрицы
    public double getAbsMax() {
        double max = 0;
        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < column; ++j) {
                if (max < Math.abs(this.elements[i][j])) {
                    max = Math.abs(this.elements[i][j]);
                }
            }
        }
        return max;
    }

    //Умножение матрицы на скаляр
    public Matrix mult(double lambda) {
        Matrix matrix = new Matrix(row, column);
        double[][] elements = matrix.getAsArray();

        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < column; ++j) {
                elements[i][j] = lambda * this.elements[i][j];
            }
        }
        return matrix;
    }

    //Умножение матрицы на матрицу
    public Matrix mult(Matrix matrix) {
        if (column != matrix.row) {
            throw new IllegalArgumentException("Кол-во столбцов 1-ой матрицы != кол-во строк 2-ой матрицы");
        }
        Matrix matrixMult = new Matrix(row, matrix.column);
        double[][] elements = matrixMult.getAsArray();

        for (int i = 0; i < matrixMult.row; ++i) {
            for (int j = 0; j < matrixMult.column; ++j) {
                for (int k = 0; k < column; ++k) {
                    elements[i][j] += this.elements[i][k] * this.elements[k][j];
                }
            }
        }

        return matrixMult;
    }

    public Matrix plus(Matrix matrix) {
        if (row != matrix.row || column != matrix.column) {
            throw new IllegalArgumentException("Матрицы должны иметь одинаковые измерения");
        }
        Matrix matrixSum = new Matrix(row, column);
        double[][] elements = matrix.getAsArray();

        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < column; ++j) {
                elements[i][j] = this.elements[i][j] + matrix.elements[i][j];
            }
        }
        return matrixSum;
    }

    //Разность матриц
    public Matrix minus(Matrix matrix) {
        return this.plus(matrix.mult(-1));
    }

    //Массив диагональных элементов
    public double[] getDiagonalArray() {
        double[] diagonal = new double[Math.min(row, column)];

        for (int i = 0; i < row && i < column; ++i) {
            diagonal[i] = this.elements[i][i];
        }
        return diagonal;
    }

    //Возвращает матрицу как двуммерный массив
    public double[][] getAsArray() {
        return this.elements;
    }

}
