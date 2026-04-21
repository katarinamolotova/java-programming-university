package part1.control_elements;

import java.util.Objects;

/**
 * Базовый класс для элементов управления.
 * Содержит общие поля: id, координаты x и y.
 */
public abstract class ControlElement {
    private String id;
    private int x;
    private int y;

    public ControlElement() {
        this.id = "default";
        this.x = 0;
        this.y = 0;
    }

    public ControlElement(String id, int x, int y) {
        setId(id);
        setX(x);
        setY(y);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id не может быть null или пустым");
        }
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        if (x < 0) {
            throw new IllegalArgumentException("x не может быть отрицательным");
        }
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        if (y < 0) {
            throw new IllegalArgumentException("y не может быть отрицательным");
        }
        this.y = y;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id='" + id + '\'' +
                ", x=" + x +
                ", y=" + y +
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
        ControlElement that = (ControlElement) o;
        return x == that.x && y == that.y && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y);
    }
}