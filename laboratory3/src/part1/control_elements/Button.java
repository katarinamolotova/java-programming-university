package part1.control_elements;

import java.util.Objects;

/**
 * Класс обычной кнопки. Добавляет поле text.
 */
public class Button extends ControlElement {
    private String text;

    public Button() {
        super();
        this.text = "Button";
    }

    public Button(String id, int x, int y, String text) {
        super(id, x, y);
        setText(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Текст кнопки не может быть null или пустым");
        }
        this.text = text;
    }

    @Override
    public String toString() {
        return "Button{" +
                "id='" + getId() + '\'' +
                ", x=" + getX() +
                ", y=" + getY() +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Button button = (Button) o;
        return text.equals(button.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), text);
    }
}