//приходит перевернутая матрица в сравнении с калькулятором, вследствие чего ошибка
import java.util.ArrayList;

public class Gomory {

    double[][] table;
    String[] signs;
    int m, n;
    ArrayList<Integer> basis;

    public Gomory(double[][] matrix, String[] array) {
        System.out.println("Входная матрица:");
        printMatrix(matrix);
        matrix = changeBySign(matrix, array);
        m = matrix.length;
        n = matrix[0].length;
        table = new double[m][n + m - 1];
        basis = new ArrayList<Integer>();

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < table[0].length; j++) {
                if (j < n)
                    table[i][j] = matrix[i][j];
                else
                    table[i][j] = 0;
            }
            if (n + i < table[0].length) {
                table[i][n + i] = 1;
                basis.add(n + i);
            }
        }
        n = table[0].length;
        System.out.println("Матрица с доп коэффициентами 1:");
        printMatrix(table);

        boolean fl = false;
        for (int i = 0; i<m; i++){
            if (table[i][0] < 0){
                fl = true;
            }
        }
        if (fl == true){
            int myRow = 0;
            double min = Double.MAX_VALUE;
            for (int i = 0; i<m; i++){
                if (table[i][0] < min && table[i][0] < 0){
                    min = table[i][0];
                    myRow = i;
                }
            }

            int myCol = 1;
            min = Double.MAX_VALUE;
            for (int i = 1; i<n; i++){
                if (table[myRow][i] < min && table[myRow][i] < 0){
                    min = table[myRow][i];
                    myCol = i;
                }
            }
            System.out.println(myCol+" "+myRow);
            basis.set(myRow, myCol);
            double[][] newTable = new double[m][n];

            for (int i = 0; i < n; i++)
                newTable[myRow][i] = table[myRow][i] / table[myRow][myCol];

            for (int i = 0; i < m; i++) {
                if (i == myRow)
                    continue;
                for (int j = 0; j < n; j++)
                    newTable[i][j] = table[i][j] - table[i][myCol] * newTable[myRow][j];
            }
            table = cloneMatrix(newTable);
            printMatrix(table);

        }

        System.out.println("Матрица с доп коэффициентами:");
        printMatrix(table);
    }

    public double[][] getAnswer(double[] result) {
        int mainCol, mainRow;
        while (!isEnd()) {
            mainCol = findMainCol();
            mainRow = findMainRow(mainCol);
            System.out.println(mainCol+" "+mainRow);
            basis.set(mainRow, mainCol);
            double[][] newTable = new double[m][n];

            for (int i = 0; i < n; i++)
                newTable[mainRow][i] = table[mainRow][i] / table[mainRow][mainCol];

            for (int i = 0; i < m; i++) {
                if (i == mainRow)
                    continue;
                for (int j = 0; j < n; j++)
                    newTable[i][j] = table[i][j] - table[i][mainCol] * newTable[mainRow][j];
            }
            table = cloneMatrix(newTable);
            printMatrix(table);
        }

        for (int i = 0; i < result.length; i++) {
            int k = basis.indexOf(i + 1);
            if (k != -1)
                result[i] = table[k][0];
            else
                result[i] = 0;
        }

        while (!isInt(result)) {
            methodGomory(result);
            //TODO изменение строк по Гомори
            if (!isEnd()) {
                mainRow = basis.size() - 1;
                mainCol = findMainColGomory(mainRow);
                System.out.println(mainCol + " " + mainRow);
                basis.set(mainRow, mainCol);
                double[][] newTable = new double[m][n];

                for (int i = 0; i < n; i++)
                    newTable[mainRow][i] = table[mainRow][i] / table[mainRow][mainCol];

                for (int i = 0; i < m; i++) {
                    if (i == mainRow)
                        continue;
                    for (int j = 0; j < n; j++)
                        newTable[i][j] = table[i][j] - table[i][mainCol] * newTable[mainRow][j];
                }
                table = cloneMatrix(newTable);
                printMatrix(table);

                while (!isEnd()) {
                    System.out.println("HERE");
                    mainCol = findMainCol();
                    mainRow = findMainRow(mainCol);
                    System.out.println(mainCol + " " + mainRow);
                    basis.set(mainRow, mainCol);
                    newTable = new double[m][n];

                    for (int i = 0; i < n; i++)
                        newTable[mainRow][i] = table[mainRow][i] / table[mainRow][mainCol];

                    for (int i = 0; i < m; i++) {
                        if (i == mainRow)
                            continue;
                        for (int j = 0; j < n; j++) {
                            newTable[i][j] = table[i][j] - table[i][mainCol] * newTable[mainRow][j];
                            reformateMatrix(newTable, i, j);
                        }
                    }
                    table = cloneMatrix(newTable);
                    printMatrix(table);
                }

                for (int i = 0; i < result.length; i++) {
                    int k = basis.indexOf(i + 1);
                    if (k != -1)
                        result[i] = table[k][0];
                    else
                        result[i] = 0;
                }
            }
            for (int i = 0; i < result.length; i++) {
                int k = basis.indexOf(i + 1);
                if (k != -1)
                    result[i] = table[k][0];
                else
                    result[i] = 0;
            }
        }

        return table;
    }

    private double[][] cloneMatrix(double[][] martrix) {
        double[][] clone = new double[martrix.length][];
        int count = 0;
        for (double[] line : martrix) {
            clone[count++] = line.clone();
        }
        return clone;
    }

    private boolean isInt(double[] res){
        if (res[0] % 1 == 0 && res[1] % 1 ==0)
            return true;
        else
            return false;
    }

    private void methodGomory(double[] res){
        double num;
        int bas;
        m+=1; n+=1;
        double[][] newMatrix = new double[table.length+1][table[0].length+1];
        if (res[0] % 1 > res[1] % 1) {
            num = res[0];
            bas = basis.indexOf(1);
        }else {
            num = res[1];
            bas = basis.indexOf(2);
        }

        double[] newCoefs = new double[table[0].length+1];
        for (int i=0; i<table[0].length; i++){
            newCoefs[i] = table[bas][i] % 1;
            if (newCoefs[i]<0)
                newCoefs[i] = newCoefs[i]+1;
            if (newCoefs[i]!=0)
                newCoefs[i] *= -1;
        }

        for (int i = 0; i<= table.length; i++){
            for (int j=0; j<= table[0].length; j++){
                if (i == table.length-1){
                    if (j!= table[0].length)
                        newMatrix[i][j] = newCoefs[j];
                    else {
                        newMatrix[i][j] = 1;
                    }
                }
                else if (i == table.length){
                    if (j!= table[0].length && table[i-1][j]!=0)
                        newMatrix[i][j] = table[i-1][j];
                    else
                        newMatrix[i][j] = 0;
                }else{
                    if (j!= table[0].length)
                        newMatrix[i][j] = table[i][j];
                    else
                        newMatrix[i][j] = 0;
                }
            }
        }
        System.out.println(newMatrix[3][6]);
        table = cloneMatrix(newMatrix);
        basis.add(newMatrix[0].length-1);

        System.out.println("fromG 1");
        printMatrix(table);

        boolean fl = false;
        for (int i = 0; i<m; i++){
            if (table[i][0] < 0){
                fl = true;
            }
        }
        if (fl == true){
            int myRow = 0;
            double min = Double.MAX_VALUE;
            for (int i = 0; i<m; i++){
                if (table[i][0] < min && table[i][0] < 0){
                    min = table[i][0];
                    myRow = i;
                }
            }

            int myCol = 1;
            min = Double.MAX_VALUE;
            for (int i = 1; i<n; i++){
                if (table[myRow][i] < min && table[myRow][i] < 0){
                    min = table[myRow][i];
                    myCol = i;
                }
            }
            System.out.println(myCol+" "+myRow);
            basis.set(myRow, myCol);
            double[][] newTable = new double[m][n];

            for (int i = 0; i < n; i++)
                newTable[myRow][i] = table[myRow][i] / table[myRow][myCol];

            for (int i = 0; i < m; i++) {
                if (i == myRow)
                    continue;
                for (int j = 0; j < n; j++)
                    newTable[i][j] = table[i][j] - table[i][myCol] * newTable[myRow][j];
            }
            table = cloneMatrix(newTable);
            System.out.println("fromG");
            printMatrix(table);

        }
    }

    private int findMainColGomory(int mainRow){
        int mainCol = 1;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < table[0].length-1; i++) {
            if (table[mainRow][i] != 0) {
                mainCol = i;
                break;
            }
        }

        for (int i=2; i< table[0].length-1; i++){
            if (table[mainRow][i]!=0 && table[mainRow+1][i]/table[mainRow][i] < min)
                mainCol = i;
        }

        return mainCol;
    }

    private int findMainRow(int mainCol) {
        int mainRow = 0;
        for (int i = 0; i < m - 1; i++) {
            if (table[i][mainCol] > 0) {
                mainRow = i;
                break;
            }
        }

        for (int i = 0; i < m - 1; i++) {
            if ((table[i][mainCol] > 0) && ((table[i][0] / table[i][mainCol]) < (table[mainRow][0] / table[mainRow][mainCol])))
                mainRow = i;
        }
        return mainRow;
    }

    private int findMainCol() {
        int mainCol = 1;
        for (int i = 2; i < n; i++) {
            if (table[m - 1][i] < table[m - 1][mainCol])
                mainCol = i;
        }
        return mainCol;
    }

    private boolean isEnd() {
        boolean flag = true;
        for (int i = 1; i < n; i++) {
            if (table[m - 1][i] < 0) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    private double[][] changeBySign(double[][] matrix, String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(">=")) {
                for (int j = 0; j < 3; j++) {
                    matrix[i][j] = matrix[i][j] * -1;
                }
            }
        }
        return matrix;
    }

    public void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + "   ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void reformateMatrix(double[][] newTable, int i, int j){
        if (1-(Math.abs(newTable[i][j])*10)%1 < 0.0001) {
            newTable[i][j] = (double) (Math.round(newTable[i][j] * 10))/10;
        }
    }

}
