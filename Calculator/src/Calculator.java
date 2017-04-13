import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Программа "Калькулятор"
 *
 * @author Герасенков Евгений
 * @version 1.0
 * */
public class Calculator {
    private JFrame frame = new JFrame("Калькулятор");
    private JPanel panelButton;
    private JTextField display;
    private boolean flag = true;
    private boolean flagMemory;
    private float result;
    private float memory;
    private String operation = "=";

    public static void main(String[] args) {
        new Calculator().buildGui();
    }

    /**
     * Метод создает основной ГПИ.
     * Создание кнопок и регистрация их слушаетелей осуществляется через метод createButton.
     * */
    private void buildGui() {
        createPanel();
        createMenu();

        createButton("MC", new MemoryClear());
        createButton("MR", new MemoryRead());
        createButton("M-", new MemoryMinus());
        createButton("M+", new MemoryPlus());
        createButton("\u221A", new Sqrt());

        createButton("C", new ClearListener());
        createButton("7", new ValueListener());
        createButton("8", new ValueListener());
        createButton("9", new ValueListener());
        createButton("/", new OperationListener());

        createButton("CE", new CeClearListener());
        createButton("4", new ValueListener());
        createButton("5", new ValueListener());
        createButton("6", new ValueListener());
        createButton("*", new OperationListener());

        createButton("\u2190", new BackSpace());
        createButton("1", new ValueListener());
        createButton("2", new ValueListener());
        createButton("3", new ValueListener());
        createButton("-", new OperationListener());

        createButton("\u00B1", new PlusMinus());
        createButton(".", new PointListener());
        createButton("0", new ValueListener());
        createButton("=", new OperationListener());
        createButton("+", new OperationListener());

        frame.setBounds(400, 200, 300, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /**
     * Метод создает основую панель, панель кнопок, дисплей.
     * */
    private void createPanel() {
        JPanel panelMain = new JPanel(new BorderLayout(0, 5));
        panelButton = new JPanel(new GridLayout(5, 5, 4, 4));
        display = new JTextField("0");
        display.setFont(new Font("Verdana", Font.PLAIN, 22));
        display.setBackground(Color.WHITE);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        panelMain.add(display, BorderLayout.NORTH);
        panelMain.add(panelButton, BorderLayout.CENTER);
        frame.add(panelMain);
    }

    /**
     * Метод создает и добавляет меню "Справка - О программе".
     * */
    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Справка");
        JMenuItem about = new JMenuItem("О программе");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new About(frame).setVisible(true);
            }});
        menu.add(about);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
    }

    /**
     * Метод создания кнопки, регистрации ее слушателя и размещение кнопки на панели.
     * */
    private void createButton(String name, ActionListener listener) {
        JButton button = new JButton(name);
        button.addActionListener(listener);
        panelButton.add(button);
    }

    /**
     * Метод математических операций.
     * В зависимости от значения в переменной "operation"
     * осуществляется та или иная операция между значениями в переменной "result" и переданным аргументом "x".
     * */
    private void calculate(float x) {
        switch (operation){
            case "+": result += x;
                break;
            case "-": result -= x;
                break;
            case "*": result *= x;
                break;
            case "/": result /= x;
                break;
            case "=": result = x;
                break;
        }
        display.setText("" + result);
    }

    /**
     * Класс-слушатель, который при нажатии соотвествующей кнопки,
     * выводит на дисплей соотвествующую цифру.
     *
     * Первая проверка условия реализована для того, чтобы число не начиналось с 0(напр. 02476).
     *
     * Вторая проверка условия реализована для проверки вводится все еще текущее число
     * или уже необходимо выводить на дисплей второе число.
     * */
    private class ValueListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String currentValue = e.getActionCommand();
            if(!(flag) && display.getText().equals("0")) {
                flag = true;
            }
            if(flag) {
                flag = false;
                display.setText("");
            }
            display.setText(display.getText() + currentValue);
        }
    }

    /**
     * Класс-слушатель, который при нажатии соотвествующей кнопки,
     * осуществляет математические действия с числами на дисплее.
     *
     * Первая проверка значения переменной "flagMemory":
     * true - вызывается метод calculate, в который передается в качестве аргумента текущее число на дисплее,
     * записывается выбранная пользователем текущая арифметичекая операция в переменную "operation",
     * переменной "flagMemory" присваивается значение false.
     * Данная реализация сделана для того, чтобы производилась арифемитеская операция с числом,
     * которое вставлено из памяти (т.е. из переменной "memory").

     * Вторая проверка переменной "flag":
     * true - записывается выбранная пользователем текущая арифметичекая операция в переменную "operation".
     * false - вызывается метод calculate, в который передается в качестве аргумента текущее число на дисплее.
     * Затем записывается выбранная пользователем текущая арифметичекая операция в переменную "operation".
     * Данная реализация сделана для того, чтобы, в случае неоднократного нажатия кнопки с арифметическим действием
     * и отсутствием второго числа для осуществления математической операции,
     * не происходило постоянно арифеметическое действие с последним введенным числом.
     * */
    private class OperationListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String currentOperation = e.getActionCommand();
            if(flagMemory) {
                calculate(Float.parseFloat(display.getText()));
                operation = currentOperation;
                flagMemory = false;
            }
            if(flag) {
                operation = currentOperation;
            }
            else {
                calculate(Float.parseFloat(display.getText()));
                operation = currentOperation;
            }
            flag = true;
        }
    }

    /**
     * Класс-слушатель, который при нажатии соотвествующей кнопки,
     * находит квадратный корень из введенного числа на дисплей
     * и выводит его на экран.
     * */
    private class Sqrt implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            display.setText("" + Math.sqrt(Double.parseDouble(display.getText())));
            flag = false;
        }
    }

    /**
     * Класс-слушатель, который при нажатии соотвествующей кнопки,
     * делает число на дисплее отрицательным или положительным.
     * */
    private class PlusMinus implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(flag){
                if(!(display.getText().contains("-"))){
                    display.setText("-"+display.getText());
                    result = Float.parseFloat(display.getText());
                    flag = true;
                }
                else {
                    display.setText(display.getText().substring(1));
                    result = Float.parseFloat(display.getText());
                    flag = true;
                }
            }
            else {
                if(!(display.getText().contains("-"))){
                    display.setText("-"+display.getText());
                    flag = false;
                }
                else {
                    display.setText(display.getText().substring(1));
                    flag = false;
                }
            }
        }
    }

    /**
     * Класс-слушатель, который при нажатии соотвествующей кнопки, добавляет точку к цислу.
     * Можно добавлять дробную часть к целому числу.
     * */
    private class PointListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String tmpValue = e.getActionCommand();
            if(!(display.getText().contains("."))){
                display.setText(display.getText() + ".");
                flag = false;
            }
        }
    }

    /**
     * Класс-слушатель, который при нажатии соотвествующей кнопки,
     * сбрасывает все значения переменных и операции к default, кроме значения в переменной "memory".
     * */
    private class ClearListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            display.setText("0");
            result = 0;
            operation = "=";
            flag = true;
        }
    }

    /**
     * Класс-слушатель, который при нажатии соотвествующей кнопки,
     * сбрасывает только текущее значение на дисплее.
     * */
    private class CeClearListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            display.setText("0");
            flag = true;
        }
    }

    /**
     * Класс-слушатель, который при нажатии соотвествующей кнопки,
     * удаляет последнюю введенную цифру в числе на дисплее.
     * Если число состоит из одной цифры, то эта цифра удаляется и появляется 0.
     * */
    private class BackSpace implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String s = display.getText();
            if(!(flag)){
                if(s.length() != 1) {
                    display.setText(s.substring(0, s.length()-1));
                }
                else {
                    display.setText("0");
                    flag = true;
                }
            }
        }
    }

    /**
     * Класс-слушатель, который при нажатии соотвествующей кнопки,
     * очищает переменную "memory", отвечающую за сохранение значения памяти.
     * */
    private class MemoryClear implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            memory = 0;
            flagMemory = false;
        }
    }

    /**
     * Класс-слушатель, который при нажатии соотвествующей кнопки,
     * выводит на дисплей текущее значение, сохраненное в памяти, т.е. из переменной "memory".
     * */
    private class MemoryRead implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            display.setText("" + memory);
            flag = true;
            flagMemory = true;
        }
    }

    /**
     * Класс-слушатель, который при нажатии соотвествующей кнопки,
     * прибавляет текущее число, отраженное на дисплее, к текущему значению, сохраненному в памяти (в переменной "memory").
     * */
    private class MemoryPlus implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            memory += Float.parseFloat(display.getText());
        }
    }

    /**
     * Класс-слушатель, который при нажатии соотвествующей кнопки,
     * отнимает текущее число, отраженное на дисплее, из текущего значению, сохраненного в памяти (в переменной "memory").
     * Если в памяти (в переменной "memory") текущее значение 0, то вычитания не происходит.
     * */
    private class MemoryMinus implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(memory != 0) memory -= Float.parseFloat(display.getText());
        }
    }

    /**
     * Меню "Справка - О программе"
     * */
    private class About extends JDialog {
        public About (JFrame frame) {
            super(frame, "О программе", true);
            add(new JLabel("<html><h2>My first program :)</h2><hr>"+
                    "<i>автор Евгений Герасенков</i></html>"));
            setResizable(false);
            pack();
            setBounds(400, 200 , 200, 100);
        }
    }
}
