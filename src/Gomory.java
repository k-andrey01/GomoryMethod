import java.util.ArrayList;

public class Gomory {

    static double[][] table;
    static int m, n;
    static ArrayList<Integer> basis;

    public Gomory(double[][] matrix, String[] array) {
        System.out.println("Входная матрица:");
        Matrix.printMatrix(matrix);
        //меняем знаки у коэффициентов по знакам уравнения
        matrix = Matrix.changeBySign(matrix, array);
        m = matrix.length;
        n = matrix[0].length;
        table = new double[m][n + m - 1];
        basis = new ArrayList<Integer>();

        //заполнение матрицы для работы
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < table[0].length; j++) {
                if (j < n)
                    table[i][j] = matrix[i][j];
                else
                    table[i][j] = 0;
            }
            //определение доп коэффициентов, заполнение базиса
            if (n + i < table[0].length) {
                table[i][n + i] = 1;
                basis.add(n + i);
            }
        }
        n = table[0].length;
        System.out.println("Матрица с доп коэффициентами:");
        Matrix.printMatrix(table);

        Simplex.getMatrixWithoutNegativeFreeElems();
    }

    //алгоритм поиска ответа
    public double[][] getAnswer(double[] result) {
        //простой симплекс-метод
        Simplex.simplex();
        getResult(result);

        //применение метода Гомори для получения цедых ответов
        while (!isInt(result)) {
            //добавление ограничения
            methodGomory(result);
            //до отсутствия отрицательных в последней строке
            if (!Simplex.isEnd()) {
                //симплекс для Гомори (один прогон)
                simplexForGomory();
                //простой симплекс
                Simplex.simplex();
                //получение промежуточного результата
                getResult(result);
            }
            //итоговый результат
            getResult(result);
        }

        return table;
    }

    //вывод результата
    private void getResult(double[] result) {
        for (int i = 0; i < result.length; i++) {
            int k = basis.indexOf(i + 1);
            if (k != -1)
                result[i] = table[k][0];
            else
                result[i] = 0;
        }
    }

    //симплекс-метод для Гомори (по-другому ищем строку и столбец)
    private void simplexForGomory() {
        System.out.println("С применением прохода по симплекс-методу для Гомори:");
        int mainCol, mainRow;
        mainRow = basis.size() - 1;
        mainCol = findMainColGomory(mainRow);
        basis.set(mainRow, mainCol);

        Simplex.transformation(mainRow, mainCol);
        Matrix.printMatrix(table);
    }

    //остались ли в последней строке только целые числа
    private boolean isInt(double[] res) {
        if (res[0] % 1 == 0 && res[1] % 1 == 0)
            return true;
        else
            return false;
    }

    //добавление ограничения
    private void methodGomory(double[] res) {
        int bas;
        m += 1;
        n += 1;
        //увеличенная матрица
        double[][] newMatrix = new double[table.length + 1][table[0].length + 1];
        //определяем индекс в базисе ответа с большей дробной частью
        if (res[0] % 1 > res[1] % 1) {
            bas = basis.indexOf(1);
        } else {
            bas = basis.indexOf(2);
        }

        //определение новой строки в матрице
        double[] newCoefs = new double[table[0].length + 1];
        for (int i = 0; i < table[0].length; i++) {
            newCoefs[i] = table[bas][i] % 1;
            if (newCoefs[i] < 0)
                newCoefs[i] = newCoefs[i] + 1;
            if (newCoefs[i] != 0)
                newCoefs[i] *= -1;
        }

        //заполнение матрицы
        for (int i = 0; i <= table.length; i++) {
            for (int j = 0; j <= table[0].length; j++) {
                //тут надо перенести новые найденные коэффициенты
                if (i == table.length - 1) {
                    if (j != table[0].length)
                        newMatrix[i][j] = newCoefs[j];
                    else {
                        //это доп коэффициент для строки
                        newMatrix[i][j] = 1;
                    }
                }
                //тут заполняем последнюю строку
                else if (i == table.length) {
                    if (j != table[0].length && table[i - 1][j] != 0)
                        newMatrix[i][j] = table[i - 1][j];
                    else
                        newMatrix[i][j] = 0;
                }
                //тут остальные
                else {
                    if (j != table[0].length)
                        newMatrix[i][j] = table[i][j];
                    else
                        newMatrix[i][j] = 0;
                }
            }
        }
        table = Matrix.cloneMatrix(newMatrix);
        //добавление вновь добавленной строки в базис
        basis.add(newMatrix[0].length - 1);

        System.out.println("С добавлением ограничения по методу Гомори:");
        Matrix.printMatrix(table);

        Simplex.getMatrixWithoutNegativeFreeElems();
    }

    //после применения Гомори ищем колонку по-другому
    private int findMainColGomory(int mainRow) {
        int mainCol = 1;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < table[0].length - 1; i++) {
            if (table[mainRow][i] != 0) {
                mainCol = i;
                break;
            }
        }

        //ищем минимум при вычислении по формуле
        for (int i = 2; i < table[0].length - 1; i++) {
            if (table[mainRow][i] != 0 && table[mainRow + 1][i] / table[mainRow][i] < min)
                mainCol = i;
        }

        return mainCol;
    }

}