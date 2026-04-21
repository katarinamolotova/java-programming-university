package part3.impl;

import part3.base.Taste;
import java.util.Objects;

/**
 * Шоколадная конфета.
 */
public class ChocolateCandy extends Candy {
    private int cocoaPercentage; // процент какао

    // Нестатический инициализатор
    {
        cocoaPercentage = 50;
        System.out.println("[Инициализатор ChocolateCandy] Устанавливается процент какао...");
    }

    // Конструкторы с вызовом super (пункт D)
    public ChocolateCandy() {
        super();
    }

    public ChocolateCandy(String name, double weight, Taste taste, String filling, int cocoaPercentage) {
        super(name, weight, taste, filling);
        setCocoaPercentage(cocoaPercentage);
    }

    public ChocolateCandy(String name, double weight, String filling, int cocoaPercentage) {
        super(name, weight, filling);
        setCocoaPercentage(cocoaPercentage);
    }

    public int getCocoaPercentage() {
        return cocoaPercentage;
    }

    public void setCocoaPercentage(int cocoaPercentage) {
        if (cocoaPercentage < 0 || cocoaPercentage > 100) {
            throw new IllegalArgumentException("Процент какао должен быть от 0 до 100");
        }
        this.cocoaPercentage = cocoaPercentage;
    }

    // Переопределение метода eat
    @Override
    public void eat() {
        System.out.println("Вы наслаждаетесь шоколадной конфетой \"" + getName() +
                "\" с " + cocoaPercentage + "% какао.");
    }

    // Перегрузка метода eat (пункт E) с дополнительным параметром
    public void eat(String mood) {
        System.out.println("Вы в настроении \"" + mood + "\" съедаете шоколадную конфету \"" + getName() + "\".");
    }

    // Переопределение getCalories (учёт какао)
    @Override
    public double getCalories() {
        double base = super.getCalories();
        // дополнительно: чем больше какао, тем меньше сахара, но мы условно прибавим
        double cocoaEffect = cocoaPercentage * 0.5;
        return base + cocoaEffect;
    }

    @Override
    public String toString() {
        return String.format("ChocolateCandy{name='%s', weight=%.1f г, вкус=%s, начинка='%s', какао=%d%%, калории=%.1f}",
                getName(), getWeight(), getTaste(), getFilling(), cocoaPercentage, getCalories());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ChocolateCandy that = (ChocolateCandy) o;
        return cocoaPercentage == that.cocoaPercentage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cocoaPercentage);
    }
}