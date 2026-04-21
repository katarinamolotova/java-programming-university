package part3.base;

/**
 * Интерфейс, описывающий съедобный продукт
 */
public interface Edible {
    /**
     * Съесть продукт
     */
    void eat();
    /**
     * Получить количество калорий в продукте
     */
    double getCalories();
}