package part3;

import part3.base.Edible;
import part3.base.Taste;
import part3.impl.Candy;
import part3.impl.ChocolateCandy;
import part3.impl.Sweet;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Демонстрация работы с иерархией сладостей ===\n");

        // Создание объектов
        Sweet s1 = new Sweet("Мармелад", 50.0, Taste.SWEET);
        Candy c1 = new Candy("Барбарис", 15.0, Taste.SOUR, "фруктовая");
        Candy c2 = new Candy("Коровка", 20.0, "сливочная помадка");
        ChocolateCandy ch1 = new ChocolateCandy("Трюфель", 30.0, Taste.BITTER, "шоколадный крем", 70);
        ChocolateCandy ch2 = new ChocolateCandy("Алёнка", 25.0, "вафельная крошка", 45);

        // Демонстрация перегруженных методов eat
        s1.eat();
        s1.eat(3);
        c1.eat();
        c1.eat(true);
        ch1.eat();
        ch1.eat("отличное");

        System.out.println("\n--- Информация о сладостях ---");
        System.out.println(s1);
        System.out.println(c1);
        System.out.println(c2);
        System.out.println(ch1);
        System.out.println(ch2);

        // Пункт H: массив/список объектов, реализующих интерфейс Edible
        List<Edible> edibles = new ArrayList<>();
        edibles.add(s1);
        edibles.add(c1);
        edibles.add(c2);
        edibles.add(ch1);
        edibles.add(ch2);

        System.out.println("\n--- Обработка списка Edible ---");
        double totalCalories = 0;
        for (Edible e : edibles) {
            System.out.println("Калорийность: " + e.getCalories() + " ккал");
            totalCalories += e.getCalories();
            e.eat(); // вызов метода eat через интерфейс
        }
        System.out.println("Общая калорийность всех сладостей: " + totalCalories + " ккал");
    }
}