package edu.ifmo.math;

public class Matrix {
    //Массив для хранения элементов матрицы
    private final double[][] elements;
    private final int rowCount;
    private final int columnCount;

    public Matrix(double[][] elements) {
        rowCount = elements.length;

        if (rowCount == 0) {
            throw new IllegalArgumentException("matrix should not be empty");
        }
        columnCount = elements[0].length;
        if (columnCount == 0) {
            throw new IllegalArgumentException("matrix should not be empty");
        }

        this.elements = elements;
    }

    //Получить элемент матрицы
    public double get(int i, int j) {
        return this.elements[i][j];
    }

    //Записать элемент матрицы
    public void set(int i, int j, double value) {
        this.elements[i][j] = value;
    }

    //Возвращает наибольший по модулю элемент матрицы
    public double getAbsMax() {
        double max = 0;
        for (int i = 0; i < getRowCount(); ++i) {
            for (int j = 0; j < getColumnCount(); ++j) {
                if (max < Math.abs(get(i, j))) {
                    max = Math.abs(get(i, j));
                }
            }
        }
        return max;
    }

    //Умножение матрицы на скаляр
    public Matrix multiply(double scalar) {
        Matrix matrix = copy();

        for (int i = 0; i < matrix.getRowCount(); ++i) {
            for (int j = 0; j < matrix.getColumnCount(); ++j) {
                matrix.set(i, j, scalar * matrix.get(i, j));
            }
        }
        return matrix;
    }

    public Matrix plus(Matrix matrix) {
        if (getRowCount() != matrix.getRowCount() || getColumnCount() != matrix.getColumnCount()) {
            throw new IllegalArgumentException("Матрицы должны иметь одинаковые измерения");
        }

        Matrix matrixSum = copy();

        for (int i = 0; i < getRowCount(); ++i) {
            for (int j = 0; j < getColumnCount(); ++j) {
                matrixSum.set(i, j, get(i, j) + matrix.get(i, j));
            }
        }
        return matrixSum;
    }

    //Разность матриц
    public Matrix minus(Matrix matrix) {
        return this.plus(matrix.multiply(-1));
    }

    //Массив диагональных элементов
    public double[] getDiagonalArray() {
        double[] diagonal = new double[Math.min(rowCount, columnCount)];

        for (int i = 0; i < rowCount && i < columnCount; ++i) {
            diagonal[i] = this.get(i, i);
        }
        return diagonal;
    }

    //Деление
    public Matrix div(double[] divisors) {
        if (divisors.length != rowCount) {
            throw new IllegalArgumentException("Кол-во строк != кол-во элементов делителя");
        }
        Matrix dividedMatrix = copy();

        for (int i = 0; i < rowCount; ++i) {
            double divisor = divisors[i];

            for (int j = 0; j < columnCount; ++j) {
                dividedMatrix.set(i, j, get(i, j) / divisor);
            }
        }

        return dividedMatrix;
    }

    //Возвращает копию матрицы
    public Matrix copy() {
        double[][] cloned = new double[rowCount][columnCount];

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                cloned[i][j] = get(i, j);
            }
        }

        return new Matrix(cloned);
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public int getColumnCount() {
        return this.columnCount;
    }

    //Получает подматрицу
    public Matrix subMatrix(int i0, int j0, int i1, int j1) {
        int subRowCount = i1 - i0 + 1;
        int subColumnCont = j1 - j0 + 1;

        double[][] subElements = new double[subRowCount][subColumnCont];

        try {
            for (int i = i0; i <= i1; ++i) {
                for (int j = j0; j <= j1; ++j) {
                    subElements[i-i0][j-j0] = this.get(i, j);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices out of range.");
        }
        return new Matrix(subElements);
    }

    public Matrix getMatrixCoefficients() {
        return this.subMatrix(0, 0, rowCount - 1, columnCount - 2);
    }

    public Matrix getMatrixFreeMembers() {
        return this.subMatrix(0, columnCount - 1, rowCount - 1, columnCount - 1);
    }

    //Возвращает диагонально доминантную матрицу
    public Matrix doRowPermutation() {
        int[] permutedRows = new int[rowCount];
        boolean[] isRowTaken = new boolean[rowCount];

        for (int i = 0; i < rowCount; i++) {
            double rowAbsSum = 0;
            int maxElementIndex = 0;

            for (int j = 0; j < rowCount; j++) {
                if (Math.abs(get(i, j)) > Math.abs(get(i, maxElementIndex))) {
                    maxElementIndex = j;
                }
                rowAbsSum += Math.abs(this.get(i, j));
            }

            if (2 * Math.abs(get(i, maxElementIndex)) > rowAbsSum && !isRowTaken[maxElementIndex]) {
                isRowTaken[maxElementIndex] = true;
                permutedRows[maxElementIndex] = i;
            } else {
                throw new RuntimeException("Impossible to get diagonally dominant matrix");
            }
        }

        double[][] permutedElements = new double[this.rowCount][this.columnCount];

        for (int i = 0; i < this.rowCount; i++) {
            for (int j = 0; j < this.columnCount; j++) {
                permutedElements[i][j] = this.get(permutedRows[i], j);
            }
        }

        return new Matrix(permutedElements);
    }
}