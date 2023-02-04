public class Simplex {

    //симплекс-метод
    public static void simplex() {
        int mainCol, mainRow;
        while (!isEnd()) {
            System.out.println("С применением прохода по симплекс-методу:");
            mainCol = findMainCol();
            mainRow = findMainRow(mainCol);
            Gomory.basis.set(mainRow, mainCol);

            transformation(mainRow, mainCol);
            Matrix.printMatrix(Gomory.table);
        }
    }

    //преобразование матрицы по формуле
    public static void transformation(int mainRow, int mainCol) {
        double[][] newTable = new double[Gomory.m][Gomory.n];

        for (int i = 0; i < Gomory.n; i++)
            newTable[mainRow][i] = Gomory.table[mainRow][i] / Gomory.table[mainRow][mainCol];

        for (int i = 0; i < Gomory.m; i++) {
            if (i == mainRow)
                continue;
            for (int j = 0; j < Gomory.n; j++) {
                newTable[i][j] = Gomory.table[i][j] - Gomory.table[i][mainCol] * newTable[mainRow][j];
                Matrix.reformateMatrix(newTable, i, j);
            }
        }
        Gomory.table = Matrix.cloneMatrix(newTable);
    }

    //убрать все отрицательные свободные члены
    public static void getMatrixWithoutNegativeFreeElems() {
        boolean fl = false;
        //есть ли такие?
        for (int i = 0; i < Gomory.m; i++) {
            if (Gomory.table[i][0] < 0) {
                fl = true;
            }
        }
        //если есть
        if (fl == true) {
            //поиск минимального отрицательного свободного члена
            int myRow = 0;
            double min = Double.MAX_VALUE;
            for (int i = 0; i < Gomory.m; i++) {
                if (Gomory.table[i][0] < min && Gomory.table[i][0] < 0) {
                    min = Gomory.table[i][0];
                    myRow = i;
                }
            }

            //поиск минимального отрицательного в найденной строке
            int myCol = 1;
            min = Double.MAX_VALUE;
            for (int i = 1; i < Gomory.n; i++) {
                if (Gomory.table[myRow][i] < min && Gomory.table[myRow][i] < 0) {
                    min = Gomory.table[myRow][i];
                    myCol = i;
                }
            }

            Gomory.basis.set(myRow, myCol);

            transformation(myRow, myCol);
            System.out.println("Матрица без отрицательных свободных элементов:");
            Matrix.printMatrix(Gomory.table);

        }
    }

    //поиск строки, ищем минимум из вычислений по формуле
    public static int findMainRow(int mainCol) {
        int mainRow = 0;
        for (int i = 0; i < Gomory.m - 1; i++) {
            if (Gomory.table[i][mainCol] > 0) {
                mainRow = i;
                break;
            }
        }

        for (int i = 0; i < Gomory.m - 1; i++) {
            if ((Gomory.table[i][mainCol] > 0) && ((Gomory.table[i][0] / Gomory.table[i][mainCol]) < (Gomory.table[mainRow][0] / Gomory.table[mainRow][mainCol])))
                mainRow = i;
        }
        return mainRow;
    }

    //ищем минимальный элемент последней строки
    public static int findMainCol() {
        int mainCol = 1;
        for (int i = 2; i < Gomory.n; i++) {
            if (Gomory.table[Gomory.m - 1][i] < Gomory.table[Gomory.m - 1][mainCol])
                mainCol = i;
        }
        return mainCol;
    }

    //проверка, положительны ли все элементы последней строки
    public static boolean isEnd() {
        boolean flag = true;
        for (int i = 1; i < Gomory.n; i++) {
            if (Gomory.table[Gomory.m - 1][i] < 0) {
                flag = false;
                break;
            }
        }
        return flag;
    }

}
