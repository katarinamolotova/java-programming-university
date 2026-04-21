package part3.impl;

import java.util.Objects;

import part3.base.Edible;
import part3.base.Taste;

public class Sweet implements Edible {
    /**
     * Название сладости
     */
    private String name;
    /**
     * Вес сладости в граммах
     */
    private double weight;
    /**
     * Вкус сладости
     */
    private Taste taste;

    {
        name = "Неизвестная сладость";
        weight = 10.0;
        taste = Taste.SWEET;
        System.out.println("[Инициализатор Sweet] Создаётся объект сладости...");
    }

    public Sweet() { }

    public Sweet(String name, double weight, Taste taste) {
        setName(name);
        setWeight(weight);
        setTaste(taste);
    }

    public Sweet(String name, double weight) {
        this(name, weight, Taste.SWEET);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название не может быть пустым");
        }
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Вес должен быть положительным");
        }
        this.weight = weight;
    }

    public Taste getTaste() {
        return taste;
    }

    public void setTaste(Taste taste) {
        this.taste = taste;
    }

    @Override
    public void eat() {
        System.out.println("Вы съели " + name + " (" + weight + " г). Вкус: " + taste);
    }

    @Override
    public double getCalories() {
        return weight * 4.0;
    }

    public void eat(int pieces) {
        System.out.println("Вы съели " + pieces + " шт. сладости \"" + name + "\".");
    }

    @Override
    public String toString() {
        return String.format("Sweet{name='%s', weight=%.1f г, вкус=%s, калории=%.1f}",
                name, weight, taste, getCalories());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sweet sweet = (Sweet) o;
        return Double.compare(sweet.weight, weight) == 0 &&
                name.equals(sweet.name) &&
                taste == sweet.taste;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, weight, taste);
    }
}