package control;

import javax.swing.*;

/**
 * Утилитный класс, обрабатывающий исключительные ситуации, происходящие во время работы программы
 */
public class ExceptionsHandler {
    private ExceptionsHandler() {}

    /**
     * Показывает сообщение об ошибке при возникновении любой исключительной ситуации
     *
     * @param e возникшее исключение
     * */
    public static void showMessageAboutGeneralExceptions(Exception e) {
        JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + "\n" + e.getMessage(),
                "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Показывает сообщение о некорректном вводе даты
     * */
    public static void showMessageAboutInvalidDate() {
        JOptionPane.showMessageDialog(null, "Неправильно указана дата.\n" +
                "Дата должна быть в формате ДД.ММ.ГГГГ(31.12.2017).", "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Показывает сообщение о некорректном вводе количества и/или цены
     * */
    public static void showMessageAboutInvalidAmountOrPrice() {
        JOptionPane.showMessageDialog(null, "Неправильно указано количество и/или цена.\n " +
                "Значения в этих полях должны быть целыми без пробелов.", "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Показывает сообщение о некорректном вводе описания товара (позиции в счете)
     * */
    public static void showMessageAboutInvalidDescriptionItem() {
        JOptionPane.showMessageDialog(null, "Не заполнено описание товара.", "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Показывает сообщение о некорректном вводе номера счета
     * */
    public static void showMessageAboutInvalidNumber() {
        JOptionPane.showMessageDialog(null, "Неправильно указан номер.\n" +
                "Номер должен состоять только из цифр без пробелов.", "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

}
