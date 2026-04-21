import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static int[] array = null; // одномерный массив
    private static int[][] matrix = null; // двумерный массив
    private static int[][] jaggedMatrix = null; // рваный массив

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        runMainMenu(scanner);
    }

    private static void runMainMenu(Scanner scanner) {
        while (true) {
            System.out.println();
            System.out.println("ГЛАВНОЕ МЕНЮ");
            System.out.println("  1. Часть 1. Работа с одномерными массивами");
            System.out.println("  2. Часть 2. Работа с многомерными массивами");
            System.out.println("  0. Выход");
            System.out.println();

            int choice = readIntInRange(scanner, "Выберите пункт: ", 0, 2);
            switch (choice) {
                case 0 -> {
                    System.out.println("Выход из программы.");
                    return;
                }
                case 1 -> runPart1Menu(scanner);
                case 2 -> runPart2Menu(scanner);
            }
        }
    }

    private static void runPart1Menu(Scanner scanner) {
        while (true) {
            System.out.println();
            System.out.println("ЧАСТЬ 1. ОДНОМЕРНЫЕ МАССИВЫ");
            System.out.println("  1. Сформировать массив (случайные числа)");
            System.out.println("  2. Сформировать массив (ввод с клавиатуры)");
            System.out.println("  3. Распечатать массив");
            System.out.println("  4. Удалить все нечётные элементы");
            System.out.println("  5. Добавить K элементов в начало массива");
            System.out.println("  6. Перестановка элементов с чётными и нечётными номерами");
            System.out.println("  7. Поиск первого чётного элемента");
            System.out.println("  8. Бинарный поиск элемента");
            System.out.println("  0. Назад в главное меню");
            System.out.println();

            int choice = readIntInRange(scanner, "Выберите подзадачу: ", 0, 8);
            switch (choice) {
                case 0 -> { return; }
                case 1 -> formArrayRandom(scanner);
                case 2 -> formArrayFromKeyboard(scanner);
                case 3 -> printArray();
                case 4 -> removeOddElements();
                case 5 -> addElementsToBeginning(scanner);
                case 6 -> swapEvenOddIndices();
                case 7 -> findFirstEvenAndCountComparisons();
                case 8 -> binarySearchElement(scanner);
            }
        }
    }

    private static void runPart2Menu(Scanner scanner) {
        while (true) {
            System.out.println();
            System.out.println("ЧАСТЬ 2. МНОГОМЕРНЫЕ МАССИВЫ");
            System.out.println("  1. Сформировать двумерный массив (случайные числа) и вывести");
            System.out.println("  2. Добавить столбец после столбца с наибольшим элементом");
            System.out.println("  3. Сформировать рваный массив (случайные числа) и вывести");
            System.out.println("  4. Добавить строку в конец рваного массива");
            System.out.println("  0. Назад в главное меню");
            System.out.println();

            int choice = readIntInRange(scanner, "Выберите подзадачу: ", 0, 4);
            switch (choice) {
                case 0 -> { return; }
                case 1 -> formMatrixRandomAndPrint(scanner);
                case 2 -> addColumnAfterMaxAndPrint();
                case 3 -> formJaggedRandomAndPrint(scanner);
                case 4 -> addRowToJaggedEnd(scanner);
            }
        }
    }

    private static void formMatrixRandomAndPrint(Scanner scanner) {
        int rows = readPositiveInt(scanner, "Введите количество строк: ");
        int cols = readPositiveInt(scanner, "Введите количество столбцов: ");
        matrix = createMatrixRandom(rows, cols);
        System.out.println("Двумерный массив " + rows + " x " + cols + " создан");
        printMatrix(matrix);
    }

    private static int[][] createMatrixRandom(int rows, int cols) {
        Random rnd = new Random();
        int[][] m = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                m[i][j] = rnd.nextInt(1000);
            }
        }
        return m;
    }

    private static void printMatrix(int[][] m) {
        if (m == null || m.length == 0) {
            System.out.println("Матрица не задана");
            return;
        }
        for (int i = 0; i < m.length; i++) {
            System.out.println(Arrays.toString(m[i]));
        }
    }

    private static void formJaggedRandomAndPrint(Scanner scanner) {
        int rows = readPositiveInt(scanner, "Введите количество строк: ");
        int maxCols = readPositiveInt(scanner, "Максимум элементов в строке (для длины каждой строки): ");
        jaggedMatrix = createJaggedRandom(rows, maxCols);
        System.out.println("Рваный массив создан");
        printMatrix(jaggedMatrix);
    }

    private static int[][] createJaggedRandom(int rows, int maxCols) {
        Random rnd = new Random();
        int[][] m = new int[rows][];
        for (int i = 0; i < rows; i++) {
            int len = 1 + rnd.nextInt(maxCols);
            m[i] = new int[len];
            for (int j = 0; j < len; j++) {
                m[i][j] = rnd.nextInt(1000);
            }
        }
        return m;
    }

    private static void addRowToJaggedEnd(Scanner scanner) {
        if (jaggedMatrix == null) {
            jaggedMatrix = new int[0][];
        }
        int len = readPositiveInt(scanner, "Количество элементов в новой строке: ");
        int[] newRow = new int[len];
        jaggedMatrix = Arrays.copyOf(jaggedMatrix, jaggedMatrix.length + 1);
        jaggedMatrix[jaggedMatrix.length - 1] = newRow;
        System.out.println("Строка добавлена в конец");
        printMatrix(jaggedMatrix);
    }

    private static void addColumnAfterMaxAndPrint() {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            System.out.println("Матрица не задана");
            return;
        }
        int colOfMax = findColumnOfMax(matrix);
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] result = new int[rows][cols + 1];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j <= colOfMax; j++) {
                result[i][j] = matrix[i][j];
            }
            for (int j = colOfMax + 1; j < cols; j++) {
                result[i][j + 1] = matrix[i][j];
            }
        }
        matrix = result;
        System.out.println("Добавлен столбец после столбца " + colOfMax);
        printMatrix(matrix);
    }

    private static int findColumnOfMax(int[][] m) {
        if (m == null || m.length == 0 || m[0].length == 0) {
            return -1;
        }
        int max = m[0][0];
        int colOfMax = 0;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                if (m[i][j] > max) {
                    max = m[i][j];
                    colOfMax = j;
                }
            }
        }
        return colOfMax;
    }

    private static void formArrayRandom(Scanner scanner) {
        int n = readPositiveInt(scanner, "Введите количество элементов массива: ");
        array = createArrayWithRandomNumbers(n);
        System.out.println("Массив из " + n + " случайных элементов создан");
    }

    private static int[] createArrayWithRandomNumbers(int n) {
        Random rnd = new Random();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = rnd.nextInt(1000);
        }
        return a;
    }

    private static void formArrayFromKeyboard(Scanner scanner) {
        int n = readPositiveInt(scanner, "Введите количество элементов массива: ");
        array = readArrayFromKeyboard(scanner, n);
        System.out.println("Массив из " + n + " элементов введён");
    }

    private static int[] readArrayFromKeyboard(Scanner scanner, int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = readInt(scanner, "Элемент [" + i + "]: ");
        }
        return a;
    }

    private static void printArray() {
        if (array == null) {
            System.out.println("Массив не задан. Сначала создайте массив (п. 1 или 2)");
            return;
        }
        System.out.println("Массив (" + array.length + " элементов):");
        System.out.println(Arrays.toString(array));
    }

    private static void removeOddElements() {
        if (array == null || array.length == 0) {
            System.out.println("Массив не задан. Сначала создайте массив (п. 1 или 2)");
            return;
        }
        int evenCount = 0;
        for (int x : array) {
            if (x % 2 == 0) evenCount++;
        }
        int[] result = new int[evenCount];
        int j = 0;
        for (int x : array) {
            if (x % 2 == 0) result[j++] = x;
        }
        array = result;
        System.out.println("Нечётные элементы удалены. Осталось " + array.length + " элементов");
    }

    private static void addElementsToBeginning(Scanner scanner) {
        if (array == null) {
            array = new int[0];
        }
        int k = readPositiveInt(scanner, "Введите количество элементов: ");

        System.out.println("  1. Добавить случайные числа");
        System.out.println("  2. Ввести элементы с клавиатуры");
        int way = readIntInRange(scanner, "Выберите способ: ", 1, 2);

        int[] prefix = (way == 1) ? createArrayWithRandomNumbers(k) : readArrayFromKeyboard(scanner, k);
        int[] result = new int[array.length + k];
        System.arraycopy(prefix, 0, result, 0, k);
        System.arraycopy(array, 0, result, k, array.length);
        array = result;
        System.out.println("В начало добавлено " + k + " элементов. Размер массива: " + array.length);
    }

    private static void swapEvenOddIndices() {
        if (array == null || array.length < 2) {
            System.out.println("Массив не задан или слишком короткий (нужно минимум 2 элемента)");
            return;
        }
        for (int i = 0; i < array.length - 1; i += 2) {
            int t = array[i];
            array[i] = array[i + 1];
            array[i + 1] = t;
        }
        System.out.println("Элементы с чётными и нечётными номерами переставлены");
    }

    private static void findFirstEvenAndCountComparisons() {
        if (array == null || array.length == 0) {
            System.out.println("Массив не задан");
            return;
        }
        int comparisons = 0;
        for (int i = 0; i < array.length; i++) {
            comparisons++;
            if (array[i] % 2 == 0) {
                System.out.println("Первый чётный элемент: индекс " + i + ", значение " + array[i] + ". Количество сравнений: " + comparisons);
                return;
            }
        }
        System.out.println("Чётных элементов нет. Количество сравнений: " + comparisons);
    }

    private static void binarySearchElement(Scanner scanner) {
        if (array == null || array.length == 0) {
            System.out.println("Массив не задан");
            return;
        }
        int key = readInt(scanner, "Введите искомый элемент: ");

        int[] sorted = array.clone();
        Arrays.sort(sorted);
        
        int left = 0;
        int right = sorted.length - 1;
        int comparisons = 0;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            comparisons++;
            if (sorted[mid] == key) {
                System.out.println("Элемент " + key + " найден. Количество сравнений: " + comparisons);
                return;
            }
            if (key < sorted[mid]) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        System.out.println("Элемент " + key + " не найден. Количество сравнений: " + comparisons);
    }

    private static int readIntInRange(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            int value = readInt(scanner, prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Введите число от " + min + " до " + max);
        }
    }

    private static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            int value = readInt(scanner, prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Введите положительное число");
        }
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = scanner.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод. Введите целое число");
            }
        }
    }
}
