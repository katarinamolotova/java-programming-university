package part1.control_elements;

import java.util.Objects;

/**
 * Класс кнопки множественного выбора.
 * Добавляет поле selected (выбрана или нет).
 */
public class CheckButton extends Button {
    private boolean selected;

    public CheckButton() {
        super();
        this.selected = false;
    }

    public CheckButton(String id, int x, int y, String text, boolean selected) {
        super(id, x, y, text);
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "CheckButton{" +
                "id='" + getId() + '\'' +
                ", x=" + getX() +
                ", y=" + getY() +
                ", text='" + getText() + '\'' +
                ", selected=" + selected +
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
        CheckButton that = (CheckButton) o;
        return selected == that.selected;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), selected);
    }
}