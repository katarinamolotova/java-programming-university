package part1;

import part1.control_elements.Button;
import part1.control_elements.CheckButton;
import part1.control_elements.ControlElement;
import part1.control_elements.TextField;

public class Main {
     public static void main(String[] args) {
        ControlElement[] elements = new ControlElement[20];

        elements[0] = new Button("btn1", 10, 20, "OK");
        elements[1] = new Button("btn2", 30, 40, "Cancel");
        elements[2] = new CheckButton("chk1", 5, 5, "Enable feature", true);
        elements[3] = new CheckButton("chk2", 15, 15, "Agree terms", false);
        elements[4] = new TextField("tf1", 0, 0, "Enter name", "John");
        elements[5] = new TextField("tf2", 50, 100, "Enter email", "");
        elements[6] = new Button("btn3", 100, 200, "Submit");
        elements[7] = new CheckButton("chk3", 8, 12, "Remember me", true);
        elements[8] = new TextField("tf3", 20, 30, "Password", "secret");
        elements[9] = new Button("btn4", 60, 70, "Reset");
        elements[10] = new CheckButton("chk4", 25, 35, "Subscribe newsletter", false);
        elements[11] = new TextField("tf4", 40, 50, "Search", "");
        elements[12] = new Button("btn5", 80, 90, "Save");
        elements[13] = new CheckButton("chk5", 9, 19, "I'm not a robot", true);
        elements[14] = new TextField("tf5", 11, 22, "Address", "123 part1.Main St");
        elements[15] = new Button("btn6", 33, 44, "Load");
        elements[16] = new CheckButton("chk6", 55, 66, "Show advanced", false);
        elements[17] = new TextField("tf6", 77, 88, "Comment", "Hello world");
        elements[18] = new Button("btn7", 99, 111, "Export");
        elements[19] = new CheckButton("chk7", 12, 24, "Dark mode", true);

        for (ControlElement el : elements) {
            System.out.println(el);
        }
    }
}

/*
public static void main(String[] args) {
        // Массив из 20 элементов управления (тот же, что в части 1)
        ControlElement[] elements = new ControlElement[20];

        elements[0] = new Button("btn1", 10, 20, "OK");
        elements[1] = new Button("btn2", 30, 40, "Cancel");
        elements[2] = new CheckButton("chk1", 5, 5, "Enable feature", true);
        elements[3] = new CheckButton("chk2", 15, 15, "Agree terms", false);
        elements[4] = new TextField("tf1", 0, 0, "Enter name", "John");
        elements[5] = new TextField("tf2", 50, 100, "Enter email", "");
        elements[6] = new Button("btn3", 100, 200, "Submit");
        elements[7] = new CheckButton("chk3", 8, 12, "Remember me", true);
        elements[8] = new TextField("tf3", 20, 30, "Password", "secret");
        elements[9] = new Button("btn4", 60, 70, "Reset");
        elements[10] = new CheckButton("chk4", 25, 35, "Subscribe newsletter", false);
        elements[11] = new TextField("tf4", 40, 50, "Search", "");
        elements[12] = new Button("btn5", 80, 90, "Save");
        elements[13] = new CheckButton("chk5", 9, 19, "I'm not a robot", true);
        elements[14] = new TextField("tf5", 11, 22, "Address", "123 part1.Main St");
        elements[15] = new Button("btn6", 33, 44, "Load");
        elements[16] = new CheckButton("chk6", 55, 66, "Show advanced", false);
        elements[17] = new TextField("tf6", 77, 88, "Comment", "Hello world");
        elements[18] = new Button("btn7", 99, 111, "Export");
        elements[19] = new CheckButton("chk7", 12, 24, "Dark mode", true);

        // Вывод массива
        System.out.println("=== Все элементы управления ===");
        for (ControlElement el : elements) {
            System.out.println(el);
        }

        // Запрос 33
        System.out.println("\n=== Текст выбранных CheckButton ===");
        List<String> selectedTexts = ControlElementQueries.getSelectedCheckButtonTexts(elements);
        for (String text : selectedTexts) {
            System.out.println(text);
        }

        // Запрос 34
        System.out.println("\n=== Непустой текст TextField с hint ===");
        List<String> nonEmptyTexts = ControlElementQueries.getNonEmptyTextsWithHint(elements);
        for (String text : nonEmptyTexts) {
            System.out.println(text);
        }

        // Запрос 35 (например, для x = 10)
        int testX = 10;
        System.out.println("\n=== Элементы с координатой x = " + testX + " ===");
        List<String> elementsAtX = ControlElementQueries.getElementsAtX(elements, testX);
        if (elementsAtX.isEmpty()) {
            System.out.println("Нет элементов с x = " + testX);
        } else {
            for (String info : elementsAtX) {
                System.out.println(info);
            }
        }
    }
}
 */