public class LinearSystem {

    private Matrix extendedMatrix;

    public Matrix getExtendedMatrix() {
        return extendedMatrix;
    }

    private int size;

    public int getSize() {
        return size;
    }

    public LinearSystem(Matrix extendedMatrix, int size) {
        this.extendedMatrix = extendedMatrix;
        this.size = size;

        if (size < 1 || 20 < size) {
            throw new IllegalArgumentException("Разрешены числа от 0 до 20");
        }

        if (extendedMatrix.getRowDimention() != size || extendedMatrix.getColumnDimention() != size + 1) {
            throw new IllegalArgumentException("Измерения расширенной матрицы не совпадают");
        }
    }

    public int[] getPermutedRowsIfPossible() {
        int[] rowsIndices = new int[size];
        boolean[] flag = new boolean[size];

        for (int i = 0; i < size; ++i) {
            double absSum = 0;
            int maxItemIndex = 0;
            for (int j = 0; j < size; ++j) {
                if (Math.abs(extendedMatrix.get(i, maxItemIndex)) < Math.abs(extendedMatrix.get(i, j))) {
                    maxItemIndex = j;
                }
                absSum += Math.abs(extendedMatrix.get(i, j));
            }
            if (2 * Math.abs(extendedMatrix.get(i, maxItemIndex)) > absSum && !flag[maxItemIndex]) {
                flag[maxItemIndex] = true;
                rowsIndices[maxItemIndex] = i;
            } else {
                return null;
            }
        }
        return rowsIndices;
    }
}













