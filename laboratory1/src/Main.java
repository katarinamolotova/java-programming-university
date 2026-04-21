import java.math.BigDecimal;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        runMenu(scanner);
    }

    private static void runMenu(Scanner scanner) {
        while (true) {
            System.out.println();
            System.out.println("Меню:");
            System.out.println("1 - Часть 1 / Задание 1");
            System.out.println("2 - Часть 1 / Задание 2");
            System.out.println("3 - Часть 2 / Задание 1");
            System.out.println("4 - Часть 2 / Задание 2");
            System.out.println("5 - Часть 2 / Задание 3");
            System.out.println("0 - Выход");

            int choice = readInt(scanner, "Выберите пункт: ", 0, 5);
            switch (choice) {
                case 0:
                    System.out.println("Выход из программы");
                    return;
                case 1:
                    part1task1(scanner);
                    break;
                case 2:
                    part1task2();
                    break;
                case 3:
                    part2task1(scanner);
                    break;
                case 4:
                    part2task2(scanner);
                    break;
                case 5:
                    part2task3(scanner);
                    break;
            }
        }
    }

    private static void part1task1(Scanner scanner) {
        double x = readDouble(scanner, "Введите X: ");
        double y = readDouble(scanner, "Введите Y: ");
        boolean inside = isInArea(x, y);
        System.out.println(inside ? "TRUE" : "FALSE");
    }

    private static boolean isInArea(double x, double y) {
        boolean inTriangle = x >= -10.0 && x <= 0.0 && Math.abs(y) <= 0.5 * (x + 10.0);
        boolean inCircle = (x - 5.0) * (x - 5.0) + y * y <= 25.0;
        return inTriangle || inCircle;
    }

    private static void part1task2() {
        float aF = 1000.0f;
        float bF = 0.0001f;
        float numF = (float) (Math.pow(aF - bF, 3) - Math.pow(aF, 3));
        float denF = (float) (-Math.pow(bF, 3) + 3 * aF * Math.pow(bF, 2) - 3 * Math.pow(aF, 2) * bF);
        float resF = numF / denF;

        double aD = 1000.0;
        double bD = 0.0001;
        double numD = Math.pow(aD - bD, 3) - Math.pow(aD, 3);
        double denD = -Math.pow(bD, 3) + 3 * aD * Math.pow(bD, 2) - 3 * Math.pow(aD, 2) * bD;
        double resD = numD / denD;

        BigDecimal aB = BigDecimal.valueOf(1000.0);
        BigDecimal bB = BigDecimal.valueOf(0.0001);
        BigDecimal numB = aB.subtract(bB).pow(3).subtract(aB.pow(3));
        BigDecimal denB = bB.pow(3).negate()
                .add(aB.multiply(bB.pow(2)).multiply(BigDecimal.valueOf(3)))
                .subtract(aB.pow(2).multiply(bB).multiply(BigDecimal.valueOf(3)));
        BigDecimal resB = numB.divide(denB);

        System.out.println("float: " + resF);
        System.out.println("double: " + resD);
        System.out.println("BigDecimal: " + resB);
    }

    private static void part2task1(Scanner scanner) {
        int n = readInt(scanner, "Введите количество элементов: ", 1, Integer.MAX_VALUE);
        int minValue = Integer.MAX_VALUE;
        int minIndex = 1;
        for (int i = 1; i <= n; i++) {
            int value = readInt(scanner, "Введите элемент " + i + ": ", Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (value < minValue) {
                minValue = value;
                minIndex = i;
            }
        }
        System.out.println("Номер минимального элемента: " + minIndex);
    }

    private static void part2task2(Scanner scanner) {
        System.out.println("Введите последовательность целых чисел, завершите 0:");
        int countEven = 0;
        int value = readInt(scanner, "Введите число: ", Integer.MIN_VALUE, Integer.MAX_VALUE);
        while (value != 0) {
            if (value % 2 == 0) {
                countEven++;
            }
            value = readInt(scanner, "Введите число: ", Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        System.out.println("Количество четных элементов: " + countEven);
    }

    private static void part2task3(Scanner scanner) {
        int n = readInt(scanner, "Введите n: ", 1, Integer.MAX_VALUE);
        double sum = 0.0;
        int i = 1;
        do {
            int denom = 2 * i + 1;
            sum += 1.0 / (denom * denom);
            i++;
        } while (i <= n);
        System.out.println("S = " + sum);
    }

    private static double readDouble(Scanner scanner, String prompt) {
        boolean isCorrect = false;
        double value = 0;
        while (!isCorrect) {
            System.out.print(prompt);
            String token = scanner.nextLine().trim().replace(',', '.');
            try {
                value = Double.parseDouble(token);
                isCorrect = true;
            } catch (NumberFormatException ex) {
                System.out.println("Ошибка: введите число.");
            }
        }
        return value;
    }

    private static int readInt(Scanner scanner, String prompt, int min, int max) {
        boolean isCorrect = false;
        int value = 0;
        while (!isCorrect) {
            System.out.print(prompt);
            String token = scanner.nextLine().trim();
            try {
                value = Integer.parseInt(token);
                isCorrect = true;
                if (value < min || value > max) {
                    System.out.println("Введите число от " + min + " до " + max + ".");
                    isCorrect = false;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Ошибка: введите целое число.");
            }
        }
        return value;
    }
}

