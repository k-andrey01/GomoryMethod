public class Main {
    public static void main(String[] args) {
        double[][] coeffMatrix = {{3, -3, 2},
                                  {5, 3, 1},
                                  {1, 1, 1},
                                  {0, 3, -3}};
        double[] funcArr = {3, -3};
        String[] signArr = {"<=", "<=", ">=", "min"};

//        double[][] coeffMatrix = {{0, 3, 2},
//                                  {4, 0, 4},
//                                  {8, 3, 4},
//                                  {0, 3, 0}};
//        double[] funcArr = {3, 0};
//        String[] signArr = {">=", "<=", "<=", "max"};

        Gomory myGomory = new Gomory(coeffMatrix, signArr);
        double[] res = new double[funcArr.length];
        double[][] resTable;

        resTable = myGomory.getAnswer(res);
        System.out.println("Итоговая таблица:");
        Matrix.printMatrix(resTable);

        System.out.println("Решение:");
        System.out.println("X1 = " + res[0]);
        System.out.println("X2 = " + res[1]);

        System.out.println("\nЗначение функции:");
        System.out.println("F = " + (res[0] * funcArr[0] + res[1] * funcArr[1]));
    }
}
