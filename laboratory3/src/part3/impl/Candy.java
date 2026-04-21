package part3.impl;

import part3.base.Taste;
import java.util.Objects;

public class Candy extends Sweet {
    /**
     * Начинка конфеты
     */
    private String filling;

    {
        filling = "без начинки";
        System.out.println("[Инициализатор Candy] Добавляются свойства конфеты...");
    }

    public Candy() {
        super();
    }

    public Candy(String name, double weight, Taste taste, String filling) {
        super(name, weight, taste);
        setFilling(filling);
    }

    public Candy(String name, double weight, String filling) {
        super(name, weight);
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

    @Override
    public void eat() {
        System.out.println("Вы разворачиваете конфету \"" + getName() + "\" с начинкой \"" + filling + "\" и съедаете её.");
    }

    public void eat(boolean share) {
        if (share) {
            System.out.println("Вы делитесь конфетой \"" + getName() + "\" с другом.");
        } else {
            eat();
        }
    }

    @Override
    public double getCalories() {
        double base = super.getCalories();
        double fillingCalories = getWeight();
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