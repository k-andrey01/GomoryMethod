public class Main {
    public static void main(String[] args) {
        double[][] coeffMatrix = {{3, -3, 2},
                {5, 3, 1},
                {1, 1, 1},
                {0, 3, -3}};
        double[] funcArr = {3, -3};
        String[] signArr = {"<=", "<=", ">=", "min"};
        Gomory myGomory = new Gomory(coeffMatrix, signArr);
        double[] res = new double[2];
        double[][] resTable;
        resTable = myGomory.getAnswer(res);

        System.out.println("Таблица:");
        myGomory.printMatrix(resTable);

        System.out.println("Решение:");
        System.out.println("X1 = " + res[0]);
        System.out.println("X2 = " + res[1]);

        System.out.println("Значение функции:");
        System.out.println(res[0]*funcArr[0]+res[1]*funcArr[1]);
    }
}
