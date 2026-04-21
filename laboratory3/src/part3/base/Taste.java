package part3.base;

/**
 * Перечисление вкусов сладостей.
 */
public enum Taste {
    SWEET("Сладкий"),
    SOUR("Кислый"),
    BITTER("Горький"),
    SALTY("Солёный"),
    MINT("Мятный");

    private final String description;

    Taste(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}