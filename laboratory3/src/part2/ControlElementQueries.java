package part2;

import java.util.ArrayList;
import java.util.List;

import part1.control_elements.CheckButton;
import part1.control_elements.ControlElement;
import part1.control_elements.TextField;

public class ControlElementQueries {

    /**
     * Возвращает текст выбранных элементов кнопок множественного выбора
     */
    public static List<String> getSelectedCheckButtonTexts(ControlElement[] elements) {
        List<String> result = new ArrayList<>();
        for (ControlElement el : elements) {
            if (el instanceof CheckButton) {
                CheckButton cb = (CheckButton) el;
                if (cb.isSelected()) {
                    result.add(cb.getText());
                }
            }
        }
        return result;
    }

    /**
     * Возвращает не пустой текст всех текстовых полей, у которых есть пояснение для ввода (hint)
     */
    public static List<String> getNonEmptyTextsWithHint(ControlElement[] elements) {
        List<String> result = new ArrayList<>();
        for (ControlElement el : elements) {
            if (el instanceof TextField) {
                TextField tf = (TextField) el;
                if (!tf.getHint().isEmpty() && !tf.getText().isEmpty()) {
                    result.add(tf.getText());
                }
            }
        }
        return result;
    }

    /**
     * Возвращает полная информация обо всех элементах управления, расположенных по заданной координате x.
     */
    public static List<String> getElementsAtX(ControlElement[] elements, int x) {
        List<String> result = new ArrayList<>();
        for (ControlElement el : elements) {
            if (el.getX() == x) {
                result.add(el.toString());
            }
        }
        return result;
    }
}