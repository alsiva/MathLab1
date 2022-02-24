package edu.ifmo.math;

public class Matrix {

    //Массив для хранения элементов матрицы
    private final double[][] elements;
    private final int rowCount;
    private final int columnCount;

    //todo changeMatrixConstructor and maybe add LinearSystem constructor

    //Создаёт нулевую матрицу размером row * column
    public Matrix(int rowCount, int columnCount) {
        this.elements = new double[rowCount][columnCount];
        this.rowCount = rowCount;
        this.columnCount = columnCount;

        if (rowCount <= 0 || columnCount <= 0) {
            throw new NumberFormatException("Количество строк и столбцов должно быть больше нуля");
        }

    }

    //Создаёт матрицу из двумерного массива
    public Matrix(double[][] elements) {
        this.elements = elements;
        this.rowCount = elements.length;
        this.columnCount = elements[0].length;

        if (this.columnCount <= 0) {
            throw new NumberFormatException("Количество строк и столбцов должно быть больше нуля");
        }

    }

    //Получить элемент матрицы
    public double get(int i, int j) {
        return this.elements[i][j];
    }

    //Записать элемент матрицы
    public void write(int i, int j, double value) {this.elements[i][j] = value;}

    //Возвращает наибольший по модулю элемент матрицы
    public double getAbsMax() {
        double max = 0D;
        for (int i = 0; i < getRowCount(); ++i) {
            for (int j = 0; j < getColumnCount(); ++j) {
                if (max < Math.abs(this.elements[i][j])) {
                    max = Math.abs(this.elements[i][j]);
                }
            }
        }
        return max;
    }

    //Умножение матрицы на скаляр
    public Matrix mult(double lambda) {
        Matrix matrix = new Matrix(rowCount, columnCount);
        double[][] elements = matrix.getAsArray();

        for (int i = 0; i < getRowCount(); ++i) {
            for (int j = 0; j < getColumnCount(); ++j) {
                elements[i][j] = lambda * this.elements[i][j];
            }
        }
        return matrix;
    }

    public Matrix plus(Matrix matrix) {
        if (getRowCount() != matrix.getRowCount() || getColumnCount() != matrix.getColumnCount()) {
            throw new IllegalArgumentException("Матрицы должны иметь одинаковые измерения");
        }
        Matrix matrixSum = new Matrix(rowCount, columnCount);
        double[][] elements = matrixSum.getAsArray();

        for (int i = 0; i < getRowCount(); ++i) {
            for (int j = 0; j < getColumnCount(); ++j) {
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
        double[] diagonal = new double[Math.min(rowCount, columnCount)];

        for (int i = 0; i < rowCount && i < columnCount; ++i) {
            diagonal[i] = this.elements[i][i];
        }
        return diagonal;
    }

    public Matrix div(double[] divisor) {
        if (divisor.length != rowCount) {
            throw new IllegalArgumentException("Кол-во строк != кол-во элементов делителя");
        }
        Matrix matrix = new Matrix(rowCount, columnCount);
        double[][] elements = matrix.getAsArray();

        for (int i = 0; i < rowCount; ++i) {
            for (int j = 0; j < columnCount; ++j) {
                elements[i][j] = this.elements[i][j] / divisor[i];
            }
        }

        return matrix;
    }

    //Возвращает копию матрицы
    public Matrix copy() {
        Matrix matrix = new Matrix(rowCount, columnCount);
        double[][] elements = matrix.getAsArray();

        for (int i = 0; i < rowCount; ++i) {
            for (int j = 0; j < columnCount; ++j) {
                elements[i][j] = this.elements[i][j];
            }
        }

        return matrix;
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public int getColumnCount() {
        return this.columnCount;
    }

    //Возвращает матрицу как двуммерный массив
    public double[][] getAsArray() {
        return this.elements;
    }

    //Получает подматрицу
    public Matrix subMatrix(int i0, int j0, int i1, int j1) {
        Matrix matrix = new Matrix(i1 - i0 + 1, j1 - j0 + 1);
        double[][] elements = matrix.getAsArray();
        try {
            for (int i = i0; i <= i1; ++i) {
                for (int j = j0; j <= j1; ++j) {
                    matrix.write(i-i0, j-j0, this.elements[i][j]);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices out of range.");
        }
        return matrix;
    }

    public Matrix getMatrixCoefficients() {
        return this.subMatrix(0, 0, rowCount - 1, columnCount - 2);
    }

    public Matrix getMatrixFreeMembers() {
        return this.subMatrix(0, columnCount - 1, rowCount - 1, columnCount - 1);
    }
}
