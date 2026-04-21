package part1.control_elements;

import java.util.Objects;

/**
 * Класс текстового поля.
 * Содержит подсказку (hint) и введённый текст (text).
 */
public class TextField extends ControlElement {
    private String hint;
    private String text;

    public TextField() {
        super();
        this.hint = "";
        this.text = "";
    }

    public TextField(String id, int x, int y, String hint, String text) {
        super(id, x, y);
        setHint(hint);
        setText(text);
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = (hint == null) ? "" : hint;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = (text == null) ? "" : text;
    }

    @Override
    public String toString() {
        return "TextField{" +
                "id='" + getId() + '\'' +
                ", x=" + getX() +
                ", y=" + getY() +
                ", hint='" + hint + '\'' +
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
        TextField textField = (TextField) o;
        return hint.equals(textField.hint) && text.equals(textField.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hint, text);
    }
}