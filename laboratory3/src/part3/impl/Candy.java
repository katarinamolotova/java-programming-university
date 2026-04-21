package part3.impl;

import part3.base.Taste;
import java.util.Objects;

/**
 * Класс конфеты (наследует Sweet).
 */
public class Candy extends Sweet {
    private String filling; // начинка (например, "карамель", "помадка")

    // Нестатический инициализатор
    {
        filling = "без начинки";
        System.out.println("[Инициализатор Candy] Добавляются свойства конфеты...");
    }

    // Перегрузка конструкторов с вызовом конструктора базового класса (пункт D)
    public Candy() {
        super(); // вызывается конструктор Sweet()
    }

    public Candy(String name, double weight, Taste taste, String filling) {
        super(name, weight, taste); // вызов конструктора Sweet с параметрами
        setFilling(filling);
    }

    public Candy(String name, double weight, String filling) {
        super(name, weight); // вкус по умолчанию SWEET
        setFilling(filling);
    }

    public String getFilling() {
        return filling;
    }

    public void setFilling(String filling) {
        if (filling == null || filling.trim().isEmpty()) {
            this.filling = "без начинки";
        } else {
            this.filling = filling;
        }
    }

    // Переопределение метода eat (перегрузка уже есть в Sweet)
    @Override
    public void eat() {
        System.out.println("Вы разворачиваете конфету \"" + getName() + "\" с начинкой \"" + filling + "\" и съедаете её.");
    }

    // Перегрузка метода eat (пункт E) - специфичная для Candy
    public void eat(boolean share) {
        if (share) {
            System.out.println("Вы делитесь конфетой \"" + getName() + "\" с другом.");
        } else {
            eat();
        }
    }

    // Переопределение getCalories (добавляем калории от начинки)
    @Override
    public double getCalories() {
        double base = super.getCalories();
        // начинка добавляет примерно 1 ккал на грамм веса
        double fillingCalories = getWeight() * 1.0;
        return base + fillingCalories;
    }

    @Override
    public String toString() {
        return String.format("Candy{name='%s', weight=%.1f г, вкус=%s, начинка='%s', калории=%.1f}",
                getName(), getWeight(), getTaste(), filling, getCalories());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Candy candy = (Candy) o;
        return filling.equals(candy.filling);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), filling);
    }
}