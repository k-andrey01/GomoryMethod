public class Matrix {
    public static double[][] cloneMatrix(double[][] martrix) {
        double[][] clone = new double[martrix.length][];
        int count = 0;
        for (double[] line : martrix) {
            clone[count++] = line.clone();
        }
        return clone;
    }

    public static double[][] changeBySign(double[][] matrix, String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(">=") || arr[i].equals("max")) {
                for (int j = 0; j < 3; j++) {
                    matrix[i][j] = matrix[i][j] * -1;
                }
            }
        }
        return matrix;
    }

    public static void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + "   ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void reformateMatrix(double[][] newTable, int i, int j) {
        if (1 - (Math.abs(newTable[i][j]) * 10) % 1 < 0.0001) {
            newTable[i][j] = (double) (Math.round(newTable[i][j] * 10)) / 10;
        }
    }
}
